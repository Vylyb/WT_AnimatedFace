package main.animation;
import java.util.Vector;

import main.AnimatedFace;


public class AnimationStep {

	private int time;
	private Vector<ChangedValue> changedvalues;
	private int duration=-1;

	public AnimationStep(int time) {
		this.time = time;
		changedvalues=new Vector<ChangedValue>();
	}
	
	public AnimationStep() {
		changedvalues=new Vector<ChangedValue>();
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String toExtendedString() {
		String s="Step @ "+AnimatedFace.toSecondsAndFrames(time)+" sec (frame "+time+")";
		for(ChangedValue c:changedvalues)
		{
			s+="\n\tID "+c.getValueID()+"\tnew Value: "+c.getChangedValue();
		}
		return s;
	}

	@Override
	public String toString() {
		return AnimatedFace.toSecondsAndFrames(time)+" ("+AnimatedFace.toSecondsAndFrames(duration)+" sec)";
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getValue(int valueID){
		for(ChangedValue c:changedvalues)
		{
			if(c.getValueID()==valueID)
			{
				return c.getChangedValue();
			}
		}
		return -1;
	}

	public boolean addChangedValue(ChangedValue changedvalue){
		for(ChangedValue c:changedvalues)
		{
			if(c.getValueID()==changedvalue.getValueID())
			{
				return false;
			}
		}
		changedvalues.add(changedvalue);

		return true;
	}

	public boolean changeNewValueOfID(int id,int newValue){
		for(ChangedValue c:changedvalues)
		{
			if(c.getValueID()==id)
			{
				c.setChangedValue(newValue);
				return true;
			}
		}
		return false;
	}

	public Vector<ChangedValue> getChangedValues() {
		return changedvalues;
	}
	
}
