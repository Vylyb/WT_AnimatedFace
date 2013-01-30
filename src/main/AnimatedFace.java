package main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;

import main.animation.Animation;
import main.animation.AnimationStep;
import main.animation.ChangedValue;
import main.animation.RenderedAnimation;
import main.sound.wave.WavePlayer;
import main.view.Window;
import main.view.sliders.TimeSlider;
import main.xml.XmlFactory;



public class AnimatedFace{

	public final static String STEP_START="stepstart",
			STEP="step",
			CHANGE="change",
			CHANGES="changes",
			CHANGE_ID="value_id",
			CHANGE_NEW_VAL="new_value",
			STEPS="steps",
			WAVE_FILE="wave_file",
			NO_SOUND="[No Sound File]",
			NO_FILE="[No Animation File]",
			ANIM="animation",
			TEMPL="template",
			SETTINGS="settings";

	private static String waveFileName=NO_SOUND;
	private static String animationFileName=NO_FILE;
		
	public static boolean editorMode=true;

	public static int FPS=25,MAX_SEC=100;

	public static Animation currentAnimation;

	public static RenderedAnimation renderedAnimation;

	public static Window window;
	
	public static WavePlayer wavePlayer;

	public static File waveFile;


	public static void main(String[] args) {
		window=new Window();
		wavePlayer=new WavePlayer();
		window.setVisible(true);
		updateWindowTitle();
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

	public static void playCurrentAnimation(JButton playButton, TimeSlider timeSlider){
		if(renderedAnimation==null){
			renderCurrentAnimation();
		}
		renderedAnimation.play(playButton,timeSlider);
	}

	public static void setWaveFile(File wavefile){
		waveFile=wavefile;
		waveFileName=wavefile.getName();
		window.animationContainer.allowWavePlay();
		updateWindowTitle();
	}

	private static void updateWindowTitle() {
		window.setTitle(animationFileName+" - "+waveFileName);
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
			ChangedValue newChange=null;
			
			for(String tag:content.split("><"))
			{
				tag=tag.trim();
				
				if(tag.length()>0 && !tag.startsWith("!--"))
				{
					if(tag.matches(STEP))
					{
						inStep=true;
					}
					else if(tag.matches(CHANGE))
					{
						inChange=true;
					}
					else if(tag.matches(XmlFactory.getCloseTag(STEP)))
					{
						inStep=false;

						newAnimation.addStep(newStep);
						newStep=null;
					}
					else if(tag.matches(XmlFactory.getCloseTag(CHANGE)))
					{
						inChange=false;

						try {
							newStep.addChangedValue(newChange);
							
						} catch (NullPointerException e) {
						}
						newChange=null;
						
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
								}
							}
						} catch (ArrayIndexOutOfBoundsException e) {
						} catch (NumberFormatException e){
						}
						
						if(inChange)
						{
							if(newChange==null)
							{
								newChange=new ChangedValue();
							}
							
							try {
								String[] parts=tag.split("[><]");
								if(parts[0].trim().matches(CHANGE_ID))
								{
									if(parts[2].trim().matches(XmlFactory.getCloseTag(CHANGE_ID)))
									{
										newChange.setValueID(Integer.parseInt(parts[1]));
									}
								}
								if(parts[0].trim().matches(CHANGE_NEW_VAL))
								{
									if(parts[2].trim().matches(XmlFactory.getCloseTag(CHANGE_NEW_VAL)))
									{
										newChange.setChangedValue(Integer.parseInt(parts[1]));
									}
								}
							} catch (ArrayIndexOutOfBoundsException e) {
							} catch (NumberFormatException e){
							}
						}
					}
					else
					{
						try {
							String[] parts=tag.split("[><]");
							if(parts[0].trim().matches(WAVE_FILE))
							{
								if(parts[2].trim().matches(XmlFactory.getCloseTag(WAVE_FILE)))
								{
									setWaveFile(new File("wavefiles/"+parts[1]));
								}
							}
						} catch (ArrayIndexOutOfBoundsException e) {
						} catch (NumberFormatException e){
						}
					}
				}
			}
			
			setCurrentAnimation(newAnimation);
			animationFileName=file.getName();
			updateWindowTitle();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			renderedAnimation=null;
		}
	}

	private static void setCurrentAnimation(Animation newAnimation) {
		currentAnimation=newAnimation;
		window.animationContainer.update();
	}

	public static void saveAnimation(File file){
		BufferedWriter writer;
		try {
			writer=new BufferedWriter(new FileWriter(file));
			
			startTag(ANIM, writer);

			startTag(SETTINGS, writer);
			if(waveFile!=null)
			{
				tag(waveFile.getName(),WAVE_FILE,writer);
			}
			closeTag(SETTINGS, writer);

			startTag(STEPS, writer);

			for(AnimationStep step:currentAnimation.getSteps()){
				comment("Neuer Schritt",writer);

				startTag(STEP,writer);
				tag(step.getTime(),STEP_START,writer);

				startTag(CHANGES, writer);
				for(ChangedValue change:step.getChangedValues()){
					startTag(CHANGE, writer);
					tag(change.getValueID(),CHANGE_ID,writer);
					tag(change.getChangedValue(),CHANGE_NEW_VAL,writer);
					closeTag(CHANGE, writer);
				}
				closeTag(CHANGES, writer);

				closeTag(STEP,writer);
			}

			closeTag(STEPS, writer);
			
			closeTag(ANIM, writer);

			writer.close();
			animationFileName=file.getName();
			updateWindowTitle();
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
