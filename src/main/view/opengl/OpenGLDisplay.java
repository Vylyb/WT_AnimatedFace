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
import static org.lwjgl.opengl.GL11.glLineWidth;
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
	private float norm,thickness,squareWidth,eyeRadius;
	private ControlContainer control;

	public OpenGLDisplay() throws LWJGLException{
	}

	public OpenGLDisplay(ControlContainer control) throws LWJGLException{
		this.control=control;
		norm=2.0f/(float)ControlContainer.MAX;
		thickness=1.5f;
		squareWidth=0.02f;
		eyeRadius=0.02f;
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
			
			glLineWidth(thickness);
			
			drawLeftEye();
			
			drawRightEye();
			
			drawLeftEyeBrow();
			
			drawRightEyeBrow();
			
			drawHead();
			
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

	private void drawHead() {
		drawBezierCurve(
				control.HLOUTX, 
				control.HLOUTY, 
				control.HLARCX, 
				control.HLARCY, 
				control.HDINX, 
				control.HDINY, 
				true);
		drawBezierCurve(
				control.HROUTX, 
				control.HROUTY, 
				control.HRARCX, 
				control.HRARCY, 
				control.HDINX, 
				control.HDINY, 
				true);
	}

	private void drawLeftEyeBrow() {
		drawBezierCurve(
				control.LBLFTX,
				control.LBLFTY,
				control.LBUPX,
				control.LBUPY,
				control.LBRGTX,
				control.LBRGTY,
				true);
		drawBezierCurve(
				control.LBLFTX,
				control.LBLFTY,
				control.LBLOWX,
				control.LBLOWY,
				control.LBRGTX,
				control.LBRGTY,
				true);
	}

	private void drawRightEyeBrow() {
		drawBezierCurve(
				control.RBLFTX,
				control.RBLFTY,
				control.RBUPX,
				control.RBUPY,
				control.RBRGTX,
				control.RBRGTY,
				true);
		drawBezierCurve(
				control.RBLFTX,
				control.RBLFTY,
				control.RBLOWX,
				control.RBLOWY,
				control.RBRGTX,
				control.RBRGTY,
				true);
	}

	private void drawRightEye() {
		drawBezierCurve(
				control.RELFTX,
				control.RELFTY,
				control.REUPX,
				control.REUPY,
				control.RERGTX,
				control.RERGTY,
				true);
		drawBezierCurve(
				control.RELFTX,
				control.RELFTY,
				control.RELOWX,
				control.RELOWY,
				control.RERGTX,
				control.RERGTY,
				true);
	}

	private void drawLeftEye() {
		drawBezierCurve(
				control.LELFTX,
				control.LELFTY,
				control.LEUPX,
				control.LEUPY,
				control.LERGTX,
				control.LERGTY,
				true);
		drawBezierCurve(
				control.LELFTX,
				control.LELFTY,
				control.LELOWX,
				control.LELOWY,
				control.LERGTX,
				control.LERGTY,
				true);
		drawEyeBall(
				control.LELFTX,
				control.LELFTY,
				control.LEUPX,
				control.LEUPY,
				control.LERGTX,
				control.LERGTY,
				control.LELOWX,
				control.LELOWY);

	}

	private void drawEyeBall(int x1, int y1, int x2, int y2,
			int x3, int y3, int x4, int y4) 
	{
		int x=x1+(x1-x3);
		int y=y2+(y2-y4);
		
	}

	private void drawBezierCurve(int p1X, int p1Y, int p2X, int p2Y, int p3X, int p3Y, boolean useValuesAsIndex) {
		float xPrev = calculateBezierValues(0.0f, p1X, p2X, p3X,useValuesAsIndex);
		xPrev=normal(xPrev);
		
		float yPrev = calculateBezierValues(0.0f, p1Y, p2Y, p3Y,useValuesAsIndex);
		yPrev=normal(yPrev);

		float x,y;
		for(float t = 0.0f;t<=1.0f;t+=0.01f)
		{
			x = calculateBezierValues(t, p1X, p2X, p3X,useValuesAsIndex);
			x=normal(x);

			y = calculateBezierValues(t, p1Y, p2Y, p3Y,useValuesAsIndex);
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

	private float calculateBezierValues(float t,int p1,int p2,int p3, boolean useValuesAsIndex) {
		if(useValuesAsIndex)
		{
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
		else
		{
			return	((p1 - 2.0f * p2 + p3) * t * t)
					+
					((-2.0f * p1 + 2.0f * p2) * t)
					+
					p1;
		}
	}

	private float normal(float v) {
		return v*norm;
	}

}
