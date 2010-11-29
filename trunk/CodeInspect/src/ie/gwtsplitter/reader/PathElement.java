package ie.gwtsplitter.reader;

import ie.gwtsplitter.codeinspect.domain.GSMethod;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class PathElement {

	private String name;
	private GSMethod method;
	
	private PathElement next;

	public PathElement(final String name) {
		this.name = name;
	}

	public final String getName() {
		return this.name;
	}

	public final PathElement getNext() {
		return this.next;
	}

	public final String getPattern() {
		if (this.next != null) {
			return this.name + "->" + this.next.name;
		} else {
			return this.name + "->";
		}
	}
	
	public final void setMethod(final GSMethod method) {
		this.method = method;
	}
	
	public final GSMethod getMethod() {
		return this.method;
	}
	
	public final String toString() {
		String text = "[" + this.name + "]:";
		if (this.method != null) {
			text += "[" + this.method.name + "]";
		}
		if (this.next != null) {
			text += " \n\t-> " + this.next.toString();
		}
		return text;
	}

	public final PathElement setNext(final PathElement next) {
		this.next = next;
		return this.next;
	}

}
