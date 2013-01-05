package main.view;

import java.awt.Container;
import java.awt.GridLayout;

import main.AnimatedFace;
import main.animation.Animation;


public class ControlContainer extends Container {

	public Window window;

	public final int	LELFTX=0,	//left eye
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

			MOLFTX=16,	//mouth
			MOLFTY=17,
			MORGTX=18,
			MORGTY=19,
			MOUPX=20,
			MOUPY=21,
			MOLOWX=22,
			MOLOWY=23,

			LBLFTX=24,	//right eye brow
			LBLFTY=25,
			LBRGTX=26,
			LBRGTY=27,
			LBUPX=28,
			LBUPY=29,

			RBLFTX=30,	//right eye brow
			RBLFTY=31,
			RBRGTX=32,
			RBRGTY=33,
			RBUPX=34,
			RBUPY=35;


	public final static int MAX=100;

	private int[] values=new int[]{
			28,	//left eye left point x
			73,	//left eye left point y
			46,	//left eye right point x
			66,	//left eye right point y
			41,	//left eye upper point x
			77,	//left eye upper point y
			38,	//left eye lower point x
			58,	//left eye lower point y

			0,	//right eye left point x
			0,	//right eye left point y
			0,	//right eye right point x
			0,	//right eye right point y
			0,	//right eye upper point x
			0,	//right eye upper point y
			0,	//right eye lower point x
			0,	//right eye lower point y

			35,	//mouth left point x
			30,	//mouth left point y
			65,	//mouth right point x
			30,	//mouth right point y
			50,	//mouth upper point x
			35,	//mouth upper point y
			50,	//mouth lower point x
			20,	//mouth lower point y

			25,	//left eye brow left point x
			80,	//left eye brow left point y
			45,	//left eye brow right point x
			70,	//left eye brow right point y
			45,	//left eye brow upper point x
			80,	//left eye brow upper point y

			0,	//right eye left point x
			0,	//right eye left point y
			0,	//right eye right point x
			0,	//right eye right point y
			0,	//right eye upper point x
			0,	//right eye upper point y
	};

	public PointPositionSlider[] sliders=new PointPositionSlider[values.length];

	public ControlContainer(Window window) {
		this.window=window;

		AnimatedFace.currentAnimation=new Animation(this);

		setLayout(new GridLayout(0,1,0,-1));

		//mirror values of the eyes
		values[RELFTY]=values[LERGTY];
		values[RERGTY]=values[LELFTY];

		values[REUPY]=values[LEUPY];
		values[RELOWY]=values[LELOWY];

		values[RELFTX]=100-values[LERGTX];
		values[RERGTX]=100-values[LELFTX];

		values[REUPX]=100-values[LEUPX];
		values[RELOWX]=100-values[LELOWX];

		//mirror values of the eye brows
		values[RBLFTY]=values[LBRGTY];
		values[RBRGTY]=values[LBLFTY];

		values[RBUPY]=values[LBUPY];

		values[RBLFTX]=100-values[LBRGTX];
		values[RBRGTX]=100-values[LBLFTX];

		values[RBUPX]=100-values[LBUPX];


		add(sliders[LELFTX]=new PointPositionSlider("L. Eye, LP, X",1,MAX,LELFTX,this));
		add(sliders[LELFTY]=new PointPositionSlider("L. Eye, LP, Y",1,MAX,LELFTY,this));
		add(sliders[LERGTX]=new PointPositionSlider("L. Eye, RP, X",1,MAX,LERGTX,this));
		add(sliders[LERGTY]=new PointPositionSlider("L. Eye, RP, Y",1,MAX,LERGTY,this));
		add(sliders[LEUPX]=new PointPositionSlider("L. Eye, UP, X",1,MAX,LEUPX,this));
		add(sliders[LEUPY]=new PointPositionSlider("L. Eye, UP, Y",1,MAX,LEUPY,this));
		add(sliders[LELOWX]=new PointPositionSlider("L. Eye, LP, X",1,MAX,LELOWX,this));
		add(sliders[LELOWY]=new PointPositionSlider("L. Eye, LP, Y",1,MAX,LELOWY,this));

		add(sliders[RELFTX]=new PointPositionSlider("R. Eye, LP, X",1,MAX,RELFTX,this));
		add(sliders[RELFTY]=new PointPositionSlider("R. Eye, LP, Y",1,MAX,RELFTY,this));
		add(sliders[RERGTX]=new PointPositionSlider("R. Eye, RP, X",1,MAX,RERGTX,this));
		add(sliders[RERGTY]=new PointPositionSlider("R. Eye, RP, Y",1,MAX,RERGTY,this));
		add(sliders[REUPX]=new PointPositionSlider("R. Eye, UP, X",1,MAX,REUPX,this));
		add(sliders[REUPY]=new PointPositionSlider("R. Eye, UP, Y",1,MAX,REUPY,this));
		add(sliders[RELOWX]=new PointPositionSlider("R. Eye, LP, X",1,MAX,RELOWX,this));
		add(sliders[RELOWY]=new PointPositionSlider("R. Eye, LP, Y",1,MAX,RELOWY,this));

		add(sliders[MOLFTX]=new PointPositionSlider("Mouth, LP, X",1,MAX,MOLFTX,this));
		add(sliders[MOLFTY]=new PointPositionSlider("Mouth, LP, Y",1,MAX,MOLFTY,this));
		add(sliders[MORGTX]=new PointPositionSlider("Mouth, RP, X",1,MAX,MORGTX,this));
		add(sliders[MORGTY]=new PointPositionSlider("Mouth, RP, Y",1,MAX,MORGTY,this));
		add(sliders[MOUPX]=new PointPositionSlider("Mouth, UP, X",1,MAX,MOUPX,this));
		add(sliders[MOUPY]=new PointPositionSlider("Mouth, UP, Y",1,MAX,MOUPY,this));
		add(sliders[MOLOWX]=new PointPositionSlider("Mouth, LP, X",1,MAX,MOLOWX,this));
		add(sliders[MOLOWY]=new PointPositionSlider("Mouth, LP, Y",1,MAX,MOLOWY,this));

		add(sliders[LBLFTX]=new PointPositionSlider("L. Brow, LP, X",1,MAX,LBLFTX,this));
		add(sliders[LBLFTY]=new PointPositionSlider("L. Brow, LP, Y",1,MAX,LBLFTY,this));
		add(sliders[LBRGTX]=new PointPositionSlider("L. Brow, RP, X",1,MAX,LBRGTX,this));
		add(sliders[LBRGTY]=new PointPositionSlider("L. Brow, RP, Y",1,MAX,LBRGTY,this));
		add(sliders[LBUPX]=new PointPositionSlider("L. Brow, UP, X",1,MAX,LBUPX,this));
		add(sliders[LBUPY]=new PointPositionSlider("L. Brow, UP, Y",1,MAX,LBUPY,this));

		add(sliders[RBLFTX]=new PointPositionSlider("R. Brow, LP, X",1,MAX,RBLFTX,this));
		add(sliders[RBLFTY]=new PointPositionSlider("R. Brow, LP, Y",1,MAX,RBLFTY,this));
		add(sliders[RBRGTX]=new PointPositionSlider("R. Brow, RP, X",1,MAX,RBRGTX,this));
		add(sliders[RBRGTY]=new PointPositionSlider("R. Brow, RP, Y",1,MAX,RBRGTY,this));
		add(sliders[RBUPX]=new PointPositionSlider("L. Brow, UP, X",1,MAX,RBUPX,this));
		add(sliders[RBUPY]=new PointPositionSlider("L. Brow, UP, Y",1,MAX,RBUPY,this));
	}

	public int getNumberOfValues(){
		return values.length;
	}

	@Override
	public void setEnabled(boolean enable) {
		for(PointPositionSlider p:sliders)
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
			sliders[sliderIndex].setValue(sliders[sliderIndex].getValue()+valueChange);
		} catch (ArrayIndexOutOfBoundsException e)
		{

		}
	}
	
	public void setSliderPosition(int sliderIndex,int value){
		try
		{
			sliders[sliderIndex].setValue(value);
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
		window.animtationContainer.stopAnimation();
	}

}
