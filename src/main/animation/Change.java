package main.animation;

public class Change {
	
	private int valueID;
	private int valueChange;
	
	public Change(int valueID, int valueChange) {
		this.valueID = valueID;
		this.valueChange = valueChange;
	}
	
	public int getValueID() {
		return valueID;
	}
	
	public int getValueChange() {
		return valueChange;
	}
	
	public void setValueChange(int valueChange) {
		this.valueChange = valueChange;
	}
	
}
