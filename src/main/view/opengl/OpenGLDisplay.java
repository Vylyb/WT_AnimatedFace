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
import main.view.sliders.MultiPositionSlider;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;
import org.lwjgl.opengl.Display;


public class OpenGLDisplay extends AWTGLCanvas {

	private int width,height,selectedIndex;
	float norm,thickness,squareWidth;
	private ControlContainer control;

	public OpenGLDisplay() throws LWJGLException{
	}

	public OpenGLDisplay(ControlContainer control) throws LWJGLException{
		this.control=control;
		norm=2.0f/(float)ControlContainer.MAX;
		thickness=0.002f;
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


			glMatrixMode(GL_MODELVIEW);
			glPushMatrix();

			glColor3f(0f, 0f, 0f);
			glTranslatef(-1.0f, -1.0f, 0.0f);
			
			drawLeftEye();
			
			drawRightEye();
			
			drawLeftEyeBrow();
			
			drawRightEyeBrow();
			
			for(MultiPositionSlider s:control.sliders)
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

	private void drawLeftEyeBrow() {
		drawBezierCurve(
				control.LBLFTX,
				control.LBLFTY,
				control.LBUPX,
				control.LBUPY,
				control.LBRGTX,
				control.LBRGTY);
		drawBezierCurve(
				control.LBLFTX,
				control.LBLFTY,
				control.LBLOWX,
				control.LBLOWY,
				control.LBRGTX,
				control.LBRGTY);
	}

	private void drawRightEyeBrow() {
		drawBezierCurve(
				control.RBLFTX,
				control.RBLFTY,
				control.RBUPX,
				control.RBUPY,
				control.RBRGTX,
				control.RBRGTY);
		drawBezierCurve(
				control.RBLFTX,
				control.RBLFTY,
				control.RBLOWX,
				control.RBLOWY,
				control.RBRGTX,
				control.RBRGTY);
	}

	private void drawRightEye() {
		drawBezierCurve(
				control.RELFTX,
				control.RELFTY,
				control.REUPX,
				control.REUPY,
				control.RERGTX,
				control.RERGTY);
		drawBezierCurve(
				control.RELFTX,
				control.RELFTY,
				control.RELOWX,
				control.RELOWY,
				control.RERGTX,
				control.RERGTY);
	}

	private void drawLeftEye() {
		drawBezierCurve(
				control.LELFTX,
				control.LELFTY,
				control.LEUPX,
				control.LEUPY,
				control.LERGTX,
				control.LERGTY);
		drawBezierCurve(
				control.LELFTX,
				control.LELFTY,
				control.LELOWX,
				control.LELOWY,
				control.LERGTX,
				control.LERGTY);
	}

	private void drawBezierCurve(int p1X, int p1Y, int p2X, int p2Y, int p3X, int p3Y) {
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

	private float calculateBezierValues(float t,int p1,int p2,int p3) {
		return ((	control.getFloatValue(p1)
				-2.0f*control.getFloatValue(p2)
				+control.getFloatValue(p3))
				*t*t)
				+
				((	-2.0f*control.getValue(p1)
						+2.0f*control.getValue(p2))
						*t)
						+
						control.getValue(p1);	
	}

	private float normal(float v) {
		return v*norm;
	}

}
