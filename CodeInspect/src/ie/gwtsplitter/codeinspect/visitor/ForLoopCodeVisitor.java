package ie.gwtsplitter.codeinspect.visitor;

import ie.gwtsplitter.codeinspect.Marker;

import com.sun.source.tree.Tree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree.JCEnhancedForLoop;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class ForLoopCodeVisitor extends CodeVisitorBase {

	public final Object scan(final Tree node, final Trees tree) {
		if (node != null) {
			if (node instanceof JCEnhancedForLoop) {
				Marker marker = setMarker(tree, compilationUnit, node);
				addMarker(marker);

				long ln = compilationUnit.getLineMap().getLineNumber(
						marker.getStartPosition());
				marker.setStartLineNumber(ln);
			}
		}
		return (node == null) ? null : node.accept(this, tree);
	}

}
