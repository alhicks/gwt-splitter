package ie.gwtsplitter.reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class DataCollecter {

	private List<DataRow> data;

	private Map<String, PathElement> paths;
	private Map<String, Integer> patternCounts;
	private List<String> patterns;

	public DataCollecter(final String rawData) {
		data = new ArrayList<DataRow>();
		paths = new HashMap<String, PathElement>();
		patternCounts = new HashMap<String, Integer>();
		patterns = new ArrayList<String>();

		convertToDataRows(rawData);
		sortData();
	}

	/**
	 * Converts raw log file data into DataRow objects.
	 * 
	 * @param rawData
	 *            data from log file as String
	 */
	private void convertToDataRows(final String rawData) {
		String[] rows = rawData.split(Reader.LINE_SEPARATOR);
		for (String row : rows) {
			DataRow dr = new DataRow(row);
			data.add(dr);
		}
	}

	private void sortData() {

		Map<String, List<DataRow>> client =
			new HashMap<String, List<DataRow>>();

		// Break data into client identifier chucks
		for (DataRow dr : data) {
			if (client.containsKey(dr.clientIdentifier)) {
				List<DataRow> clientList = client.get(dr.clientIdentifier);
				clientList.add(dr);
			} else {
				List<DataRow> clientList = new ArrayList<DataRow>();
				clientList.add(dr);
				client.put(dr.clientIdentifier, clientList);
			}
		}

		// For each client collection sort them by time so we can see
		// the order of the client requests. Then add them to a path tree.
		for (Entry<String, List<DataRow>> entry : client.entrySet()) {
			List<DataRow> list = entry.getValue();
			Collections.sort(list, new DataRowTimeComparator());
			PathElement root = null;
			PathElement p = null;
			for (DataRow dr : list) {
				if (p == null) {
					// if its the 1st element create the root
					if (root == null) {
						root = new PathElement(dr.pathElement);
					} else {
						p = root.setNext(new PathElement(dr.pathElement));
					}
				} else {
					p = p.setNext(new PathElement(dr.pathElement));
				}
			}
			p = null;
			paths.put(entry.getKey(), root);
		}

		// now we want to search for all unique patterns in the paths.
		for (String key : paths.keySet()) {
			PathElement current = paths.get(key);
			while (current.getNext() != null) {
				if (!patterns.contains(current.getPattern())) {
					patterns.add(current.getPattern());
				}
				current = current.getNext(); // move to next element
			}
			//this loop will not capture the last element, thats OK as we the 
			//last element is the end of the user journey so we are not
			//concerned with it.
		}

		// now lets count the unique patterns
		for (String pattern : patterns) {
			int count = 0;
			for (String key : paths.keySet()) {
				PathElement current = paths.get(key);
				while (current.getNext() != null) {
					if (pattern.equals(current.getPattern())) {
						count++;
					}
					current = current.getNext(); // move to next element
				}
			}
			patternCounts.put(pattern, Integer.valueOf(count));
		}
	}

	public final Map<String, PathElement> getPaths() {
		return this.paths;
	}

	public final Map<String, Integer> getPatternCounts() {
		return this.patternCounts;
	}

	public final List<String> getPatterns() {
		return this.patterns;
	}
	
	public final String toString() {
		String text = "";
		Map<String, PathElement> paths = this.getPaths();
		for (Entry<String, PathElement> entry : paths.entrySet()) {
			final String key = entry.getKey();
			text += key + "\n\t" + entry.getValue();
		}		
		return text;
	}
	
	
}
