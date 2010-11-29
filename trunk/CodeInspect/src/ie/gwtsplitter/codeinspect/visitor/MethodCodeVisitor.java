package ie.gwtsplitter.codeinspect.visitor;

import ie.gwtsplitter.codeinspect.Marker;
import ie.gwtsplitter.codeinspect.domain.GSClass;
import ie.gwtsplitter.codeinspect.domain.GSMethod;

import com.sun.source.tree.Tree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class MethodCodeVisitor extends CodeVisitorBase implements
		CodeVisitorInterface {

	public final Object scan(final Tree node, final Trees tree) {
		if (node != null) {
			if (node instanceof JCMethodDecl) {
				JCMethodDecl decl = (JCMethodDecl) node;
				GSClass gsClass = getCurrentGSClass();
				GSMethod method = null;
				if (gsClass != null) {
					
					String prettyMethodName = decl.getName().toString();
					String name = GSMethod.methodName(decl.sym.owner.flatName()
							.toString(), prettyMethodName);
					method = new GSMethod(gsClass, name,
							prettyMethodName);
					method.accessor = decl.mods.toString();
					
					// Guess method size based on length of method with
					// white space removed.
					JCBlock body = decl.getBody();
					if (body != null) {
						String sizeBody = body.toString()
								.replaceAll("\\s+", "");
						method.setSize(sizeBody.length());
						// System.out.println(sizeBody +
						// "\n*"+(sizeBody.length())+"*");
					}
					gsClass.addMethod(method);
				}
				Marker marker = setMarker(tree, compilationUnit, node);
				addMarker(marker);
				
				// record marker in GSMethod
				if (method != null) {
					method.setMarker(marker);
				}
			}
		}
		return (node == null) ? null : node.accept(this, tree);
	}

}
