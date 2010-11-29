package ie.gwtsplitter.splits;

import ie.gwtsplitter.reader.PathElement;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public interface ClickPair {

	PathElement left();
	PathElement right();
	int count();
	
}
