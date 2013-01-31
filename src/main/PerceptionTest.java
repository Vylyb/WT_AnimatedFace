package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

import main.perceptiontest.PerceptionTestWindow;
import main.perceptiontest.TestCase;
import main.xml.XmlFactory;

public class PerceptionTest {

	private static PerceptionTestWindow window;
	private static Vector<TestCase> testCases;
	private static int nextTest;
	private static String description="";
	private final static String 
	VIDEO_FILE="videofile",
	VOICE_EMPH="voice_emph",
	FACE_EMPH="face_emph",
	DESC="description",
	SENTENCE="sentence",
	WORD="word",
	TEST="test_case";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		testCases=new Vector<TestCase>();

		if(args.length>0)
		{
			BufferedReader reader;
			String content="",line="",path;
			try {
				reader=new BufferedReader(new FileReader(new File(path="tests/"+args[0])));
				while((line=reader.readLine())!=null)
				{
					line=line.replaceAll("\\\t", "");
					content+=line.trim();
				}

				boolean inTest=false,inSentence=false;
				TestCase newTest=null;

				for(String tag:content.split("><"))
				{
					tag=tag.trim();

					if(tag.length()>0 && !tag.startsWith("!--"))
					{
						if(tag.matches(TEST))
						{
							inTest=true;
						}
						else if(tag.matches(XmlFactory.getCloseTag(TEST)))
						{
							inTest=false;

							testCases.add(newTest);
							newTest=null;
						}
						else if(tag.matches(SENTENCE))
						{
							inSentence=true;
						}
						else if(tag.matches(XmlFactory.getCloseTag(SENTENCE)))
						{
							inSentence=false;
						}

						if(inTest)
						{
							if(newTest==null)
							{
								newTest=new TestCase();
							}

							if(inSentence)
							{
								try {
									String[] parts=tag.split("[><]");
									if(parts[0].trim().matches(WORD))
									{
										if(parts[2].trim().matches(XmlFactory.getCloseTag(WORD)))
										{
											newTest.addWord(parts[1]);
										}
									}
								} catch (ArrayIndexOutOfBoundsException e) {
								} 

							}
							else
							{

								try {
									String[] parts=tag.split("[><]");
									if(parts[0].trim().matches(VIDEO_FILE))
									{
										if(parts[2].trim().matches(XmlFactory.getCloseTag(VIDEO_FILE)))
										{
											newTest.setVideoFile(parts[1]);
										}
									}
								} catch (ArrayIndexOutOfBoundsException e) {
								} 

								try {
									String[] parts=tag.split("[><]");
									if(parts[0].trim().matches(FACE_EMPH))
									{
										if(parts[2].trim().matches(XmlFactory.getCloseTag(FACE_EMPH)))
										{
											newTest.setFaceEmphasize(Integer.parseInt(parts[1]));
										}
									}
								} catch (ArrayIndexOutOfBoundsException e) {
								} catch (NumberFormatException e) {
								} 

								try {
									String[] parts=tag.split("[><]");
									if(parts[0].trim().matches(VOICE_EMPH))
									{
										if(parts[2].trim().matches(XmlFactory.getCloseTag(VOICE_EMPH)))
										{
											newTest.setVoiceEmphasize(Integer.parseInt(parts[1]));
										}
									}
								} catch (ArrayIndexOutOfBoundsException e) {
								} catch (NumberFormatException e) {
								} 
							}

						}

						else
						{
							try {
								String[] parts=tag.split("[><]");
								if(parts[0].trim().matches(DESC))
								{
									if(parts[2].trim().matches(XmlFactory.getCloseTag(DESC)))
									{
										description=parts[1].replaceAll("#", "<br>");
									}
								}
							} catch (ArrayIndexOutOfBoundsException e) {
							} catch (NumberFormatException e) {
							} 
						}
					}
				}
				nextTest=0;
				System.out.println("Loaded Test\nDescription: '"+getDescription()+"'\nFile: "+path+"\nTest Cases: "+getNumberOfTests());
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}

		window=new PerceptionTestWindow();
		window.setVisible(true);
	}

	public static String getDescription() {
		return description;
	}

	public static int getNumberOfTests() {
		return testCases.size();
	}

	public static TestCase getNextTest(){

		return testCases.get((nextTest=(nextTest+1<testCases.size()?nextTest+1:0)));
	}

	public static TestCase getCurrentTest(){

		return testCases.get(nextTest);
	}

}
