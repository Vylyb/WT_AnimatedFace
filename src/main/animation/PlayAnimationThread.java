package main.animation;

import javax.swing.JButton;

import main.AnimatedFace;
import main.view.sliders.TimeSlider;

public class PlayAnimationThread extends Thread {
	
	private RenderedAnimation animation;
	private JButton playButton;
	private TimeSlider timeSlider;

	public PlayAnimationThread(RenderedAnimation animation,JButton playButton, TimeSlider timeSlider) {
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
//				AnimatedFace.window.controlContainer.setSliderPosition(index,animation.getFrameValue(frame,index));
				AnimatedFace.window.controlContainer.setValue(index, animation.getFrameValue(frame,index));
			}
			AnimatedFace.window.animationContainer.moveTimeSlider(frame+1);
			try {
				sleep(animation.getMilliSecondsPerFrame()-(System.currentTimeMillis()-start));
			} catch (Exception e) {
			}
		}
		playButton.setEnabled(true);
		timeSlider.setEnabled(true);
	}

}
