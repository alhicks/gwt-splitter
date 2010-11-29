package ie.gwtsplitter.reader;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class DataRow {

	long time;
	String clientIdentifier;
	String pathElement;

	public DataRow(final String row) {
		String[] values = row.split("\t");
		time = Long.parseLong(values[0]);
		clientIdentifier = values[1];
		pathElement = values[2];
	}

	public final String toString() {
		return "[DataRow]\t" + time + "\t" + clientIdentifier + "\t"
				+ pathElement;
	}

}