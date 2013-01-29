package main.xml;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class XmlFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		if(f.isDirectory())
			return true;
		
		return f.getName().toLowerCase().endsWith(".xml");
	}

	@Override
	public String getDescription() {
		return "XML Files with Animations";
	}

}
