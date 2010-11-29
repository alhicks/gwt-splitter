package ie.gwtsplitter.splits;

import ie.gwtsplitter.codeinspect.domain.GSClass;
import ie.gwtsplitter.codeinspect.domain.GSMethod;

import java.util.Set;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public interface Split {

	/*
	 * (NAT-Natural Number, Location, Size, SET[CLASS], SET[Methods])
	 * 
	 */
	
	int id();
	int location();
	GSMethod method();
	int size();
	Set<GSClass> clazz();
	Set<GSMethod> methods();
	
}
