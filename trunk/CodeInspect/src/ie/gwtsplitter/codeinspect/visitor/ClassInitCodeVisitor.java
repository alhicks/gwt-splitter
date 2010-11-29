package ie.gwtsplitter.codeinspect.visitor;

import ie.gwtsplitter.codeinspect.domain.GSClass;
import ie.gwtsplitter.codeinspect.domain.GSMethod;

import com.sun.source.tree.Tree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCNewClass;

/**
 * Find a record creation of classes. We need to record the use of constructors
 * so that dead-code analysis can be done.
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class ClassInitCodeVisitor extends CodeVisitorBase {

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
			
			if (node instanceof JCNewClass) {
				JCNewClass c = (JCNewClass) node;
				
				// Get method class fullname
				String className = c.constructor.owner.flatName().toString();
				
				// Get reference to class
				GSClass methodClass = gsClasses.get(className);
				
				// Set invokedMethodName
				String invokedMethodName = c.constructor.name.toString();
				
				// Add method to calls and inner methods
				if (methodClass != null) {
					String methodName = GSMethod.methodName(
							methodClass.name, invokedMethodName);
					GSMethod method = methodClass.getMethod(methodName);
					if (method != null) {
						method.calls.add(currentMethod);
						if (currentMethod != null) {
							currentMethod.innerMethodCalls.add(method);
						}
					}
				}
				
			}
		}
		return (node == null) ? null : node.accept(this, tree);
	}

}
