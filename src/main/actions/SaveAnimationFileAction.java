package main.actions;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import main.AnimatedFace;

public class SaveAnimationFileAction extends XmlFileAction {

	public SaveAnimationFileAction() {
		super("Save Animation As ...");
	}
	
	@Override
	protected void actionPerformed() {
		chooser=new JFileChooser(lastDirectory);
		chooser.setFileFilter(filter);
		chooser.showSaveDialog(AnimatedFace.window);
		
		try {
			file=chooser.getSelectedFile();
			if(!file.isDirectory()){
				if(!file.getName().toLowerCase().endsWith(".xml")){
					file=new File(file.getAbsolutePath()+".xml");
				}
				if(!file.exists()){
					if(file.createNewFile()){
						AnimatedFace.saveAnimation(file);
					}
				}
				else{
					AnimatedFace.saveAnimation(file);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
