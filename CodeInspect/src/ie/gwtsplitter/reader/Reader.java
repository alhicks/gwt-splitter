package ie.gwtsplitter.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class Reader {

	public static final String LINE_SEPARATOR =
		System.getProperty("line.separator");
	
	private static final File GWT_LOG =
		new File("/Users/alanh/local/workspace/" +
				"GWTSplitterWebApp/war/gsTrack.log"); 
	
	public static DataCollecter getDataCollector(final String logFile) {
		try {
			return new DataCollecter(readFile(logFile));
		} catch (Exception e) {
			return null;
		}
	}
	
	private static String readFile(final String logFile) {
		StringBuilder data = new StringBuilder();
		try {
	        BufferedReader in =
	        	new BufferedReader(new FileReader(logFile));
	        String str = "";
	        while ((str = in.readLine()) != null) {
	            data.append(str).append(LINE_SEPARATOR);
	        }
	        in.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	System.out.println("\n\n");
	    }
	    return data.toString();
	}	
	
	private static long lm = 0;  
	
	public static void main(final String[] args) {
		
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (lm != GWT_LOG.lastModified()) {
					lm = GWT_LOG.lastModified();
					//
					System.out.println("\n\n\n\n");
					DataCollecter dc = getDataCollector(GWT_LOG.toString());
					System.out.println(dc.toString());
				}
			}
		};
		
		timer.schedule(task, 0, 1000);
		
		// Keep program running
		while (true) {
			boolean b = false;
		}
		
	}	
	
}
