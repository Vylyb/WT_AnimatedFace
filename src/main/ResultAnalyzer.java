package main;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedHashMap;
import java.util.Vector;

import main.analyzer.Result;

public class ResultAnalyzer {
	
	private static LinkedHashMap<String, Vector<Result>> results;
	private static File folder;
	private static int countFiles=0;
	private final static int MAX=3;
	private final static int MIN=1; 
	private final static String colFace="F79F81",colVoice="CED8F6",colBoth="cdcdcd";

	public static void main(String[] args) {
		
		results=new LinkedHashMap<String, Vector<Result>>();
		Result newResult=null;
		
		try {
			folder=new File("logs"+File.separator+args[0]);
			if(folder.isDirectory())
			{
				System.out.println("Analyzing Folder '"+folder.getName()+"'");
				for(File f:folder.listFiles())
				{
					if(f.getName().endsWith(".txt"))
					{
						System.out.println("Analyzing File '"+f.getName()+"'");
						countFiles++;
						
						BufferedReader reader=new BufferedReader(new FileReader(f));
						
						String line="";
						
						while((line=reader.readLine())!=null)
						{
							if(line.startsWith("Video: "))
							{
								if(newResult!=null)
								{
									addResult(newResult);
								}
								newResult=new Result();
								newResult.setVideofile(line.substring(line.indexOf(" ")+1, line.length()));
//								System.out.println("Video File: '"+newResult.getVideofile()+"'");
							}
							else if(line.matches("\\d\\s[a-zA-Z\\.aöüÄÖÜ']+\\s\\d")) //ziffer wort ziffer
							{
								String word=line.substring(line.indexOf(" ")+1,line.lastIndexOf(" "));
								if(word.matches("John"))
									word="Johns";
								int value=Math.min(MAX, Math.max(MIN, Integer.parseInt(line.substring(line.lastIndexOf(" ")+1,line.length()))));
								
								newResult.addWordValue(word, value);

//								System.out.println("Word: '"+word+"'");
//								System.out.println("Value: '"+value+"'");
							}
						}
						
						reader.close();
					}
				}
			}
			System.out.println("\nResults:\n");
			printResults();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printResults() {
		String[] keys=new String[results.keySet().size()];
		results.keySet().toArray(keys);
		boolean sorted;
		String temp;
		do
		{
			sorted=true;
			for(int i=0;i<keys.length-1;i++)
			{
				if(keys[i].compareTo(keys[i+1])>0)
				{
					sorted=false;
					temp=keys[i];
					keys[i]=keys[i+1];
					keys[i+1]=temp;
				}
			}
		}
		while(!sorted);
		
		int cols=results.get(keys[0]).get(0).countWordValues();
		
		try {
			BufferedWriter writer=new BufferedWriter(new FileWriter(new File(folder.getAbsolutePath()+File.separator+"results.html")));
			
			String title="Analyse Wahrnehmungstest | "+folder.getName();
			
			writer.write("<html><head>\n" +
					"<link rel=\"stylesheet\" href=\"http://twitter.github.com/bootstrap/assets/css/bootstrap.css\"/>" +
					"\n<style type=\"text/css\">" +
					"\nH4 { color: rgb(0,100,100) }" +
					"\nH5 { color: rgb(0,0,0) }" +
					"\n</style><title>"+title+"</title></head>\n" +
					"<body><center><h3>"+title+"</h3><br>\n" +
					"<h4>Auswertung von "+countFiles+" Tests</h4><br>\n" +
					"<h4>Bedeutung der Farben:</h4>" +
					"\n<table border=1 width=60%><tr>" +
					"<td align=center width=20% bgcolor="+colFace+"><h5>Betonung durch Mimik</h5>" +
					"<td align=center width=20% bgcolor="+colVoice+"><h5>Betonung durch Stimme</h5>" +
					"<td align=center width=20% bgcolor="+colBoth+"><h5>Betonung durch Mimik und Stimme</h5>" +
					"</tr></table><br><br>\n" +
					"<table width=70% border=1>\n");
			
			boolean firstLine=true;
			for(String key:keys)
			{
				Vector<Result> vector=results.get(key);
				
				System.out.println(vector.get(0).getVideofile());
				writer.write("<tr><td align=left bgcolor=#eeeeee colspan="+cols+"><h4>&nbsp;&nbsp;" +
						vector.get(0).getVideofile()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<small>" +
						vector.size()+
						" Bewertungen</small></h5></td></tr>\n");
				
				double[] midValues=new double[cols];
				for(Result result:vector)
				{
					for(int i=0;i<result.countWordValues();i++)
					{
						midValues[i]+=result.getValue(i).doubleValue();
					}
				}
				
				writer.write("<tr>\n");
				
				int voice_emph=parseVoiceEmphasize(vector.get(0).getVideofile());
				int face_emph=parseFaceEmphasize(vector.get(0).getVideofile());
				String bgcolor;
				for(int i=0;i<cols;i++)
				{
					if(voice_emph==face_emph && voice_emph==i)
					{
						bgcolor=" bgcolor=#"+colBoth;
					}
					else if(voice_emph==i)
					{
						bgcolor=" bgcolor=#"+colVoice;
					}
					else if(face_emph==i)
					{
						bgcolor=" bgcolor=#"+colFace;
					}
					else
					{
						bgcolor="";
					}
					
					writer.write("<td align=center"+bgcolor+" width="+(100/cols)+"%><h5>" +
							vector.get(0).getWord(i)+"</h5></td>\n");
				}
				writer.write("</tr>\n");

				writer.write("<tr>\n");
				for(int i=0;i<cols;i++)
				{
					midValues[i]=((int)((midValues[i]*1000)/(double)vector.size()))/1000.0;
					System.out.print(vector.get(0).getWord(i)+" "+midValues[i]+" |\t");
					writer.write("<td align=center bgcolor=" +
							getRelativeColor(Color.GREEN,midValues[i])+
							(firstLine?" width="+(100/cols)+"%":"") +
							">" + midValues[i]+"</td>\n");
				}
				System.out.println("\n");
				writer.write("</tr>\n");
				
				firstLine=false;
			}
			
			writer.write("</table><br></body></html>");
			
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * benshaar_synth_1_video8.avi 	=> 8 => 4 => 3
	 * johnsbein_synth_8_video5.avi	=> 5 => 3 => 2
	 * johnsbein_synth_9.avi		=> N/A    => -1
	 */
	private static int parseFaceEmphasize(String videofile) {

		String video="video";
		
		if(videofile.matches("[a-zA-Z0-9\\_]+\\_"+video+"\\d\\.avi")) 
		{
			int index=videofile.indexOf(video)+video.length();
			int emph=Integer.parseInt(videofile.substring(index, index+1));
			
			if(emph%2==1)
				emph++;
			
			emph/=2;
			
			return emph-1;
		}
		
		return -1;
	}

	/*
	 * benshaar_synth_1_video8.avi 		=> 1 => 1 => 0
	 * nod_jimsrad_synth_7_video8.avi	=> 7 => 4 => 3
	 * johnsbein_monoton_video6.avi		=> N/A    => -1
	 */
	private static int parseVoiceEmphasize(String videofile) {
		
		String synth="synth";

		if(videofile.matches("[a-zA-Z\\_]+\\_"+synth+"\\_\\d[a-zA-Z0-9\\.\\_]+"))
		{
			int index=videofile.indexOf(synth)+synth.length()+1;
			int emph=Integer.parseInt(videofile.substring(index, index+1));
			
			if(emph%2==1)
				emph++;
			
			emph/=2;
			
			return emph-1;
		}
		
		return -1;
	}

	private static String getRelativeColor(Color color, double value) {
		double factor=MAX-(double)MAX/value;
		int r=Math.min((int)(color.getRed() *(1/factor)), 255);
		int g=Math.min((int)(color.getGreen() *(1/factor)), 255);
		int b=Math.min((int)(color.getBlue() *(1/factor)), 255);
		return "#"+(r<16?"0":"")+Integer.toHexString(r)
				+(g<16?"0":"")+Integer.toHexString(g)
				+(b<16?"0":"")+Integer.toHexString(b);
	}

	private static void addResult(Result result) {
		if(results.get(result.getVideofile())==null)
		{
			Vector<Result> vector=new Vector<Result>();
			vector.add(result);
			results.put(result.getVideofile(), vector);
		}
		else
		{
			((Vector<Result>)results.get(result.getVideofile())).add(result);
		}
	}

}
