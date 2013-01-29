package main.actions;

import javax.swing.JFileChooser;

import main.AnimatedFace;

public class LoadAnimationFileAction extends XmlFileAction {

	public LoadAnimationFileAction() {
		super("Open Animation File");
		lastDirectory="animations";
	}

	@Override
	protected void actionPerformed() {
		chooser=new JFileChooser(lastDirectory);
		chooser.setFileFilter(filter);
		chooser.showOpenDialog(AnimatedFace.window);
		
		if(checkFile()){
			try {
				AnimatedFace.loadAnimation(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
