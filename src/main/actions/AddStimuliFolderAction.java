package main.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import main.TestCaseGenerator;
import main.testgenerator.folder.StimuliFolder;


public class AddStimuliFolderAction extends StimuliFolderAction {
	
	public AddStimuliFolderAction(StimuliFolder folder) {
		super(folder);
	}

	@Override
	public Object getValue(String key) {
		if(key.matches(Action.NAME))
			return "Aus diesem Ordner und allen Unterordnern Tests generieren"; 
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		TestCaseGenerator.addUsedFolder(folder);
		super.actionPerformed(e);
	}

}
