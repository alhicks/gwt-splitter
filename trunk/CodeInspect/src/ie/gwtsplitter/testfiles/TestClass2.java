package ie.gwtsplitter.testfiles;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class TestClass2 {
	
	/**
	 * 
	 * @param args parameters
	 */
	public static void main(final String[] args) {
		System.out.println("Test Compile 1");
		
		List<String> list = new ArrayList<String>();
		list.add("my test list");
		
		TestClass t = new TestClass();
		t.test("alan");
		
		TestClass.methodInvocationA();
		
	}
	
	/**
	 * 
	 */
	private void test() {
	    GWT.runAsync(new RunAsyncCallback() {
	        public void onFailure(final Throwable err) {
	          System.out.println("Failure");
	        }

	        public void onSuccess() {
	        	int[] n = { 1, 2, 3, 4, 5 };
	        	int a = Calculator.sum(n);
	        	System.out.println("Success[" + a + "]");
	        }
	      });
	}
}
