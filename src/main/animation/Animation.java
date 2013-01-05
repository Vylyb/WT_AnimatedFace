package main.animation;

import java.util.Vector;

import main.view.ControlContainer;

public class Animation{

	private Vector<AnimationStep> steps;
	private int duration;
	private ControlContainer control;

	public Animation(ControlContainer control) {
		steps=new Vector<AnimationStep>();
		this.control=control;
	}

	/**
	 * @return duration of this {@link Animation} in frames
	 */
	public int getNumberOfFrames() {
		return duration;
	}

	public int getNumberOfValues(){
		return control.getNumberOfValues();
	}

	public boolean addStep(AnimationStep step)
	{
		for(AnimationStep s:steps)
		{
			if(s.getTime()==step.getTime())
			{
				return false;
			}
		}

		if(steps.size()==0)
		{
			steps.add(step);
		}

		else if(step.getTime()<steps.firstElement().getTime())
		{
			steps.insertElementAt(step, 0);
		}

		else if(step.getTime()>steps.lastElement().getTime())
		{
			steps.add(step);
		}

		else
		{
			for(int i=0;i<steps.size()-1;i++)
			{
				if(step.getTime()>steps.get(i).getTime())
				{
					steps.insertElementAt(step, i+1);
				}
			}
		}

		if(steps.size()>1)
		{
			steps.get(steps.size()-1).setDuration(step.getTime()-steps.get(steps.size()-2).getTime());
		}
		else if(steps.size()==1)
		{
			steps.get(0).setDuration(step.getTime());
		}

		duration=steps.lastElement().getTime();
		return true;
	}

	/**
	 * @return last Element of the {@link Vector}<{@link AnimationStep}> <code>steps</code>
	 */
	public AnimationStep lastStep() {
		return steps.lastElement();
	}

	/**
	 * @return copy of the {@link Vector}<{@link AnimationStep}> <code>steps</code> 
	 */
	@SuppressWarnings("unchecked")
	public Vector<AnimationStep> getSteps() {
		return (Vector<AnimationStep>)steps.clone();
	}

	public int getValueFromStep(int stepID,int valueID)
			throws NullPointerException{
		return steps.get(stepID).getValue(valueID);
	}

	public int getLastValue(int valueID) 
			throws Exception {
		return control.window.animtationContainer.getLastValue(valueID);
	}

}
