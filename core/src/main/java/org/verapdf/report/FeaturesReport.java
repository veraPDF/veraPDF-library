package org.verapdf.report;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.tools.FeatureTreeNode;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement(name="featuresReport")
public class FeaturesReport {

	private final static String ERROR_STATUS = "Could not finish features collecting due to unexpected error.";

	@XmlElement
	private final String status;
	@XmlElement
	private final FeaturesNode informationDict;
	@XmlElement
	private final FeaturesNode metadata;
	@XmlElement
	private final FeaturesNode documentSecurity;
	@XmlElement
	private final FeaturesNode signatures;
	@XmlElement
	private final FeaturesNode lowLevelInfo;
	@XmlElement
	private final FeaturesNode embeddedFiles;
	@XmlElement
	private final FeaturesNode iccProfiles;
	@XmlElement
	private final FeaturesNode outputIntents;
	@XmlElement
	private final FeaturesNode outlines;
	@XmlElement
	private final FeaturesNode annotations;
	@XmlElement
	private final FeaturesNode pages;
	@XmlElement
	private final DocumentResourcesFeatures documentResources;
	@XmlElement
	private final FeaturesNode errors;

	private FeaturesReport(FeaturesNode informationDict, FeaturesNode metadata,
						   FeaturesNode documentSecurity, FeaturesNode signatures,
						   FeaturesNode lowLevelInfo,
						   FeaturesNode embeddedFiles, FeaturesNode iccProfiles,
						   FeaturesNode outputIntents, FeaturesNode outlines,
						   FeaturesNode annotations, FeaturesNode pages,
						   DocumentResourcesFeatures documentResources,
						   FeaturesNode errors, String status) {
		this.informationDict = informationDict;
		this.metadata = metadata;
		this.documentSecurity = documentSecurity;
		this.signatures = signatures;
		this.lowLevelInfo = lowLevelInfo;
		this.embeddedFiles = embeddedFiles;
		this.iccProfiles = iccProfiles;
		this.outputIntents = outputIntents;
		this.outlines = outlines;
		this.annotations = annotations;
		this.pages = pages;
		this.documentResources = documentResources;
		this.errors = errors;
		this.status = status;
	}

	private FeaturesReport() {
		this(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	}

	static FeaturesReport createErrorReport() {
		return createErrorReport(ERROR_STATUS);
	}

	static FeaturesReport createErrorReport(String errorMessage) {
		return new FeaturesReport(null, null, null, null, null, null, null, null, null, null, null, null, null, errorMessage);
	}

	/**
	 * @param collection
	 * @return
	 */
	public static FeaturesReport fromValues(FeatureExtractionResult collection) {
		if (collection == null) {
			return null;
		}
		FeaturesNode info = getFirstNodeFromType(collection, FeatureObjectType.INFORMATION_DICTIONARY);
		FeaturesNode metadata = getFirstNodeFromType(collection, FeatureObjectType.METADATA);
		FeaturesNode docSec = getFirstNodeFromType(collection, FeatureObjectType.DOCUMENT_SECURITY);
		FeaturesNode sig = FeaturesNode.fromValues(collection, FeatureObjectType.SIGNATURE);
		FeaturesNode lowLvl = getFirstNodeFromType(collection, FeatureObjectType.LOW_LEVEL_INFO);
		FeaturesNode embeddedFiles = FeaturesNode.fromValues(collection, FeatureObjectType.EMBEDDED_FILE);
		FeaturesNode iccProfiles = FeaturesNode.fromValues(collection, FeatureObjectType.ICCPROFILE);
		FeaturesNode outputIntents = FeaturesNode.fromValues(collection, FeatureObjectType.OUTPUTINTENT);
		FeaturesNode outlines = getFirstNodeFromType(collection, FeatureObjectType.OUTLINES);
		FeaturesNode annotations = FeaturesNode.fromValues(collection, FeatureObjectType.ANNOTATION);
		FeaturesNode pages = FeaturesNode.fromValues(collection, FeatureObjectType.PAGE);
		DocumentResourcesFeatures res = DocumentResourcesFeatures.fromValues(collection);
		FeaturesNode errors = FeaturesNode.fromValues(collection, FeatureObjectType.ERROR);
		return new FeaturesReport(info, metadata, docSec, sig, lowLvl, embeddedFiles, iccProfiles, outputIntents, outlines, annotations, pages, res, errors, null);
	}

	static FeaturesNode getFirstNodeFromType(FeatureExtractionResult collection, FeatureObjectType type) {
		List<FeatureTreeNode> featureTreesForType = collection.getFeatureTreesForType(type);
		if (featureTreesForType.isEmpty()) {
			return null;
		}
		return FeaturesNode.fromValues(collection.getFeatureTreesForType(type).get(0), collection);
	}
}
