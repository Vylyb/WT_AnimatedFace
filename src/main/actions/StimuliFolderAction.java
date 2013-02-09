package main.actions;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import main.TestCaseGenerator;
import main.testgenerator.folder.StimuliFolder;

abstract class StimuliFolderAction implements Action {
	
	protected StimuliFolder folder;
	
	public StimuliFolderAction(StimuliFolder folder) {
		this.folder = folder;
	}

	private boolean enabled=true;

	@Override
	public void putValue(String key, Object value) {
		
	}

	@Override
	public void setEnabled(boolean enable) {
		enabled=enable;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		TestCaseGenerator.window.controlpanel.updatePanel();
	}

}
