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
	private static File resultFolder;
	private final static int MAX=3,MIN=1,EMPHS=9; 
	private final static String colFace="ff0000",colVoice="0000ff",colBoth="888888";
	private final static Color resColor=Color.GREEN;

	public static void main(String[] args) {

		if(args.length==1)
		{
			results=new LinkedHashMap<String, Vector<Result>>();
			createResultOverview(args[0]);
			createParticipantOverview(args[0]);
		}
		else
		{
			System.err.println("Subfolder with test results needed as 1st parameter!");
		}
	}

	private static void createParticipantOverview(String subfolder) {
		try {
			folder=new File("logs"+File.separator+subfolder);
			if(folder.isDirectory())
			{
				resultFolder=new File(folder.getAbsolutePath()+File.separator+"results");
				if(!resultFolder.exists())
					resultFolder.mkdirs();

				File out=new File(resultFolder.getAbsolutePath()+File.separator+"teilnehmer.txt");
				//				if(!out.exists())
				//					out.createNewFile();

				BufferedWriter writer=new BufferedWriter(new FileWriter(out));
				String filename="",videofile="",word="";
				int index = 0,value = 0,faceEmphWord = 0,voiceEmphWord = 0;

				/*
				 * 1. Versuchsperson; 
				 * 2. Stimulusdatei; 
				 * 3. Ergebnis Silbe 1; 
				 * 4. Silbe 1 akustische Prominenz ja/nein; 
				 * 5. 4. Silbe 1 visuelle Prominenz ja/nein				
				 */

				for(File f:folder.listFiles())
				{
					if(!f.isDirectory() && f.getName().endsWith(".txt"))
					{
						BufferedReader reader=new BufferedReader(new FileReader(f));

						filename=f.getName().substring(0, f.getName().indexOf("."));

						String line="";

						while((line=reader.readLine())!=null)
						{
							if(line.startsWith("Video: "))
							{
								videofile=line.substring(line.indexOf(" ")+1, line.length());
								voiceEmphWord=parseVoiceEmphasizeForWord(videofile);
								faceEmphWord=parseFaceEmphasizeForWord(videofile);
							}
							else if(line.matches("\\d\\s[a-zA-Z\\.aöüÄÖÜ']+\\s\\d")) //ziffer wort ziffer
							{
								index=Math.min(5, Math.max(MIN, Integer.parseInt(line.substring(0,line.indexOf(" ")))));
								value=Math.min(MAX, Math.max(MIN, Integer.parseInt(line.substring(line.lastIndexOf(" ")+1,line.length()))));

								word=line.substring(line.indexOf(" ")+1,line.lastIndexOf(" "));
								if(word.matches("John"))
									word="Johns";

								//								//System.out.println(line);
								//								//System.out.println(filename+"\t"+videofile+"\t"+value+"\t"+(faceEmphWord==index?"ja":"nein")+"\t"+(voiceEmphWord==index?"ja":"nein")+"\t["+index+": "+word+"]");
								writer.write(filename+"\t"+videofile+"\t"+value+"\t"+(faceEmphWord==index?"ja":"nein")+"\t"+(voiceEmphWord==index?"ja":"nein")+"\t["+index+": "+word+"]\n");
								//								//System.out.println("index: "+index+"\tvalue: "+value+"\tvoice: "+voiceEmphWord+"\tface: "+faceEmphWord);
								//								//System.out.println();
							}
						}
						reader.close();
					}
				}
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createResultOverview(String subfolder) {
		Result newResult=null;

		try {
			folder=new File("logs"+File.separator+subfolder);
			if(folder.isDirectory())
			{
				//System.out.println("Analyzing Folder '"+folder.getName()+"'");
				for(File f:folder.listFiles())
				{
					if(f.getName().endsWith(".txt"))
					{
						//System.out.println("Analyzing File '"+f.getName()+"'");
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
								//								//System.out.println("Video File: '"+newResult.getVideofile()+"'");
							}
							else if(line.matches("\\d\\s[a-zA-Z\\.aöüÄÖÜ']+\\s\\d")) //ziffer wort ziffer
							{
								String word=line.substring(line.indexOf(" ")+1,line.lastIndexOf(" "));
								if(word.matches("John"))
									word="Johns";
								int value=Math.min(MAX, Math.max(MIN, Integer.parseInt(line.substring(line.lastIndexOf(" ")+1,line.length()))));

								newResult.addWordValue(word, value);

								//								//System.out.println("Word: '"+word+"'");
								//								//System.out.println("Value: '"+value+"'");
							}
						}

						reader.close();
					}
				}
			}
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

		int words=results.get(keys[0]).get(0).countWordValues();

		try {
			resultFolder=new File(folder.getAbsolutePath()+File.separator+"results");
			if(!resultFolder.exists())
				resultFolder.mkdirs();

			final int voiceOnly=0,
					faceOnly=1,
					voiceAndFaceSeparated=2,
					voiceAndFace=3;

			final int maxOnVoice=1,
					maxOnFace=2,
					maxBeforeVoice=3,
					maxBeforeFace=4,
					maxAfterVoice=5,
					maxAfterFace=6;

			final String[] rowNames={
					"Auf Prominenz<br>der Stimme",
					"Auf Prominenz<br>der Mimik",
					"Vor Prominenz<br>der Stimme",
					"Vor Prominenz<br>der Mimik",
					"Nach Prominenz<br>der Stimme",
					"Nach Prominenz<br>der Mimik"
			};

			int maxCols=4;
			int tableEyeBrows[][]=new int[rowNames.length+1][maxCols];
			int tableNod[][]=new int[rowNames.length+1][maxCols];

			for(String key:keys)
			{
				Vector<Result> vector=results.get(key);
				Result result=vector.get(0);

				double[] midValues=new double[words];
				for(Result res:vector)
				{
					for(int i=0;i<res.countWordValues();i++)
					{
						midValues[i]+=res.getValue(i).doubleValue();
					}
				}
				for(int i=0;i<words;i++)
				{
					midValues[i]=round(midValues[i]/(double)vector.size(),1000);
				}
				result.setMidValues(midValues);

				System.out.println("\n"+result.getVideofile());

				if(result.isNod())
				{
					if(result.isFaceOnly())
					{
						tableNod[0][faceOnly]++;
						System.out.println("Face Only => "+tableNod[0][faceOnly]);
						if(isMaximumOnEmph(result,result.getFaceEmph()))
						{
							tableNod[maxOnFace][faceOnly]++;
							System.out.println("\t=> table[maxOnFace][faceOnly] = "+tableNod[maxOnFace][faceOnly]);
						}
						else if(isMaximumAfterEmph(result,result.getFaceEmph()))
						{
							tableNod[maxAfterFace][faceOnly]++;
							System.out.println("\t=> table[maxAfterFace][faceOnly] = "+tableNod[maxAfterFace][faceOnly]);
						}
						else if(isMaximumBeforeEmph(result,result.getFaceEmph()))
						{
							tableNod[maxBeforeFace][faceOnly]++;
							System.out.println("\t=> table[maxBeforeFace][faceOnly] = "+tableNod[maxBeforeFace][faceOnly]);
						}
					}
					else if(result.isVoiceOnly())
					{
						tableNod[0][voiceOnly]++;
						System.out.println("Voice Only => "+tableNod[0][voiceOnly]);
						if(isMaximumOnEmph(result,result.getVoiceEmph()))
						{
							tableNod[maxOnVoice][voiceOnly]++;
							System.out.println("\t=> table[maxOnVoice][voiceOnly] = "+tableNod[maxOnVoice][voiceOnly]);
						}
						else if(isMaximumAfterEmph(result,result.getVoiceEmph()))
						{
							tableNod[maxAfterVoice][voiceOnly]++;
							System.out.println("\t=> table[maxAfterVoice][voiceOnly] = "+tableNod[maxAfterVoice][voiceOnly]);
						}
						else if(isMaximumBeforeEmph(result,result.getVoiceEmph()))
						{
							tableNod[maxBeforeVoice][voiceOnly]++;
							System.out.println("\t=> table[maxBeforeVoice][voiceOnly] = "+tableNod[maxBeforeVoice][voiceOnly]);
						}
					}
					else if(result.isVoiceAndFaceSeparated())
					{
						System.out.println("Voice And Face Separated => "+tableNod[0][voiceAndFaceSeparated]);
						if(isMaximumOnEmph(result,result.getVoiceEmph()))
						{
							tableNod[0][voiceAndFaceSeparated]++;
							tableNod[maxOnVoice][voiceAndFaceSeparated]++;
							System.out.println("\t=> table[maxOnVoice][voiceAndFaceSeparated] = "+tableNod[maxOnVoice][voiceAndFaceSeparated]);
						}
						else if(isMaximumAfterEmph(result,result.getVoiceEmph()))
						{
							tableNod[0][voiceAndFaceSeparated]++;
							tableNod[maxAfterVoice][voiceAndFaceSeparated]++;
							System.out.println("\t=> table[maxAfterVoice][voiceAndFaceSeparated] = "+tableNod[maxAfterVoice][voiceAndFaceSeparated]);
						}
						else if(isMaximumBeforeEmph(result,result.getVoiceEmph()))
						{
							tableNod[0][voiceAndFaceSeparated]++;
							tableNod[maxBeforeVoice][voiceAndFaceSeparated]++;
							System.out.println("\t=> table[maxBeforeVoice][voiceAndFaceSeparated] = "+tableNod[maxBeforeVoice][voiceAndFaceSeparated]);
						}

						if(isMaximumOnEmph(result,result.getFaceEmph()))
						{
							tableNod[0][voiceAndFaceSeparated]++;
							tableNod[maxOnFace][voiceAndFaceSeparated]++;
							System.out.println("\t=> table[maxOnFace][voiceAndFaceSeparated] = "+tableNod[maxOnFace][voiceAndFaceSeparated]);
						}
						else if(isMaximumAfterEmph(result,result.getFaceEmph()))
						{
							tableNod[0][voiceAndFaceSeparated]++;
							tableNod[maxAfterFace][voiceAndFaceSeparated]++;
							System.out.println("\t=> table[maxAfterFace][voiceAndFaceSeparated] = "+tableNod[maxAfterFace][voiceAndFaceSeparated]);
						}
						else if(isMaximumBeforeEmph(result,result.getFaceEmph()))
						{
							tableNod[0][voiceAndFaceSeparated]++;
							tableNod[maxBeforeFace][voiceAndFaceSeparated]++;
							System.out.println("\t=> table[maxBeforeFace][voiceAndFaceSeparated] = "+tableNod[maxBeforeFace][voiceAndFaceSeparated]);
						}
					}
					else if(result.isVoiceAndFace())
					{
						System.out.println("Voice And Face => "+tableNod[0][voiceAndFace]);
						if(isMaximumOnEmph(result,result.getVoiceEmph()))
						{
							tableNod[0][voiceAndFace]++;
							tableNod[maxOnVoice][voiceAndFace]++;
							System.out.println("\t=> table[maxOnVoice][voiceAndFace] = "+tableNod[maxOnVoice][voiceAndFace]);
						}
						else if(isMaximumAfterEmph(result,result.getVoiceEmph()))
						{
							tableNod[0][voiceAndFace]++;
							tableNod[maxAfterVoice][voiceAndFace]++;
							System.out.println("\t=> table[maxAfterVoice][voiceAndFace] = "+tableNod[maxAfterVoice][voiceAndFace]);
						}
						else if(isMaximumBeforeEmph(result,result.getVoiceEmph()))
						{
							tableNod[0][voiceAndFace]++;
							tableNod[maxBeforeVoice][voiceAndFace]++;
							System.out.println("\t=> table[maxBeforeVoice][voiceAndFace] = "+tableNod[maxBeforeVoice][voiceAndFace]);
						}

						if(isMaximumOnEmph(result,result.getFaceEmph()))
						{
							tableNod[0][voiceAndFace]++;
							tableNod[maxOnFace][voiceAndFace]++;
							System.out.println("\t=> table[maxOnFace][voiceAndFace] = "+tableNod[maxOnFace][voiceAndFace]);
						}
						else if(isMaximumAfterEmph(result,result.getFaceEmph()))
						{
							tableNod[0][voiceAndFace]++;
							tableNod[maxAfterFace][voiceAndFace]++;
							System.out.println("\t=> table[maxAfterFace][voiceAndFace] = "+tableNod[maxAfterFace][voiceAndFace]);
						}
						else if(isMaximumBeforeEmph(result,result.getFaceEmph()))
						{
							tableNod[0][voiceAndFace]++;
							tableNod[maxBeforeFace][voiceAndFace]++;
							System.out.println("\t=> table[maxBeforeFace][voiceAndFace] = "+tableNod[maxBeforeFace][voiceAndFace]);
						}
					}
				}
				else
				{
					if(result.isFaceOnly())
					{
						tableEyeBrows[0][faceOnly]++;
						System.out.println("Face Only => "+tableEyeBrows[0][faceOnly]);
						if(isMaximumOnEmph(result,result.getFaceEmph()))
						{
							tableEyeBrows[maxOnFace][faceOnly]++;
							System.out.println("\t=> table[maxOnFace][faceOnly] = "+tableEyeBrows[maxOnFace][faceOnly]);
						}
						else if(isMaximumAfterEmph(result,result.getFaceEmph()))
						{
							tableEyeBrows[maxAfterFace][faceOnly]++;
							System.out.println("\t=> table[maxAfterFace][faceOnly] = "+tableEyeBrows[maxAfterFace][faceOnly]);
						}
						else if(isMaximumBeforeEmph(result,result.getFaceEmph()))
						{
							tableEyeBrows[maxBeforeFace][faceOnly]++;
							System.out.println("\t=> table[maxBeforeFace][faceOnly] = "+tableEyeBrows[maxBeforeFace][faceOnly]);
						}
					}
					else if(result.isVoiceOnly())
					{
						tableEyeBrows[0][voiceOnly]++;
						System.out.println("Voice Only => "+tableEyeBrows[0][voiceOnly]);
						if(isMaximumOnEmph(result,result.getVoiceEmph()))
						{
							tableEyeBrows[maxOnVoice][voiceOnly]++;
							System.out.println("\t=> table[maxOnVoice][voiceOnly] = "+tableEyeBrows[maxOnVoice][voiceOnly]);
						}
						else if(isMaximumAfterEmph(result,result.getVoiceEmph()))
						{
							tableEyeBrows[maxAfterVoice][voiceOnly]++;
							System.out.println("\t=> table[maxAfterVoice][voiceOnly] = "+tableEyeBrows[maxAfterVoice][voiceOnly]);
						}
						else if(isMaximumBeforeEmph(result,result.getVoiceEmph()))
						{
							tableEyeBrows[maxBeforeVoice][voiceOnly]++;
							System.out.println("\t=> table[maxBeforeVoice][voiceOnly] = "+tableEyeBrows[maxBeforeVoice][voiceOnly]);
						}
					}
					else if(result.isVoiceAndFaceSeparated())
					{
						System.out.println("Voice And Face Separated => "+tableEyeBrows[0][voiceAndFaceSeparated]);
						if(isMaximumOnEmph(result,result.getVoiceEmph()))
						{
							tableEyeBrows[0][voiceAndFaceSeparated]++;
							tableEyeBrows[maxOnVoice][voiceAndFaceSeparated]++;
							System.out.println("\t=> table[maxOnVoice][voiceAndFaceSeparated] = "+tableEyeBrows[maxOnVoice][voiceAndFaceSeparated]);
						}
						else if(isMaximumAfterEmph(result,result.getVoiceEmph()))
						{
							tableEyeBrows[0][voiceAndFaceSeparated]++;
							tableEyeBrows[maxAfterVoice][voiceAndFaceSeparated]++;
							System.out.println("\t=> table[maxAfterVoice][voiceAndFaceSeparated] = "+tableEyeBrows[maxAfterVoice][voiceAndFaceSeparated]);
						}
						else if(isMaximumBeforeEmph(result,result.getVoiceEmph()))
						{
							tableEyeBrows[0][voiceAndFaceSeparated]++;
							tableEyeBrows[maxBeforeVoice][voiceAndFaceSeparated]++;
							System.out.println("\t=> table[maxBeforeVoice][voiceAndFaceSeparated] = "+tableEyeBrows[maxBeforeVoice][voiceAndFaceSeparated]);
						}

						if(isMaximumOnEmph(result,result.getFaceEmph()))
						{
							tableEyeBrows[0][voiceAndFaceSeparated]++;
							tableEyeBrows[maxOnFace][voiceAndFaceSeparated]++;
							System.out.println("\t=> table[maxOnFace][voiceAndFaceSeparated] = "+tableEyeBrows[maxOnFace][voiceAndFaceSeparated]);
						}
						else if(isMaximumAfterEmph(result,result.getFaceEmph()))
						{
							tableEyeBrows[0][voiceAndFaceSeparated]++;
							tableEyeBrows[maxAfterFace][voiceAndFaceSeparated]++;
							System.out.println("\t=> table[maxAfterFace][voiceAndFaceSeparated] = "+tableEyeBrows[maxAfterFace][voiceAndFaceSeparated]);
						}
						else if(isMaximumBeforeEmph(result,result.getFaceEmph()))
						{
							tableEyeBrows[0][voiceAndFaceSeparated]++;
							tableEyeBrows[maxBeforeFace][voiceAndFaceSeparated]++;
							System.out.println("\t=> table[maxBeforeFace][voiceAndFaceSeparated] = "+tableEyeBrows[maxBeforeFace][voiceAndFaceSeparated]);
						}
					}
					else if(result.isVoiceAndFace())
					{
						System.out.println("Voice And Face => "+tableEyeBrows[0][voiceAndFace]);
						if(isMaximumOnEmph(result,result.getVoiceEmph()))
						{
							tableEyeBrows[0][voiceAndFace]++;
							tableEyeBrows[maxOnVoice][voiceAndFace]++;
							System.out.println("\t=> table[maxOnVoice][voiceAndFace] = "+tableEyeBrows[maxOnVoice][voiceAndFace]);
						}
						else if(isMaximumAfterEmph(result,result.getVoiceEmph()))
						{
							tableEyeBrows[0][voiceAndFace]++;
							tableEyeBrows[maxAfterVoice][voiceAndFace]++;
							System.out.println("\t=> table[maxAfterVoice][voiceAndFace] = "+tableEyeBrows[maxAfterVoice][voiceAndFace]);
						}
						else if(isMaximumBeforeEmph(result,result.getVoiceEmph()))
						{
							tableEyeBrows[0][voiceAndFace]++;
							tableEyeBrows[maxBeforeVoice][voiceAndFace]++;
							System.out.println("\t=> table[maxBeforeVoice][voiceAndFace] = "+tableEyeBrows[maxBeforeVoice][voiceAndFace]);
						}

						if(isMaximumOnEmph(result,result.getFaceEmph()))
						{
							tableEyeBrows[0][voiceAndFace]++;
							tableEyeBrows[maxOnFace][voiceAndFace]++;
							System.out.println("\t=> table[maxBeforeVoice][voiceOnly] = "+tableEyeBrows[maxBeforeVoice][voiceOnly]);
						}
						else if(isMaximumAfterEmph(result,result.getFaceEmph()))
						{
							tableEyeBrows[0][voiceAndFace]++;
							tableEyeBrows[maxAfterFace][voiceAndFace]++;
							System.out.println("\t=> table[maxBeforeVoice][voiceOnly] = "+tableEyeBrows[maxBeforeVoice][voiceOnly]);
						}
						else if(isMaximumBeforeEmph(result,result.getFaceEmph()))
						{
							tableEyeBrows[0][voiceAndFace]++;
							tableEyeBrows[maxBeforeFace][voiceAndFace]++;
							System.out.println("\t=> table[maxBeforeVoice][voiceOnly] = "+tableEyeBrows[maxBeforeVoice][voiceOnly]);
						}
					}
				}
			}

			/*
			 * ausgabe
			 */

			BufferedWriter writer=new BufferedWriter(new FileWriter(new File(resultFolder.getAbsolutePath()+File.separator+"results.html")));

			String title="Analyse Wahrnehmungstest | "+folder.getName();

			writer.write("<html><head>\n" +
					"<link rel=\"stylesheet\" href=\"http://twitter.github.com/bootstrap/assets/css/bootstrap.css\"/>" +
					"\n<style type=\"text/css\">" +
					"\nH4 { color: rgb(0,100,100) }" +
					"\nH5 { color: rgb(0,0,0) }" +
					"\n</style><title>"+title+"</title></head>\n" +
					"<body><center><h3>"+title+"</h3><br>\n" +
					"<h4>Auswertung von "+countFiles+" Tests</h4><br>\n");

			writer.write("<h4>Statistiken (Videos mit Augenbrauenbewegung)</h4>\n" +
					"<table width=70% border=1><tr>\n" +
					"<td width=14% align=center valign=middle>\n" +
					"<table border=0 width=100% height=100%><tr>\n" +
					"<td width=100% align=right valign=middle>Prominenzen<br>im Stimulus</tr><tr>\n" +
					"<td width=100% align=left valign=middle>Maximum<br>der Bewertungen<br>der Betonungen</tr>\n" +
					"</tr></table></td>\n" +
					"<td width=14% align=center valign=bottom><h5>Nur Stimme</h5>" +
					tableEyeBrows[0][voiceOnly]+"</td>\n" +
					"<td width=14% align=center valign=bottom><h5>Nur Mimik</h5>" +
					tableEyeBrows[0][faceOnly]+"</td>\n" +
					"<td width=14% align=center valign=bottom><h5>Stimme und Mimik getrennt im Stimulus</h5>" +
					tableEyeBrows[0][voiceAndFaceSeparated]+"</td>\n" +
					"<td width=14% align=center valign=bottom><h5>Stimme und Mimik</h5>" +
					tableEyeBrows[0][voiceAndFace]+"</td></tr>\n");

			String content="",bgcolor="";

			int max,index[]=new int[maxCols];
			for(int col=0;col<maxCols;col++)
			{
				max=Integer.MIN_VALUE;
				index[col]=-1;
				for(int row=1;row<tableEyeBrows.length;row++)
				{
					if(tableEyeBrows[row][col]>max && tableEyeBrows[row][col]>0)
					{
						max=tableEyeBrows[row][col];
						index[col]=row;
					}
				}
			}

			for(int row=1;row<tableEyeBrows.length;row++)
			{
				writer.write("<tr><td align=center valign=middle><h5>" +
						rowNames[row-1]+"</h5></td>");


				for(int col=0;col<maxCols;col++)
				{
					if(	(col==voiceOnly && row%2==0) || 
							(col==faceOnly && row%2==1))
					{
						content="<h5>-</h5>";
					}
					else
					{
						content="<h5>"+round((100.0/(double)tableEyeBrows[0][col])*(double)tableEyeBrows[row][col],100)+
								"%</h5>"+tableEyeBrows[row][col];
					}

					if(index[col]==row)
					{
						bgcolor="bgcolor=#dedede";
					}
					else
					{
						bgcolor="";
					}

					if(col<maxCols-1)
					{
						writer.write("<td align=center "+bgcolor+" valign=middle>"+content+"</td>");
					}
					else
					{
						if(row%2==1)
						{
							writer.write("<td rowspan=2 "+bgcolor+" align=center valign=middle>"+content+"</td>");
						}
					}
				}
				writer.write("</tr>");
			}
			writer.write("</table><br><br>\n");

			writer.write("<h4>Statistiken (Videos mit Kopfnicken)</h4>\n" +
					"<table width=70% border=1><tr>\n" +
					"<td width=14% align=center valign=middle>\n" +
					"<table border=0 width=100% height=100%><tr>\n" +
					"<td width=100% align=right valign=middle>Prominenzen<br>im Stimulus</tr><tr>\n" +
					"<td width=100% align=left valign=middle>Maximum<br>der Bewertungen<br>der Betonungen</tr>\n" +
					"</tr></table></td>\n" +
					"<td width=14% align=center valign=bottom><h5>Nur Stimme</h5>" +
					tableNod[0][voiceOnly]+"</td>\n" +
					"<td width=14% align=center valign=bottom><h5>Nur Mimik</h5>" +
					tableNod[0][faceOnly]+"</td>\n" +
					"<td width=14% align=center valign=bottom><h5>Stimme und Mimik getrennt im Stimulus</h5>" +
					tableNod[0][voiceAndFaceSeparated]+"</td>\n" +
					"<td width=14% align=center valign=bottom><h5>Stimme und Mimik</h5>" +
					tableNod[0][voiceAndFace]+"</td></tr>\n");
			
			for(int col=0;col<maxCols;col++)
			{
				max=Integer.MIN_VALUE;
				index[col]=-1;
				for(int row=1;row<tableNod.length;row++)
				{
					if(tableNod[row][col]>max && tableNod[row][col]>0)
					{
						max=tableNod[row][col];
						index[col]=row;
					}
				}
			}

			for(int row=1;row<tableNod.length;row++)
			{
				writer.write("<tr><td align=center valign=middle><h5>" +
						rowNames[row-1]+"</h5></td>");

				for(int col=0;col<maxCols;col++)
				{
					if(	(col==voiceOnly && row%2==0) || 
							(col==faceOnly && row%2==1))
					{
						content="<h5>-</h5>";
					}
					else
					{
						content="<h5>"+round((100.0/(double)tableNod[0][col])*(double)tableNod[row][col],100)+
								"%</h5>"+tableNod[row][col];
					}

					bgcolor=(index[col]==row?"bgcolor=#dedede":"");

					if(col<maxCols-1)
					{
						writer.write("<td align=center "+bgcolor+" valign=middle>"+content+"</td>");
					}
					else
					{
						if(row%2==1)
						{
							writer.write("<td rowspan=2 "+bgcolor+" align=center valign=middle>"+content+"</td>");
						}
					}
				}
				writer.write("</tr>");
			}
			writer.write("</table><br><br>");

			/*
			 * ****************************
			 */
			
			writer.write("<h4>Übersicht</h4><h4><small>Bedeutung der Farben:</small></h4>" +
					"\n<table border=1 width=60%><tr>" +
					"<td align=center width=20% bgcolor="+colFace+"><h5>Betonung durch Mimik</h5>" +
					"<td align=center width=20% bgcolor="+colVoice+"><h5>Betonung durch Stimme</h5>" +
					"<td align=center width=20% bgcolor="+colBoth+"><h5>Betonung durch Mimik und Stimme</h5>" +
					"</tr></table><br>\n" +
					"\n<table border=1 width=60%><tr>" +
					"<td align=center width=30% bgcolor="+getRelativeColor(resColor, MIN)+"><h5>Geringe durchschnittliche Bewertung der Betonung</h5>" +
					"<td align=center width=30% bgcolor="+getRelativeColor(resColor, MAX)+"><h5>Hohe durchschnittliche Bewertung der Betonung</h5>" +
					"</tr></table><br><br>\n");

			writer.write("<table width=70% cellpadding=0 cellspacing=0 border=1>\n");

			boolean firstLine=true;
			for(String key:keys)
			{
				Vector<Result> vector=results.get(key);

				//				//System.out.println(vector.get(0).getVideofile());
				writer.write("<tr><td align=left bgcolor=#eeeeee colspan="+words+"><h4>&nbsp;&nbsp;" +
						vector.get(0).getVideofile()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<small>" +
						vector.size()+
						" Bewertungen</small></h5></td></tr>\n");

				writer.write("<tr>\n");

				for(int i=0;i<words;i++)
				{
					writer.write("<td align=center width="+(100/words)+"%><h5>" +
							vector.get(0).getWord(i)+"</h5></td>\n");
				}
				writer.write("</tr>\n");

				writer.write("<tr><td cellpadding=0 cellspacing=0 align=center colspan="+words+"><table width=100% border=0 cellpadding=0 cellspacing=0><tr>\n");

				int voice_emph=vector.get(0).getVoiceEmph();
				int face_emph=vector.get(0).getFaceEmph();

				for(int i=-1;i<=EMPHS;i++)
				{
					if(voice_emph==face_emph && voice_emph==i)
					{
						bgcolor=" bgcolor=#"+colBoth;
						//						content="Beide";
					}
					else if(voice_emph==i)
					{
						bgcolor=" bgcolor=#"+colVoice;
						//						content="Stimme";
					}
					else if(face_emph==i)
					{
						bgcolor=" bgcolor=#"+colFace;
						//						content="Mimik";
					}
					else
					{
						bgcolor="";
						//						content="&nbsp;";
					}

					writer.write("<td align=center "+(i<0?"height=10":"")+" width="+(i<0||i==EMPHS?"5":"10")+"%"+bgcolor+"></td>\n");

				}

				writer.write("</tr></table></td></tr><tr>\n");

				for(int i=0;i<words;i++)
				{
					writer.write("<td align=center bgcolor=" +
							getRelativeColor(resColor,vector.get(0).getMidValues()[i])+
							(firstLine?" width="+(100/words)+"%":"") +
							">" + vector.get(0).getMidValues()[i]+"</td>\n");
				}
				writer.write("</tr>\n");

				firstLine=false;
			}

			writer.write("</table><br></body></html>");

			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static double round(double val, int len) {
		//((int)((midValues[i]*1000)/(double)vector.size()))/1000.0
		return (double)((int)(val*len))/(double)len;
	}

	private static boolean isMaximumOnEmph(Result result,int emph) {
		//System.out.println(result.getMaximumIndex()+"\t"+"\t"+emph+"\t"+(result.getMaximumIndex()*2)+" <-> "+emph);
		return result.getMaximumIndex()*2==emph;
	}

	private static boolean isMaximumBeforeEmph(Result result,int emph) {
		//System.out.println(result.getMaximumIndex()+"\t"+"\t"+emph+"\t"+(result.getMaximumIndex()*2)+" <-> "+(emph-1));
		return result.getMaximumIndex()*2==emph-1;
	}

	private static boolean isMaximumAfterEmph(Result result,int emph) {
		//System.out.println(result.getMaximumIndex()+"\t"+"\t"+emph+"\t"+(result.getMaximumIndex()*2)+" <-> "+(emph+1));
		return result.getMaximumIndex()*2==emph+1;
	}

	/*
	 * benshaar_synth_1_video8.avi 	=> 8 => 7 => 3
	 * johnsbein_synth_8_video5.avi	=> 5 => 4 => 2
	 * johnsbein_synth_9.avi		=> N/A    => -1
	 */
	private static int parseFaceEmphasizeForWord(String videofile) {

		String video="video";

		if(videofile.matches("[a-zA-Z0-9\\_]+\\_"+video+"\\d\\.avi")) 
		{
			int index=videofile.indexOf(video)+video.length();
			int emph=Integer.parseInt(videofile.substring(index, index+1));

			if(emph%2==1)
				emph++;

			emph/=2;

			return emph;
		}

		return Integer.MIN_VALUE;
	}

	/*
	 * benshaar_synth_1_video8.avi 		=> 1 => 0
	 * nod_jimsrad_synth_7_video8.avi	=> 7 => 6 => 3
	 * johnsbein_monoton_video6.avi		=> N/A    => -1
	 */
	private static int parseVoiceEmphasizeForWord(String videofile) {

		String synth="synth";

		if(videofile.matches("[a-zA-Z\\_]+\\_"+synth+"\\_\\d[a-zA-Z0-9\\.\\_]+"))
		{
			int index=videofile.indexOf(synth)+synth.length()+1;
			int emph=Integer.parseInt(videofile.substring(index, index+1));

			if(emph%2==1)
				emph++;

			emph/=2;

			return emph;
		}

		return Integer.MIN_VALUE;
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
