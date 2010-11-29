package ie.gwtsplitter.codeinspect.visitor;

import ie.gwtsplitter.codeinspect.domain.GSClass;
import ie.gwtsplitter.codeinspect.domain.GSMethod;

import com.sun.source.tree.Tree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class MethodInvocationCodeVisitor extends CodeVisitorBase {
	
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
			
			// Capture a method call
			if (node instanceof JCMethodInvocation) {
				
				JCMethodInvocation invo = (JCMethodInvocation) node;
				
				if (invo.meth != null) {
					String invokedMethodName = "";
					GSClass methodClass = null;
					
					// Capture instantiated methods
					if (invo.meth instanceof JCFieldAccess) {
						JCFieldAccess fa = (JCFieldAccess) invo.meth;
						
						// Get method class fullname
						String className = fa.sym.owner.flatName().toString();
						
						// Get reference to class
						methodClass = gsClasses.get(className);
						
						// Set invokedMethodName
						invokedMethodName = fa.name.toString();
						
					// Capture static methods 
					} else if (invo.meth instanceof JCIdent) {
						JCIdent idt = (JCIdent) invo.meth;
						
						// Get method class fullname
						String className = idt.sym.owner.flatName().toString();
						
						// Get reference to class
						methodClass = gsClasses.get(className);
						
						// Set invokedMethodName
						invokedMethodName = idt.name.toString();
						
					}
					
					// Add method to calls and inner methods
					if (methodClass != null) {
						String methodName = GSMethod.methodName(
								methodClass.name, invokedMethodName);
						GSMethod method = methodClass.getMethod(methodName);
						if (method != null) {
							method.calls.add(currentMethod);
							method.callMarkers.put(methodName, 
									setMarker(tree, compilationUnit, node));
							if (currentMethod != null) {
								currentMethod.innerMethodCalls.add(method);
							}
						}
					}
					
				}
			}
		}
		return (node == null) ? null : node.accept(this, tree);
	}

}
