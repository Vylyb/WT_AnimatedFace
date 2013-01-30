package main.view;

import java.awt.Container;
import java.awt.GridLayout;
import java.util.Vector;

import main.AnimatedFace;
import main.animation.Animation;
import main.view.sliders.ValueSlider;


public class ControlContainer extends Container {

	public Window window;

	public final int	
			EYE_OUTER_POINT_X=0,	//eye
			EYE_OUTER_POINT_Y=1,
			EYE_INNER_POINT_X=2,
			EYE_INNER_POINT_Y=3,
			EYE_UPPER_POINT_X=4,
			EYE_UPPER_POINT_Y=5,
			EYE_LOWER_POINT_X=6,
			EYE_LOWER_POINT_Y=7,

			BROW_OUTER_POINT_X=8,	//eye brow
			BROW_OUTER_POINT_Y=9,
			BROW_INNER_POINT_X=10,
			BROW_INNER_POINT_Y=11,
			BROW_UPPER_POINT_X=12,
			BROW_UPPER_POINT_Y=13,
			BROW_LOWER_POINT_X=14,
			BROW_LOWER_POINT_Y=15,

			HEAD_OUTER_POINT_X=16,	//kopfform
			HEAD_OUTER_POINT_Y=17,
			HEAD_ARC_POINT_X=18,
			HEAD_ARC_POINT_Y=19,
			HEAD_CENTER_POINT_Y=20;


	public final static int MAX=1000;

	/*
	 * startwerte
	 */
	private int[] values=new int[21];

	public Vector<ValueSlider> sliders=new Vector<ValueSlider>();

	public ControlContainer(Window window) {
		this.window=window;

		AnimatedFace.currentAnimation=new Animation(this);

		setLayout(new GridLayout(0,1,0,-1));
		
		/*
		 * slider für augen
		 */
		sliders.add(new ValueSlider(
				"<html><b>Eyes</b><br>Outer Point X", 
				EYE_OUTER_POINT_X, 
				this));
		add(sliders.lastElement());
		sliders.add(new ValueSlider(
				"<html>Outer Point Y", 
				EYE_OUTER_POINT_Y, 
				this));
		add(sliders.lastElement());
		sliders.add(new ValueSlider(
				"<html>Inner Point X", 
				EYE_INNER_POINT_X, 
				this));
		add(sliders.lastElement());
		sliders.add(new ValueSlider(
				"<html>Inner Point Y", 
				EYE_INNER_POINT_Y, 
				this));
		add(sliders.lastElement());
		sliders.add(new ValueSlider(
				"<html>Upper Point X", 
				EYE_UPPER_POINT_X, 
				this));
		add(sliders.lastElement());
		sliders.add(new ValueSlider(
				"<html>Upper Point Y", 
				EYE_UPPER_POINT_Y, 
				this));
		add(sliders.lastElement());
		sliders.add(new ValueSlider(
				"<html>Lower Point X", 
				EYE_LOWER_POINT_X, 
				this));
		add(sliders.lastElement());
		sliders.add(new ValueSlider(
				"<html>Lower Point Y", 
				EYE_LOWER_POINT_Y, 
				this));
		add(sliders.lastElement());
		
		/*
		 * slider für augenbrauen
		 */
		sliders.add(new ValueSlider(
				"<html><b>Eye Brows</b><br>Outer Point X", 
				BROW_OUTER_POINT_X, 
				this));
		add(sliders.lastElement());
		sliders.add(new ValueSlider(
				"<html>Outer Point Y", 
				BROW_OUTER_POINT_Y, 
				this));
		add(sliders.lastElement());
		sliders.add(new ValueSlider(
				"<html>Inner Point X", 
				BROW_INNER_POINT_X, 
				this));
		add(sliders.lastElement());
		sliders.add(new ValueSlider(
				"<html>Inner Point Y", 
				BROW_INNER_POINT_Y, 
				this));
		add(sliders.lastElement());
		sliders.add(new ValueSlider(
				"<html>Upper Center Point X", 
				BROW_UPPER_POINT_X, 
				this));
		add(sliders.lastElement());
		sliders.add(new ValueSlider(
				"<html>Upper Center Point Y", 
				BROW_UPPER_POINT_Y, 
				this));
		add(sliders.lastElement());
		sliders.add(new ValueSlider(
				"<html>Lower Center Point X", 
				BROW_LOWER_POINT_X, 
				this));
		add(sliders.lastElement());
		sliders.add(new ValueSlider(
				"<html>Lower Center Point Y", 
				BROW_LOWER_POINT_Y, 
				this));
		add(sliders.lastElement());
		
		
		/*
		 * slider für kopfform
		 */
		sliders.add(new ValueSlider(
				"<html><b>Head</b><br>Outer Point X", 
				HEAD_OUTER_POINT_X, 
				this));
		add(sliders.lastElement());
		sliders.add(new ValueSlider(
				"<html>Outer Point Y", 
				HEAD_OUTER_POINT_Y, 
				this));
		add(sliders.lastElement());
		sliders.add(new ValueSlider(
				"<html>Arc Point X", 
				HEAD_ARC_POINT_X, 
				this));
		add(sliders.lastElement());
		sliders.add(new ValueSlider(
				"<html>Arc Point Y", 
				HEAD_ARC_POINT_Y, 
				this));
		add(sliders.lastElement());
		sliders.add(new ValueSlider(
				"<html>Center Point Y", 
				HEAD_CENTER_POINT_Y, 
				this));
		add(sliders.lastElement());
	}

	public int getNumberOfValues(){
		return values.length;
	}

	@Override
	public void setEnabled(boolean enable) {
		for(ValueSlider p:sliders)
		{
			p.setEnabled(enable);
		}
		super.setEnabled(enable);
	}

	public void setValue(int index, int value) 
	{
		try {
			values[index]=value;
//			System.out.println("New Value @ Index "+index+": "+value);
		} catch (IndexOutOfBoundsException e) {
		}
	}

	public void setSliderPosition(int sliderIndex,int value){
		try
		{
			sliders.get(sliderIndex).setValue(value);
//			System.out.println("Setting Slider "+sliderIndex+" to "+value);
		} catch (ArrayIndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}
	}

	public int getValue(int index) {
		return values[index];
	}

	public float getFloatValue(int index) {
		return (float)values[index];
	}

	public void stopAnimation() {
		setEnabled(AnimatedFace.editorMode);
		window.animationContainer.stopAnimation();
	}

	public int getSliderPosition(int index) {
		try {
			return sliders.get(index).getValue();
		} catch (Exception e) {
		}
		return 0;
	}

}
