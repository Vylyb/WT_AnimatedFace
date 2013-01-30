package main.view.sliders;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.AnimatedFace;

public class TimeSlider extends Container {
	
	private JSlider slider;
	
	public TimeSlider() {
		slider=new JSlider();
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent evt) {
				try {
					AnimatedFace.window.animationContainer.updateTimeLabel(((Integer)slider.getValue()).intValue(),AnimatedFace.currentAnimation.getNumberOfFrames());
				} catch (NullPointerException e) {
				}
			}
		});
		slider.setValue(0);
		slider.setMaximum(0);
		slider.setEnabled(false);
		slider.setMinorTickSpacing(1);
		slider.setMajorTickSpacing(AnimatedFace.FPS);
		slider.setPaintTicks(true);
		slider.setSnapToTicks(true);
		slider.setPaintTrack(false);
		
		setLayout(new BorderLayout(0,0));
		add(slider,BorderLayout.CENTER);
	}
	
	@Override
	public void setEnabled(boolean b) {
		slider.setEnabled(b);
	}

	public void setMaximum(int max) {
		slider.setMaximum(max);
	}

	public int getValue() {
		return slider.getValue();
	}

	public void setValue(int value) {
		slider.setValue(value);
	}
}
