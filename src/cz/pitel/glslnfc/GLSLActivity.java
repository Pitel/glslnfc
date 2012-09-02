package cz.pitel.glslnfc;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

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

	class GLSLSurfaceView extends GLSurfaceView {
		public GLSLSurfaceView(Context context) {
			super(context);
			setEGLContextClientVersion(2);
			setRenderer(new GLSLRenderer());
		}
	}
}
