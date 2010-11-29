package ie.gwtsplitter.codeinspect.visitor;

import ie.gwtsplitter.codeinspect.CodeInspector;
import ie.gwtsplitter.codeinspect.Marker;
import ie.gwtsplitter.codeinspect.domain.Feature;
import ie.gwtsplitter.codeinspect.domain.GSClass;
import ie.gwtsplitter.codeinspect.domain.GSPackage;
import ie.gwtsplitter.codeinspect.domain.GSSplit;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.Tree;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class CodeVisitorBase extends TreePathScanner<Object, Trees> implements
		CodeVisitorInterface {

	public CompilationUnitTree compilationUnit = null;
	
	// Some convenience reference variables
	static Map<Integer, GSSplit> gsSplits = CodeInspector.gsSplits;
	static Map<String, GSPackage> gsPackages = CodeInspector.gsPackages;
	static Map<String, Feature> features = CodeInspector.features;	
	static Map<String, GSClass> gsClasses = CodeInspector.gsClasses;
	
	
	static void print(final String s) {
		System.out.println(s);
	}

	public final int getCount() {
		return markers.size();
	}

	List<Marker> markers = new LinkedList<Marker>();

	protected final void addMarker(final Marker marker) {
		markers.add(marker);
	}

	public final List<Marker> getMarkers() {
		return markers;
	}

	static Marker setMarker(final Trees trees, final CompilationUnitTree ast,
			final Tree tree) {
		Marker marker = new Marker();
		marker.setFileName(ast.getSourceFile().toString());
		marker.setNode(tree);
		
		SourcePositions sourcePosition = trees.getSourcePositions();
		long startPosition = sourcePosition.getStartPosition(ast, tree);
		long endPosition = sourcePosition.getEndPosition(ast, tree);
		marker.setStartOffset((int) startPosition);
		marker.setEndOffset((int) endPosition);
		
		LineMap lineMap = ast.getLineMap();
		long startLine = lineMap.getLineNumber(startPosition);
		long endLine = lineMap.getLineNumber(endPosition);
		marker.setStartLineNumber((int) startLine);
		marker.setEndLineNumber((int) endLine);
		return marker;
	}

	public final GSClass getCurrentGSClass() {
		String packageName = compilationUnit.getPackageName().toString();
		String className = compilationUnit.getSourceFile().getName()
				.replaceAll(".java", "");
		GSClass gsClass = gsPackages.get(packageName)
				.getGSClass(className);
		return gsClass;
	}

	public final String getPackageName() {
		return compilationUnit.getPackageName().toString();
	}

	public static GSClass getGSClass(final String packageName,
			final String className) {
		if (gsPackages.containsKey(packageName)) {
			return gsPackages.get(packageName).getGSClass(className);	
		}
		return null;
	}

}
