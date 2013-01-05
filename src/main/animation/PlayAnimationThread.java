package main.animation;

import javax.swing.JButton;
import javax.swing.JSlider;

import main.AnimatedFace;

public class PlayAnimationThread extends Thread {
	
	private RenderedAnimation animation;
	private JButton playButton;
	private JSlider timeSlider;

	public PlayAnimationThread(RenderedAnimation animation,JButton playButton, JSlider timeSlider) {
		this.animation=animation;
		this.playButton=playButton;
		this.timeSlider=timeSlider;
	}

	@Override
	public void run() 
	{
		playButton.setEnabled(false);
		timeSlider.setEnabled(false);
		long start;
		for(int frame=0;frame<animation.getNumberOfFrames();frame++)
		{
			start=System.currentTimeMillis();
			for(int index=0;index<animation.getNumberOfValues();index++)
			{
				AnimatedFace.window.controlContainer.setSliderPosition(index,animation.getFrameValue(frame,index));
			}
			AnimatedFace.window.animtationContainer.moveTimeSlider(frame);
			try {
				sleep(animation.getMilliSecondsPerFrame()-(System.currentTimeMillis()-start));
			} catch (InterruptedException e) {
			}
		}
		playButton.setEnabled(true);
		timeSlider.setEnabled(true);
	}

}
