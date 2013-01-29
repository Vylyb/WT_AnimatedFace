package main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JSlider;

import main.animation.Animation;
import main.animation.AnimationStep;
import main.animation.Change;
import main.animation.RenderedAnimation;
import main.view.Window;
import main.xml.XmlFactory;



public class AnimatedFace{

	public final static String STEP_START="stepstart",
			STEP="step",
			CHANGE="change",
			CHANGES="changes",
			CHANGE_ID="value_id",
			CHANGE_VAL="value_change",
			STEPS="steps";

	public static boolean editorMode=true;

	public static int FPS=25,MAX_SEC=100;

	public static Animation currentAnimation;

	public static RenderedAnimation renderedAnimation;

	public static Window window;

	public static void main(String[] args) {
		window=new Window();
		window.setVisible(true);
		System.out.println(window.getDisplaySizeAndLocation());
	}

	public static String toSecondsAndFrames(int time) {
		int seconds=(int)time/FPS;
		int frames=(int)time%FPS;
		return (seconds<10?"0":"")+seconds+":"+(frames<10?"0":"")+frames;
	}

	public static int framesfromString(String time)
			throws NullPointerException{
		String start=time.split(" \\(")[0];
		int seconds=Integer.parseInt(start.substring(0, start.indexOf(":")));
		int frames=Integer.parseInt(start.substring(start.indexOf(":")+1,start.length()));
		return seconds*FPS+frames;
	}

	public static void playCurrentAnimation(JButton playButton, JSlider timeSlider){
		if(renderedAnimation==null){
			renderCurrentAnimation();
		}
		renderedAnimation.play(playButton,timeSlider);
	}

	public static void renderCurrentAnimation(){
		renderedAnimation=new RenderedAnimation(currentAnimation.getNumberOfFrames());
		renderedAnimation.render(currentAnimation);
	}

	public static void loadAnimation(File file){
		
		Animation newAnimation=new Animation(window.controlContainer);
		
		BufferedReader reader;
		try {
			String content="",line="";
			reader=new BufferedReader(new FileReader(file));
			while((line=reader.readLine())!=null)
			{
				line=line.replaceAll("\\\t", "").toLowerCase();
				content+=line.trim();
			}

			boolean inStep=false,inChange=false;
			AnimationStep newStep = null;
			Change newChange=null;
			
			for(String tag:content.split("><"))
			{
				tag=tag.trim();
				
				if(tag.length()>0 && !tag.startsWith("!--"))
				{
					System.out.println("\n"+tag);
					if(tag.matches(STEP))
					{
						inStep=true;
						System.out.println("inStep = "+inStep);
					}
					else if(tag.matches(CHANGE))
					{
						inChange=true;
						System.out.println("inChange = "+inChange);
					}
					else if(tag.matches(XmlFactory.getCloseTag(STEP)))
					{
						inStep=false;

						newAnimation.addStep(newStep);
						newStep=null;
						System.out.println("Added Step => "+newAnimation.getSteps().size());
						
						System.out.println("inStep = "+inStep);
					}
					else if(tag.matches(XmlFactory.getCloseTag(CHANGE)))
					{
						inChange=false;

						try {
							newStep.addChange(newChange);
							System.out.println("Added Change => "+newStep.getChanges().size());
						} catch (NullPointerException e) {
						}
						newChange=null;
						
						System.out.println("inChange = "+inChange);
					}
					
					if(inStep)
					{
						if(newStep==null)
						{
							newStep=new AnimationStep();
						}
						
						try {
							String[] parts=tag.split("[><]");
							if(parts[0].trim().matches(STEP_START))
							{
								if(parts[2].trim().matches(XmlFactory.getCloseTag(STEP_START)))
								{
									newStep.setTime(Integer.parseInt(parts[1]));
									System.out.println("newStep.time = "+newStep.getTime());
								}
							}
						} catch (ArrayIndexOutOfBoundsException e) {
						} catch (NumberFormatException e){
						}
						
						if(inChange)
						{
							if(newChange==null)
							{
								newChange=new Change();
							}
							
							try {
								String[] parts=tag.split("[><]");
								if(parts[0].trim().matches(CHANGE_ID))
								{
									if(parts[2].trim().matches(XmlFactory.getCloseTag(CHANGE_ID)))
									{
										newChange.setValueID(Integer.parseInt(parts[1]));
										System.out.println("newChange.value_id = "+newChange.getValueID());
									}
								}
								if(parts[0].trim().matches(CHANGE_VAL))
								{
									if(parts[2].trim().matches(XmlFactory.getCloseTag(CHANGE_VAL)))
									{
										newChange.setValueChange(Integer.parseInt(parts[1]));
										System.out.println("newChange.value_change = "+newChange.getValueChange());
									}
								}
							} catch (ArrayIndexOutOfBoundsException e) {
							} catch (NumberFormatException e){
							}
						}
					}
				}
			}
			
			setCurrentAnimation(newAnimation);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void setCurrentAnimation(Animation newAnimation) {
		currentAnimation=newAnimation;
		window.animationContainer.update();
		System.out.println("New Animation loaded\n"+currentAnimation.getNumberOfFrames()+" frames");
	}

	public static void saveAnimation(File file){
		BufferedWriter writer;
		try {
			writer=new BufferedWriter(new FileWriter(file));

			startTag(STEPS, writer);

			for(AnimationStep step:currentAnimation.getSteps()){
				comment("Neuer Schritt",writer);

				startTag(STEP,writer);
				tag(step.getTime(),STEP_START,writer);

				startTag(CHANGES, writer);
				for(Change change:step.getChanges()){
					startTag(CHANGE, writer);
					tag(change.getValueID(),CHANGE_ID,writer);
					tag(change.getValueChange(),CHANGE_VAL,writer);
					closeTag(CHANGE, writer);
				}
				closeTag(CHANGES, writer);

				closeTag(STEP,writer);
			}

			closeTag(STEPS, writer);

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void closeTag(String tag, BufferedWriter writer) throws IOException {
		writer.write(XmlFactory.closeTag(tag));
	}

	public static void startTag(String tag, BufferedWriter writer) throws IOException {
		writer.write(XmlFactory.startTag(tag));
	}

	public static void tag(int value, String tag, BufferedWriter writer) throws IOException {
		writer.write(XmlFactory.intToTag(value,tag));
	}

	public static void tag(String value, String tag, BufferedWriter writer) throws IOException {
		writer.write(XmlFactory.stringToTag(value,tag));
	}

	public static void comment(String string, BufferedWriter writer) throws IOException {
		writer.write(XmlFactory.stringToComment(string));
	}

}
