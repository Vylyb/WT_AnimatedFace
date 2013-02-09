package main;

import main.testgenerator.folder.StimuliFolder;
import main.testgenerator.view.GeneratorWindow;

public class TestCaseGenerator {
	
	public static GeneratorWindow window;
	
	public static void main(String[] args) {
		window=new GeneratorWindow();
		window.setVisible(true);
	}

	public static void addUsedFolder(StimuliFolder folder) {
		window.folderlist.addUsedFolder(folder);
	}

}
