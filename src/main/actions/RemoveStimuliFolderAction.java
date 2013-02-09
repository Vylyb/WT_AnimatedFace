package main.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import main.TestCaseGenerator;

public class RemoveStimuliFolderAction extends StimuliFolderAction {

	public RemoveStimuliFolderAction() {
		super(null);
	}

	@Override
	public Object getValue(String key) {
		if(key.matches(Action.NAME))
			return "Diesen Ordner und alle Unterordner entfernen"; 
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		TestCaseGenerator.window.folderlist.removeSelectedUsedFolder();
		super.actionPerformed(e);
	}

}
