package ie.gwtsplitter.codeinspect;

import ie.gwtsplitter.codeinspect.domain.CodeInspectResult;
import ie.gwtsplitter.codeinspect.domain.Feature;
import ie.gwtsplitter.codeinspect.domain.GSClass;
import ie.gwtsplitter.codeinspect.domain.GSMethod;

import java.util.Map.Entry;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class CodeInspectUtil {

	public static void graphResults(final CodeInspectResult cir) {
		
		for (Entry<String, Feature> feature : cir.features.entrySet()) {
			Feature f = feature.getValue();
			System.out.println(feature.getKey() + " : " + f.getSize());
			
			GSMethod root = f.rootMethod();
			System.out.println("_> " + root.name);
			
			for(GSMethod m : f.getMethods()) {
				System.out.println("\t- " + m.gsClass.name + "\t" + m.gsClass.prettyName + "\t: " + m.prettyName);
				for (GSMethod c : m.calls) {
					System.out.println("\t\t=" + c.name);
				}
			}
		}
		
		System.out.println("\n\nClasses");
		
		for (Entry<String, GSClass> clazz : cir.gsClasses.entrySet()) {
			GSClass c = clazz.getValue();
			System.out.println("\t- " + c.name );
		}
		
		
		
	}

	public static void graph() {
		
		String[] y = { "A","B","C","D","E" };
		int[] x = { 1, 4, 9, 4, 5 };
		
		double total = 0;
		for (int i : x) {
			total += i;
		}
		
		int yIndex = 0;
		for (String s : y) {
			System.out.print(s + " | ");
			int size = x[yIndex++];
			double u = size/total; 
			u = u*100;
			size = (int)u;
			for (int i=0; i<size; i++) {
				System.out.print("#");
			}
			System.out.print(" " + size + "%\n");
		}
	}
	
}
