package ie.gwtsplitter.codeinspect.visitor;

import ie.gwtsplitter.codeinspect.domain.GSClass;
import ie.gwtsplitter.codeinspect.domain.GSMethod;

import com.sun.source.tree.Tree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class TrackCodeVisitor extends CodeVisitorBase {

	GSMethod currentMethod = null;
	
	public final Object scan(final Tree node, final Trees tree) {
		if (node != null) {
			
			// Get a reference to the method we are in
			if (node instanceof JCMethodDecl) {
				JCMethodDecl decl = (JCMethodDecl) node;
				
				// Get reference to current class
				GSClass currentClass = getCurrentGSClass();
				
				// Get method pretty name
				String prettyMethodName = decl.getName().toString();
				String methodName = GSMethod.methodName(
						decl.sym.owner.flatName().toString(),
						prettyMethodName);
				currentMethod = currentClass.getMethod(methodName);
			}
			
			// Associate track tag to method
			if (node instanceof JCExpressionStatement) {
				JCExpressionStatement exp = (JCExpressionStatement) node;
				if (exp.toString().indexOf(".track(\"") != -1) {
					JCMethodInvocation invo = (JCMethodInvocation) exp.expr;
					JCLiteral lit = (JCLiteral) invo.args.head;
					currentMethod.setTrackTag(lit.value.toString());
				}
			}
		}
		return (node == null) ? null : node.accept(this, tree);
	}

}
