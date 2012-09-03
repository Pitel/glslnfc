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
	private int program, resolution, mouse, time;
	private float w, h;
	private long start;

	private static final String vertexShaderCode =
		"attribute vec2 position;" +
		"void main() {" +
			"gl_Position = vec4(position, 0, 1);" +
		"}";

	private static final String fragmentShaderCode =
		"#ifdef GL_ES\n" +
		"precision highp float;\n" +
		"#endif\n" +

		"uniform float time;" +
		"uniform vec2 mouse;" +
		"uniform vec2 resolution;" +

		"void main(void) {" +
			"vec2 uPos = ( gl_FragCoord.xy / resolution.y );" +
			"uPos -= vec2((resolution.x/resolution.y)/2.0, 0.5);" +

			"float angle = atan(uPos.y, uPos.x);" +
			"float len = sqrt(uPos.x*uPos.x + uPos.y*uPos.y);" +

			"float newAngle = angle - 0.1*sin(len*20.0-time*8.0) - 0.9*sin(len*5.0-time);" +
			"float flower = 1.0 - smoothstep(0.2, 0.5, len);" +
			"flower *= 5.0 * (sin(newAngle*20.0 )+1.0);" +
			"vec3 flowerColor = vec3(flower*0.4, flower*0.1, flower*0.6);" +

			"float gradient = smoothstep(0.5, 1.5, len);" +
			"vec3 gradientColor = vec3(gradient*0.4, gradient*0.1, gradient*0.6);" +

			"vec3 color = flowerColor + gradientColor;" +
			"gl_FragColor = vec4(color, 1.0);" +
	"}";

	/*
	private static final String fragmentShaderCode =
		"precision mediump float;" +

		"uniform float time;" +
		"uniform vec2 mouse;" +
		"uniform vec2 resolution;" +

		"void main(void) {" +
			"vec2 position = ( gl_FragCoord.xy / resolution.xy ) + mouse / 4.0;" +

			"float color = 0.0;" +
			"color += sin( position.x * cos( time / 15.0 ) * 80.0 ) + cos( position.y * cos( time / 15.0 ) * 10.0 );" +
			"color += sin( position.y * sin( time / 10.0 ) * 40.0 ) + cos( position.x * sin( time / 25.0 ) * 40.0 );" +
			"color += sin( position.x * sin( time / 5.0 ) * 10.0 ) + sin( position.y * sin( time / 35.0 ) * 80.0 );" +
			"color *= sin( time / 10.0 ) * 0.5;" +
			"gl_FragColor = vec4( vec3( color, color * 0.5, sin( color + time / 3.0 ) * 0.75 ), 1.0 );" +
		"}";
	*/

	@Override
	public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
		GLES20.glClearColor(1, 0, 0, 1);
		setShader(fragmentShaderCode);
	}

	@Override
	public void onDrawFrame(final GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		//Log.v("GLSL", "onDrawFrame");
		GLES20.glUniform2f(resolution, w, h);
		GLES20.glUniform2f(mouse, w / 2f, h / 2f);
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
		Log.v("GLSL", shader);

		// Quad
		final ByteBuffer bb = ByteBuffer.allocateDirect(verts.length * 4);
		bb.order(ByteOrder.nativeOrder());
		final FloatBuffer quad = bb.asFloatBuffer();
		quad.put(verts);
		quad.position(0);

		// Vertex shader
		final int vs = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		GLES20.glShaderSource(vs, vertexShaderCode);
		GLES20.glCompileShader(vs);
		Log.i("GLSL", "Vertex: " + GLES20.glGetShaderInfoLog(vs));

		// Fragment shader
		final int fs = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		GLES20.glShaderSource(fs, shader);
		GLES20.glCompileShader(fs);
		Log.i("GLSL", "Fragment: " + GLES20.glGetShaderInfoLog(fs));

		// Program
		final int newprogram = GLES20.glCreateProgram();
		GLES20.glAttachShader(newprogram, vs);
		GLES20.glAttachShader(newprogram, fs);
		GLES20.glDeleteShader(vs);
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
		resolution = GLES20.glGetUniformLocation(newprogram, "resolution");
		mouse = GLES20.glGetUniformLocation(newprogram, "mouse");
		time = GLES20.glGetUniformLocation(newprogram, "time");
		start = System.currentTimeMillis();
	}
}
