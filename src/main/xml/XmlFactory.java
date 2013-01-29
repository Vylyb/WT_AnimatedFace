package main.xml;

public class XmlFactory {

	public static String intToTag(int value, String tag) {
		return stringToTag(""+value,tag);
	}

	public static String stringToTag(String string, String tag) {
		return "<"+tag+">\n\t"+string+"\n</"+tag+">\n";
	}

	public static String stringToComment(String string) {
		return "<!--\n\t"+string+"\n-->\n";
	}

	public static String startTag(String tag) {
		return "<"+tag+">\n";
	}

	public static String closeTag(String tag) {
		return "</"+tag+">\n";
	}
	
	public static String getCloseTag(String tag){
		return "/"+tag;
	}
}
