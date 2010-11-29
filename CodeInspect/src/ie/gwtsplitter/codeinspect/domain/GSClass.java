package ie.gwtsplitter.codeinspect.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class GSClass {

	public final String name;
	public final String prettyName;
	private List<GSMethod> methods;
	private List<GSSplit> splits;
	public GSClass gsExtends = null; 
	
	public GSClass(final String prettyName, final String name) {
		this.prettyName = prettyName;
		this.name = name;
		methods = new LinkedList<GSMethod>();
		splits = new LinkedList<GSSplit>();
	}
	

	public final String toString() {
		StringBuilder s = new StringBuilder();
		s.append(this.name).append("\nClass Size:").append(this.getSize())
				.append("\tMethods:").append(methods.size());
		s.append("\n\n\tDeclared Methods:\n");
		for (GSMethod method : getMethods()) {
			s.append("\t |-> ").append(method.toString()).append("\n");
		}
		s.append("\n\tDeclared Code Splits:\n");
		for (GSSplit split : getSplits()) {
			s.append("\t" + split.toString()).append("\n");
		}
		return s.toString();
	}

	public final int getSize() {
		int size = 0;
		for (GSMethod method : methods) {
			size += method.totalSize();
		}
		return size;
	}

	public final boolean findUsages(final GSMethod currentMethod) {
		for (GSMethod method : methods) {
			if (method.name.equals(currentMethod.name)) {
				return true;
			}
		}
		return false;
	}

	public final void addMethod(final GSMethod method) {
		methods.add(method);
	}

	public final List<GSMethod> getMethods() {
		List<GSMethod> tMethods = methods;
		if (gsExtends != null) {
			for (GSMethod m : gsExtends.getMethods()) {
				boolean add = true;
				for (GSMethod t : methods) {
					if (t.name.equals(m.name)) {
						add = false;
						break;
					}
				}
				if (add) {
					tMethods.add(m);
				}
			}
		}
		return tMethods;
	}

	public final GSMethod getMethod(final String methodName) {
		for (GSMethod method : methods) {
			if (method.name.equals(methodName)) {
				return method;
			}
		}
		if (gsExtends != null) {
			return gsExtends.getMethod(methodName);
		}
		return null;
	}

	public final void addSplit(final GSSplit split) {
		if (!splits.contains(split)) {
			splits.add(split);
		}
	}	
	
	public final List<GSSplit> getSplits() {
		return splits;
	}
	
}
