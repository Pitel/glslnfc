package cz.pitel.glslnfc;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class GLSLFragment extends Fragment {
	private GLSLSurfaceView glsl;
	private final GLSLRenderer renderer = new GLSLRenderer();

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		setShader(getActivity().getPreferences(0).getString("shader", getString(R.string.default_shader)));
		glsl = new GLSLSurfaceView(getActivity());
		return glsl;
	}

	@Override
	public void onPause() {
		super.onPause();
		glsl.onPause();
		Log.d("GLSL", "Rendering paused");
	}

	@Override
	public void onResume() {
		super.onResume();
		glsl.onResume();
		Log.d("GLSL", "Rendering resumed");
	}

	public void setShader(final String shader) {
		renderer.setShader(shader);
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
}
