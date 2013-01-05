package main.view;
import javax.swing.JLabel;


public class HtmlLabel extends JLabel {
	
	public HtmlLabel(String string) {
		setText(string);
	}

	private final String htmlFirst="<html><body><font size=2 face=verdana>",htmlLast="</font></body></html>";
	
	@Override
	public void setText(String string) {
		super.setText(htmlFirst+string+htmlLast);
	}

}
