/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 * <p>
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 * <p>
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 * <p>
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
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Feature object for document security
 *
 * @author Maksim Bezrukov
 */
public class DocSecurityFeaturesObject extends FeaturesObject {

    /**
     * Constructs new Document Security Feature Object
     *
     * @param adapter class represents annotation adapter
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
        FeatureTreeNode root = FeatureTreeNode.createRootNode("documentSecurity");
        CreateNodeHelper.addNotEmptyNode("filter", docSecAdapter.getFilter(), root);
        CreateNodeHelper.addNotEmptyNode("subFilter", docSecAdapter.getSubFilter(), root);
        CreateNodeHelper.addNotEmptyNode("version", String.valueOf(docSecAdapter.getVersion()), root);
        CreateNodeHelper.addNotEmptyNode("length", String.valueOf(docSecAdapter.getLength()), root);
        CreateNodeHelper.addNotEmptyNode("ownerKey", docSecAdapter.getHexEncodedOwnerKey(), root);
        CreateNodeHelper.addNotEmptyNode("userKey", docSecAdapter.getHexEncodedUserKey(), root);
        CreateNodeHelper.addNotEmptyNode("encryptMetadata", String.valueOf(docSecAdapter.isEncryptMetadata()), root);

        if (docSecAdapter.isUserPermissionsPresent()) {
            CreateNodeHelper.addNotEmptyNode("printAllowed", String.valueOf(docSecAdapter.isPrintAllowed()), root);
            CreateNodeHelper.addNotEmptyNode("printDegradedAllowed", String.valueOf(docSecAdapter.isPrintDegradedAllowed()), root);
            CreateNodeHelper.addNotEmptyNode("changesAllowed", String.valueOf(docSecAdapter.isModifyAnnotationsAllowed()), root);
            CreateNodeHelper.addNotEmptyNode("modifyAnnotationsAllowed", String.valueOf(docSecAdapter.isChangesAllowed()), root);
            CreateNodeHelper.addNotEmptyNode("fillingSigningAllowed", String.valueOf(docSecAdapter.isFillingSigningAllowed()), root);
            CreateNodeHelper.addNotEmptyNode("documentAssemblyAllowed", String.valueOf(docSecAdapter.isDocumentAssemblyAllowed()), root);
            CreateNodeHelper.addNotEmptyNode("extractContentAllowed", String.valueOf(docSecAdapter.isExtractContentAllowed()), root);
            CreateNodeHelper.addNotEmptyNode("extractAccessibilityAllowed", String.valueOf(docSecAdapter.isExtractAccessibilityAllowed()), root);
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
        featuresList.add(new Feature("Filter", "/documentSecurity/filter", Feature.FeatureType.STRING));
        featuresList.add(new Feature("SubFilter", "/documentSecurity/filter", Feature.FeatureType.STRING));
        featuresList.add(new Feature("Version", "/documentSecurity/filter", Feature.FeatureType.NUMBER));
        featuresList.add(new Feature("Length", "/documentSecurity/filter", Feature.FeatureType.NUMBER));
        featuresList.add(new Feature("Owner Key", "/documentSecurity/filter", Feature.FeatureType.STRING));
        featuresList.add(new Feature("User Key", "/documentSecurity/filter", Feature.FeatureType.STRING));
        featuresList.add(new Feature("Encrypt Metadata", "/documentSecurity/filter", Feature.FeatureType.BOOLEAN));
        featuresList.add(new Feature("Print Allowed", "/documentSecurity/filter", Feature.FeatureType.BOOLEAN));
        featuresList.add(new Feature("Print Degraded Allowed", "/documentSecurity/filter", Feature.FeatureType.BOOLEAN));
        featuresList.add(new Feature("Changes Allowed", "/documentSecurity/filter", Feature.FeatureType.BOOLEAN));
        featuresList.add(new Feature("Modify Annotations Allowed", "/documentSecurity/filter", Feature.FeatureType.BOOLEAN));
        featuresList.add(new Feature("Filling Signing Allowed", "/documentSecurity/filter", Feature.FeatureType.BOOLEAN));
        featuresList.add(new Feature("Document Assembly Allowed", "/documentSecurity/filter", Feature.FeatureType.BOOLEAN));
        featuresList.add(new Feature("Extract Content Allowed", "/documentSecurity/filter", Feature.FeatureType.BOOLEAN));
        featuresList.add(new Feature("Extract Accessibility Allowed", "/documentSecurity/filter", Feature.FeatureType.BOOLEAN));

        featuresList.add(new Feature("Error IDs", "/documentSecurity/@errorId", Feature.FeatureType.STRING));
        return featuresList;
    }
}
