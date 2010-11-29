package ie.gwtsplitter.splits;

import java.util.List;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public interface SOYC {

	// (prog size, init size, list of soyc splits, leftovers)
	
	int programSize();
	int initSize();
	List<SoycSplit> splits();
	int leftovers(); //TODO:
 	
}
