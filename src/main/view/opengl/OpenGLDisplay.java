package main.view.opengl;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glViewport;

import main.view.ControlContainer;
import main.view.PointPositionSlider;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;
import org.lwjgl.opengl.Display;


public class OpenGLDisplay extends AWTGLCanvas {

	private int width,height,selectedIndex;
	float t,x,y,xPrev,yPrev,norm,thickness,squareWidth;
	private ControlContainer control;

	public OpenGLDisplay() throws LWJGLException {
	}

	public OpenGLDisplay(ControlContainer control) throws LWJGLException{
		this.control=control;
		norm=2.0f/(float)control.MAX;
		thickness=0.002f;
		squareWidth=0.02f;
	}

	@Override
	protected void paintGL() 
	{
		try 
		{
			if (getWidth()!=width||getHeight()!=height) 
			{
				width=getWidth();
				height=getHeight();
				glViewport(0, 0, width, height);
			}

			glViewport(0, 0, getWidth(), getHeight());
			glOrtho(0,Display.getWidth(),Display.getHeight(),0,-1,1);//2D projection matrix
			glClearColor(0.7f, 0.7f, 0.7f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT);
			glMatrixMode(GL_PROJECTION);


			glMatrixMode(GL_MODELVIEW);
			glPushMatrix();

			glColor3f(0f, 0f, 0f);
			glTranslatef(-1.0f, -1.0f, 0.0f);
			
			drawLeftEyeBrow();
			
			drawRightEyeBrow();
			
			drawLeftEye();
			
			drawRightEye();
			
			drawMouth();
			
			for(PointPositionSlider s:control.sliders)
			{
				if(s.isActivated())
				{
					//gerader index: x-wert
					//ungerader index: y-wert
					
					if((selectedIndex=s.getIndex())%2==0)
					{
						x=normal((float)control.getValue(selectedIndex));
						y=normal((float)control.getValue(selectedIndex+1));
					}
					else
					{
						x=normal((float)control.getValue(selectedIndex-1));
						y=normal((float)control.getValue(selectedIndex));
					}
					
					glBegin(GL_LINES);
					glVertex2f(x-squareWidth/2, y);
					glVertex2f(x, y+squareWidth/2);
					glVertex2f(x, y+squareWidth/2);
					glVertex2f(x+squareWidth/2, y);
					glVertex2f(x+squareWidth/2, y);
					glVertex2f(x, y-squareWidth/2);
					glVertex2f(x, y-squareWidth/2);
					glVertex2f(x-squareWidth/2, y);
					glEnd();
				}
			}

			glPopMatrix();

			swapBuffers();
			repaint();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	private void drawMouth() {
		drawMouthUpperPart();
		drawMouthLowerPart();
	}

	private void drawRightEye() {
		drawRightEyeUpperPart();
		drawRightEyeLowerPart();
	}

	private void drawLeftEye() {
		drawLeftEyeUpperPart();
		drawLeftEyeLowerPart();
	}

	private void drawRightEyeBrow() {
		xPrev=calculateRightEyeBrowX(0.0f);
		xPrev=normal(xPrev);
		
		yPrev=calculateRightEyeBrowY(0.0f);
		yPrev=normal(yPrev);

		for(t=0.0f;t<=1.0f;t+=0.01f)
		{
			x=calculateRightEyeBrowX(t);
			x=normal(x);

			y=calculateRightEyeBrowY(t);
			y=normal(y);

			if(xPrev!=x&&yPrev!=y)
			{
				glBegin(GL_LINES);
				glVertex2f(xPrev,yPrev);
				glVertex2f(x,y);
				glEnd();
			}
			
			xPrev=x;
			yPrev=y;
		}
	}

	private float calculateRightEyeBrowY(float t) {
		return ((	control.getFloatValue(control.RBLFTY)
				-2.0f*control.getFloatValue(control.RBUPY)
				+control.getFloatValue(control.RBRGTY))
				*t*t)
				+
				((	-2.0f*control.getValue(control.RBLFTY)
						+2.0f*control.getValue(control.RBUPY))
						*t)
						+
						control.getValue(control.RBLFTY);	
	}

	private float calculateRightEyeBrowX(float t) {
		return ((	control.getFloatValue(control.RBLFTX)
				-2.0f*control.getFloatValue(control.RBUPX)
				+control.getFloatValue(control.RBRGTX))
				*t*t)
				+
				((	-2.0f*control.getValue(control.RBLFTX)
						+2.0f*control.getValue(control.RBUPX))
						*t)
						+
						control.getValue(control.RBLFTX);	
	}

	private void drawLeftEyeBrow() {
		xPrev=calculateLeftEyeBrowX(0.0f);
		xPrev=normal(xPrev);
		
		yPrev=calculateLeftEyeBrowY(0.0f);
		yPrev=normal(yPrev);

		for(t=0.0f;t<=1.0f;t+=0.01f)
		{
			x=calculateLeftEyeBrowX(t);
			x=normal(x);

			y=calculateLeftEyeBrowY(t);
			y=normal(y);

			if(xPrev!=x&&yPrev!=y)
			{
				glBegin(GL_LINES);
				glVertex2f(xPrev,yPrev);
				glVertex2f(x,y);
				glEnd();
			}
			
			xPrev=x;
			yPrev=y;
		}

	}

	private float calculateLeftEyeBrowY(float t) {
		return ((	control.getFloatValue(control.LBLFTY)
				-2.0f*control.getFloatValue(control.LBUPY)
				+control.getFloatValue(control.LBRGTY))
				*t*t)
				+
				((	-2.0f*control.getValue(control.LBLFTY)
						+2.0f*control.getValue(control.LBUPY))
						*t)
						+
						control.getValue(control.LBLFTY);	
	}

	private float calculateLeftEyeBrowX(float t) {
		return ((	control.getFloatValue(control.LBLFTX)
				-2.0f*control.getFloatValue(control.LBUPX)
				+control.getFloatValue(control.LBRGTX))
				*t*t)
				+
				((	-2.0f*control.getValue(control.LBLFTX)
						+2.0f*control.getValue(control.LBUPX))
						*t)
						+
						control.getValue(control.LBLFTX);	
	}

	private void drawMouthLowerPart() {
		xPrev=calculateMouthLowerPartX(0.0f);
		xPrev=normal(xPrev);
		
		yPrev=calculateMouthLowerPartY(0.0f);
		yPrev=normal(yPrev);

		for(t=0.0f;t<=1.0f;t+=0.01f)
		{
			x=calculateMouthLowerPartX(t);
			x=normal(x);

			y=calculateMouthLowerPartY(t);
			y=normal(y);

			if(xPrev!=x&&yPrev!=y)
			{
				glBegin(GL_LINES);
				glVertex2f(xPrev,yPrev);
				glVertex2f(x,y);
				glEnd();
			}
			
			xPrev=x;
			yPrev=y;
		}

	}

	private float calculateMouthLowerPartX(float t) {
		return ((	control.getFloatValue(control.MOLFTX)
				-2.0f*control.getFloatValue(control.MOLOWX)
				+control.getFloatValue(control.MORGTX))
				*t*t)
				+
				((	-2.0f*control.getValue(control.MOLFTX)
						+2.0f*control.getValue(control.MOLOWX))
						*t)
						+
						control.getValue(control.MOLFTX);	
	}

	private float calculateMouthLowerPartY(float t) {
		return ((	control.getFloatValue(control.MOLFTY)
				-2.0f*control.getFloatValue(control.MOLOWY)
				+control.getFloatValue(control.MORGTY))
				*t*t)
				+
				((	-2.0f*control.getValue(control.MOLFTY)
						+2.0f*control.getValue(control.MOLOWY))
						*t)
						+
						control.getValue(control.MOLFTY);	
	}

	private void drawMouthUpperPart() {
		xPrev=calculateMouthUpperPartX(0.0f);
		xPrev=normal(xPrev);
		
		yPrev=calculateMouthUpperPartY(0.0f);
		yPrev=normal(yPrev);

		for(t=0.0f;t<=1.0f;t+=0.01f)
		{
			x=calculateMouthUpperPartX(t);
			x=normal(x);

			y=calculateMouthUpperPartY(t);
			y=normal(y);

			if(xPrev!=x&&yPrev!=y)
			{
				glBegin(GL_LINES);
				glVertex2f(xPrev,yPrev);
				glVertex2f(x,y);
				glEnd();
			}
			
			xPrev=x;
			yPrev=y;
		}

	}

	private float calculateMouthUpperPartX(float t) {
		return ((	control.getFloatValue(control.MOLFTX)
				-2.0f*control.getFloatValue(control.MOUPX)
				+control.getFloatValue(control.MORGTX))
				*t*t)
				+
				((	-2.0f*control.getValue(control.MOLFTX)
						+2.0f*control.getValue(control.MOUPX))
						*t)
						+
						control.getValue(control.MOLFTX);	
	}

	private float calculateMouthUpperPartY(float t) {
		return ((	control.getFloatValue(control.MOLFTY)
				-2.0f*control.getFloatValue(control.MOUPY)
				+control.getFloatValue(control.MORGTY))
				*t*t)
				+
				((	-2.0f*control.getValue(control.MOLFTY)
						+2.0f*control.getValue(control.MOUPY))
						*t)
						+
						control.getValue(control.MOLFTY);	
	}

	private void drawRightEyeLowerPart() 
	{
		xPrev=calculateRightEyeLowerPartX(0.0f);
		xPrev=normal(xPrev);
		
		yPrev=calculateRightEyeLowerPartY(0.0f);
		yPrev=normal(yPrev);

		for(t=0.0f;t<=1.0f;t+=0.01f)
		{
			x=calculateRightEyeLowerPartX(t);
			x=normal(x);

			y=calculateRightEyeLowerPartY(t);
			y=normal(y);

			if(xPrev!=x&&yPrev!=y)
			{
				glBegin(GL_LINES);
				glVertex2f(xPrev,yPrev);
				glVertex2f(x,y);
				glEnd();
			}
			
			xPrev=x;
			yPrev=y;
		}

	}

	private float calculateRightEyeLowerPartY(float t) 
	{
		return ((	control.getFloatValue(control.RELFTY)
				-2.0f*control.getFloatValue(control.RELOWY)
				+control.getFloatValue(control.RERGTY))
				*t*t)
				+
				((	-2.0f*control.getValue(control.RELFTY)
						+2.0f*control.getValue(control.RELOWY))
						*t)
						+
						control.getValue(control.RELFTY);	
	}

	private float calculateRightEyeLowerPartX(float t) 
	{
		return ((	control.getFloatValue(control.RELFTX)
				-2.0f*control.getFloatValue(control.RELOWX)
				+control.getFloatValue(control.RERGTX))
				*t*t)
				+
				((	-2.0f*control.getValue(control.RELFTX)
						+2.0f*control.getValue(control.RELOWX))
						*t)
						+
						control.getValue(control.RELFTX);	
	}

	private void drawRightEyeUpperPart() {
		xPrev=calculateRightEyeUpperPartX(0.0f);
		xPrev=normal(xPrev);
		
		yPrev=calculateRightEyeUpperPartY(0.0f);
		yPrev=normal(yPrev);

		for(t=0.0f;t<=1.0f;t+=0.01f)
		{
			x=calculateRightEyeUpperPartX(t);
			x=normal(x);

			y=calculateRightEyeUpperPartY(t);
			y=normal(y);

			if(xPrev!=x&&yPrev!=y)
			{
				glBegin(GL_LINES);
				glVertex2f(xPrev,yPrev);
				glVertex2f(x,y);
				glEnd();
			}
			
			xPrev=x;
			yPrev=y;
		}
	}

	private float calculateRightEyeUpperPartY(float t) {
		return ((	control.getFloatValue(control.RELFTY)
				-2.0f*control.getFloatValue(control.REUPY)
				+control.getFloatValue(control.RERGTY))
				*t*t)
				+
				((	-2.0f*control.getValue(control.RELFTY)
						+2.0f*control.getValue(control.REUPY))
						*t)
						+
						control.getValue(control.RELFTY);	
	}

	private float calculateRightEyeUpperPartX(float t) {
		return ((	control.getFloatValue(control.RELFTX)
				-2.0f*control.getFloatValue(control.REUPX)
				+control.getFloatValue(control.RERGTX))
				*t*t)
				+
				((	-2.0f*control.getValue(control.RELFTX)
						+2.0f*control.getValue(control.REUPX))
						*t)
						+
						control.getValue(control.RELFTX);	
	}

	private void drawLeftEyeLowerPart() 
	{
		xPrev=calculateLeftEyeLowerPartX(0.0f);
		xPrev=normal(xPrev);
		
		yPrev=calculateLeftEyeLowerPartY(0.0f);
		yPrev=normal(yPrev);

		for(t=0.0f;t<=1.0f;t+=0.01f)
		{
			x=calculateLeftEyeLowerPartX(t);
			x=normal(x);

			y=calculateLeftEyeLowerPartY(t);
			y=normal(y);

			if(xPrev!=x&&yPrev!=y)
			{
				glBegin(GL_LINES);
				glVertex2f(xPrev,yPrev);
				glVertex2f(x,y);
				glEnd();
			}
			
			xPrev=x;
			yPrev=y;
		}
	}

	private void drawLeftEyeUpperPart() 
	{
		xPrev=calculateLeftEyeUpperPartX(0.0f);
		xPrev=normal(xPrev);
		
		yPrev=calculateLeftEyeUpperPartY(0.0f);
		yPrev=normal(yPrev);

		for(t=0.0f;t<=1.0f;t+=0.01f)
		{
			x=calculateLeftEyeUpperPartX(t);
			x=normal(x);

			y=calculateLeftEyeUpperPartY(t);
			y=normal(y);

			if(xPrev!=x&&yPrev!=y)
			{
				glBegin(GL_LINES);
				glVertex2f(xPrev,yPrev);
				glVertex2f(x,y);
				glEnd();
			}
			
			xPrev=x;
			yPrev=y;
		}

	}

	private float normal(float v) {
		return v*norm;
	}

	private float calculateLeftEyeUpperPartY(float t) {
		return ((	control.getFloatValue(control.LELFTY)
				-2.0f*control.getFloatValue(control.LEUPY)
				+control.getFloatValue(control.LERGTY))
				*t*t)
				+
				((	-2.0f*control.getValue(control.LELFTY)
						+2.0f*control.getValue(control.LEUPY))
						*t)
						+
						control.getValue(control.LELFTY);	
	}

	private float calculateLeftEyeUpperPartX(float t) {
		return ((	control.getFloatValue(control.LELFTX)
				-2.0f*control.getFloatValue(control.LEUPX)
				+control.getFloatValue(control.LERGTX))
				*t*t)
				+
				((	-2.0f*control.getValue(control.LELFTX)
						+2.0f*control.getValue(control.LEUPX))
						*t)
						+
						control.getValue(control.LELFTX);
	}

	private float calculateLeftEyeLowerPartY(float t) {
		return ((	control.getFloatValue(control.LELFTY)
				-2.0f*control.getFloatValue(control.LELOWY)
				+control.getFloatValue(control.LERGTY))
				*t*t)
				+
				((	-2.0f*control.getValue(control.LELFTY)
						+2.0f*control.getValue(control.LELOWY))
						*t)
						+
						control.getValue(control.LELFTY);	
	}

	private float calculateLeftEyeLowerPartX(float t) {
		return ((	control.getFloatValue(control.LELFTX)
				-2.0f*control.getFloatValue(control.LELOWX)
				+control.getFloatValue(control.LERGTX))
				*t*t)
				+
				((	-2.0f*control.getValue(control.LELFTX)
						+2.0f*control.getValue(control.LELOWX))
						*t)
						+
						control.getValue(control.LELFTX);
	}

}
