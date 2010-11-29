package ie.gwtsplitter.codeinspect.visitor;

import com.sun.source.tree.Tree;
import com.sun.source.util.Trees;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public interface CodeVisitorInterface {

	Object scan(Tree node, Trees tree);
	int getCount();
	
}
