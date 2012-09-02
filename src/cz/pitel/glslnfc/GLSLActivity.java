package cz.pitel.glslnfc;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class GLSLActivity extends Activity {
	private GLSLSurfaceView glsl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		glsl = new GLSLSurfaceView(this);
		setContentView(glsl);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.load:
				//TODO
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	class GLSLSurfaceView extends GLSurfaceView {
		public GLSLSurfaceView(Context context) {
			super(context);
			setEGLContextClientVersion(2);
			setRenderer(new GLSLRenderer());
		}
	}
}
