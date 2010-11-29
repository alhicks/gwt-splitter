package ie.gwtsplitter.codeinspect.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class GSPackage {

	public String name;
	public Map<String, GSClass> gsClasses = null;

	public GSPackage(final String name) {
		this.name = name;
		gsClasses = new HashMap<String, GSClass>();
	}

	public final GSClass getGSClass(final String name) {
		if (gsClasses.containsKey(name)) {
			return gsClasses.get(name);
		}
		return null;
	}
}
