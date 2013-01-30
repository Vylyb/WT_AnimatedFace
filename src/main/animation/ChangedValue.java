package main.animation;

public class ChangedValue {
	
	private int valueID;
	private int changedValue;
	
	public ChangedValue() {
	}
	
	public ChangedValue(int valueID, int changedValue) {
		this.valueID = valueID;
		this.changedValue = changedValue;
	}
	
	public int getValueID() {
		return valueID;
	}
	
	public int getChangedValue() {
		return changedValue;
	}
	
	public void setChangedValue(int valueChange) {
		this.changedValue = valueChange;
	}
	
	public void setValueID(int valueID) {
		this.valueID = valueID;
	}
	
}
