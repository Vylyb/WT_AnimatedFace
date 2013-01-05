package main.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import main.view.opengl.OpenGLDisplay;

import org.lwjgl.LWJGLException;




public class Window extends JFrame {
	
	private OpenGLDisplay openglDisplay;
	public ControlContainer controlContainer;
	public AnimationContainer animtationContainer;

	public Window() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setSize(1000, 800);
		setResizable(false);
		setLocation(5,5);
		
		setLayout(new BorderLayout(5, 5));
		
		add(controlContainer=new ControlContainer(this),BorderLayout.EAST);
		add(animtationContainer=new AnimationContainer(this),BorderLayout.NORTH);

		try {
			add(openglDisplay=new OpenGLDisplay(controlContainer),BorderLayout.CENTER);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
	}

}
