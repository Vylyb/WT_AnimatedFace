package main.animation;
import java.util.Vector;

import main.AnimatedFace;


public class AnimationStep {

	private int time;
	private Vector<Change> changes;
	private int duration=-1;

	public AnimationStep(int time) {
		this.time = time;
		changes=new Vector<Change>();
	}
	
	public AnimationStep() {
		changes=new Vector<Change>();
	}
	
	public double getChangePerFrame(int valueID){
		for(Change c:changes)
		{
			if(c.getValueID()==valueID)
			{
				return (double)((double)c.getValueChange()/(double)duration);
			}
		}
		return 0.0;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String toExtendedString() {
		String s="Step @ "+AnimatedFace.toSecondsAndFrames(time)+" sec (frame "+time+")";
		for(Change c:changes)
		{
			s+="\n\tID "+c.getValueID()+"\tnew Value: "+c.getValueChange();
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
		for(Change c:changes)
		{
			if(c.getValueID()==valueID)
			{
				return c.getValueChange();
			}
		}
		return -1;
	}

	public boolean addChange(Change change){
		for(Change c:changes)
		{
			if(c.getValueID()==change.getValueID())
			{
				return false;
			}
		}
		changes.add(change);

		return true;
	}

	public boolean changeNewValueOfID(int id,int newValue){
		for(Change c:changes)
		{
			if(c.getValueID()==id)
			{
				c.setValueChange(newValue);
				return true;
			}
		}
		return false;
	}

	public Vector<Change> getChanges() {
		return changes;
	}
	
	public int getEndValueOfStep(int valueID){
		for(Change c:changes)
		{
			if(c.getValueID()==valueID)
			{
				return c.getValueChange();
			}
		}
		return -1;
	}

}
