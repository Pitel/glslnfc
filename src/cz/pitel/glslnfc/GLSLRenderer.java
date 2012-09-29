package cz.pitel.glslnfc;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLSLRenderer implements Renderer {
	private final static float[] verts = new float[] {-1,-1, 1,-1, -1,1, 1,-1, 1,1, -1,1};
	private FloatBuffer quad;
	private int program, vs, resolution, mouse, time;
	private float w, h, mx = 0.5f, my = 0.5f;
	private long start;
	private boolean dirty = true;

	private static final String vertexShaderCode =
		"attribute vec2 position;" +
		"void main() {" +
			"gl_Position = vec4(position, 0, 1);" +
		"}";

	private String shader;

	@Override
	public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
		GLES20.glClearColor(1, 0, 0, 1);

		// Quad
		final ByteBuffer bb = ByteBuffer.allocateDirect(verts.length * 4);
		bb.order(ByteOrder.nativeOrder());
		quad = bb.asFloatBuffer();
		quad.put(verts);
		quad.position(0);

		// Vertex shader
		vs = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		GLES20.glShaderSource(vs, vertexShaderCode);
		GLES20.glCompileShader(vs);
		Log.i("GLSL", "Vertex: " + GLES20.glGetShaderInfoLog(vs));

		dirty = true;
	}

	@Override
	public void onDrawFrame(final GL10 gl) {
		if (dirty) {
			Log.v("GLSL", shader);

			// Fragment shader
			final int fs = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
			GLES20.glShaderSource(fs, shader);
			GLES20.glCompileShader(fs);
			Log.i("GLSL", "Fragment: " + GLES20.glGetShaderInfoLog(fs));

			// Program
			final int newprogram = GLES20.glCreateProgram();
			GLES20.glAttachShader(newprogram, vs);
			GLES20.glAttachShader(newprogram, fs);
			GLES20.glDeleteShader(fs);
			GLES20.glLinkProgram(newprogram);
			Log.i("GLSL", "Program: " + GLES20.glGetProgramInfoLog(newprogram));
			GLES20.glDeleteProgram(program);
			program = newprogram;
			GLES20.glUseProgram(program);

			// Vertex array
			final int position = GLES20.glGetAttribLocation(program, "position");
			GLES20.glEnableVertexAttribArray(position);
			GLES20.glVertexAttribPointer(position, 2, GLES20.GL_FLOAT, false, 2 * 4, quad);

			// Uniforms
			resolution = GLES20.glGetUniformLocation(program, "resolution");
			mouse = GLES20.glGetUniformLocation(program, "mouse");
			time = GLES20.glGetUniformLocation(program, "time");
			start = System.currentTimeMillis();
			dirty = false;
		}
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		//Log.v("GLSL", "onDrawFrame");
		GLES20.glUniform2f(resolution, w, h);
		GLES20.glUniform2f(mouse, mx, my);
		GLES20.glUniform1f(time, (System.currentTimeMillis() - start) / 1000f);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
	}

	@Override
	public void onSurfaceChanged(final GL10 gl, final int width, final int height) {
		Log.d("GLSL", Integer.toString(width));
		Log.d("GLSL", Integer.toString(height));
		w = width;
		h = height;
		GLES20.glViewport(0, 0, width, height);
	}

	void setShader(final String shader) {
		this.shader = shader;
		dirty = true;
	}

	void setMouse(final float x, final float y) {
		mx = x / w;
		my = 1 - y / h;
	}
}
