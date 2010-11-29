package ie.gwtsplitter.codeinspect.domain;

import ie.gwtsplitter.codeinspect.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class GSMethod {
	
	public final String name;
	public final String prettyName;
	public final boolean codeGenerated;
	public String accessor;
	private int size;
	public final GSClass gsClass;
	public boolean isInnnerSplitMethod = false;
	private String trackTag;
	private Marker marker;
	
	/**
	 * Used to record Marker locations of where this method is called.
	 */
	public Map<String, Marker> callMarkers;
	
	/**
	 * Used to record places in where this method is called.
	 */
	public List<GSMethod> calls;
	
	/**
	 * Used to record methods that are called within this method.
	 */
	public List<GSMethod> innerMethodCalls;
	
	
	public GSMethod(final GSClass gsClass, final String name,
			final String prettyName) {
		this.gsClass = gsClass;
		this.name = name;
		this.prettyName = prettyName;
		this.codeGenerated = prettyName.startsWith("<");
		this.calls = new ArrayList<GSMethod>();
		this.innerMethodCalls = new ArrayList<GSMethod>();
		this.callMarkers = new HashMap<String, Marker>();
	}

	public final String toString() {
		StringBuilder s = new StringBuilder();
		s.append("DFN:" + (this.isDeadCode() ? "Y" : "N") + "\n")
		 .append("ISM:" + isInnnerSplitMethod + "\n")
		 .append("Size: " + this.totalSize() + " (").append(this.size + ")\n")
		 .append(this.name).append("\tTrackTag:" + this.trackTag);
		boolean addSpace = false;
		for (GSMethod c : this.calls) {
			s.append("\n\t\t| called by |=> ").append(c.name);
			addSpace = true;
		}
		for (GSMethod method : this.innerMethodCalls) {
			s.append("\n\t\t| inner call |=> ").append(method.name);
			addSpace = true;
		}
		
		if (addSpace) {
			s.append("\n");
		}
		return s.toString();
	}
	
	public final void setTrackTag(final String trackTag) {
		this.trackTag = trackTag;
	}
	
	public final String getTrackTag() {
		return this.trackTag;
	}
	
	public final void setMarker(final Marker marker) {
		this.marker = marker;
	}
	
	public final Marker getMarker() {
		return this.marker;
	}
	
	/**
	 * Dead code is code that is not called and hence gets
	 * removed by the GWT compiler. If method is onModuleLoad return false.
	 * This method is the GWT entry point.
	 * @return boolean
	 */
	public final boolean isDeadCode() {
		
		return false;
		/*
		if (this.prettyName.equals("onModuleLoad")) {
			return false;
		}
		
		if (calls == null || calls.isEmpty()) {
			return true;
		} else {
			// check calling methods for deadness
			for (GSMethod m : calls) {
				if (!m.isDeadCode()) {
					return false;
				}
			}
			return true;
		}
		*/
	}
	
	
	/**
	 * Set the raw size of the method block.
	 * @param size method body size
	 */
	public final void setSize(final int size) {
		this.size = size;
	}
	
	
	/**
	 * Calculates the total size of the method including the inner method
	 * call sizes.
	 * @return total size of method
	 */
	public final int totalSize() {
		int size = this.size;
		for (GSMethod method : innerMethodCalls) {
			size += method.totalSize();
		}
		return size;
	}
	
	
	/**
	 * Construct the full method name.
	 * @param uniqueName flatname of method class
	 * @param prettyName human readable method name
	 * @return full unique method name, example: uniqueName_prettyName 
	 */
	public static String methodName(final String uniqueName,
			final String prettyName) {
		return uniqueName + "_" + prettyName;
	}


}
