package main.testgenerator.folder;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class StimuliFileFilter extends FileFilter {
	
	private String[] types={"wav","avi"};

	@Override
	public boolean accept(File f) {
		
		for(String type:types)
		{
			if(f.getName().endsWith("."+type))
				return true;
		}
		
		return f.isDirectory();
	}
	
	

	@Override
	public String getDescription() {
		String s="Ordner mit Stimuli (";
		
		for(String type:types)
		{
			s+=type+", ";
		}
		
		return s.substring(0, s.length()-2)+")";
	}

}
