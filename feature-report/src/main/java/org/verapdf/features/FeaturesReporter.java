package org.verapdf.features;

import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.tools.FeaturesCollection;

/**
 * Features reporter
 *
 * @author Maksim Bezrukov
 */
public class FeaturesReporter {

	private final FeaturesCollection collection;

	/**
	 * Creates new FeaturesReporter
	 */
	public FeaturesReporter() {
		collection = new FeaturesCollection();
	}

	/**
	 * Reports feature object for feature report
	 *
	 * @param obj object for reporting
	 */
	public void report(IFeaturesObject obj) {
		try {
			obj.reportFeatures(collection);
		} catch (FeaturesTreeNodeException ignore) {
			// The method logic should ensure this never happens, so if it does
			// it's catastrophic. We'll throw an IllegalStateException with this
			// as a cause. The only time it's ignored is when the unthinkable
			// happens
			throw new IllegalStateException(
					"FeaturesReporter.report() illegal state.", ignore);
			// This exception occurs when wrong node creates for feature tree.
			// The logic of the method guarantees this doesn't occur.
		}
	}

	/**
	 * @return collection of featurereport
	 */
	public FeaturesCollection getCollection() {
		return collection;
	}
}
