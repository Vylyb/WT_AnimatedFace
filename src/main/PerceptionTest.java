package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

import main.perceptiontest.PerceptionTestWindow;
import main.perceptiontest.TestCase;
import main.perceptiontest.TutorialTestCase;
import main.xml.XmlFactory;

public class PerceptionTest {

	private static PerceptionTestWindow window;
	private static Vector<TestCase> testCases;
	private static int nextTest;
	private static String description="";
	private static String endText;
	private final static String 
	VIDEO_FILE="videofile",
	VOICE_EMPH="voice_emph",
	FACE_EMPH="face_emph",
	DESC="description",
	ENDTEXT="endtext",
	SENTENCE="sentence",
	WORD="word",
	TESTCASE="test_case",
	TUTORIAL="tutorial",
	EMPHS="emphasizes",
	VALUE="value";

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

				boolean inTest=false,inSentence=false,inTutorial=false,inEmphasizes=false;
				TestCase newTest=null;

				for(String tag:content.split("><"))
				{
					tag=tag.trim();

					if(tag.length()>0 && !tag.startsWith("!--"))
					{
						if(tag.matches(TESTCASE))
						{
							inTest=true;
						}
						else if(tag.matches(XmlFactory.getCloseTag(TESTCASE)))
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

						else if(tag.matches(TUTORIAL))
						{
							inTutorial=true;
						}
						else if(tag.matches(XmlFactory.getCloseTag(TUTORIAL)))
						{
							inTutorial=false;
							
							testCases.add(newTest);
							newTest=null;
						}

						else if(tag.matches(EMPHS))
						{
							inEmphasizes=true;
						}
						else if(tag.matches(XmlFactory.getCloseTag(EMPHS)))
						{
							inEmphasizes=false;
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

						else if(inTutorial)
						{
							if(newTest==null)
							{
								newTest=new TutorialTestCase();
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
							else if(inEmphasizes)
							{
								try {
									String[] parts=tag.split("[><]");
									if(parts[0].trim().matches(VALUE))
									{
										if(parts[2].trim().matches(XmlFactory.getCloseTag(VALUE)))
										{
											((TutorialTestCase)newTest).addValue(Integer.parseInt(parts[1]));
										}
									}
								} catch (ArrayIndexOutOfBoundsException e) {
								} catch (NumberFormatException e) {
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

							try {
								String[] parts=tag.split("[><]");
								if(parts[0].trim().matches(ENDTEXT))
								{
									if(parts[2].trim().matches(XmlFactory.getCloseTag(ENDTEXT)))
									{
										endText=parts[1].replaceAll("#", "<br>");
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

	public static String getEndText() {
		return endText;
	}

	public static int getNumberOfTests() {
		return testCases.size();
	}

	public static TestCase getNextTest(){

		try {
			return testCases.get(++nextTest);
		} catch (Exception e) {
			return null;
		}
	}

	public static TestCase getCurrentTest(){

		return testCases.get(nextTest);
	}

}
