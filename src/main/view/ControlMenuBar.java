package main.view;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import main.actions.LoadAnimationFileAction;
import main.actions.LoadTemplateFileAction;
import main.actions.LoadWaveFileAction;
import main.actions.SaveAnimationFileAction;
import main.actions.SaveTemplateFileAction;

public class ControlMenuBar extends JMenuBar {

	private JMenu filemenu;
	private JMenu templatemenu;
	private JMenu soundmenu;

	public ControlMenuBar() {
		
		add(filemenu=new JMenu("File"));
		filemenu.add(new JMenuItem(new LoadAnimationFileAction()));
		filemenu.add(new JMenuItem(new SaveAnimationFileAction()));
		
		add(templatemenu=new JMenu("Templates"));
		templatemenu.add(new JMenuItem(new LoadTemplateFileAction()));
		templatemenu.add(new JMenuItem(new SaveTemplateFileAction()));

		add(soundmenu=new JMenu("Sound"));
		soundmenu.add(new JMenuItem(new LoadWaveFileAction()));
	}

}
