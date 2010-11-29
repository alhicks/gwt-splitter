package ie.gwtsplitter.reader;

import java.util.Comparator;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class DataRowTimeComparator implements Comparator<DataRow> {

	public final int compare(final DataRow o1, final DataRow o2) {
		Long oi1 = Long.valueOf(o1.time);
		Long oi2 = Long.valueOf(o2.time);
		return oi1.compareTo(oi2);
	}

}
