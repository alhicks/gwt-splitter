package ie.gwtsplitter.testfiles;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class TestClass {
	public static void main(final String[] args) {
		System.out.println("Test Compile 1");

		List<String> list = new ArrayList<String>();
		list.add("my test list 1");

		for (String s : list) {
			System.out.println(s);
		}

		SuperUser user = new SuperUser();
		user.getFName();

		TestClass x = new TestClass();
		x.test("ct");

		methodInvocationA();

	}

	static void methodInvocationA() {
		methodInvocationB();
	}

	static void methodInvocationB() {
		String x = "";
	}

	final void test(final String x) {
		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(final Throwable err) {
				System.out.println("Failure");
			}

			public void onSuccess() {
				System.out.println("Success");
			}
		});
	}
}
