package org.verapdf.features;

import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.*;

/**
 * Features reporter
 *
 * @author Maksim Bezrukov
 */
public class FeaturesReporter {

	private final FeaturesCollection collection;
	private Map<FeaturesObjectTypesEnum, List<ExtractorStructure>> featuresExtractors;

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
	 * @param id UUID object which represents id for the extractor
	 */
	public void registerFeaturesExtractor(IFeaturesExtractor extractor, UUID id) {
		if (featuresExtractors.get(extractor.getType()) == null) {
			featuresExtractors.put(extractor.getType(), new ArrayList<ExtractorStructure>());
		}

		featuresExtractors.get(extractor.getType()).add(new ExtractorStructure(extractor, id));
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
				for (ExtractorStructure extSt : featuresExtractors.get(obj.getType())) {
					List<FeatureTreeNode> cust = extSt.extractor.getFeatures(obj.getData());
					if (cust != null) {
						FeatureTreeNode custRoot = FeatureTreeNode.newChildInstance("pluginFeatures", custom);
						custRoot.addAttribute("id", extSt.id.toString());
						for (FeatureTreeNode ftn : cust) {
							if (ftn != null) {
								custRoot.addChild(ftn);
							}
						}
					}
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

	private static class ExtractorStructure {
		UUID id;
		IFeaturesExtractor extractor;

		ExtractorStructure(IFeaturesExtractor extractor, UUID id) {
			this.extractor = extractor;
			this.id = id;
		}
	}
}
