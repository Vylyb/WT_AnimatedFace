package main;
import javax.swing.JButton;
import javax.swing.JSlider;

import main.animation.Animation;
import main.animation.RenderedAnimation;
import main.view.Window;



public class AnimatedFace{

	public static boolean editorMode=true;

	public static int FPS=25,MAX_SEC=100;

	public static Animation currentAnimation;

	public static RenderedAnimation renderedAnimation;

	public static Window window;

	public static void main(String[] args) {
		window=new Window();
		window.setVisible(true);
	}

	public static String toSecondsAndFrames(int time) {
		int seconds=(int)time/FPS;
		int frames=(int)time%FPS;
		return (seconds<10?"0":"")+seconds+":"+(frames<10?"0":"")+frames;
	}

	public static int framesfromString(String time)
			throws NullPointerException{
		String start=time.split(" \\(")[0];
		int seconds=Integer.parseInt(start.substring(0, start.indexOf(":")));
		int frames=Integer.parseInt(start.substring(start.indexOf(":")+1,start.length()));
		return seconds*FPS+frames;
	}

	public static void playCurrentAnimation(JButton playButton, JSlider timeSlider){
		if(renderedAnimation==null){
			renderCurrentAnimation();
		}
		renderedAnimation.play(playButton,timeSlider);
	}

	public static void renderCurrentAnimation(){
		renderedAnimation=new RenderedAnimation(currentAnimation.getNumberOfFrames());
		renderedAnimation.render(currentAnimation);
	}
}
