package ie.gwtsplitter.codeinspect.visitor;

import ie.gwtsplitter.codeinspect.Marker;
import ie.gwtsplitter.codeinspect.domain.GSClass;
import ie.gwtsplitter.codeinspect.domain.GSMethod;
import ie.gwtsplitter.codeinspect.domain.GSSplit;

import com.sun.source.tree.Tree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewClass;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class GWTSplitCodeVisitor extends CodeVisitorBase {
	
    public final Object scan(final Tree node, final Trees tree) {
    	
    	if (node != null) { 
    		// find a split and parse out split block
			if (node.toString().startsWith(
					"GWT.runAsync(new RunAsyncCallback(){")) {
				if (node instanceof JCMethodInvocation) {
					
					// create Marker
					Marker marker = setMarker(tree, compilationUnit, node);
					marker.setDisplayTag("GWT Split");
	    			addMarker(marker);
	    			long ln = compilationUnit.getLineMap().getLineNumber(
	    					marker.getStartPosition());
	    			marker.setStartLineNumber(ln);
					
	    			GSClass gsClass = getCurrentGSClass();
	    			
	    			// create split object
	    			GSSplit gsSplit = new GSSplit(marker);
	    			gsSplit.gsClass = gsClass;
	    			
	    			gsSplits.put(gsSplit.id, gsSplit);
					
					JCMethodInvocation invo = (JCMethodInvocation) node;
					if (invo.args.head instanceof JCNewClass) {
						JCNewClass t = (JCNewClass) invo.args.head;
						for (JCTree j : t.def.defs) {
							if (j instanceof JCMethodDecl) {
								JCMethodDecl decl = (JCMethodDecl) j;

								// ignore generated code.
								if (decl.toString().trim().startsWith("<")) {
									continue;
								}
								
								// look up method in current class
								String methodName = GSMethod.methodName(
										decl.sym.owner.flatName().toString(),
										decl.name.toString());
								GSMethod gsMethod = gsClass
										.getMethod(methodName);

								// add method to split listings
								gsSplit.addMethod(gsMethod);

								// add split to current class
								gsClass.addSplit(gsSplit);

								// mark method as inner split
								gsMethod.isInnnerSplitMethod = true;
							}
						}
					}
				}
			} // find.
    	}
    	return (node == null) ? null : node.accept(this, tree);
    }
    
}
