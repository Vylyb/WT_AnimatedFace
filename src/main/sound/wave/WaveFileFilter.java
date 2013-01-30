package main.sound.wave;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class WaveFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		if(f.isDirectory())
			return true;
		
		return f.getName().toLowerCase().endsWith(".wav");
	}

	@Override
	public String getDescription() {
		return "Wave Audio Files";
	}

}
