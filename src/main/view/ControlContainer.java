package main.view;

import java.awt.Container;
import java.awt.GridLayout;
import java.util.Vector;

import main.AnimatedFace;
import main.animation.Animation;
import main.view.sliders.MultiPositionSlider;


public class ControlContainer extends Container {

	public Window window;

	public final int	
			LELFTX=0,	//left eye
			LELFTY=1,
			LERGTX=2,
			LERGTY=3,
			LEUPX=4,
			LEUPY=5,
			LELOWX=6,
			LELOWY=7,

			RELFTX=8,	//right eye
			RELFTY=9,
			RERGTX=10,
			RERGTY=11,
			REUPX=12,
			REUPY=13,
			RELOWX=14,
			RELOWY=15,

			LBLFTX=16,	//left eye brow
			LBLFTY=17,
			LBRGTX=18,
			LBRGTY=19,
			LBUPX=20,
			LBUPY=21,
			LBLOWX=22,
			LBLOWY=23,

			RBLFTX=24,	//right eye brow
			RBLFTY=25,
			RBRGTX=26,
			RBRGTY=27,
			RBUPX=28,
			RBUPY=29,
			RBLOWX=30,
			RBLOWY=31,
		
			HLOUTX=32,	//kopfform - linker bogen
			HLOUTY=33,
			HLARCX=34,
			HLARCY=35,
			HDINX=36,
			HDINY=37,	//rechter bogen
			HROUTX=38,
			HROUTY=39,
			HRARCX=40,
			HRARCY=41;


	public final static int MAX=1000;

	/*
	 * startwerte
	 */
	private int[] values=new int[42];
//			23,	//left eye left point x
//			31,	//left eye left point y
//			43,	//left eye right point x
//			31,	//left eye right point y
//			34,	//left eye upper point x
//			38,	//left eye upper point y
//			35,	//left eye lower point x
//			25,	//left eye lower point y
//
//			0,	//right eye left point x
//			0,	//right eye left point y
//			0,	//right eye right point x
//			0,	//right eye right point y
//			0,	//right eye upper point x
//			0,	//right eye upper point y
//			0,	//right eye lower point x
//			0,	//right eye lower point y
//
//			22,	//left eye brow left point x
//			36,	//left eye brow left point y
//			47,	//left eye brow right point x
//			36,	//left eye brow right point y
//			19,	//left eye brow upper point x
//			47,	//left eye brow upper point y
//			21,	//left eye brow lower point x
//			45,	//left eye brow lower point y
//
//			0,	//right eye left point x
//			0,	//right eye left point y
//			0,	//right eye right point x
//			0,	//right eye right point y
//			0,	//right eye upper point x
//			0,	//right eye upper point y
//			0,	//right eye brow lower point x
//			0,	//right eye brow lower point y
//			
//			15,	//head left outer point x
//			20,	//head left outer point y
//			15,	//head left arc point x
//			55,	//head left arc point y
//			50,	//head inner point x
//			60,	//head inner point y
//			0,	//head right outer point x
//			0,	//head right outer point y
//			0,	//head right arc point x
//			0,	//head right arc point y
//	};

	public Vector<MultiPositionSlider> sliders=new Vector<MultiPositionSlider>();

	public ControlContainer(Window window) {
		this.window=window;

		AnimatedFace.currentAnimation=new Animation(this);

		setLayout(new GridLayout(0,1,0,-1));
		
		for(int i=0;i<values.length;i++){
			values[i]*=MAX/100;
		}

//		//mirror values of the eyes
//		values[RELFTY]=values[LERGTY];
//		values[RERGTY]=values[LELFTY];
//
//		values[REUPY]=values[LEUPY];
//		values[RELOWY]=values[LELOWY];
//
//		values[RELFTX]=MAX-values[LERGTX];
//		values[RERGTX]=MAX-values[LELFTX];
//
//		values[REUPX]=MAX-values[LEUPX];
//		values[RELOWX]=MAX-values[LELOWX];
//
//		//mirror values of the eye brows
//		values[RBLFTY]=values[LBRGTY];
//		values[RBRGTY]=values[LBLFTY];
//
//		values[RBUPY]=values[LBUPY];
//		values[RBLOWY]=values[LBLOWY];
//
//		values[RBLFTX]=MAX-values[LBRGTX];
//		values[RBRGTX]=MAX-values[LBLFTX];
//
//		values[RBUPX]=MAX-values[LBUPX];
//		values[RBLOWX]=MAX-values[LBLOWX];
//		
//		//mirror values of the head form
//		values[HROUTX]=MAX-values[HLOUTX];
//		values[HROUTY]=values[HLOUTY];
//		values[HRARCX]=MAX-values[HLARCX];
//		values[HRARCY]=values[HLARCY];
		
		values[HDINX]=MAX/2;

		/*
		 * slider für augen
		 */
		sliders.add(new MultiPositionSlider(
				"<html><b>Eyes</b><br>Outer Point X", 
				LELFTX, 
				RERGTX, 
				true,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Outer Point Y", 
				LELFTY, 
				RERGTY, 
				false,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Inner Point X", 
				LERGTX, 
				RELFTX, 
				true,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Inner Point Y", 
				LERGTY, 
				RELFTY, 
				false,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Upper Point X", 
				LEUPX, 
				REUPX, 
				true,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Upper Point Y", 
				LEUPY, 
				REUPY, 
				false,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Lower Point X", 
				LELOWX, 
				RELOWX, 
				true,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Lower Point Y", 
				LELOWY, 
				RELOWY, 
				false,
				this));
		add(sliders.lastElement());
		
		/*
		 * slider für augenlider
		 */
		sliders.add(new MultiPositionSlider(
				"<html>Outer Point X", 
				LELFTX, 
				RERGTX, 
				true,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Outer Point Y", 
				LELFTY, 
				RERGTY, 
				false,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Inner Point X", 
				LERGTX, 
				RELFTX, 
				true,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Inner Point Y", 
				LERGTY, 
				RELFTY, 
				false,
				this));
		add(sliders.lastElement());
		
		
		/*
		 * slider für augenbrauen
		 */
		sliders.add(new MultiPositionSlider(
				"<html><b>Eye Brows</b><br>Outer Point X", 
				LBLFTX, 
				RBRGTX, 
				true,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Outer Point Y", 
				LBLFTY, 
				RBRGTY, 
				false,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Inner Point X", 
				LBRGTX, 
				RBLFTX, 
				true,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Inner Point Y", 
				LBRGTY, 
				RBLFTY, 
				false,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Upper Center Point X", 
				LBUPX, 
				RBUPX, 
				true,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Upper Center Point Y", 
				LBUPY, 
				RBUPY, 
				false,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Lower Center Point X", 
				LBLOWX, 
				RBLOWX, 
				true,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Lower Center Point Y", 
				LBLOWY, 
				RBLOWY, 
				false,
				this));
		add(sliders.lastElement());
		
		
		/*
		 * slider für kopfform
		 */
		sliders.add(new MultiPositionSlider(
				"<html><b>Head</b><br>Outer Point X", 
				HLOUTX, 
				HROUTX, 
				true,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Outer Point Y", 
				HLOUTY, 
				HROUTY, 
				false,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Arc Point X", 
				HLARCX, 
				HRARCX, 
				true,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Arc Point Y", 
				HLARCY, 
				HRARCY, 
				false,
				this));
		add(sliders.lastElement());
		sliders.add(new MultiPositionSlider(
				"<html>Center Point Y", 
				HDINY, 
				HDINY, 
				false,
				this));
		add(sliders.lastElement());
	}

	public int getNumberOfValues(){
		return values.length;
	}

	@Override
	public void setEnabled(boolean enable) {
		for(MultiPositionSlider p:sliders)
		{
			p.setEnabled(enable);
		}
		super.setEnabled(enable);
	}

	public void setValue(int index, int value) 
	{
		try {
			values[index]=value;
		} catch (IndexOutOfBoundsException e) {
		}
	}

	public void moveSliderBy(int sliderIndex,int valueChange)
	{
		try
		{
			sliders.get(sliderIndex).setValue(sliders.get(sliderIndex).getValue()+valueChange);
		} catch (ArrayIndexOutOfBoundsException e)
		{

		}
	}
	
	public void setSliderPosition(int sliderIndex,int value){
		try
		{
			sliders.get(sliderIndex).setValue(value);
		} catch (ArrayIndexOutOfBoundsException e)
		{

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
