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
import org.verapdf.features.EmbeddedFileFeaturesData;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.tools.CreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Feature object for embedded file
 *
 * @author Maksim Bezrukov
 */
public class EmbeddedFileFeaturesObject extends FeaturesObject {

    private static final Logger LOGGER = Logger.getLogger(EmbeddedFileFeaturesObject.class.getCanonicalName());

    private static final String EMBEDDED_FILE = "embeddedFile";
    private static final String FILE_NAME = "fileName";
    private static final String DESCRIPTION = "description";
    private static final String AF_RELATIONSHIP = "afRelationship";
    private static final String SUB_TYPE = "subtype";
    private static final String FILTER = "filter";
    private static final String CREATION_DATE = "creationDate";
    private static final String MOD_DATE = "modDate";
    private static final String CHECK_SUM = "checkSum";
    private static final String SIZE = "size";

    /**
     * Constructs new Embedded File Feature Object
     *
     * @param adapter class represents embedded file adapter
     */
    public EmbeddedFileFeaturesObject(EmbeddedFileFeaturesObjectAdapter adapter) {
        super(adapter);
    }

    /**
     * @return EMBEDDED_FILE instance of the FeatureObjectType enumeration
     */
    @Override
    public FeatureObjectType getType() {
        return FeatureObjectType.EMBEDDED_FILE;
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
        EmbeddedFileFeaturesObjectAdapter efAdapter = (EmbeddedFileFeaturesObjectAdapter) this.adapter;
        FeatureTreeNode root = FeatureTreeNode.createRootNode(EMBEDDED_FILE);
        root.setAttribute("id", "file" + efAdapter.getIndex());
        CreateNodeHelper.addNotEmptyNode(FILE_NAME, efAdapter.getFileName(), root);
        CreateNodeHelper.addNotEmptyNode(DESCRIPTION, efAdapter.getDescription(), root);
        CreateNodeHelper.addNotEmptyNode(AF_RELATIONSHIP, efAdapter.getAFRelationship(), root);
        CreateNodeHelper.addNotEmptyNode(SUB_TYPE, efAdapter.getSubtype(), root);
        CreateNodeHelper.addNotEmptyNode(FILTER, efAdapter.getFilter(), root);
        CreateNodeHelper.createDateNode(CREATION_DATE, root, efAdapter.getCreationDate(), this);
        CreateNodeHelper.createDateNode(MOD_DATE, root, efAdapter.getModDate(), this);
        CreateNodeHelper.addNotEmptyNode(CHECK_SUM, efAdapter.getCheckSum(), root);
        CreateNodeHelper.addNotEmptyNode(SIZE, String.valueOf(efAdapter.getSize().longValue()), root);
        return root;
    }

    /**
     * @return null if it can not get embedded file stream and features data of the embedded file in other case.
     */
    @Override
    public FeaturesData getData() {
        EmbeddedFileFeaturesObjectAdapter efAdapter = (EmbeddedFileFeaturesObjectAdapter) this.adapter;
        InputStream efIn = efAdapter.getData();
        if (efIn == null) {
            LOGGER.log(Level.FINE, "Missed embedded file InputStream");
            return null;
        }

        EmbeddedFileFeaturesData.Builder builder = new EmbeddedFileFeaturesData.Builder(efIn);
        builder.name(efAdapter.getFileName());
        builder.description(efAdapter.getDescription());
        builder.afRelationship(efAdapter.getAFRelationship());
        builder.subtype(efAdapter.getSubtype());
        builder.creationDate(efAdapter.getCreationDate());
        builder.modDate(efAdapter.getModDate());
        builder.checkSum(efAdapter.getCheckSum());
        Long size = efAdapter.getSize();
        if (size != null) {
            builder.size(Integer.valueOf(size.intValue()));
        }
        return builder.build();
    }

    static List<Feature> getFeaturesList() {
        // All fields are present
        List<Feature> featuresList = new ArrayList<>();
        featuresList.add(new Feature("File Name",
                generateVariableXPath(EMBEDDED_FILE, FILE_NAME), Feature.FeatureType.STRING));
        featuresList.add(new Feature("Description",
                generateVariableXPath(EMBEDDED_FILE, DESCRIPTION), Feature.FeatureType.STRING));
        featuresList.add(new Feature("AFRelationship",
                generateVariableXPath(EMBEDDED_FILE, AF_RELATIONSHIP), Feature.FeatureType.STRING));
        featuresList.add(new Feature("Subtype",
                generateVariableXPath(EMBEDDED_FILE, SUB_TYPE), Feature.FeatureType.STRING));
        featuresList.add(new Feature("Filter",
                generateVariableXPath(EMBEDDED_FILE, FILTER), Feature.FeatureType.STRING));
        featuresList.add(new Feature("Creation Date",
                generateVariableXPath(EMBEDDED_FILE, CREATION_DATE), Feature.FeatureType.STRING));
        featuresList.add(new Feature("Modification Date",
                generateVariableXPath(EMBEDDED_FILE, MOD_DATE), Feature.FeatureType.STRING));
        featuresList.add(new Feature("CheckSum",
                generateVariableXPath(EMBEDDED_FILE, CHECK_SUM), Feature.FeatureType.STRING));
        featuresList.add(new Feature("Size",
                generateVariableXPath(EMBEDDED_FILE, SIZE), Feature.FeatureType.NUMBER));

        featuresList.add(new Feature("Error IDs",
                generateAttributeXPath(EMBEDDED_FILE, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
        return featuresList;
    }
}
