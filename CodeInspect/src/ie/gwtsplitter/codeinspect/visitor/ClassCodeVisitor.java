package ie.gwtsplitter.codeinspect.visitor;

import ie.gwtsplitter.codeinspect.domain.GSClass;
import ie.gwtsplitter.codeinspect.domain.GSPackage;

import com.sun.source.tree.Tree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCIdent;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class ClassCodeVisitor extends CodeVisitorBase {

	public final Object scan(final Tree node, final Trees tree) {
		if (node != null) {
			if (node instanceof JCClassDecl) {
				GSClass currentGSClass = null;
				GSPackage gsPackage = null;

				JCClassDecl classDecl = (JCClassDecl) node;
				String packageName = getPackageName();
				String className = classDecl.name.toString();
				String flatName = classDecl.sym.flatName().toString();

				if (className != null && className.length() > 1) {

					// check if have recorded the class already
					if (gsPackages.containsKey(packageName)) {
						gsPackage = gsPackages.get(getPackageName());
					} else {
						gsPackage = new GSPackage(packageName);
						gsPackages.put(packageName, gsPackage);
					}

					if (gsPackage.gsClasses.containsKey(className)) {
						currentGSClass = gsPackage.gsClasses.get(className);
					} else {
						currentGSClass = new GSClass(className, flatName);
						gsPackage.gsClasses.put(className, currentGSClass);
						gsClasses.put(flatName, currentGSClass);
					}

					// record extends
					if (classDecl.extending != null) {
						gsPackage = null;
						if (classDecl.extending instanceof JCIdent) {
							JCIdent idt = (JCIdent) classDecl.extending;
							String extPackageName = idt.sym.owner.toString();
							String extClassName = idt.name.toString();
							String extFlatName = idt.sym.flatName().toString();

							if (gsPackages.containsKey(extPackageName)) {
								gsPackage = gsPackages.get(extPackageName);
								if (gsPackage.gsClasses
										.containsKey(extClassName)) {
									currentGSClass.gsExtends = 
										gsPackage.gsClasses.get(extClassName);
								}
							} else {
								gsPackage = new GSPackage(extPackageName);
								gsPackages.put(extPackageName, gsPackage);
							}
							if (currentGSClass.gsExtends == null) {
								GSClass extendsGSClass = new GSClass(
										extClassName, extFlatName);
								gsPackage.gsClasses.put(extClassName,
										extendsGSClass);
								currentGSClass.gsExtends = extendsGSClass;
								gsClasses.put(extFlatName, extendsGSClass);
							}
						}
					}
				}
			}
		}
		return (node == null) ? null : node.accept(this, tree);
	}

}
