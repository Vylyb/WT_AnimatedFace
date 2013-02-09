package main.testgenerator.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import main.testgenerator.folder.FolderList;

public class GeneratorWindow extends JFrame {
	
	public FolderList folderlist;
	public ControlPanel controlpanel;

	public GeneratorWindow() {
		setLayout(new BorderLayout(10, 10));
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		add(controlpanel=new ControlPanel(),BorderLayout.NORTH);
		add(folderlist=new FolderList(),BorderLayout.CENTER);
	}

}
