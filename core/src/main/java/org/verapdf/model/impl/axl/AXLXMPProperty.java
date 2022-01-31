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
package org.verapdf.model.impl.axl;

import org.verapdf.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.tools.xmp.SchemasDefinition;
import org.verapdf.model.tools.xmp.SchemasDefinitionCreator;
import org.verapdf.model.xmplayer.XMPProperty;
import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * @author Maksim Bezrukov
 */
public class AXLXMPProperty extends AXLXMPObject implements XMPProperty {

    public static final String XMP_PROPERTY_TYPE = "XMPProperty";

    protected final VeraPDFXMPNode xmpNode;
    private final boolean isMainMetadata;
    private final boolean isClosedChoiceCheck;
    private final PDFAFlavour flavour;
    private final SchemasDefinition mainPackageSchemasDefinition;
    private SchemasDefinition currentSchemasDefinitionPDFA_1;
    private SchemasDefinition currentSchemasDefinitionPDFA_2_3;

    public AXLXMPProperty(VeraPDFXMPNode xmpNode, boolean isMainMetadata, boolean isClosedChoiceCheck, SchemasDefinition mainPackageSchemasDefinition, SchemasDefinition currentSchemasDefinitionPDFA_1, SchemasDefinition currentSchemasDefinitionPDFA_2_3, PDFAFlavour flavour) {
        this(xmpNode, XMP_PROPERTY_TYPE, isMainMetadata, isClosedChoiceCheck, mainPackageSchemasDefinition, currentSchemasDefinitionPDFA_1, currentSchemasDefinitionPDFA_2_3, flavour);
    }

    protected AXLXMPProperty(VeraPDFXMPNode xmpNode, String type, boolean isMainMetadata, boolean isClosedChoiceCheck, SchemasDefinition mainPackageSchemasDefinition, SchemasDefinition currentSchemasDefinitionPDFA_1, SchemasDefinition currentSchemasDefinitionPDFA_2_3, PDFAFlavour flavour) {
        super(type);
        this.xmpNode = xmpNode;
        this.isMainMetadata = isMainMetadata;
        this.isClosedChoiceCheck = isClosedChoiceCheck;
        this.mainPackageSchemasDefinition = mainPackageSchemasDefinition;
        this.currentSchemasDefinitionPDFA_1 = currentSchemasDefinitionPDFA_1;
        this.currentSchemasDefinitionPDFA_2_3 = currentSchemasDefinitionPDFA_2_3;
        this.contextDependent = Boolean.TRUE;
        this.flavour = flavour;
    }

    @Override
    public String getID() {
        return this.xmpNode.getNamespaceURI() + " - " + this.xmpNode.getPrefix() + ":" + this.xmpNode.getName();
    }

    @Override
    public Boolean getisPredefinedInXMP2004() {
        return Boolean.valueOf(SchemasDefinitionCreator.getPredefinedSchemaDefinitionForPDFA_1(this.isClosedChoiceCheck).isDefinedProperty(this.xmpNode));
    }

    @Override
    public Boolean getisPredefinedInXMP2005() {
        return Boolean.valueOf(SchemasDefinitionCreator.getPredefinedSchemaDefinitionForPDFA_2_3(this.isClosedChoiceCheck).isDefinedProperty(this.xmpNode));
    }

    @Override
    public Boolean getisDefinedInCurrentPackage() {
        if (this.flavour != null && this.flavour.getPart() == PDFAFlavour.Specification.ISO_19005_1) {
            return Boolean.valueOf(this.currentSchemasDefinitionPDFA_1.isDefinedProperty(this.xmpNode));
        }
        return Boolean.valueOf(this.currentSchemasDefinitionPDFA_2_3.isDefinedProperty(this.xmpNode));
    }

    @Override
    public Boolean getisDefinedInMainPackage() {
        return this.isMainMetadata ? Boolean.valueOf(this.currentSchemasDefinitionPDFA_2_3.isDefinedProperty(this.xmpNode)) :
            Boolean.valueOf(this.mainPackageSchemasDefinition.isDefinedProperty(this.xmpNode));
    }

    @Override
    public Boolean getisValueTypeCorrect() {
        if (this.xmpNode == null) {
            return Boolean.FALSE;
        }
        if (this.flavour != null && this.flavour.getPart() == PDFAFlavour.Specification.ISO_19005_1) {
            return isValueTypeCorrectForPDFA_1();
        }
        return isValueTypeCorrectForPDFA_2_3();
    }

    private Boolean isValueTypeCorrectForPDFA_1() {
        if (this.currentSchemasDefinitionPDFA_1.isDefinedProperty(this.xmpNode)) {
            return this.currentSchemasDefinitionPDFA_1.isCorrespondsDefinedType(this.xmpNode);
        }
        if (SchemasDefinitionCreator.getPredefinedSchemaDefinitionForPDFA_1(this.isClosedChoiceCheck).isDefinedProperty(this.xmpNode)) {
            return SchemasDefinitionCreator.getPredefinedSchemaDefinitionForPDFA_1(this.isClosedChoiceCheck).isCorrespondsDefinedType(this.xmpNode);
        }
        return null;
    }

    private Boolean isValueTypeCorrectForPDFA_2_3() {
        if (this.currentSchemasDefinitionPDFA_2_3.isDefinedProperty(this.xmpNode)) {
            return this.currentSchemasDefinitionPDFA_2_3.isCorrespondsDefinedType(this.xmpNode);
        }
        if (this.mainPackageSchemasDefinition.isDefinedProperty(this.xmpNode)) {
            return this.mainPackageSchemasDefinition.isCorrespondsDefinedType(this.xmpNode);
        }
        if (SchemasDefinitionCreator.getPredefinedSchemaDefinitionForPDFA_2_3(this.isClosedChoiceCheck).isDefinedProperty(this.xmpNode)) {
            return SchemasDefinitionCreator.getPredefinedSchemaDefinitionForPDFA_2_3(this.isClosedChoiceCheck).isCorrespondsDefinedType(this.xmpNode);
        }
        return null;
    }
}
