package main.perceptiontest;

import java.util.Vector;

public class TutorialTestCase extends TestCase {
	
	private Vector<Integer> values;
	
	public TutorialTestCase() {
		super();
		values=new Vector<Integer>();
	}

	public void addValue(int value){
		if(value<PerceptionTestWindow.MIN)
			value=PerceptionTestWindow.MIN;
		
		if(value>PerceptionTestWindow.MAX)
			value=PerceptionTestWindow.MAX;
		
		values.add(new Integer(value));
	}

	public void addValue(Integer value){
		addValue(value.intValue());
	}
	
	@Override
	public String toString() {
		return getVideoFile()+"\t"+getNumberOfValues()+" Values";
	}
	
	public int getNumberOfValues(){
		return values.size();
	}
	
	public int getValue(int index){
		try {
			return values.get(index);
		} catch (Exception e) {
			return PerceptionTestWindow.MIN;
		}
	}
}
