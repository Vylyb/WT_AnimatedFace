package main.view.sliders;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.view.ControlContainer;
import main.view.HtmlLabel;

public class ValueSlider extends Container implements ChangeListener,MouseWheelListener,MouseListener{

	private String str;
	private int index;
	private ControlContainer control;
	private JSlider slider;
	private HtmlLabel label;
	private boolean activated;

	public ValueSlider(String str, int index, ControlContainer control) 
	{
		this.str=str;
		this.index=index;
		this.control=control;
		activated=false;

		slider=new JSlider();

		slider.setMinimum(1);
		slider.setMaximum(ControlContainer.MAX);
		slider.setValue(control.getValue(index));

		slider.addMouseWheelListener(this);
		slider.addChangeListener(this);

		slider.addMouseListener(this);

		label=new HtmlLabel(str+" ("+slider.getValue()+")");
		label.setFont(Font.decode("verdana-6"));

		setLayout(new FlowLayout(FlowLayout.RIGHT, 1, 0));

		add(label);
		add(slider);
	}

	@Override
	public void setEnabled(boolean enable) {
		slider.setEnabled(enable);
	}

	public boolean isActivated() {
		return activated;
	}

	public int getIndex() {
		return index;
	}

	public void setValue(int value)
	{
		if(value<slider.getMinimum())
		{
			slider.setValue(slider.getMinimum());
		}
		else if(value>slider.getMaximum())
		{
			slider.setValue(slider.getMaximum());
		}
		else
		{
			slider.setValue(value);
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		control.setValue(index,slider.getValue());
		label.setText(str+" ("+slider.getValue()+")");
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int value=slider.getValue()-e.getWheelRotation();
		if(value>=slider.getMinimum()&&value<=slider.getMaximum())
			slider.setValue(value);
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		activated=true;
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		activated=false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	public int getValue() {
		return slider.getValue();
	}

}