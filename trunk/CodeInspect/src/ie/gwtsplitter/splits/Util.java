package ie.gwtsplitter.splits;

import java.util.Set;

/**
 * 
 * @author Alan Hicks (al.hicks@gmail.com)
 */
public class Util {

	/**
	 * counts number of times the pair p1 and p2 appear in the click log.
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	static int count(final ClickPair cp) {
		return -1;
	}

	/**
	 * makes a count of the various clickpairs.
	 * 
	 * @param cp
	 */
	static Set<ClickPair> countClosure(final Set<ClickPair> cp) {
		return null;
	}

	/**
	 * returns the quality of a set of splits.
	 * 
	 * @return
	 */
	static int splitQuality(final Set<Split> splits) {
		return -1;
	}

	/**
	 * compare the static analysis with the SOYC.
	 * 
	 * @return
	 */
	static float corelation(final StaticAnalysisResult sa, final SOYC soyc) {
		return -1;
	}

	/**
	 * calculate split size.
	 */
	static int splitSize(final Split split) {
		return -1;
	}

	/**
	 * finds optimal splits. maybe.
	 */
	static Set<Split> splitter(final StaticAnalysisResult sa,
			final ServerAnalysisResults sar) {
		return null;
	}
}
