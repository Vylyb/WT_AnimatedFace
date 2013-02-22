package main.analyzer;

import java.util.Vector;

public class Result {
	
	private String videofile;
	private Vector<String> words;
	private Vector<Integer> values;
	private int voiceEmph;
	private int faceEmph;
	private double[] midValues;
	private boolean nod;
	
	public Result() {
		words=new Vector<String>();
		values=new Vector<Integer>();
	}
	
	public boolean isNod() {
		return nod;
	}
	
	public void setMidValues(double[] midValues) {
		this.midValues = midValues;
	}
	
	public double[] getMidValues() {
		return midValues;
	}
	
	public int getFaceEmph() {
		return faceEmph;
	}
	
	public int getVoiceEmph() {
		return voiceEmph;
	}
	
	public boolean isVoiceOnly()
	{
		return voiceEmph>=0 && faceEmph<0;
	}
	
	public boolean isFaceOnly()
	{
		return voiceEmph<0 && faceEmph>=0;
	}
	
	public boolean isVoiceAndFace()
	{
		return voiceEmph>=0 && faceEmph==voiceEmph;
	}
	
	public boolean isVoiceAndFaceSeparated()
	{
		return voiceEmph>=0 && faceEmph>=0 && faceEmph!=voiceEmph;
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
		voiceEmph=parseVoiceEmphasize(videofile);
		faceEmph=parseFaceEmphasize(videofile);
		
		nod=videofile.startsWith("nod_");
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

	/*
	 * benshaar_synth_1_video8.avi 	=> 8 => 7
	 * johnsbein_synth_8_video5.avi	=> 5 => 4
	 * johnsbein_synth_9.avi		=> N/A    => -1
	 */
	private int parseFaceEmphasize(String videofile) {

		String video="video";

		if(videofile.matches("[a-zA-Z0-9\\_]+\\_"+video+"\\d\\.avi")) 
		{
			int index=videofile.indexOf(video)+video.length();
			int emph=Integer.parseInt(videofile.substring(index, index+1));

			//			if(emph%2==1)
			//				emph++;
			//			
			//			emph/=2;

			return emph-1;
		}

		return Integer.MIN_VALUE;
	}

	/*
	 * benshaar_synth_1_video8.avi 		=> 1 => 0
	 * nod_jimsrad_synth_7_video8.avi	=> 7 => 6
	 * johnsbein_monoton_video6.avi		=> N/A    => -1
	 */
	private int parseVoiceEmphasize(String videofile) {

		String synth="synth";

		if(videofile.matches("[a-zA-Z\\_]+\\_"+synth+"\\_\\d[a-zA-Z0-9\\.\\_]+"))
		{
			int index=videofile.indexOf(synth)+synth.length()+1;
			int emph=Integer.parseInt(videofile.substring(index, index+1));

			//			if(emph%2==1)
			//				emph++;
			//			
			//			emph/=2;

			return emph-1;
		}

		return Integer.MIN_VALUE;
	}

	public int getMaximumIndex() {
		double max=Double.MIN_VALUE;
		int index=-1;
		for(int i=0;i<midValues.length;i++)
		{
			if(midValues[i]>max)
			{
				max=midValues[i];
				index=i;
			}
		}
		return index;
	}

}
