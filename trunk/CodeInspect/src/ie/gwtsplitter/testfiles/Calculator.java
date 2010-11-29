package ie.gwtsplitter.testfiles;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class Calculator {

	public static int sum(final int[] nums) {
		int ans = 0;
		for (int i = 0; i < nums.length; i++) {
			ans += nums[i];
		}
		return ans;
	}

}
