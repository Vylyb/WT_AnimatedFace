package main.perceptiontest;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.PerceptionTest;
import main.perceptiontest.video.PlayerPanel;
import net.sf.fmj.utility.URLUtils;

public class PerceptionTestWindow extends JFrame {

	private PlayerPanel playerPanel;
	private Container controlPanel;
	private JButton playButton;
	private JButton nextTestButton;
	private JButton startTestButton;
	private TestCase currentTest;
	private JEditorPane textfield;
	private Container testPanel;
	private String logFileName;
	
	public final static int MIN=1;
	public final static int MAX=5;
	protected boolean testEnded=false;
	protected boolean tutorial=false;
	private JLabel label;
	private int testNumber=0;

	public PerceptionTestWindow() {
		setLayout(new BorderLayout());
		setSize(getToolkit().getScreenSize().width, getToolkit().getScreenSize().height);
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Wahrnehmungsexperiment: Synthetische Sprache und Avatare | Beuth Hochschule für Technik Berlin");
		
		Container north=new Container();
		north.setLayout(new GridLayout(2, 1));
		
		north.add(label=new JLabel("<html><body><font face=verdana size=6><i><b>Wahrnehmungsexperiment: </i></b><font face=verdana size=5>Synthetische Sprache und Avatare</body></html>"));

		add(playerPanel=new PlayerPanel(),BorderLayout.CENTER);

		north.add(controlPanel=new Container());
		controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,15));
		
		add(north,BorderLayout.NORTH);

		add(testPanel=new Container(),BorderLayout.SOUTH);

		testPanel.setLayout(new BorderLayout());
		testPanel.add(textfield=new JEditorPane("text/html", ""),BorderLayout.CENTER);
		textfield.setText("<html><body><br><br><center><table width=80% height=100%>" +
				"<tr><td align=justify valign=middle>" +
				"<font face=verdana size=4>"+
				PerceptionTest.getDescription()+
				"</td></tr></table><br><br></body></html>");

		controlPanel.add(startTestButton=new JButton("Tests beginnen"));
		startTestButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				startTests();
			}
		});

		controlPanel.add(playButton=new JButton("Video abspielen"));
		playerPanel.setPlayButton(playButton);
		playButton.setEnabled(false);
		playButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				playerPanel.startMedia();
				if(!tutorial)
					resetInputs(!testEnded);
			}
		});

		controlPanel.add(nextTestButton=new JButton("Weiter"));
		nextTestButton.setEnabled(false);
		nextTestButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurrentTest();
				testEnded=!loadTest(PerceptionTest.getNextTest());
				nextTestButton.setEnabled(false);
			}
		});


	}

	protected void resetInputs(boolean enable) {
		for(Component c:testPanel.getComponents())
		{
			if(c instanceof Container)
			{
				for(Component in:((Container)c).getComponents())
				{
					if(in instanceof JSpinner)
					{
						in.setEnabled(enable);
						((JSpinner)in).setValue(new Integer(MIN));
					}
				}
			}
		}
	}

	protected void startTests() {
		startTestButton.setEnabled(false);
		if(loadTest(PerceptionTest.getCurrentTest()))
		{
			logFileName="result_perceptiontest_"+Calendar.getInstance().getTime().toString().replaceAll("[ :]", "_");
		}
	}

	protected void saveCurrentTest() {
		if(tutorial)
			return;
		
		File file=new File("logs/"+logFileName+".txt");
		boolean log=true;
		if(!file.exists())
		{
			try {
				log=file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(log)
		{
			try {
				PrintWriter writer=new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
				writer.println("Test #"+testNumber);
				writer.println("Video: "+currentTest.getVideoFile());
				writer.println("Face Emphasize: "+currentTest.getFaceEmphasize());
				writer.println("Voice Emphasize: "+currentTest.getVoiceEmphasize());

				for(int i=0;i<currentTest.getNumberOfWords();i++)
				{
					writer.println((i+1)+" "+currentTest.getUserSelection(i));
				}
				writer.println();
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			System.err.println("LOGGING FAILED");
			System.out.println("Video:           "+currentTest.getVideoFile());
			System.out.println("Face Emphasize:  "+currentTest.getFaceEmphasize());
			System.out.println("Voice Emphasize: "+currentTest.getVoiceEmphasize());
			for(int i=0;i<currentTest.getNumberOfWords();i++)
			{
				System.out.println((i+1)+"\t"+currentTest.getUserSelection(i));
			}
		}
	}


	protected boolean loadTest(TestCase test) {

		/*
		 * Testreihe beenden
		 */
		if(test==null)
		{
			endTest();
			return false;
		}

		try {
			currentTest=test;

			System.out.println(currentTest);

			remove(testPanel);
			repaint();
			add(testPanel=createTestPanel(),BorderLayout.SOUTH);
			updateComponents();

			playerPanel.loadMedia(URLUtils.createUrlStr(new File("videos/"+currentTest.getVideoFile())),false);

			playButton.setEnabled(true);
			
			/*
			 * Tutorial?
			 */
			if(currentTest instanceof TutorialTestCase)
			{
				tutorial=true;
				
				label.setText("<html><body><font face=verdana size=6><i><b>Beispiel</i></b><font face=verdana size=4> | Die Betonungen sind am unteren Rand unter den jeweiligen Satzteilen eingetragen</body></html>");
				
				if(currentTest.getNumberOfWords() ==
					((TutorialTestCase)currentTest).getNumberOfValues())
				{
					int inputIndex=0;
					for(Component c:testPanel.getComponents())
					{
						if(c instanceof Container)
						{
							for(Component in:((Container)c).getComponents())
							{
								if(in instanceof JSpinner)
								{
									((JSpinner)in).setValue(((TutorialTestCase)currentTest).getValue(inputIndex++));
									((JSpinner)in).setEnabled(true);
								}
							}
						}
					}
				}
			}
			else
			{
				label.setText("<html><body><font face=verdana size=6><i><b>Test #"+(++testNumber)+"</i></b></body></html>");
				tutorial=false;
			}

			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void updateComponents() {
		setSize(getWidth(), getHeight()-1);
		setSize(getWidth(), getHeight()+1);
		setExtendedState(MAXIMIZED_BOTH);
	}

	private void endTest() {
		System.out.println("Test finished.");
		nextTestButton.setEnabled(false);
		playButton.setEnabled(false);
		
		textfield.setText("<html><body><br><br><center><table width=80% height=100%>" +
				"<tr><td align=justify valign=middle>" +
				"<font face=verdana size=5>" +
				"<b>Der Test wurde erfolgreich beendet.</b><br><br>" +
				"<font face=verdana size=4>" +
				PerceptionTest.getEndText() +
				"</td></tr></table><br><br></body></html>");
		
		label.setText("<html><body><font face=verdana size=6><i><b>Test beendet.</i></b></body></html>");
		
		remove(testPanel);
		add(textfield,BorderLayout.SOUTH);
		updateComponents();
	}

	private Container createTestPanel() {
		Container panel=new Container();
		panel.setLayout(new GridLayout(2, currentTest.getNumberOfWords()+1, 10, 20));

		panel.add(createFlowContainer(
				new JLabel("<html><body><center><font face=verdana size=5><i>Satz:</i><br><font face=verdana size=3>(<font face=verdana size=5><b>-</b><font face=verdana size=3>: Betonung<br>zwischen<br>den Silben)")));

		for(final String word:currentTest.getWords())
		{
			panel.add(createFlowContainer(
					new JLabel("<html><body><center><font face=\"times new roman\" size=6><br>"+word)));
		}

		panel.add(createFlowContainer(
				new JLabel("<html><body><center><font face=verdana size=5><i>Betonung:</i><br><font face=verdana size=3>("+MIN+": nicht betont,<br>"+MAX+": stark betont)")));

		int index=0;
		for(final String word:currentTest.getWords())
		{
			panel.add(createFlowContainer(
					createNumberInput(word,MIN,MAX,index++)));
		}

		return panel;
	}

	private Container createFlowContainer(Component comp) {
		Container l=new Container();
		l.setLayout(new FlowLayout(FlowLayout.CENTER));
		l.add(comp);
		return l;
	}

	private Component createNumberInput(final String word, final int min, final int max, final int index) {

		final JSpinner input=new JSpinner();

		input.setModel(new SpinnerNumberModel(1, min, max, 1));
		input.setFont(Font.decode("courier-16"));
		input.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(input.isEnabled())
				{
					Integer newValue=new Integer(((Integer)input.getValue()).intValue()-e.getWheelRotation());
					if(
							newValue.intValue() >= min && 
							newValue.intValue() <= max)
					{
						input.setValue(newValue);
					}
				}
			}
		});
		input.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				nextTestButton.setEnabled(!testEnded);
				currentTest.setUserSelectionForWord(index, ((Integer)input.getValue()));
			}
		});
		input.setEnabled(false);

		return input;
	}

}
