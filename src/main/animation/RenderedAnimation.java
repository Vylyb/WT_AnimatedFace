package main.animation;

import javax.swing.JButton;

import main.AnimatedFace;
import main.view.ControlContainer;
import main.view.sliders.TimeSlider;

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
			if(step.getTime()==0)
			{
				for(ChangedValue c:step.getChangedValues())
				{
					frameValues[currentFrame][c.getValueID()]=c.getChangedValue();
				}
				startValuesIndex=currentFrame;
				currentFrame++;
			}
			else
			{
				double changesPerFrame[]=new double[animation.getNumberOfValues()];
				int val;
				for(int k=0;k<animation.getNumberOfValues();k++){

					if((val=step.getValue(k))>=0 && val<=ControlContainer.MAX)
					{
						changesPerFrame[k]=(double)((double)(val-frameValues[startValuesIndex][k])/step.getDuration());
					}
					else
					{
						changesPerFrame[k]=0.0;
					}
				}

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
								if((frameValues[currentFrame][k]=step.getValue(k))<0)
								{
									frameValues[currentFrame][k]=frameValues[startValuesIndex][k];
								}
							}
							/*
							 * interpolate the values for the other frames
							 */
							else
							{
								frameValues[currentFrame][k]=(int)((double)frameValues[startValuesIndex][k]+(double)i*changesPerFrame[k]);
							}
						}
						currentFrame++;
					}
				}
				startValuesIndex=currentFrame-1;
			}
		}

	}

	public void play(JButton playButton, TimeSlider timeSlider) {
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
