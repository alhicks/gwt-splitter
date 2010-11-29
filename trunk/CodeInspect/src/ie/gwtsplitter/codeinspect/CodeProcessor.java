package ie.gwtsplitter.codeinspect;

import ie.gwtsplitter.codeinspect.domain.GSClass;
import ie.gwtsplitter.codeinspect.domain.GSPackage;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */

@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("*")
public class CodeProcessor extends AbstractProcessor {
	
	private List<TreePathScanner<Object, Trees>> visitors =
		new LinkedList<TreePathScanner<Object, Trees>>();
	
    private Trees trees;
    
    public CodeProcessor() {
    	
    }
    
    public final void addVisitor(final TreePathScanner<Object, Trees> visitor) {
    	visitors.add(visitor);
    }
    
    @Override
    public final void init(final ProcessingEnvironment pe) {
        super.init(pe);
        trees = Trees.instance(pe);
    }	
	
    @Override
    public final boolean process(final Set<? extends TypeElement> annotations,
			final RoundEnvironment roundEnv) {
        
    	for (Element e : roundEnv.getRootElements()) {
                System.out.println("Element is " + e.getSimpleName());
                
                String packageName = null;
                GSPackage gsPackage = null;
                if (CodeInspector.gsPackages.containsKey(packageName)) {
                	gsPackage = CodeInspector.gsPackages.get(packageName);
                } else {
                	gsPackage = new GSPackage(packageName);
                }
                gsPackage.gsClasses.put(e.toString(),
                		new GSClass(e.getSimpleName().toString(), ""));
                
                // Add code here to analyze each root element
                TreePath tp = trees.getPath(e);
                // invoke the scanner
                for (TreePathScanner<Object, Trees> scanner : visitors) {
                	scanner.scan(tp, trees);
                }
        }
    	
        return true;
    }

}

