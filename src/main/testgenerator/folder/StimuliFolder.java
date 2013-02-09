package main.testgenerator.folder;

import java.io.File;
import java.util.Vector;

public class StimuliFolder {

	private String path;
	private String name;
	
	private Vector<String> sentence;

	public StimuliFolder(String path) {
		this.path=path;
		this.name=path.substring(path.lastIndexOf(File.separator)+1);
		sentence=new Vector<String>();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getPath() {
		return path;
	}
	
	public File toFile()
	{
		return new File(path);
	}
	
	public void addWordToSentence(String word)
	{
		sentence.add(word);
	}
	
	public int countWords(){
		return sentence.size();
	}
	
	public String getSentence() {
		String sent="";
		for(String word:sentence)
		{
			sent+=word+" ";
		}
		return sent;
	}
	
	public String getSentenceAsXML(){
		String sent="";
		for(String word:sentence)
		{
			sent+="<word>"+word+"</word>\n";
		}
		return sent;
	}
}
