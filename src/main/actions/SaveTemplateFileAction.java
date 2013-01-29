package main.actions;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import main.AnimatedFace;

public class SaveTemplateFileAction extends XmlFileAction {

	public SaveTemplateFileAction() {
		super("Save the current Face as a Template");
		lastDirectory="facetemplates";
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
						AnimatedFace.window.animationContainer.saveTemplate(file);
					}
				}
				else{
					AnimatedFace.window.animationContainer.saveTemplate(file);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
