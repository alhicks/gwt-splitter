package ie.gwtsplitter.codeinspect;

import com.sun.source.tree.Tree;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class Marker {

	private Tree node;
	private String fileName;
	private int startPostition;
	private int endPosition;
	private long startLineNumber;
	private long endLineNumber;
	private String displayTag;

	public final void setDisplayTag(final String displayTag) {
		this.displayTag = displayTag;
	}

	public final String getDisplayTag() {
		return this.displayTag;
	}

	public final Tree getNode() {
		return node;
	}

	public final void setNode(final Tree node) {
		this.node = node;
	}

	public final String getFileName() {
		return fileName;
	}

	public final void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	public final void setStartOffset(final int startPosition) {
		this.startPostition = startPosition;
	}

	public final void setEndOffset(final int endPosition) {
		this.endPosition = endPosition;
	}

	public final int getStartPosition() {
		return this.startPostition;
	}

	public final int getEndPosition() {
		return this.endPosition;
	}

	public final void setStartLineNumber(final long startLineNumber) {
		this.startLineNumber = startLineNumber;
	}

	public final void setEndLineNumber(final long endLineNumber) {
		this.endLineNumber = endLineNumber;
	}	
	
	public final int getStartLineNumber() {
		// return int. IMarker.LINE_NUMBER crashes using 'long'
		return (int) this.startLineNumber;
	}

	public final int getEndLineNumber() {
		// return int. IMarker.LINE_NUMBER crashes using 'long'
		return (int) this.endLineNumber;
	}	
	
	public final String toString() {
		return this.fileName + "\n\t" + this.node.getKind().name() + "\n\t"
				+ this.startLineNumber;
	}

}
