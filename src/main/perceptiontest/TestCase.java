package main.perceptiontest;

import java.util.Vector;

public class TestCase {

	private String videoFile;
	private int faceEmphasize,voiceEmphasize;
	private Vector<String> words;
	private Vector<Integer> userSelections;
	
	public TestCase() {
		words=new Vector<String>();
		userSelections=new Vector<Integer>();
	}
	
	public String getVideoFile() {
		return videoFile;
	}
	
	public void addWord(String word){
		words.add(word);
		userSelections.add(new Integer(0));
	}
	
	public void setVideoFile(String videoFile) {
		this.videoFile = videoFile;
	}
	
	public int getFaceEmphasize() {
		return faceEmphasize;
	}
	
	public void setFaceEmphasize(int faceEmphasize) {
		this.faceEmphasize = faceEmphasize;
	}
	
	public int getVoiceEmphasize() {
		return voiceEmphasize;
	}
	
	public void setVoiceEmphasize(int voiceEmphasize) {
		this.voiceEmphasize = voiceEmphasize;
	}
	
	@Override
	public String toString() {
		return "TestCase | Video: "+getVideoFile();
	}

	public Vector<String> getWords() {
		return words;
	}

	public void setUserSelectionForWord(String word, int value) {
		int i=0;
		for(String w:words)
		{
			if(w.matches(word))
			{
				userSelections.set(i, new Integer(value));
			}
			i++;
		}
	}

	public int getNumberOfWords() {
		return words.size();
	}
	
	public String getUserSelection(int index){
		try {
			return words.get(index)+"\t"+userSelections.get(index);
		} catch (Exception e) {
		}
		return "";
	}
}
