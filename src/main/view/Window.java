package main.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import main.view.opengl.OpenGLDisplay;

import org.lwjgl.LWJGLException;




public class Window extends JFrame {
	
	private OpenGLDisplay openglDisplay;
	public ControlContainer controlContainer;
	public AnimationContainer animationContainer;
	public ControlMenuBar menubar;

	public Window() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setSize(1000, 800);
		setResizable(false);
		setLocation(5,5);
		
		setLayout(new BorderLayout(5, 5));
		
		add(controlContainer=new ControlContainer(this),BorderLayout.EAST);
		add(animationContainer=new AnimationContainer(this),BorderLayout.NORTH);

		try {
			add(openglDisplay=new OpenGLDisplay(controlContainer),BorderLayout.CENTER);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		setJMenuBar(menubar=new ControlMenuBar());
	}
	
	public String getDisplaySizeAndLocation(){
		/*
		 * extra options to copy into vlc player 
		 * for capturing only the part of the part of the screen
		 * where the face is displayed
		 */
		return 	" :screen-top="+openglDisplay.getLocationOnScreen().y+
				" :screen-left="+openglDisplay.getLocationOnScreen().x+
				" :screen-width="+openglDisplay.getSize().width+
				" :screen-height="+openglDisplay.getSize().height;
	}

}
