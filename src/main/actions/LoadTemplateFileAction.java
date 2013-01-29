package main.actions;

import javax.swing.JFileChooser;

import main.AnimatedFace;

public class LoadTemplateFileAction extends XmlFileAction {

	public LoadTemplateFileAction() {
		super("Load a Template ...");
	}

	@Override
	protected void actionPerformed() {
		chooser=new JFileChooser(lastDirectory);
		chooser.setFileFilter(filter);
		chooser.showOpenDialog(AnimatedFace.window);
		
		if(checkFile()){
			try {
				AnimatedFace.window.animationContainer.loadTemplate(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
