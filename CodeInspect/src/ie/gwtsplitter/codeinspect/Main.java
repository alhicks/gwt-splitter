package ie.gwtsplitter.codeinspect;

import ie.gwtsplitter.codeinspect.domain.CodeInspectResult;
import ie.gwtsplitter.codeinspect.domain.DerivedSplit;
import ie.gwtsplitter.codeinspect.domain.GSSplit;
import ie.gwtsplitter.codeinspect.visitor.CodeVisitorBase;
import ie.gwtsplitter.codeinspect.visitor.FeatureCodeVisitor;
import ie.gwtsplitter.codeinspect.visitor.GWTSplitCodeVisitor;
import ie.gwtsplitter.codeinspect.visitor.TrackCodeVisitor;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class Main {
	
	private static final String PROJECT_BASE = 
		"/Users/alanh/local/gwt-splitter-svn/trunk/test-apps/ShowcaseGWT_BasicSplit";
//		"/Users/alanh/local/gwt-splitter-svn/trunk/test-apps/ShowcaseGWT_GoogleTeamSplit";
//		"/Users/alanh/local/workspace/GWTSplitterWebApp";
//		"/Users/alanh/local/gwt-splitter-svn/trunk/test-apps/GWTBaseMailRoomApp";
	
	private static final String PROJECT_SRC = 
		PROJECT_BASE + "/src";
	//	"src";
	// for current project use "src"

	private static final String PROJECT_USAGE_LOG = 
		PROJECT_BASE + "/war/gsTrack.log";
	
	/**
	 * Main class to test analyzeCode method.
	 * 
	 * @param args
	 *            parameters
	 */
	public static void main(final String[] args) {

		// Add visitors
		List<CodeVisitorBase> visitors = new LinkedList<CodeVisitorBase>();

		GWTSplitCodeVisitor gwtSplitVisitor = new GWTSplitCodeVisitor();
		visitors.add(gwtSplitVisitor);
		
		FeatureCodeVisitor featureVisitor = new FeatureCodeVisitor();
		visitors.add(featureVisitor);
		
		TrackCodeVisitor trackVisitor = new TrackCodeVisitor();
		visitors.add(trackVisitor);
		
		// Analyse code
		// - Static Analysis
		// - Feature Meta data
		CodeInspectResult cir = 
			CodeInspector.analyzeCode(PROJECT_SRC, visitors);
		/*
		List<DerivedSplit> basicSplits = cir.staticAnalysisBasicSplits();
		
		Collections.sort(basicSplits, new java.util.Comparator<DerivedSplit>() {
			public int compare(final DerivedSplit a, final DerivedSplit b) {
				return b.marker.getStartLineNumber()
						- a.marker.getStartLineNumber();
			}
		});
		
		Map<String, DerivedSplit> sp = new HashMap<String, DerivedSplit>();
		for (DerivedSplit s : basicSplits) {
			sp.put(s.name, s);
		}
		
		CodeInspectResult.displayDerivedSplits(sp.values());
		*/
		
		System.out.println(gwtSplitVisitor.getCount());
		Map<Integer, GSSplit> splits = CodeInspector.gsSplits;
		for (GSSplit split : splits.values()) {
			System.out.println(split.gsClass.name + "\t" + split.marker.getStartLineNumber());
		}
		
		/*
		
		// User Usage
		DataCollecter dc = Reader.getDataCollector(PROJECT_USAGE_LOG);
		
		// Associate GSMethods and usage data
		for (Entry<String, GSClass> c : cir.gsClasses.entrySet()) {
			List<GSMethod> methods = c.getValue().getMethods();
			for (GSMethod method : methods) {
				String tag = method.getTrackTag();
				if (tag != null) {
					// look up PathElement
					for (String p : dc.getPaths().keySet()) {
						PathElement e = dc.getPaths().get(p);
						// recursive loop to find all methods in usage paths
						while (e != null) {
							if (e != null && e.getName().equals(tag)) {
								e.setMethod(method);
							}
							e = e.getNext();
						}
					}
				}
			}
		}
		
		//System.out.println(dc.toString());
		
		//CodeInspectUtil.graph();
		//CodeInspectUtil.graphResults(cir);
		
		// Display results
		for (String key : cir.gsPackages.keySet()) {
			//if (key.indexOf("ie.gwtsplitter.webapp.client.subpackage") != -1) {
				GSPackage gsPackage = cir.gsPackages.get(key);
				for (String classKey : gsPackage.gsClasses.keySet()) {
					GSClass gsClass = gsPackage.gsClasses.get(classKey);
					System.out.println(gsClass.toString());
				}
			//}
		}
		
		System.out.println("Features");
		for (Entry<String, Feature> f : cir.features.entrySet()) {
			System.out.println(f.getValue().toString());
		}		
		*/
	}

}
