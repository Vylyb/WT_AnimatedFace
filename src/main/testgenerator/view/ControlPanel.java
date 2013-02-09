package main.testgenerator.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.TestCaseGenerator;
import main.testgenerator.file.FileWithSentence;
import main.xml.XmlFactory;

public class ControlPanel extends Container {

	private JLabel status;
	private Vector<FileWithSentence> usedFiles;
	private Container settingContainer;
	private int max;
	private int numberOfStimuli;
	protected JSpinner numberOfTestsSpinner;
	private FlowLayout controlLayout;
	private JSpinner numberOfStimuliPerTestSpinner;
	protected int numberOfTests;
	protected int numberOfStimuliPerTest;
	private final Font spinnerFont=Font.decode("courier-bold-14");
	private Container generateContainer;
	private JButton generateTestsButton;

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

	public ControlPanel() {
		setLayout(new BorderLayout(10, 10));
		usedFiles=new Vector<FileWithSentence>();

		
		
		settingContainer=new Container();
		settingContainer.setLayout(new GridLayout(0, 1, 10, 5));
		
		controlLayout=new FlowLayout(FlowLayout.RIGHT, 10, 5);

		Container numberOfTestsContainer=new Container();
		numberOfTestsContainer.setLayout(controlLayout);
		numberOfTestsContainer.add(new JLabel("Anzahl der generierten Tests:"));
		numberOfTestsContainer.add(numberOfTestsSpinner=new JSpinner(new SpinnerNumberModel(0, 0, 0, 1)));
		numberOfTestsSpinner.setFont(spinnerFont);
		numberOfTestsSpinner.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				Integer newValue=new Integer(((Integer)numberOfTestsSpinner.getValue()).intValue()-e.getWheelRotation());
				if(newValue.intValue() >= 0) 
				{
					numberOfTestsSpinner.setValue(newValue);
				}
			}
		});
		numberOfTestsSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				numberOfTests=((Integer)numberOfTestsSpinner.getValue()).intValue();
				updateStatusLabel();
			}
		});
		settingContainer.add(numberOfTestsContainer);

		Container numberOfStimuliPerTestContainer=new Container();
		numberOfStimuliPerTestContainer.setLayout(controlLayout);
		numberOfStimuliPerTestContainer.add(new JLabel("Anzahl Stimuli pro Test:"));
		numberOfStimuliPerTestContainer.add(numberOfStimuliPerTestSpinner=new JSpinner(new SpinnerNumberModel(0, 0, 0, 1)));
		numberOfStimuliPerTestSpinner.setFont(spinnerFont);
		numberOfStimuliPerTestSpinner.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				Integer newValue=new Integer(((Integer)numberOfStimuliPerTestSpinner.getValue()).intValue()-e.getWheelRotation());
				if(newValue.intValue() >= 0) 
				{
					numberOfStimuliPerTestSpinner.setValue(newValue);
				}
			}
		});
		numberOfStimuliPerTestSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				numberOfStimuliPerTest=((Integer)numberOfStimuliPerTestSpinner.getValue()).intValue();
				updateStatusLabel();
			}
		});
		settingContainer.add(numberOfStimuliPerTestContainer);

		Container labelContainer=new Container();
		labelContainer.setLayout(controlLayout);
		labelContainer.add(status=new JLabel());
		settingContainer.add(labelContainer);

		add(settingContainer,BorderLayout.WEST);

		updatePanel();
		
		
		generateContainer=new Container();
		generateContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		generateContainer.add(generateTestsButton=new JButton("Tests generieren"));
		generateTestsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				generateTests();
			}
		});
		
		add(generateContainer,BorderLayout.CENTER);
	}

	protected void generateTests() {
		System.out.println("Generiere "+numberOfTests+" Tests à "+numberOfStimuliPerTest+" Stimuli\n");
		if(numberOfStimuli>numberOfStimuliPerTest)
		{
			for(int i=0;i<numberOfTests;i++)
			{
				System.out.println("Test #"+(i+1));
				
				boolean[] usedFiles=new boolean[numberOfStimuli];
				for(int k=0;k<usedFiles.length;k++)
				{
					usedFiles[k]=false;
				}
				
				int createdTests=0;
				while(createdTests<numberOfStimuliPerTest)
				{
					int index=(int) (Math.random()*numberOfStimuli);
					if(!usedFiles[index])
					{
						usedFiles[index]=true;
						createdTests++;
						
						System.out.println(XmlFactory.startTag(TESTCASE));
						
						System.out.println(XmlFactory.stringToTag(
								this.usedFiles.get(index).getFile().getAbsolutePath(),
								VIDEO_FILE));
						
						System.out.println(XmlFactory.stringToTag(
								this.usedFiles.get(index).getFolder().getSentenceAsXML(), 
								SENTENCE));
						
						System.out.println(XmlFactory.closeTag(TESTCASE));
					}
				}
			}
		}
	}

	public void updatePanel() {
		try {
			usedFiles=TestCaseGenerator.window.folderlist.listAllUsedFiles();
			numberOfStimuli=usedFiles.size();
		} catch (Exception e) {
		}
		
		updateStatusLabel();

	}

	private void updateStatusLabel() {
		status.setText(numberOfStimuli+" Dateien sind verfügbar");
	}


}
