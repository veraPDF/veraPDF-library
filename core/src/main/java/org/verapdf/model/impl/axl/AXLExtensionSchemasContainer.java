/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.model.impl.axl;

import org.verapdf.xmp.XMPConst;
import org.verapdf.xmp.impl.VeraPDFXMPNode;
import org.verapdf.xmp.options.PropertyOptions;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.tools.xmp.SchemasDefinition;
import org.verapdf.model.tools.xmp.ValidatorsContainer;
import org.verapdf.model.tools.xmp.ValidatorsContainerCreator;
import org.verapdf.model.xmplayer.ExtensionSchemasContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Maksim Bezrukov
 */
public class AXLExtensionSchemasContainer extends AXLXMPObject implements ExtensionSchemasContainer {

    public static final String EXTENSION_SCHEMAS_CONTAINER = "ExtensionSchemasContainer";

    public static final String EXTENSION_SCHEMA_DEFINITIONS = "ExtensionSchemaDefinitions";

    protected final VeraPDFXMPNode xmpNode;
    protected final Map<String, SchemasDefinition> containerForPDFA_1;
    protected final Map<String, SchemasDefinition> containerForPDFA_2_3;

    public AXLExtensionSchemasContainer(VeraPDFXMPNode xmpNode, Map<String, SchemasDefinition> containerForPDFA_1, Map<String, SchemasDefinition> containerForPDFA_2_3) {
        super(EXTENSION_SCHEMAS_CONTAINER);
        this.xmpNode = xmpNode;
        this.containerForPDFA_1 = containerForPDFA_1;
        this.containerForPDFA_2_3 = containerForPDFA_2_3;
    }

    /**
     * @param link name of the link
     * @return List of all objects with link name
     */
    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case EXTENSION_SCHEMA_DEFINITIONS:
                return this.getExtensionSchemaDefinitions();
            default:
                return super.getLinkedObjects(link);
        }
    }

    private List<AXLExtensionSchemaDefinition> getExtensionSchemaDefinitions() {
        if (this.xmpNode != null && this.xmpNode.getOptions().isArray()) {
            List<AXLExtensionSchemaDefinition> res = new ArrayList<>();
            for (VeraPDFXMPNode node : this.xmpNode.getChildren()) {
                ValidatorsContainer containerForPDFA_1 = ValidatorsContainerCreator.EMPTY_VALIDATORS_CONTAINER;
                ValidatorsContainer containerForPDFA_2_3 = ValidatorsContainerCreator.EMPTY_VALIDATORS_CONTAINER;
                for (VeraPDFXMPNode child : node.getChildren()) {
                    if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && AXLExtensionSchemaDefinition.NAMESPACE_URI.equals(child.getName())) {
                        String namespace = child.getValue();
                        SchemasDefinition schemasDefinitionForPDFA_1 = this.containerForPDFA_1.get(namespace);
                        if (schemasDefinitionForPDFA_1 != null) {
                            containerForPDFA_1 = schemasDefinitionForPDFA_1.getValidatorsContainer();
                        }
                        SchemasDefinition schemasDefinitionForPDFA_2_3 = this.containerForPDFA_2_3.get(namespace);
                        if (schemasDefinitionForPDFA_2_3 != null) {
                            containerForPDFA_2_3 = schemasDefinitionForPDFA_2_3.getValidatorsContainer();
                        }
                    }
                }
                res.add(new AXLExtensionSchemaDefinition(node, containerForPDFA_1, containerForPDFA_2_3));
            }
            return Collections.unmodifiableList(res);
        }
        return Collections.emptyList();
    }

    @Override
    public Boolean getisValidBag() {
        PropertyOptions options = this.xmpNode.getOptions();
        return options.isArray() && !(options.isArrayOrdered() || options.isArrayAlternate());
    }

    @Override
    public String getprefix() {
        return this.xmpNode == null ? null : this.xmpNode.getPrefix();
    }

}
