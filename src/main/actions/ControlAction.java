package main.actions;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

public abstract class ControlAction implements Action {

	protected boolean enabled=true;
	private String label;
	
	public ControlAction(String label) {
		this.label=label;
	}
	
	protected void actionPerformed(){
		System.err.println("Method actionPerformed has to be implemented in Child Classes!");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getModifiers()==16){
			actionPerformed();
		}
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getValue(String key) {
		if(key==NAME)
			return label;
		return null;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void putValue(String key, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {}

	@Override
	public void setEnabled(boolean b) {
		enabled=b;
	}

}
