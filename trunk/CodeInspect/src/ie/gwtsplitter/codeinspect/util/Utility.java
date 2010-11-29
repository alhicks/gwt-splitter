package ie.gwtsplitter.codeinspect.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class Utility {

	private static final String SPLIT_HEADER = 
		"\n\n/** start: gwt-splitter defined */" 
		+ "\nGWT.runAsync(new RunAsyncCallback() {" 
		+ "\n\tpublic void onSuccess() {";

	private static final String SPLIT_FOOTER = 
		"\n\t}"
		+ "\n\tpublic void onFailure(Throwable reason) {"
		+ "\n\t\t// TODO: Error Handling"
		+ "\n\t}"
		+ "\n});"
		+ "\n/** end: gwt-splitter defined */\n\n"; 
	
	/**
	 * 
	 * @param filename
	 * @param linenumbers
	 * @return
	 */
	public static boolean splitFile(String filename, List<Integer> linenumbers) {
		List<String> file = new ArrayList<String>();
		
		String fileCopy = "";
		
		try {
	    	// Open the file that is the first 
	        // command line parameter
	    	FileInputStream fstream = new FileInputStream(filename);
	        // Get the object of DataInputStream
	        DataInputStream in = new DataInputStream(fstream);
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        String strLine;
	        //Read File Line By Line
	        while ((strLine = br.readLine()) != null)   {
	        	// Print the content on the console
	        	file.add(strLine);
	        	fileCopy += strLine + "\n";
	        }
	        //Close the input stream
	        in.close();
        } catch (Exception e) {
        	System.err.println("Error: " + e.getMessage());
        }
        
    	Collections.sort(linenumbers, Collections.reverseOrder());
		for (Integer linenumber : linenumbers) {
			if (linenumber <= file.size()) {
				file.add(linenumber, SPLIT_FOOTER);
				file.add(linenumber - 1, SPLIT_HEADER);
			}
		}  
        
    	String contents = "";
    	int i = 0;
        for (String line : file) {
        	i++;
        	contents += line + "\n";
        	//System.out.println(i + "\t" + line);
        }
        
        
        writeFile(filename + ".pre-split", fileCopy);
        
        writeFile(filename, contents);
        
        //System.out.println(contents);
        
        return true;
	}
	
	/**
	 * 
	 * @param filename
	 * @param contents
	 * @return boolean
	 */
	private static boolean writeFile(String filename, String contents) {
        try {
			FileWriter writer = new FileWriter(new File(filename));
			writer.write(contents);
			writer.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
}

