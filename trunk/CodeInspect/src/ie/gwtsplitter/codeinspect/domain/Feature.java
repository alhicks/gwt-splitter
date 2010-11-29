package ie.gwtsplitter.codeinspect.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class Feature {

	private final String name;
	private List<GSMethod> methods;
	
	public Feature(final String name) {
		this.name = name;
		methods = new ArrayList<GSMethod>();
	}
	
	public final String getName() {
		return name;
	}
	
	public final List<GSMethod> getMethods() {
		return methods;
	}
	
	public final void addMethod(final GSMethod method) {
		methods.add(method);
	}
	
	public final int getSize() {
		int size = 0;
		for (GSMethod method : methods) {
			size += method.totalSize();
		}
		return size;
	}
	
	public final String toString() {
		StringBuilder s = new StringBuilder();
		s.append(this.name).append("\n[Methods:").append(methods.size())
				.append(", Size: " + getSize() + "]");
		s.append("\n\tDeclared Methods:\n");
		for (GSMethod method : getMethods()) {
			s.append("\t |-> ").append(method.toString()).append("\n");
		}
		return s.toString();
	}
	
	/**
	 * Finds the first calling method.
	 * @return reference to first calling method
	 */
	public final GSMethod rootMethod() {
		for (GSMethod m : methods) {
//			return m.rootMethod();
		}
		return null;
	}
	
	public static final String FEATURE_TAG = "@feature";
	
	public static String[] featureName(final String commentValue) {
		if (commentValue != null && commentValue.startsWith(FEATURE_TAG)) {
			String s = commentValue.replaceAll(FEATURE_TAG, "");
			return s.split(",");
		}
		return null;
	}

	public static Feature findIn(final Map<String, Feature> map,
			final String featureName) {
		if (map.containsKey(featureName)) {
				return map.get(featureName);
		}
		return null;
	}
	
}
