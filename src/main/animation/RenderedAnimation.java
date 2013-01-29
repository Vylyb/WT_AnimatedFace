package main.animation;

import javax.swing.JButton;
import javax.swing.JSlider;

import main.AnimatedFace;

public class RenderedAnimation{

	private int frames;
	private long milliSecondsPerFrame;
	private int[][] frameValues;

	public RenderedAnimation(int frames) {
		this.frames = frames;
		this.milliSecondsPerFrame=1000/AnimatedFace.FPS;
	}

	public int getNumberOfFrames() {
		return frames;
	}

	public void render(Animation animation) 
	{
		frameValues=new int[frames][animation.getNumberOfValues()];

		int currentFrame=0,startValuesIndex=0;

		for(AnimationStep step:animation.getSteps())
		{
			if(step.getDuration()==0)
			{
				for(Change c:step.getChanges())
				{
					frameValues[currentFrame][c.getValueID()]=c.getValueChange();
				}
				startValuesIndex=currentFrame;
				currentFrame++;
			}
			else
			{
				for(int i=1;i<=step.getDuration();i++)
				{
					if(currentFrame<frames)
					{
						for(int k=0;k<animation.getNumberOfValues();k++)
						{
							/*
							 * use the value set at the end
							 */
							if(i==step.getDuration())
							{
								if((frameValues[currentFrame][k]=step.getEndValueOfStep(k))<0)
								{
									frameValues[currentFrame][k]=frameValues[startValuesIndex][k];
								}
							}
							/*
							 * interpolate the values for the other frames
							 */
							else
							{
								frameValues[currentFrame][k]=(int)((double)frameValues[startValuesIndex][k]+(double)i*step.getChangePerFrame(k));
							}
						}
						currentFrame++;
					}
				}
				startValuesIndex=currentFrame-1;
			}
		}

	}

	public void play(JButton playButton, JSlider timeSlider) {
		if(AnimatedFace.window.controlContainer!=null
				&& AnimatedFace.window.animationContainer!=null)
			(new PlayAnimationThread(this,playButton,timeSlider)).start();
		else
			System.err.println("no access to control container or animation container!");
	}

	public int getNumberOfValues() {
		try {
			return frameValues.length;
		} catch (Exception e) {
			return -1;
		}
	}
	
	public long getMilliSecondsPerFrame() {
		return milliSecondsPerFrame;
	}

	public int getFrameValue(int frame, int index) {
		try {
			return frameValues[frame][index];
		} catch (Exception e) {
			return -1;
		}
	}

}
