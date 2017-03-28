package org.verapdf.features.objects;

/**
 * @author Maksim Bezrukov
 */
public class Feature {

	// TODO: refactor it later (after logic fixes when all features objects will be checked)
	// such that for every pair of name and XPath there will be only one Feature object
	// Currently it could be not good idea because of nested features
	// (outlines are contains inside other outline in features report)
	private final String featureName;
	private final String featureXPath;
	private final FeatureType featureType;

	public Feature(String featureName, String featureXPath, FeatureType featureType) {
		this.featureName = featureName;
		this.featureXPath = featureXPath;
		this.featureType = featureType;
	}


	public String getFeatureName() {
		return featureName;
	}

	public String getFeatureXPath() {
		return featureXPath;
	}

	public FeatureType getFeatureType() {
		return featureType;
	}

	public enum FeatureType {
		STRING,
		NUMBER,
		BOOLEAN
	}
}
