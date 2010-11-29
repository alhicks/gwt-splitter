package ie.gwtsplitter.codeinspect.domain;

import ie.gwtsplitter.codeinspect.Marker;
import ie.gwtsplitter.codeinspect.util.Utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class CodeInspectResult {

	public Map<Integer, GSSplit> gsSplits;
	public Map<String, GSPackage> gsPackages;	
	public Map<String, Feature> features;
	public Map<String, GSClass> gsClasses;
	
	
	
	/**
	 * BASIC.
	 * ============
	 * Split = Method != DeadCode && Method Only Has 1 Caller
	 * @return List of calculated splits.
	 */
	public final List<DerivedSplit> staticAnalysisBasicSplits() {     
		
		List<String> print = new ArrayList<String>();
		print.add("ie.gwtsplitter.webapp.client.subpackage.Dummy");
		print.add("ie.gwtsplitter.webapp.client.GWTSplitterWebApp");
		
		List<DerivedSplit> splits = new ArrayList<DerivedSplit>();
		
		for (Entry<String, GSClass> entry : gsClasses.entrySet()) {
			if (true || print.contains(entry.getKey())) {
				GSClass c = entry.getValue();
				//System.out.println("\n\n\n" + c.name);
				for (GSMethod m : c.getMethods()) {
					if (!m.isDeadCode()) {
						//System.out.println(m.toString());
						if (m.calls.size() == 1 && !m.codeGenerated && m.totalSize()>0) {
							GSMethod caller = m.calls.get(0);
							DerivedSplit split = new DerivedSplit();
							Marker marker = m.callMarkers.get(m.name);
							split.name = " ([" + m.totalSize() + "]\tline: " + marker.getStartLineNumber() + "\t" + m.name + ")\t" + caller.name;
							split.marker = marker;
							splits.add(split);
							//System.out.println("SPLIT:(line:" + caller.getMarker() + ")" + caller.name + "\n");
						}
						
					}
				}
				
			}
		}
		return splits;
	}
	
	
	
	/* static methods */
	public static void applyDerivedSplits(
			final Collection<DerivedSplit> splits) {
		List<DerivedSplit> sp = new ArrayList<DerivedSplit>(splits);

		Map<String, List<Integer>> s = new HashMap<String, List<Integer>>();
		
		for (DerivedSplit split : sp) {
			String filename = split.marker.getFileName();
			if (!s.containsKey(filename)) {
				List<Integer> ints = new ArrayList<Integer>();
				ints.add(split.marker.getStartLineNumber());
				s.put(filename, ints);
			} else {
				List<Integer> ints = s.get(filename);
				ints.add(split.marker.getStartLineNumber());
				s.put(filename, ints);
			}
		}
		
		for (Entry<String, List<Integer>> entry : s.entrySet()) {
			boolean rc = Utility.splitFile(entry.getKey(), entry.getValue());
			System.out.println(rc + "\t" + entry.getKey());
		}
		
		System.out.println("Number of Splits: " + sp.size());
	}
	
	public static void displayDerivedSplits(
			final Collection<DerivedSplit> splits) {
		for (DerivedSplit split : splits) {
			System.out.println("SPLIT:" + split.name);
		}
		System.out.println("Number of Splits: " + splits.size());
	}	
	
	
}
