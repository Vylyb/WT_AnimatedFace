package main.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.AnimatedFace;
import main.animation.AnimationStep;
import main.animation.Change;


public class AnimationContainer extends Container {

	private final static String EDITOR="Editor",SIMUL="Simulation";

	private Window window;
	private JSlider timeSlider;
	private JButton stateButton;
	private JButton newStepButton;
	private JSpinner timeToggle;
	private JComboBox<AnimationStep> keyFrameList;

	private Vector<Integer> lastValues;

	private HtmlLabel maxTimeLabel;

	private JButton playButton;

	public AnimationContainer(final Window window) {
		this.window=window;

		setLayout(new GridLayout(2,1));

		Container c1=new Container();
		c1.setLayout(new BorderLayout());

		c1.add(new HtmlLabel(AnimatedFace.toSecondsAndFrames(0)),BorderLayout.WEST);

		c1.add(timeSlider=new JSlider(),BorderLayout.CENTER);
		timeSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent evt) {
				try {
					updateTimeLabel(((Integer)timeSlider.getValue()).intValue(),AnimatedFace.currentAnimation.getNumberOfFrames());
				} catch (NullPointerException e) {
				}
			}
		});
		timeSlider.setValue(0);
		timeSlider.setMaximum(0);
		timeSlider.setEnabled(false);
		timeSlider.setMinorTickSpacing(1);
		timeSlider.setMajorTickSpacing(AnimatedFace.FPS);
		timeSlider.setPaintTicks(true);
		timeSlider.setSnapToTicks(true);
		timeSlider.setPaintTrack(false);

		c1.add(maxTimeLabel=new HtmlLabel("--:-- / --:--"),BorderLayout.EAST);

		add(c1);

		Container c2=new Container();
		c2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

		c2.add(stateButton=new JButton(AnimatedFace.editorMode?EDITOR:SIMUL));
		stateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				AnimatedFace.editorMode=!AnimatedFace.editorMode;
				stateButton.setText(AnimatedFace.editorMode?EDITOR:SIMUL);
				window.controlContainer.setEnabled(AnimatedFace.editorMode);
				newStepButton.setEnabled(AnimatedFace.editorMode);
				playButton.setEnabled(!AnimatedFace.editorMode);
			}
		});

		c2.add(newStepButton=new JButton("Add scene to animation"));
		newStepButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				addCurrentStep();
			}
		});

		c2.add(timeToggle=new JSpinner());
		timeToggle.setModel(new SpinnerNumberModel(0, 0, AnimatedFace.FPS*AnimatedFace.MAX_SEC, 1));
		timeToggle.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent evt) {
				Integer newValue=new Integer(((Integer)timeToggle.getValue()).intValue()-evt.getWheelRotation());
				if(newValue.intValue()>=0&&newValue.intValue()<=AnimatedFace.FPS*AnimatedFace.MAX_SEC)
				{
					timeToggle.setValue(newValue);
				}
			}
		});
		timeToggle.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent evt) {
			}
		});

		c2.add(keyFrameList=new JComboBox<AnimationStep>());
		keyFrameList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
//					System.out.println(keyFrameList.getSelectedItem()+"\t"+AnimatedFace.framesfromString((String) keyFrameList.getSelectedItem().toString()));
				} catch (NullPointerException e) {
				}
			}
		});

		c2.add(playButton=new JButton("Play"));
		playButton.setEnabled(false);
		playButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				startAnimation();
			}
		});

		addCurrentStep();

		add(c2);
	}

	protected void startAnimation() {
		AnimatedFace.playCurrentAnimation(playButton,timeSlider);
	}

	protected void addCurrentStep() {
		AnimationStep newStep=new AnimationStep(getTime());
		if(lastValues==null)
		{
			lastValues=new Vector<Integer>();
			for(int i=0;i<window.controlContainer.getNumberOfValues();i++)
			{
				lastValues.add(new Integer(window.controlContainer.getValue(i)));
				newStep.addChange(new Change(i, window.controlContainer.getValue(i)));
			}
		}
		else
		{
			for(int i=0;i<window.controlContainer.getNumberOfValues();i++)
			{
				if(lastValues.get(i).intValue()!=window.controlContainer.getValue(i))
				{
					newStep.addChange(new Change(i, window.controlContainer.getValue(i)-lastValues.get(i)));
					lastValues.setElementAt(new Integer(window.controlContainer.getValue(i)), i);
				}
			}
		}
		AnimatedFace.currentAnimation.addStep(newStep);

		updateTimeSlider();

		/*
		 * update the list of key frames
		 */
		keyFrameList.removeAllItems();
		for(AnimationStep step:AnimatedFace.currentAnimation.getSteps())
		{
			keyFrameList.addItem(step);
		}

	}

	private void updateTimeSlider() {
		int newMaxTime=AnimatedFace.currentAnimation.getNumberOfFrames();
		timeSlider.setEnabled(newMaxTime>0);
		timeSlider.setMaximum(newMaxTime);
		if(timeSlider.getValue()>newMaxTime)
		{
			timeSlider.setValue(newMaxTime);
		}
		updateTimeLabel(timeSlider.getValue(),newMaxTime);
	}

	public void updateTimeLabel(int currentTime, int maxTime) {
		maxTimeLabel.setText(AnimatedFace.toSecondsAndFrames(currentTime)
				+" / "+AnimatedFace.toSecondsAndFrames(maxTime));
	}

	public int getTime() {
		return ((Integer)timeToggle.getValue()).intValue();
	}

	public int getLastValue(int valueID)
			throws Exception{
		return lastValues.get(valueID).intValue();
	}

	public void stopAnimation() {
		playButton.setEnabled(true);
		timeSlider.setEnabled(true);
	}

	public void moveTimeSlider(int value) {
		timeSlider.setValue(value);
	}

}
