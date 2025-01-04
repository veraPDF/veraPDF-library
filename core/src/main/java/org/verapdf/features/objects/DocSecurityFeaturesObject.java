/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.features.objects;

import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.tools.CreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Feature object for document security
 *
 * @author Maksim Bezrukov
 */
public class DocSecurityFeaturesObject extends FeaturesObject {

    private static final String DOCUMENT_SECURITY = "documentSecurity";
    private static final String FILTER = "filter";
    private static final String SUB_FILTER = "subFilter";
    private static final String VERSION = "version";
    private static final String LENGTH = "length";
    private static final String OWNER_KEY = "ownerKey";
    private static final String USER_KEY = "userKey";
    private static final String ENCRYPT_METADATA = "encryptMetadata";
    private static final String PRINT_ALLOWED = "printAllowed";
    private static final String PRINT_DEGRADED_ALLOWED = "printDegradedAllowed";
    private static final String CHANGES_ALLOWED = "changesAllowed";
    private static final String MODIFY_ANNOTATIONS_ALLOWED = "modifyAnnotationsAllowed";
    private static final String FILLING_SIGNING_ALLOWED = "fillingSigningAllowed";
    private static final String DOCUMENT_ASSEMBLY_ALLOWED = "documentAssemblyAllowed";
    private static final String EXTRACT_CONTENT_ALLOWED = "extractContentAllowed";
    private static final String EXTRACT_ACCESSIBILITY_ALLOWED = "extractAccessibilityAllowed";

    /**
     * Constructs new Document Security Feature Object
     *
     * @param adapter class represents doc security adapter
     */
    public DocSecurityFeaturesObject(DocSecurityFeaturesObjectAdapter adapter) {
        super(adapter);
    }

    /**
     * @return DOCUMENT_SECURITY instance of the FeatureObjectType enumeration
     */
    @Override
    public FeatureObjectType getType() {
        return FeatureObjectType.DOCUMENT_SECURITY;
    }

    /**
     * Reports all features from the object into the collection
     *
     * @return FeatureTreeNode class which represents a root node of the
     * constructed collection tree
     * @throws FeatureParsingException occurs when wrong features tree node constructs
     */
    @Override
    public FeatureTreeNode collectFeatures() throws FeatureParsingException {
        DocSecurityFeaturesObjectAdapter docSecAdapter = (DocSecurityFeaturesObjectAdapter) this.adapter;
        FeatureTreeNode root = FeatureTreeNode.createRootNode(DOCUMENT_SECURITY);
        CreateNodeHelper.addNotEmptyNode(FILTER, docSecAdapter.getFilter(), root);
        CreateNodeHelper.addNotEmptyNode(SUB_FILTER, docSecAdapter.getSubFilter(), root);
        CreateNodeHelper.addNotEmptyNode(VERSION, String.valueOf(docSecAdapter.getVersion()), root);
        CreateNodeHelper.addNotEmptyNode(LENGTH, String.valueOf(docSecAdapter.getLength()), root);
        CreateNodeHelper.addNotEmptyNode(OWNER_KEY, docSecAdapter.getHexEncodedOwnerKey(), root);
        CreateNodeHelper.addNotEmptyNode(USER_KEY, docSecAdapter.getHexEncodedUserKey(), root);
        CreateNodeHelper.addNotEmptyNode(ENCRYPT_METADATA, String.valueOf(docSecAdapter.isEncryptMetadata()), root);

        if (docSecAdapter.isUserPermissionsPresent()) {
            CreateNodeHelper.addNotEmptyNode(PRINT_ALLOWED, String.valueOf(docSecAdapter.isPrintAllowed()), root);
            CreateNodeHelper.addNotEmptyNode(PRINT_DEGRADED_ALLOWED, String.valueOf(docSecAdapter.isPrintDegradedAllowed()), root);
            CreateNodeHelper.addNotEmptyNode(CHANGES_ALLOWED, String.valueOf(docSecAdapter.isModifyAnnotationsAllowed()), root);
            CreateNodeHelper.addNotEmptyNode(MODIFY_ANNOTATIONS_ALLOWED, String.valueOf(docSecAdapter.isChangesAllowed()), root);
            CreateNodeHelper.addNotEmptyNode(FILLING_SIGNING_ALLOWED, String.valueOf(docSecAdapter.isFillingSigningAllowed()), root);
            CreateNodeHelper.addNotEmptyNode(DOCUMENT_ASSEMBLY_ALLOWED, String.valueOf(docSecAdapter.isDocumentAssemblyAllowed()), root);
            CreateNodeHelper.addNotEmptyNode(EXTRACT_CONTENT_ALLOWED, String.valueOf(docSecAdapter.isExtractContentAllowed()), root);
            CreateNodeHelper.addNotEmptyNode(EXTRACT_ACCESSIBILITY_ALLOWED, String.valueOf(docSecAdapter.isExtractAccessibilityAllowed()), root);
        }

        return root;
    }

    /**
     * @return null
     */
    @Override
    public FeaturesData getData() {
        return null;
    }

    static List<Feature> getFeaturesList() {
        // All fields are present
        List<Feature> featuresList = new ArrayList<>();
        featuresList.add(new Feature("Filter",
                generateVariableXPath(DOCUMENT_SECURITY, FILTER), Feature.FeatureType.STRING));
        featuresList.add(new Feature("SubFilter",
                generateVariableXPath(DOCUMENT_SECURITY, SUB_FILTER), Feature.FeatureType.STRING));
        featuresList.add(new Feature("Version",
                generateVariableXPath(DOCUMENT_SECURITY, VERSION), Feature.FeatureType.NUMBER));
        featuresList.add(new Feature("Length",
                generateVariableXPath(DOCUMENT_SECURITY, LENGTH), Feature.FeatureType.NUMBER));
        featuresList.add(new Feature("Owner Key",
                generateVariableXPath(DOCUMENT_SECURITY, OWNER_KEY), Feature.FeatureType.STRING));
        featuresList.add(new Feature("User Key",
                generateVariableXPath(DOCUMENT_SECURITY, USER_KEY), Feature.FeatureType.STRING));
        featuresList.add(new Feature("Encrypt Metadata",
                generateVariableXPath(DOCUMENT_SECURITY, ENCRYPT_METADATA), Feature.FeatureType.BOOLEAN));
        featuresList.add(new Feature("Print Allowed",
                generateVariableXPath(DOCUMENT_SECURITY, PRINT_ALLOWED), Feature.FeatureType.BOOLEAN));
        featuresList.add(new Feature("Print Degraded Allowed",
                generateVariableXPath(DOCUMENT_SECURITY, PRINT_DEGRADED_ALLOWED), Feature.FeatureType.BOOLEAN));
        featuresList.add(new Feature("Changes Allowed",
                generateVariableXPath(DOCUMENT_SECURITY, CHANGES_ALLOWED), Feature.FeatureType.BOOLEAN));
        featuresList.add(new Feature("Modify Annotations Allowed",
                generateVariableXPath(DOCUMENT_SECURITY, MODIFY_ANNOTATIONS_ALLOWED), Feature.FeatureType.BOOLEAN));
        featuresList.add(new Feature("Filling Signing Allowed",
                generateVariableXPath(DOCUMENT_SECURITY, FILLING_SIGNING_ALLOWED), Feature.FeatureType.BOOLEAN));
        featuresList.add(new Feature("Document Assembly Allowed",
                generateVariableXPath(DOCUMENT_SECURITY, DOCUMENT_ASSEMBLY_ALLOWED), Feature.FeatureType.BOOLEAN));
        featuresList.add(new Feature("Extract Content Allowed",
                generateVariableXPath(DOCUMENT_SECURITY, EXTRACT_CONTENT_ALLOWED), Feature.FeatureType.BOOLEAN));
        featuresList.add(new Feature("Extract Accessibility Allowed",
                generateVariableXPath(DOCUMENT_SECURITY, EXTRACT_ACCESSIBILITY_ALLOWED), Feature.FeatureType.BOOLEAN));

        featuresList.add(new Feature("Error IDs",
                generateAttributeXPath(DOCUMENT_SECURITY, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
        return featuresList;
    }
}
