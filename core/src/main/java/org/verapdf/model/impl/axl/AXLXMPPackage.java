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

import org.verapdf.xmp.XMPConst;
import org.verapdf.xmp.impl.VeraPDFMeta;
import org.verapdf.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.tools.xmp.SchemasDefinition;
import org.verapdf.model.tools.xmp.SchemasDefinitionCreator;
import org.verapdf.model.xmplayer.XMPPackage;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Current class is representation of XMPPackage interface from abstract model
 * based on adobe xmp library
 *
 * @author Maksim Bezrukov
 */
public class AXLXMPPackage extends AXLXMPObject implements XMPPackage {

    public static final String XMP_PACKAGE_TYPE = "XMPPackage";

    public static final String PROPERTIES = "Properties";
    public static final String EXTENSION_SCHEMAS_CONTAINERS = "ExtensionSchemasContainers";

    private static final String BYTES_REGEXP = "bytes\\s*=\\s*'[^']*'|bytes\\s*=\\s*\"[^\"]*\"";
    private static final String ENCODING_REGEXP = "encoding\\s*=\\s*'[^']*'|encoding\\s*=\\s*\"[^\"]*\"";

    private final VeraPDFMeta xmpMetadata;
    private final boolean isSerializationValid;
    //------------------------------------------------------------------------------ veraPDF: additional field for actual encoding
    private final String actualEncoding;
    private final boolean isMainMetadata;
    private final PDFAFlavour flavour;
    private final boolean isClosedChoiceCheck;
    private Map<String, SchemasDefinition> mainPackageSchemasDefinition;
    private Map<String, SchemasDefinition> currentSchemasDefinitionPDFA_1;
    private Map<String, SchemasDefinition> currentSchemasDefinitionPDFA_2_3;

    public AXLXMPPackage(VeraPDFMeta xmpMetadata, boolean isSerializationValid,
            boolean isClosedChoiceCheck,
            VeraPDFXMPNode mainPackageExtensionNode, PDFAFlavour flavour) {
        this(xmpMetadata, isSerializationValid, false, isClosedChoiceCheck,
                mainPackageExtensionNode, XMP_PACKAGE_TYPE, flavour);
    }

    public AXLXMPPackage(VeraPDFMeta xmpMetadata, boolean isSerializationValid,
            VeraPDFXMPNode mainPackageExtensionNode, PDFAFlavour flavour) {
        this(xmpMetadata, isSerializationValid, false, false,
                mainPackageExtensionNode, XMP_PACKAGE_TYPE, flavour);
    }

    protected AXLXMPPackage(VeraPDFMeta xmpMetadata,
            boolean isSerializationValid, boolean isMainMetadata,
            boolean isClosedChoiceCheck,
            VeraPDFXMPNode mainPackageExtensionNode, final String type,
            PDFAFlavour flavour) {
        super(type);
        this.xmpMetadata = xmpMetadata;
        this.isSerializationValid = isSerializationValid;
        //------------------------------------------------------------------------------ veraPDF: added actual encoding into constructor
        this.actualEncoding = this.xmpMetadata != null ? this.xmpMetadata.getActualEncoding() : null;
        this.isMainMetadata = isMainMetadata;
        this.isClosedChoiceCheck = isClosedChoiceCheck;
        this.mainPackageSchemasDefinition = SchemasDefinitionCreator
                .createExtendedSchemasDefinitionForPDFA_2_3(
                        mainPackageExtensionNode, this.isClosedChoiceCheck);
        this.flavour = flavour;
    }

    /**
     * @param link
     *            name of the link
     * @return List of all objects with link name
     */
    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
        case PROPERTIES:
            return this.getXMPProperties();
        case EXTENSION_SCHEMAS_CONTAINERS:
            return this.getExtensionSchemasContainers();
        default:
            return super.getLinkedObjects(link);
        }
    }

    private List<AXLExtensionSchemasContainer> getExtensionSchemasContainers() {
        if (this.xmpMetadata != null
                && this.xmpMetadata.getExtensionSchemasNode() != null) {
            List<AXLExtensionSchemasContainer> res = new ArrayList<>(1);
            res.add(new AXLExtensionSchemasContainer(this.getXmpMetadata()
                    .getExtensionSchemasNode(), getCurrentSchemasDefinitionPDFA_1(),
                    getCurrentSchemasDefinitionPDFA_2_3(),
                    this.flavour));
            return Collections.unmodifiableList(res);
        }

        return Collections.emptyList();
    }

    protected List<AXLXMPProperty> getXMPProperties() {
        if (this.getXmpMetadata() != null) {
            List<VeraPDFXMPNode> properties = this.xmpMetadata.getProperties();
            List<AXLXMPProperty> res = new ArrayList<>(properties.size());
            for (VeraPDFXMPNode node : properties) {
                res.add(createProperty(node));
            }
            return Collections.unmodifiableList(res);
        }

        return Collections.emptyList();
    }

    private AXLXMPProperty createProperty(VeraPDFXMPNode node) {
        String namespaceURI = node.getNamespaceURI();
        if (XMPConst.NS_XMP_MM.equals(namespaceURI)
                && "History".equals(node.getName())) {
            return new AXLXMPMMHistoryProperty(node, this.isMainMetadata,
                    this.isClosedChoiceCheck,
                    this.getMainPackageSchemasDefinitionForNS(namespaceURI),
                    this.getCurrentSchemasDefinitionPDFA_1ForNS(namespaceURI),
                    this.getCurrentSchemasDefinitionPDFA_2_3ForNS(namespaceURI),
                    this.flavour);
        }
        if (node.isLanguageAlternative()) {
            return new AXLXMPLangAlt(node, this.isMainMetadata,
                    this.isClosedChoiceCheck,
                    this.getMainPackageSchemasDefinitionForNS(namespaceURI),
                    this.getCurrentSchemasDefinitionPDFA_1ForNS(namespaceURI),
                    this.getCurrentSchemasDefinitionPDFA_2_3ForNS(namespaceURI),
                    this.flavour);
        }
        return new AXLXMPProperty(node, this.isMainMetadata,
                this.isClosedChoiceCheck,
                this.getMainPackageSchemasDefinitionForNS(namespaceURI),
                this.getCurrentSchemasDefinitionPDFA_1ForNS(namespaceURI),
                this.getCurrentSchemasDefinitionPDFA_2_3ForNS(namespaceURI),
                this.flavour);
    }

    protected VeraPDFMeta getXmpMetadata() {
        return this.xmpMetadata;
    }

    @Override
    public Boolean getisSerializationValid() {
        return Boolean.valueOf(this.isSerializationValid);
    }

    //------------------------------------------------------------------------------ veraPDF: getter method for actual encoding
    @Override
    public String getactualEncoding() {
        return this.actualEncoding;
    }

    @Override
    public String getbytes() {
        return getAttributeByRegexp(BYTES_REGEXP);
    }

    @Override
    public String getencoding() {
        return getAttributeByRegexp(ENCODING_REGEXP);
    }

    private String getAttributeByRegexp(String regexp) {
        VeraPDFMeta xmpMetadataLocal = this.getXmpMetadata();
        if (xmpMetadataLocal == null) {
            return null;
        }
        String packetHeader = xmpMetadataLocal.getPacketHeader();
        if (packetHeader == null || packetHeader.isEmpty()) {
            return null;
        }
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(packetHeader);
        if (m.find()) {
            String attr = m.group();
            int sq = attr.indexOf("'");
            int dq = attr.indexOf("\"");
            int min = Math.min(sq, dq);
            int index = min == -1 ? Math.max(sq, dq) : min;
            return attr.substring(index + 1, attr.length() - 1);
        }
        return null;
    }

    protected Map<String, SchemasDefinition> getMainPackageSchemasDefinition() {
        if (this.mainPackageSchemasDefinition == null) {
            this.mainPackageSchemasDefinition = SchemasDefinitionCreator.EMPTY_SCHEMAS_DEFINITION;
        }
        return this.mainPackageSchemasDefinition;
    }

    protected Map<String, SchemasDefinition> getCurrentSchemasDefinitionPDFA_1() {
        if (this.currentSchemasDefinitionPDFA_1 == null) {
            if (this.xmpMetadata != null
                    && this.xmpMetadata.getExtensionSchemasNode() != null) {
                this.currentSchemasDefinitionPDFA_1 = SchemasDefinitionCreator
                        .createExtendedSchemasDefinitionForPDFA_1(
                                this.xmpMetadata.getExtensionSchemasNode(),
                                this.isClosedChoiceCheck);
            } else {
                this.currentSchemasDefinitionPDFA_1 = SchemasDefinitionCreator.EMPTY_SCHEMAS_DEFINITION;
            }
        }
        return this.currentSchemasDefinitionPDFA_1;
    }

    protected Map<String, SchemasDefinition> getCurrentSchemasDefinitionPDFA_2_3() {
        if (this.currentSchemasDefinitionPDFA_2_3 == null) {
            if (this.xmpMetadata != null
                    && this.xmpMetadata.getExtensionSchemasNode() != null) {
                this.currentSchemasDefinitionPDFA_2_3 = isMainMetadata ?
                        SchemasDefinitionCreator.createExtendedSchemasDefinitionForPDFA_2_3(
                                this.xmpMetadata.getExtensionSchemasNode(),
                                this.isClosedChoiceCheck) :
                        SchemasDefinitionCreator.extendSchemasDefinitionForPDFA_2_3(
                                getMainPackageSchemasDefinition(),
                                this.xmpMetadata.getExtensionSchemasNode(),
                                this.isClosedChoiceCheck
                        );
            } else {
                this.currentSchemasDefinitionPDFA_2_3 = SchemasDefinitionCreator.EMPTY_SCHEMAS_DEFINITION;
            }
        }
        return this.currentSchemasDefinitionPDFA_2_3;
    }

    protected SchemasDefinition getMainPackageSchemasDefinitionForNS(String nameSpace) {
        SchemasDefinition schemasDefinition = getMainPackageSchemasDefinition().get(nameSpace);
        return schemasDefinition == null ? SchemasDefinitionCreator.EMPTY_SCHEMA_DEFINITION : schemasDefinition;
    }

    protected SchemasDefinition getCurrentSchemasDefinitionPDFA_1ForNS(String nameSpace) {
        SchemasDefinition schemasDefinition = getCurrentSchemasDefinitionPDFA_1().get(nameSpace);
        return schemasDefinition == null ? SchemasDefinitionCreator.EMPTY_SCHEMA_DEFINITION : schemasDefinition;
    }

    protected SchemasDefinition getCurrentSchemasDefinitionPDFA_2_3ForNS(String nameSpace) {
        SchemasDefinition schemasDefinition = getCurrentSchemasDefinitionPDFA_2_3().get(nameSpace);
        return schemasDefinition == null ? SchemasDefinitionCreator.EMPTY_SCHEMA_DEFINITION : schemasDefinition;
    }
}
