package main.actions;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import main.xml.XmlFilter;

abstract class XmlFileAction extends ControlAction {

	protected String lastDirectory="";
	protected FileFilter filter;
	protected File file;
	protected JFileChooser chooser;

	protected XmlFileAction(String label) {
		super(label);
		filter=new XmlFilter();
	}
	
	protected boolean checkFile(){
		if((file=chooser.getSelectedFile())!=null){
			if(!file.isDirectory()){
				try {
					if(file.getParentFile().isDirectory()){
						lastDirectory=file.getParent();
						return true;
					}
				} catch (Exception e) {
				}
			}
		}
		return false;
	}

}
