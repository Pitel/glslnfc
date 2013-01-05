package cz.pitel.glslnfc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
/*
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
*/

public class SandboxActivity extends FragmentActivity {
	private final ArrayList<Fragment> fragments = new ArrayList<Fragment>(2);

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sandbox);
		final FragmentManager fm = getSupportFragmentManager();
		if (savedInstanceState != null) {
			fragments.add(fm.getFragment(savedInstanceState, GLSLFragment.class.getName()));
			fragments.add(fm.getFragment(savedInstanceState, EditorFragment.class.getName()));
		}
		final ViewPager pager = (ViewPager) findViewById(R.id.pager);
		final SandboxFragmentAdapter adapter = new SandboxFragmentAdapter(fm);
		pager.setAdapter(adapter);
		final Intent intent = getIntent();
		if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			try {
				new ShaderTask().execute(new URL("http://glsl.heroku.com/item/" + intent.getData().getFragment()));
			} catch (MalformedURLException e) {
				Log.w("GLSL", e);
			}
		} else {
			//renderer.setShader(getPreferences(0).getString("shader", getString(R.string.default_shader)));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		MenuItemCompat.setShowAsAction(menu.findItem(R.id.load), MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.load:
				final EditText input = new EditText(this);
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				new AlertDialog.Builder(this)
					.setTitle(R.string.insert_shader_id)
					.setView(input)
					.setPositiveButton(R.string.load, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							try {
								new ShaderTask().execute(new URL("http://glsl.heroku.com/item/" + input.getText().toString()));
							} catch (MalformedURLException e) {
								Log.w("GLSL", e);
							}
						}
					})
					.show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private class SandboxFragmentAdapter extends FragmentPagerAdapter {
		private String[] pages;

		public SandboxFragmentAdapter(final FragmentManager fm) {
			super(fm);
			pages = getResources().getStringArray(R.array.pages);
			if (fragments.isEmpty()) {
				fragments.add(new GLSLFragment());
				fragments.add(new EditorFragment());
			}
		}

		@Override
		public Fragment getItem(final int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return pages.length;
		}

		@Override
		public CharSequence getPageTitle(final int position) {
			return pages[position];
		}
	}

	private class ShaderTask extends AsyncTask<URL, Void, String> {
		@Override
		protected String doInBackground(final URL... url) {
			Log.w("GLSLNFC", url[0].toString());
			try {
				final HttpURLConnection http = (HttpURLConnection) url[0].openConnection();
				try {
				JSONObject json = (JSONObject) new JSONTokener(new Scanner(http.getInputStream()).useDelimiter("\\A").next()).nextValue();
					return json.getString("code");
				} catch (IOException e) {
					Log.w("GLSL", e);
				} catch (JSONException e) {
					Log.w("GLSL", e);
				} finally {
					http.disconnect();
				}
			} catch (IOException e) {
				Log.w("GLSL", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(final String shader) {
			Log.v("GLSL", shader);
			//renderer.setShader(shader);
			getPreferences(Context.MODE_PRIVATE).edit().putString("shader", shader).apply();
		}
	}

	/*
	private GLSLSurfaceView glsl;
	private final GLSLRenderer renderer = new GLSLRenderer();

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		glsl = new GLSLSurfaceView(this);
		setContentView(glsl);
		final Intent intent = getIntent();
		if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			try {
				new ShaderTask().execute(new URL("http://glsl.heroku.com/item/" + intent.getData().getFragment()));
			} catch (MalformedURLException e) {
				Log.w("GLSL", e);
			}
		} else {
			renderer.setShader(getPreferences(0).getString("shader", getString(R.string.default_shader)));
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		glsl.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		glsl.onResume();
	}

	private class GLSLSurfaceView extends GLSurfaceView {
		public GLSLSurfaceView(final Context context) {
			super(context);
			setEGLContextClientVersion(2);
			//setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
			setRenderer(renderer);
		}

		@Override
		public boolean onTouchEvent(final MotionEvent e) {
			renderer.setMouse(e.getX(), e.getY());
			return true;
		}
	}
	*/
}
