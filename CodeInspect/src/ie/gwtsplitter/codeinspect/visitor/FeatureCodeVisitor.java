package ie.gwtsplitter.codeinspect.visitor;

import ie.gwtsplitter.codeinspect.domain.Feature;
import ie.gwtsplitter.codeinspect.domain.GSClass;
import ie.gwtsplitter.codeinspect.domain.GSMethod;

import java.util.Map;
import java.util.Map.Entry;

import com.sun.source.tree.Tree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class FeatureCodeVisitor extends CodeVisitorBase {

	public final Object scan(final Tree node, final Trees tree) {
		if (node != null && node instanceof JCCompilationUnit) {
			GSClass gsClass = getCurrentGSClass();
			JCCompilationUnit unit = (JCCompilationUnit) node;
			Map<JCTree, String> comments = unit.docComments;
			
			for (Entry<JCTree, String> entry : comments.entrySet()) {
				String[] names = Feature.featureName(entry.getValue());
				if (names != null) {
					// one method can be used in multiple features
					for (String name : names) { 
						name = name.trim();
						Feature feature = Feature.findIn(features, name);
					
						if (feature == null) { // add to features
							feature = new Feature(name);
							features.put(name, feature);
						}
						
						JCTree nodeBlock = entry.getKey();
						if (nodeBlock instanceof JCMethodDecl) {
							// find method and add to Feature
							JCMethodDecl decl = (JCMethodDecl) nodeBlock;
							String prettyMethodName = decl.getName().toString();
							String methodName = GSMethod.methodName(
									decl.sym.owner.flatName().toString(),
									prettyMethodName);
							GSMethod method = gsClass.getMethod(methodName);
							feature.addMethod(method);
						}
					}
				}
			}
		}
		return (node == null) ? null : node.accept(this, tree);
	}

}