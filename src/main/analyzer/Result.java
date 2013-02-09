package main.analyzer;

import java.util.Vector;

public class Result {
	
	String videofile;
	Vector<String> words;
	Vector<Integer> values;
	
	public Result() {
		words=new Vector<String>();
		values=new Vector<Integer>();
	}
	
	public int countWordValues() {
		return Math.min(words.size(), values.size());
	}
	
	public void addWordValue(String word,int value){
		words.add(word);
		values.add(value);
	}
	
	public void setVideofile(String videofile) {
		this.videofile = videofile;
	}
	
	public String getVideofile() {
		return videofile;
	}
	
	public String getWord(int index){
		try {
			return words.get(index);
		} catch (Exception e) {
			return "";
		}
	}

	public Integer getValue(int index){
		try {
			return values.get(index);
		} catch (Exception e) {
			return null;
		}
	}

}
