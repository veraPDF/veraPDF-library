package org.verapdf.features;

import org.verapdf.core.FeatureParsingException;
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

	public static final String CUSTOM_FEATURES_ROOT_NODE_NAME = "customFeatures";

	private final FeaturesCollection collection;
	private Map<FeaturesObjectTypesEnum, List<FeaturesExtractor>> featuresExtractors;

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
	public void registerFeaturesExtractor(FeaturesExtractor extractor) {
		if (featuresExtractors.get(extractor.getType()) == null) {
			featuresExtractors.put(extractor.getType(), new ArrayList<FeaturesExtractor>());
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
				FeaturesData objData = obj.getData();
				if (objData != null) {
					FeatureTreeNode custom = FeatureTreeNode.newChildInstance(CUSTOM_FEATURES_ROOT_NODE_NAME, root);
					for (FeaturesExtractor ext : featuresExtractors.get(obj.getType())) {
						List<FeatureTreeNode> cust = ext.getFeatures(objData);
						if (cust != null) {
							FeatureTreeNode custRoot = FeatureTreeNode.newChildInstance("pluginFeatures", custom);
							custRoot.addAttribute("pluginId", ext.getID());
							custRoot.addAttribute("description", ext.getDescription());
							for (FeatureTreeNode ftn : cust) {
								if (ftn != null) {
									custRoot.addChild(ftn);
								}
							}
						}
					}
				}
			}

		} catch (FeatureParsingException ignore) {
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
