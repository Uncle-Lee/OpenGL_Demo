package com.will.airhockey;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.will.util.LoggerConfig;
import com.will.util.ShaderHelper;
import com.will.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

/**
 * Create By jie.li on 2019-05-28.
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {
	
	private static final String U_COLOR = "u_Color";
	private static final String A_POSITION = "a_Position";
	private int uColorLocation;
	private int aPositionLocation;
	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final  int BYTES_PER_FLOAT = 4;
	private final FloatBuffer vertexData;
	private final Context context;
	private int program;
	
	public AirHockeyRenderer(Context context) {
		/*float[] tableVertices = {
				0f, 0f,
				0f, 14f,
				9f, 14f,
				9f, 0f
		};*/
		
		float[] tableVerticesWithTriangles = {
				// Order of coordinates: X, Y, R, G, B
				
				// Triangle Fan
				0, 0, 1f, 1f, 1f,
				-0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
				0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
				0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
				-0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
				-0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
				
				// Line 1
				-0.5f, 0f, 1f, 0f, 0f,
				0.5f, 0f, 1f, 0f, 0f,
				
				// Mallets
				0f, -0.25f, 0f, 0f, 1f,
				0f, 0.25f, 1f, 0f, 0f
		};
		
		this.context = context;
		vertexData = ByteBuffer
				.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		vertexData.put(tableVerticesWithTriangles);
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
		String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);
		// 编译着色器代码，并获得它们的值
		int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
		int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
		
		// 将着色器代码结合，并保存OpenGL程序的值
		program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
		
		if (LoggerConfig.ON) {
			ShaderHelper.validateProgram(program);
		}
		
		glUseProgram(program);
		
		uColorLocation = glGetUniformLocation(program, U_COLOR);
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		
		vertexData.position(0);  // 从内存区域的开头开始读值
		// 告诉OpenGL去哪里读数据
		glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData);
		GLES20.glEnableVertexAttribArray(aPositionLocation);
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		glViewport(0, 0, width, height);
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT);
		
		// 画桌子
		glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
		glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
		
		// 画桌子中心分割线
		GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
		GLES20.glDrawArrays(GL_LINES, 6, 2);
		
		// 画木槌
		GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
		GLES20.glDrawArrays(GL_POINTS, 8, 1);
		glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
		glDrawArrays(GL_POINTS, 9, 1);
	}
}
