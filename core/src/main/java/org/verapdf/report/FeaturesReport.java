/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.report;

import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.tools.FeatureTreeNode;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement(name="featuresReport")
public class FeaturesReport {

	private final static String ERROR_STATUS = "Could not finish features collecting due to unexpected error."; //$NON-NLS-1$

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
	private final FeaturesNode actions;
	@XmlElement
	private final FeaturesNode interactiveFormFields;
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
						   FeaturesNode lowLevelInfo, FeaturesNode actions,
						   FeaturesNode interactiveFormFields,
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
		this.actions = actions;
		this.interactiveFormFields = interactiveFormFields;
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
		this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	}

	static FeaturesReport createErrorReport() {
		return createErrorReport(ERROR_STATUS);
	}

	static FeaturesReport createErrorReport(String errorMessage) {
		return new FeaturesReport(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, errorMessage);
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
		FeaturesNode actions = FeaturesNode.fromValues(collection, FeatureObjectType.ACTION);
		FeaturesNode interactiveFormFields = FeaturesNode.fromValues(collection, FeatureObjectType.INTERACTIVE_FORM_FIELDS);
		FeaturesNode embeddedFiles = FeaturesNode.fromValues(collection, FeatureObjectType.EMBEDDED_FILE);
		FeaturesNode iccProfiles = FeaturesNode.fromValues(collection, FeatureObjectType.ICCPROFILE);
		FeaturesNode outputIntents = FeaturesNode.fromValues(collection, FeatureObjectType.OUTPUTINTENT);
		FeaturesNode outlines = getFirstNodeFromType(collection, FeatureObjectType.OUTLINES);
		FeaturesNode annotations = FeaturesNode.fromValues(collection, FeatureObjectType.ANNOTATION);
		FeaturesNode pages = FeaturesNode.fromValues(collection, FeatureObjectType.PAGE);
		DocumentResourcesFeatures res = DocumentResourcesFeatures.fromValues(collection);
		FeaturesNode errors = FeaturesNode.fromValues(collection, FeatureObjectType.ERROR);
		return new FeaturesReport(info, metadata, docSec, sig, lowLvl, actions, interactiveFormFields, embeddedFiles, iccProfiles, outputIntents, outlines, annotations, pages, res, errors, null);
	}

	static FeaturesNode getFirstNodeFromType(FeatureExtractionResult collection, FeatureObjectType type) {
		List<FeatureTreeNode> featureTreesForType = collection.getFeatureTreesForType(type);
		if (featureTreesForType.isEmpty()) {
			return null;
		}
		return FeaturesNode.fromValues(collection.getFeatureTreesForType(type).get(0), collection);
	}

	public FeaturesNode getPages() {
		return pages;
	}

	public FeaturesNode getAnnotations() {
		return annotations;
	}

	public FeaturesNode getOutlines() {
		return outlines;
	}

	public FeaturesNode getOutputIntents() {
		return outputIntents;
	}

	public FeaturesNode getIccProfiles() {
		return iccProfiles;
	}

	public FeaturesNode getEmbeddedFiles() {
		return embeddedFiles;
	}

	public FeaturesNode getInteractiveFormFields() {
		return interactiveFormFields;
	}

	public FeaturesNode getActions() {
		return actions;
	}

	public FeaturesNode getErrors() {
		return errors;
	}

	public DocumentResourcesFeatures getDocumentResources() {
		return documentResources;
	}

	public FeaturesNode getLowLevelInfo() {
		return lowLevelInfo;
	}

	public FeaturesNode getSignatures() {
		return signatures;
	}

	public FeaturesNode getDocumentSecurity() {
		return documentSecurity;
	}

	public FeaturesNode getMetadata() {
		return metadata;
	}

	public FeaturesNode getInformationDict() {
		return informationDict;
	}

	public String getStatus() {
		return status;
	}
}
