package org.verapdf.report;

import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
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
	@XmlElementWrapper
	@XmlElement(name = "embeddedFile")
	private final List<FeaturesNode> embeddedFiles;
	@XmlElementWrapper
	@XmlElement(name = "iccProfile")
	private final List<FeaturesNode> iccProfiles;
	@XmlElementWrapper
	@XmlElement(name = "outputIntent")
	private final List<FeaturesNode> outputIntents;
	@XmlElement
	private final FeaturesNode outlines;
	@XmlElementWrapper
	@XmlElement(name = "annotation")
	private final List<FeaturesNode> annotations;
	@XmlElementWrapper
	@XmlElement(name = "page")
	private final List<FeaturesNode> pages;
	@XmlElement
	private final DocumentResourcesFeatures documentResources;
	@XmlElementWrapper
	@XmlElement(name = "error")
	private final List<FeaturesNode> errors;

	private FeaturesReport(FeaturesNode informationDict, FeaturesNode metadata, FeaturesNode documentSecurity, FeaturesNode lowLevelInfo, List<FeaturesNode> embeddedFiles, List<FeaturesNode> iccProfiles, List<FeaturesNode> outputIntents, FeaturesNode outlines, List<FeaturesNode> annotations, List<FeaturesNode> pages, DocumentResourcesFeatures documentResources, List<FeaturesNode> errors) {
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

	private FeaturesReport() {
		this(null, null, null, null, null, null, null, null, null, null, null, null);
	}

	static FeaturesReport fromValues(FeaturesCollection collection) {
		FeaturesNode info = getFirstNodeFromType(collection, FeaturesObjectTypesEnum.INFORMATION_DICTIONARY);
		FeaturesNode metadata = getFirstNodeFromType(collection, FeaturesObjectTypesEnum.METADATA);
		FeaturesNode docSec = getFirstNodeFromType(collection, FeaturesObjectTypesEnum.DOCUMENT_SECURITY);
		FeaturesNode lowLvl = getFirstNodeFromType(collection, FeaturesObjectTypesEnum.LOW_LEVEL_INFO);

		List<FeaturesNode> embeddedFiles = getListFromType(collection, FeaturesObjectTypesEnum.EMBEDDED_FILE);
		List<FeaturesNode> iccProfiles = getListFromType(collection, FeaturesObjectTypesEnum.ICCPROFILE);
		List<FeaturesNode> outputIntents = getListFromType(collection, FeaturesObjectTypesEnum.OUTPUTINTENT);
		FeaturesNode outlines = getFirstNodeFromType(collection, FeaturesObjectTypesEnum.OUTLINES);
		List<FeaturesNode> annotations = getListFromType(collection, FeaturesObjectTypesEnum.ANNOTATION);
		List<FeaturesNode> pages = getListFromType(collection, FeaturesObjectTypesEnum.PAGE);
		List<FeaturesNode> errors = getListFromType(collection, FeaturesObjectTypesEnum.ERROR);
		DocumentResourcesFeatures res = DocumentResourcesFeatures.fromValues(collection);
		return new FeaturesReport(info, metadata, docSec, lowLvl, embeddedFiles, iccProfiles, outputIntents, outlines, annotations, pages, res, errors);
	}

	static List<FeaturesNode> getListFromType(FeaturesCollection collection, FeaturesObjectTypesEnum type) {
		List<FeatureTreeNode> featureTreesForType = collection.getFeatureTreesForType(type);
		if (featureTreesForType.size() == 0) {
			return null;
		}
		List<FeaturesNode> res = new ArrayList<>();
		for (FeatureTreeNode node : featureTreesForType) {
			res.add(FeaturesNode.fromValues(node));
		}
		return res;
	}

	static FeaturesNode getFirstNodeFromType(FeaturesCollection collection, FeaturesObjectTypesEnum type) {
		List<FeatureTreeNode> featureTreesForType = collection.getFeatureTreesForType(type);
		if (featureTreesForType.size() == 0) {
			return null;
		} else {
			return FeaturesNode.fromValues(collection.getFeatureTreesForType(type).get(0));
		}
	}
}
