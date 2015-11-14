package org.verapdf.report;

import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class FeaturesReport {

	@XmlElement
	private final FeaturesNode informationDict;
	@XmlElement
	private final FeaturesNode metadata;
	@XmlElement
	private final FeaturesNode documentSecurity;
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

	private FeaturesReport(FeaturesNode informationDict, FeaturesNode metadata, FeaturesNode documentSecurity, FeaturesNode lowLevelInfo, FeaturesNode embeddedFiles, FeaturesNode iccProfiles, FeaturesNode outputIntents, FeaturesNode outlines, FeaturesNode annotations, FeaturesNode pages, DocumentResourcesFeatures documentResources, FeaturesNode errors) {
		this.informationDict = informationDict;
		this.metadata = metadata;
		this.documentSecurity = documentSecurity;
		this.lowLevelInfo = lowLevelInfo;
		this.embeddedFiles = embeddedFiles;
		this.iccProfiles = iccProfiles;
		this.outputIntents = outputIntents;
		this.outlines = outlines;
		this.annotations = annotations;
		this.pages = pages;
		this.documentResources = documentResources;
		this.errors = errors;
	}

	static FeaturesReport fromValues(FeaturesCollection collection) {
		FeaturesNode info = getFirstNodeFromType(collection, FeaturesObjectTypesEnum.INFORMATION_DICTIONARY);
		FeaturesNode metadata = getFirstNodeFromType(collection, FeaturesObjectTypesEnum.METADATA);
		FeaturesNode docSec = getFirstNodeFromType(collection, FeaturesObjectTypesEnum.DOCUMENT_SECURITY);
		FeaturesNode lowLvl = getFirstNodeFromType(collection, FeaturesObjectTypesEnum.LOW_LEVEL_INFO);

		FeaturesNode embeddedFiles = FeaturesNode.fromValues(collection, FeaturesObjectTypesEnum.EMBEDDED_FILE);
		FeaturesNode iccProfiles = FeaturesNode.fromValues(collection, FeaturesObjectTypesEnum.ICCPROFILE);
		FeaturesNode outputIntents = FeaturesNode.fromValues(collection, FeaturesObjectTypesEnum.OUTPUTINTENT);
		FeaturesNode outlines = getFirstNodeFromType(collection, FeaturesObjectTypesEnum.OUTLINES);
		FeaturesNode annotations = FeaturesNode.fromValues(collection, FeaturesObjectTypesEnum.ANNOTATION);
		FeaturesNode pages = FeaturesNode.fromValues(collection, FeaturesObjectTypesEnum.PAGE);
		DocumentResourcesFeatures res = DocumentResourcesFeatures.fromValues(collection);
		FeaturesNode errors = FeaturesNode.fromValues(collection, FeaturesObjectTypesEnum.ERROR);
		return new FeaturesReport(info, metadata, docSec, lowLvl, embeddedFiles, iccProfiles, outputIntents, outlines, annotations, pages, res, errors);
	}

	static FeaturesNode getFirstNodeFromType(FeaturesCollection collection, FeaturesObjectTypesEnum type) {
		List<FeatureTreeNode> featureTreesForType = collection.getFeatureTreesForType(type);
		if (featureTreesForType.isEmpty()) {
			return null;
		}
		return FeaturesNode.fromValues(collection.getFeatureTreesForType(type).get(0), collection);
	}
}
