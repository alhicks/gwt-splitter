package ie.gwtsplitter.codeinspect.domain;

import ie.gwtsplitter.codeinspect.Marker;


/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class DerivedSplit {
	
	public static final String SPLIT_HEADER = 
		"\n\t/** start: gwt-splitter defined */\n"
		+ "\tcom.google.gwt.core.client.GWT\n"
		+ "\t\t.runAsync(new com.google.gwt.core.client.RunAsyncCallback() {\n"
		+ "\t\t\tpublic void onSuccess() {\n";

	public static final String SPLIT_FOOTER = 
		"\t\t\t}\n"
		+ "\t\t\tpublic void onFailure(Throwable reason) {\n"
		+ "\t\t\t\t// TODO: Error Handling\n"
		+ "\t\t\t}\n"
		+ "\t});\n"
		+ "\t/** end: gwt-splitter defined */\n\n";
	
	public String name;
	public Marker marker;
}
