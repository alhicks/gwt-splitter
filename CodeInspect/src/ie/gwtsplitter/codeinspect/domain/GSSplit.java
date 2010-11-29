package ie.gwtsplitter.codeinspect.domain;

import java.util.LinkedList;
import java.util.List;

import ie.gwtsplitter.codeinspect.Marker;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class GSSplit {

	public final Integer id;
	public Marker marker;
	private List<GSMethod> methods;
	public GSClass gsClass;

	public GSSplit(final Marker marker) {
		this.id = getUniqueId();
		this.marker = marker;
		this.methods = new LinkedList<GSMethod>();
	}

	public GSSplit(final Integer id, final Marker marker) {
		this.id = id;
		this.marker = marker;
		this.methods = new LinkedList<GSMethod>();
	}

	public final String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Split Id: " + id + "\tSize: " + getSize());
		for (GSMethod method : methods) {
			s.append("\n\t\t |> ").append(method.toString());
		}
		return s.toString();
		
		
	}

	public final void addMethod(final GSMethod method) {
		this.methods.add(method);
	}

	public final int getSize() {
		int size = 0;
		for (GSMethod method : methods) {
			size += method.totalSize();
		}
		return size;
	}
	
	
	private static Integer seed = Integer.valueOf(0);
	
	/**
	 * Generate unique id for split.
	 * @return id of split
	 */
	public static Integer getUniqueId() {
		seed++;
		return seed;
	}

}
