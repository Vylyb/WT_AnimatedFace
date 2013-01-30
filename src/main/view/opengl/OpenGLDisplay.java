package main.view.opengl;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glViewport;
import main.view.ControlContainer;
import main.view.sliders.ValueSlider;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;


public class OpenGLDisplay extends AWTGLCanvas {

	private int width,height,selectedIndex;
	private float norm,thickness,squareWidth;
	private ControlContainer control;

	public OpenGLDisplay() throws LWJGLException{
	}

	public OpenGLDisplay(ControlContainer control) throws LWJGLException{
		this.control=control;
		norm=2.0f/(float)ControlContainer.MAX;
		thickness=1.5f;
		squareWidth=0.02f;
	}

	@Override
	protected void paintGL() 
	{
		float x,y;
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


			glMatrixMode(GL11.GL_MODELVIEW_MATRIX);
			glPushMatrix();

			glColor3f(0f, 0f, 0f);
			glTranslatef(-1.0f, -1.0f, 0.0f);

			glLineWidth(thickness);

			drawLeftEye();

			drawRightEye();

			drawLeftEyeBrow();

			drawRightEyeBrow();

			drawHead();

			for(ValueSlider s:control.sliders)
			{
				if(s.isActivated())
				{
					//gerader index: x-wert
					//ungerader index: y-wert

					try {
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
					} catch (Exception e) {
					}
				}
			}

			glPopMatrix();

			swapBuffers();
			repaint();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	private void drawHead() {
		drawBezierCurve(
				control.getFloatValue(control.HEAD_OUTER_POINT_X), 
				control.getFloatValue(control.HEAD_OUTER_POINT_Y), 
				control.getFloatValue(control.HEAD_ARC_POINT_X), 
				control.getFloatValue(control.HEAD_ARC_POINT_Y), 
				(float)(ControlContainer.MAX/2), 
				control.getFloatValue(control.HEAD_CENTER_POINT_Y));
		drawBezierCurve(
				ControlContainer.MAX - control.getFloatValue(control.HEAD_OUTER_POINT_X), 
				control.getFloatValue(control.HEAD_OUTER_POINT_Y), 
				ControlContainer.MAX - control.getFloatValue(control.HEAD_ARC_POINT_X), 
				control.getFloatValue(control.HEAD_ARC_POINT_Y), 
				(float)(ControlContainer.MAX/2), 
				control.getFloatValue(control.HEAD_CENTER_POINT_Y));
	}

	private void drawLeftEyeBrow() {
		drawBezierCurve(
				control.getFloatValue(control.BROW_OUTER_POINT_X),
				control.getFloatValue(control.BROW_OUTER_POINT_Y),
				control.getFloatValue(control.BROW_UPPER_POINT_X),
				control.getFloatValue(control.BROW_UPPER_POINT_Y),
				control.getFloatValue(control.BROW_INNER_POINT_X),
				control.getFloatValue(control.BROW_INNER_POINT_Y));
		drawBezierCurve(
				control.getFloatValue(control.BROW_OUTER_POINT_X),
				control.getFloatValue(control.BROW_OUTER_POINT_Y),
				control.getFloatValue(control.BROW_LOWER_POINT_X),
				control.getFloatValue(control.BROW_LOWER_POINT_Y),
				control.getFloatValue(control.BROW_INNER_POINT_X),
				control.getFloatValue(control.BROW_INNER_POINT_Y));
	}

	private void drawRightEyeBrow() {
		drawBezierCurve(
				ControlContainer.MAX - control.getFloatValue(control.BROW_OUTER_POINT_X),
				control.getFloatValue(control.BROW_OUTER_POINT_Y),
				ControlContainer.MAX - control.getFloatValue(control.BROW_UPPER_POINT_X),
				control.getFloatValue(control.BROW_UPPER_POINT_Y),
				ControlContainer.MAX - control.getFloatValue(control.BROW_INNER_POINT_X),
				control.getFloatValue(control.BROW_INNER_POINT_Y));
		drawBezierCurve(
				ControlContainer.MAX - control.getFloatValue(control.BROW_OUTER_POINT_X),
				control.getFloatValue(control.BROW_OUTER_POINT_Y),
				ControlContainer.MAX - control.getFloatValue(control.BROW_LOWER_POINT_X),
				control.getFloatValue(control.BROW_LOWER_POINT_Y),
				ControlContainer.MAX - control.getFloatValue(control.BROW_INNER_POINT_X),
				control.getFloatValue(control.BROW_INNER_POINT_Y));
	}

	private void drawRightEye() {
		drawBezierCurve(
				ControlContainer.MAX - control.getFloatValue(control.EYE_OUTER_POINT_X),
				control.getFloatValue(control.EYE_OUTER_POINT_Y),
				ControlContainer.MAX - control.getFloatValue(control.EYE_UPPER_POINT_X),
				control.getFloatValue(control.EYE_UPPER_POINT_Y),
				ControlContainer.MAX - control.getFloatValue(control.EYE_INNER_POINT_X),
				control.getFloatValue(control.EYE_INNER_POINT_Y));
		drawBezierCurve(
				ControlContainer.MAX - control.getFloatValue(control.EYE_OUTER_POINT_X),
				control.getFloatValue(control.EYE_OUTER_POINT_Y),
				ControlContainer.MAX - control.getFloatValue(control.EYE_LOWER_POINT_X),
				control.getFloatValue(control.EYE_LOWER_POINT_Y),
				ControlContainer.MAX - control.getFloatValue(control.EYE_INNER_POINT_X),
				control.getFloatValue(control.EYE_INNER_POINT_Y));
	}

	private void drawLeftEye() {
		drawBezierCurve(
				control.getFloatValue(control.EYE_OUTER_POINT_X),
				control.getFloatValue(control.EYE_OUTER_POINT_Y),
				control.getFloatValue(control.EYE_UPPER_POINT_X),
				control.getFloatValue(control.EYE_UPPER_POINT_Y),
				control.getFloatValue(control.EYE_INNER_POINT_X),
				control.getFloatValue(control.EYE_INNER_POINT_Y));
		drawBezierCurve(
				control.getFloatValue(control.EYE_OUTER_POINT_X),
				control.getFloatValue(control.EYE_OUTER_POINT_Y),
				control.getFloatValue(control.EYE_LOWER_POINT_X),
				control.getFloatValue(control.EYE_LOWER_POINT_Y),
				control.getFloatValue(control.EYE_INNER_POINT_X),
				control.getFloatValue(control.EYE_INNER_POINT_Y));
	}

	private void drawBezierCurve(
			float p1X, float p1Y, 
			float p2X, float p2Y, 
			float p3X, float p3Y) {
		
		float xPrev = calculateBezierValues(0.0f, p1X, p2X, p3X);
		xPrev=normal(xPrev);

		float yPrev = calculateBezierValues(0.0f, p1Y, p2Y, p3Y);
		yPrev=normal(yPrev);

		float x,y;
		for(float t = 0.0f;t<=1.0f;t+=0.01f)
		{
			x = calculateBezierValues(t, p1X, p2X, p3X);
			x=normal(x);

			y = calculateBezierValues(t, p1Y, p2Y, p3Y);
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

	private float calculateBezierValues(float t,float p1,float p2,float p3) {
		return (
				(p1 - 2.0f * p2 + p3) * t * t)
				+
				((-2.0f * p1 + 2.0f * p2) * t)
				+
				p1;
	}

	private float normal(float v) {
		return v*norm;
	}

}
