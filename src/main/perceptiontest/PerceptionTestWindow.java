package main.perceptiontest;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;

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

	public PerceptionTestWindow() {
		setLayout(new BorderLayout());
//		setSize(getToolkit().getScreenSize().width, getToolkit().getScreenSize().height);
		setSize(1024, 768);
//		setExtendedState(MAXIMIZED_BOTH);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		add(playerPanel=new PlayerPanel(),BorderLayout.CENTER);
		
		add(controlPanel=new Container(),BorderLayout.NORTH);
		controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,5));
		
		add(testPanel=new Container(),BorderLayout.SOUTH);
		testPanel.setLayout(new BorderLayout());
		testPanel.add(textfield=new JEditorPane("text/html", ""),BorderLayout.CENTER);
		textfield.setText("<html><body><table width=100% height=100%>" +
				"<tr><td align=center valign=middle>" +
				"<font face=verdana size=3>"+
				PerceptionTest.getDescription()+
				"</td></tr></table></body></html>");
		
		controlPanel.add(nextTestButton=new JButton("Next Test"));
		nextTestButton.setEnabled(false);
		nextTestButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurrentTest();
				loadTest(PerceptionTest.getNextTest());
			}
		});
		
		controlPanel.add(playButton=new JButton("Play Video"));
		playerPanel.setPlayButton(playButton);
		playButton.setEnabled(false);
		playButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				playerPanel.startMedia();
			}
		});

		controlPanel.add(startTestButton=new JButton("Start Tests"));
		startTestButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				startTests();
			}
		});
		
		
	}

	protected void startTests() {
		startTestButton.setEnabled(false);
		loadTest(PerceptionTest.getCurrentTest());
	}

	protected void saveCurrentTest() {
		for(int i=0;i<currentTest.getNumberOfWords();i++){
			System.out.println(currentTest.getUserSelection(i));
		}
	}

	protected void loadTest(TestCase test) {
		try {
			currentTest=test;
			
			System.out.println(currentTest);
			
			remove(testPanel);
			repaint();
			add(testPanel=createTestPanel(),BorderLayout.SOUTH);
			setSize(getWidth(), getHeight()-1);
			setSize(getWidth(), getHeight()+1);
			
			playerPanel.loadMedia(URLUtils.createUrlStr(new File("videos/"+currentTest.getVideoFile())),false);
			
			playButton.setEnabled(true);
			nextTestButton.setEnabled(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Container createTestPanel() {
		Container panel=new Container();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		
		int index=0;
		for(final String word:currentTest.getWords())
		{
			final JCheckBox box;
			panel.add(box=new JCheckBox(word, false));
			box.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					currentTest.setUserSelectionForWord(word,(box.isSelected()?1:0));
				}
			});
		}
		
		return panel;
	}

}
