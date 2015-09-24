package org.verapdf.features;

import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Features reporter
 *
 * @author Maksim Bezrukov
 */
public class FeaturesReporter {

	private final FeaturesCollection collection;
	private Map<FeaturesObjectTypesEnum, List<IFeaturesExtractor>> featuresExtractors;

	/**
	 * Creates new FeaturesReporter
	 */
	public FeaturesReporter() {
		collection = new FeaturesCollection();
		featuresExtractors = new HashMap<>();
	}

	/**
	 * Registers feature extractor for features object type
	 *
	 * @param extractor object for extract custom features
	 */
	public void registerFeaturesExtractor(IFeaturesExtractor extractor) {
		if (featuresExtractors.get(extractor.getType()) == null) {
			featuresExtractors.put(extractor.getType(), new ArrayList<IFeaturesExtractor>());
		}
		featuresExtractors.get(extractor.getType()).add(extractor);
	}

	/**
	 * Reports feature object for feature report
	 *
	 * @param obj object for reporting
	 */
	public void report(IFeaturesObject obj) {
		try {
			FeatureTreeNode root = obj.reportFeatures(collection);
			if (featuresExtractors.get(obj.getType()) != null) {
				FeatureTreeNode custom = FeatureTreeNode.newChildInstance("customFeatures", root);
				for (IFeaturesExtractor ext : featuresExtractors.get(obj.getType())) {
					FeatureTreeNode cust = ext.getFeatures(obj.getData());
					custom.addChild(cust);
				}
			}

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
