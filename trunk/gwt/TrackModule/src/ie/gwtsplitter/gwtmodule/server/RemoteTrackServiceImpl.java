package ie.gwtsplitter.gwtmodule.server;

import ie.gwtsplitter.gwtmodule.client.RemoteTrackService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RemoteTrackServiceImpl extends RemoteServiceServlet implements
    RemoteTrackService {
	
	/**  */
	private static final String DELIMINATOR = "\t";
	
	/**  */
	private static final String LINE_SEPARATOR 
		= System.getProperty("line.separator");
	
	/**  */
	private static final String FILE_NAME = "gsTrack.log";

	/**
	 * 
	 */
	public boolean recordTrack(String trackId) {
		String logMessage = 
	  		addLogValue(getTimeInMillis()) +
			addLogValue(getSessionId()) +
	  		addLogValue(trackId) +
	  		LINE_SEPARATOR
	  	;
	  		
	    try {
			BufferedWriter out = 
				new BufferedWriter(new FileWriter(FILE_NAME, true));
			out.write(logMessage);
			out.close();
	    } catch (IOException e) {
	    	return false;
	    }
	    return true;
	}
	
	
	private String addLogValue(Object value) {
		return String.valueOf(value) + DELIMINATOR;
	}
	
	
	private String getSessionId () {
		return this.getThreadLocalRequest().getSession().getId();
	}
	
	
	private static long getTimeInMillis() {
		Calendar now = Calendar.getInstance();
		return now.getTimeInMillis();
	}
  
}