package main.testgenerator.file;

import java.io.File;

import main.testgenerator.folder.StimuliFolder;

public class FileWithSentence {

	private File file;
	private StimuliFolder folder;

	public FileWithSentence(File f, StimuliFolder parentFolder) {
		this.file=f;
		this.folder=parentFolder;
	}
	
	public File getFile() {
		return file;
	}
	
	public StimuliFolder getFolder() {
		return folder;
	}

	@Override
	public String toString() {
		return file.getName()+"\t"+folder.getSentence();
	}
}
