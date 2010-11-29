package ie.gwtsplitter.reader;

import java.util.Comparator;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class DataRowClientComparator implements Comparator<DataRow> {

	public final int compare(final DataRow o1, final DataRow o2) {
		return o1.clientIdentifier.compareTo(o2.clientIdentifier);
	}

}
