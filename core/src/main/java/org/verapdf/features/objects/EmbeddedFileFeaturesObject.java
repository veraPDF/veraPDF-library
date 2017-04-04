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

    /**
     * Constructs new Embedded File Feature Object
     *
     * @param adapter class represents annotation adapter
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
        FeatureTreeNode root = FeatureTreeNode.createRootNode("embeddedFile");
        root.setAttribute("id", "file" + efAdapter.getIndex());
        CreateNodeHelper.addNotEmptyNode("fileName", efAdapter.getFileName(), root);
        CreateNodeHelper.addNotEmptyNode("description", efAdapter.getDescription(), root);
        CreateNodeHelper.addNotEmptyNode("afRelationship", efAdapter.getAFRelationship(), root);
        CreateNodeHelper.addNotEmptyNode("subtype", efAdapter.getSubtype(), root);
        CreateNodeHelper.addNotEmptyNode("filter", efAdapter.getFilter(), root);
        CreateNodeHelper.createDateNode("creationDate", root, efAdapter.getCreationDate(), this);
        CreateNodeHelper.createDateNode("modDate", root, efAdapter.getModDate(), this);
        CreateNodeHelper.addNotEmptyNode("checkSum", efAdapter.getCheckSum(), root);
        CreateNodeHelper.addNotEmptyNode("size", String.valueOf(efAdapter.getSize().longValue()), root);
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
        featuresList.add(new Feature("File Name", "/embeddedFile/fileName", Feature.FeatureType.STRING));
        featuresList.add(new Feature("Description", "/embeddedFile/description", Feature.FeatureType.STRING));
        featuresList.add(new Feature("AFRelationship", "/embeddedFile/afRelationship", Feature.FeatureType.STRING));
        featuresList.add(new Feature("Subtype", "/embeddedFile/subtype", Feature.FeatureType.STRING));
        featuresList.add(new Feature("Filter", "/embeddedFile/filter", Feature.FeatureType.STRING));
        featuresList.add(new Feature("Creation Date", "/embeddedFile/creationDate", Feature.FeatureType.STRING));
        featuresList.add(new Feature("Modification Date", "/embeddedFile/modDate", Feature.FeatureType.STRING));
        featuresList.add(new Feature("CheckSum", "/embeddedFile/checkSum", Feature.FeatureType.STRING));
        featuresList.add(new Feature("Size", "/embeddedFile/size", Feature.FeatureType.NUMBER));

        featuresList.add(new Feature("Error IDs", "/embeddedFile/@errorId", Feature.FeatureType.STRING));
        return featuresList;
    }
}
