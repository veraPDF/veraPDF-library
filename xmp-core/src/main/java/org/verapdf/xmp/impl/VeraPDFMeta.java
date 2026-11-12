/*
 * Copyright (c) 2015-2026, veraPDF Consortium.
 * All rights reserved.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *
 * Redistribution and use are permitted under the BSD 3-Clause License.
 * See the LICENSE.txt file distributed with this module for the full
 * license terms and disclaimer.
 */

package org.verapdf.xmp.impl;

import org.verapdf.xmp.*;
import org.verapdf.xmp.options.ParseOptions;
import org.verapdf.xmp.impl.xpath.XMPPath;
import org.verapdf.xmp.impl.xpath.XMPPathParser;
import org.verapdf.xmp.options.PropertyOptions;
import org.verapdf.xmp.options.SerializeOptions;
import org.verapdf.xmp.properties.XMPProperty;

import java.io.*;
import java.util.*;

/**
 * @author Maksim Bezrukov
 */
public class VeraPDFMeta {

    public static final String PDFAID_PREFIX = "pdfaid";
    public static final String PDFUAID_PREFIX = "pdfuaid";
    public static final String PDFA_EXTENSION_PREFIX = "pdfaExtension";
    public static final String DC_PREFIX = "dc";
    public static final String PDFD_PREFIX = "pdfd";
    public static final String SCHEMAS = "schemas";
    public static final String CONFORMANCE = "conformance";
    public static final String PART = "part";
    public static final String REVISION_YEAR = "rev";
    public static final String CORR = "corr";
    public static final String AMD = "amd";
    public static final String DECLARATIONS = "declarations";
    public static final String CONFORMS_TO = "conformsTo";
    public static final String PDFA_DECLARATIONS = "http://pdfa.org/declarations/";
    public static final String WTPDF_ACCESSIBILITY_DECLARATION = "http://pdfa.org/declarations/wtpdf#accessibility1.0";
    public static final String WTPDF_REUSE_DECLARATION = "http://pdfa.org/declarations/wtpdf#reuse1.0";


    private final XMPMetaImpl meta;
    private VeraPDFXMPNode extensionSchemasNode;
    private final List<VeraPDFXMPNode> properties = new ArrayList<>();
    private String packetHeader;

    private VeraPDFMeta(XMPMetaImpl meta) {
        this.meta = meta;
    }

    private void update() {
        this.packetHeader = this.meta.getPacketHeader();
        this.extensionSchemasNode = null;
        this.properties.clear();
        XMPNode root = this.meta.getRoot();
        if (root != null) {
            List rootChildren = root.getUnmodifiableChildren();
            if (rootChildren != null) {
                for (Object schema : rootChildren) {
                    XMPNode xmpSchema = (XMPNode) schema;
                    String registeredPrefix = XMPMetaFactory.getSchemaRegistry()
                            .getNamespacePrefix(xmpSchema.getName());
                    String schemaPrefix = xmpSchema.getValue();
                    if (registeredPrefix != null && schemaPrefix != null
                            && !registeredPrefix.equals(schemaPrefix)) {
                        String normalizedPrefix = schemaPrefix.endsWith(":")
                                ? schemaPrefix.substring(0, schemaPrefix.length() - 1)
                                : schemaPrefix;
                        updateOriginalPrefix(xmpSchema, registeredPrefix, normalizedPrefix);
                    }
                    addSchema(xmpSchema);
                }
            }
        }
    }

    private void addSchema(XMPNode xmpSchema) {
        List children = xmpSchema.getUnmodifiableChildren();
        if (children != null) {
            XMPSchemaRegistry registry = XMPMetaFactory.getSchemaRegistry();
            for (Object child : children) {
                XMPNode xmpChild = (XMPNode) child;
                String originalName = xmpChild.getName();
                int prefixEndIndex = originalName.indexOf(":");
                String name = originalName.substring(prefixEndIndex + 1, originalName.length());
                String namespaceURI = registry.getNamespaceURI(originalName.substring(0, Math.max(prefixEndIndex, 0)));
                if (XMPMetaImpl.NS_PDFA_EXTENSION.equals(namespaceURI) && SCHEMAS.equals(name)) {
                    this.extensionSchemasNode = VeraPDFXMPNode.fromXMPNode(xmpChild);
                } else {
                    this.properties.add(VeraPDFXMPNode.fromXMPNode(xmpChild));
                }
            }
        }
    }
    
    public VeraPDFExtensionSchemasContainer getExtensionSchema() throws XMPException {
        final XMPPath expPath = XMPPathParser.expandXPath(XMPMetaImpl.NS_PDFA_EXTENSION, SCHEMAS);
        final XMPNode propNode = XMPNodeUtils.findNode(meta.getRoot(), expPath, false, null);
        return propNode != null ? new VeraPDFExtensionSchemasContainer(VeraPDFXMPNode.fromXMPNode(propNode)) : null;
    }
    
    public void createExtensionSchema() throws XMPException {
        XMPNode extension = new XMPNode(PDFA_EXTENSION_PREFIX + ":" + SCHEMAS, "", 
                new PropertyOptions(PropertyOptions.ARRAY), PDFA_EXTENSION_PREFIX);
        XMPNode node = new XMPNode(XMPConst.NS_PDFA_EXTENSION, PDFA_EXTENSION_PREFIX + ":", 
                new PropertyOptions(PropertyOptions.SCHEMA_NODE), null);
        node.addChild(extension);
        meta.getRoot().addChild(node);
    }

    public static VeraPDFMeta create() {
        XMPMetaImpl xmpMeta = (XMPMetaImpl) XMPMetaFactory.create();
        VeraPDFMeta node = new VeraPDFMeta(xmpMeta);
        node.update();
        return node;
    }

    public static VeraPDFMeta parse(InputStream in) throws XMPException {
        if (in == null) {
            throw new IllegalArgumentException("Metadata InputStream can not be null");
        }
        XMPMetaImpl xmpMeta = (XMPMetaImpl) XMPMetaFactory.parse(in, new ParseOptions().setOmitNormalization(true));
        VeraPDFMeta node = new VeraPDFMeta(xmpMeta);
        node.update();
        return node;
    }

    public static void serialize(VeraPDFMeta veraPDFMeta, OutputStream out) throws XMPException {
        XMPMetaFactory.serialize(getMetadataForSerialization(veraPDFMeta.meta), out,
                new SerializeOptions().setUseCanonicalFormat(true));
    }

    private static XMPMetaImpl getMetadataForSerialization(XMPMetaImpl metadata) {
        XMPSchemaRegistry registry = XMPMetaFactory.getSchemaRegistry();
        XMPNode root = metadata.getRoot();
        List schemas = root.getUnmodifiableChildren();
        boolean hasCustomPrefix = false;
        if (schemas != null) {
            for (Object schema : schemas) {
                XMPNode schemaNode = (XMPNode) schema;
                String registeredPrefix = registry.getNamespacePrefix(schemaNode.getName());
                if (registeredPrefix != null && !registeredPrefix.equals(schemaNode.getValue())) {
                    hasCustomPrefix = true;
                    break;
                }
            }
        }
        if (!hasCustomPrefix) {
            return metadata;
        }

        XMPMetaImpl serializedMetadata = (XMPMetaImpl) metadata.clone();
        serializedMetadata.setPacketHeader(metadata.getPacketHeader());
        List serializedSchemas = serializedMetadata.getRoot().getUnmodifiableChildren();
        if (serializedSchemas != null) {
            for (Object schema : serializedSchemas) {
                XMPNode schemaNode = (XMPNode) schema;
                String registeredPrefix = registry.getNamespacePrefix(schemaNode.getName());
                String requestedPrefix = schemaNode.getValue();
                if (registeredPrefix != null && requestedPrefix != null
                        && !registeredPrefix.equals(requestedPrefix)) {
                    renamePrefix(schemaNode, registeredPrefix, requestedPrefix);
                }
            }
        }
        return serializedMetadata;
    }

    private static void renamePrefix(XMPNode node, String oldPrefix, String newPrefix) {
        String oldQNamePrefix = oldPrefix.endsWith(":") ? oldPrefix : oldPrefix + ":";
        String newQNamePrefix = newPrefix.endsWith(":") ? newPrefix : newPrefix + ":";
        List children = node.getUnmodifiableChildren();
        if (children != null) {
            for (Object child : children) {
                XMPNode childNode = (XMPNode) child;
                renameNodePrefix(childNode, oldQNamePrefix, newQNamePrefix);
                renamePrefix(childNode, oldQNamePrefix, newQNamePrefix);
            }
        }
        for (Iterator qualifiers = node.iterateQualifier(); qualifiers.hasNext();) {
            XMPNode qualifier = (XMPNode) qualifiers.next();
            renameNodePrefix(qualifier, oldQNamePrefix, newQNamePrefix);
            renamePrefix(qualifier, oldQNamePrefix, newQNamePrefix);
        }
    }

    private static void renameNodePrefix(XMPNode node, String oldPrefix, String newPrefix) {
        String name = node.getName();
        if (name != null && name.startsWith(oldPrefix)) {
            node.setName(newPrefix + name.substring(oldPrefix.length()));
        }
    }

    public List<VeraPDFXMPNode> getProperties() {
        return Collections.unmodifiableList(properties);
    }

    public String getPacketHeader() {
        return packetHeader;
    }

    public String getActualEncoding() {
        return meta.getActualEncoding();
    }

    public VeraPDFXMPNode getExtensionSchemasNode() {
        return extensionSchemasNode;
    }

    public VeraPDFXMPNode getProperty(String namespaceURI, String name) {
        for (VeraPDFXMPNode node : properties) {
            String nodeNamespaceURI = node.getNamespaceURI();
            boolean isNamespaceURIEquals = Objects.equals(namespaceURI, nodeNamespaceURI);
            String nodeName = node.getName();
            boolean isNameEquals = Objects.equals(name, nodeName);
            if (isNamespaceURIEquals && isNameEquals) {
                return node;
            }
        }
        return null;
    }

    /**
     * Sets the namespace prefix for the given property. Because XMP stores a single prefix per
     * schema, this also updates all top-level properties in the namespace.
     *
     * @param namespaceURI the namespace URI containing the property
     * @param propertyName the property name
     * @param prefix the namespace prefix, with or without a trailing colon
     */
    public VeraPDFMeta setPropertyPrefix(String namespaceURI, String propertyName, String prefix) throws XMPException {
        ParameterAsserts.assertSchemaNS(namespaceURI);
        ParameterAsserts.assertPropName(propertyName);
        ParameterAsserts.assertPrefix(prefix);

        String normalizedPrefix = prefix.endsWith(":")
                ? prefix.substring(0, prefix.length() - 1)
                : prefix;
        if (normalizedPrefix.isEmpty() || normalizedPrefix.indexOf(':') >= 0 || "xmlns".equals(normalizedPrefix)) {
            throw new XMPException("Invalid namespace prefix", XMPError.BADXML);
        }
        if (!Utils.isXMLNameNS(normalizedPrefix)) {
            throw new XMPException("Invalid namespace prefix", XMPError.BADXML);
        }
        String requestedPrefix = normalizedPrefix + ":";

        XMPNode schemaNode = findSchemaNode(namespaceURI);
        XMPNode propertyNode = findSchemaProperty(schemaNode, propertyName);
        if (propertyNode == null) {
            throw new XMPException("Property " + propertyName + " does not exist in namespace " + namespaceURI,
                    XMPError.BADXPATH);
        }

        if (schemaNode == null || !schemaNode.getOptions().isSchemaNode()
                || !namespaceURI.equals(schemaNode.getName())) {
            throw new XMPException("Property " + propertyName + " is not in namespace " + namespaceURI,
                    XMPError.BADSCHEMA);
        }

        XMPSchemaRegistry registry = XMPMetaFactory.getSchemaRegistry();
        String existingPrefix = registry.getNamespacePrefix(namespaceURI);
        String registeredNamespace = registry.getNamespaceURI(normalizedPrefix);
        if (registeredNamespace != null && !namespaceURI.equals(registeredNamespace)) {
            throw new XMPException("Prefix " + normalizedPrefix + " is already registered for another namespace",
                    XMPError.BADSCHEMA);
        }
        List schemas = this.meta.getRoot().getUnmodifiableChildren();
        if (schemas != null) {
            for (Object schema : schemas) {
                XMPNode otherSchema = (XMPNode) schema;
                if (otherSchema != schemaNode && requestedPrefix.equals(otherSchema.getValue())) {
                    throw new XMPException("Prefix " + normalizedPrefix + " is already used by another namespace",
                            XMPError.BADSCHEMA);
                }
            }
        }

        schemaNode.setValue(requestedPrefix);
        updateOriginalPrefix(schemaNode, existingPrefix, normalizedPrefix);

        update();
        return this;
    }

    private XMPNode findSchemaNode(String namespaceURI) {
        List schemas = this.meta.getRoot().getUnmodifiableChildren();
        if (schemas != null) {
            for (Object schema : schemas) {
                XMPNode schemaNode = (XMPNode) schema;
                if (schemaNode.getOptions().isSchemaNode() && namespaceURI.equals(schemaNode.getName())) {
                    return schemaNode;
                }
            }
        }
        return null;
    }

    private static XMPNode findSchemaProperty(XMPNode schemaNode, String propertyName) {
        if (schemaNode == null || schemaNode.getUnmodifiableChildren() == null) {
            return null;
        }
        for (Object child : schemaNode.getUnmodifiableChildren()) {
            XMPNode property = (XMPNode) child;
            String name = property.getName();
            int prefixEndIndex = name.indexOf(':');
            String localName = name.substring(prefixEndIndex + 1);
            if (propertyName.equals(localName)) {
                return property;
            }
        }
        return null;
    }

    private static void updateOriginalPrefix(XMPNode node, String oldPrefix, String newPrefix) {
        if (oldPrefix != null) {
            renameOriginalPrefix(node, oldPrefix.endsWith(":") ? oldPrefix : oldPrefix + ":", newPrefix);
        }
    }

    private static void renameOriginalPrefix(XMPNode node, String prefix, String newPrefix) {
        String name = node.getName();
        if (name != null && name.startsWith(prefix)) {
            node.setOriginalPrefix(newPrefix);
        }
        List children = node.getUnmodifiableChildren();
        if (children != null) {
            for (Object child : children) {
                renameOriginalPrefix((XMPNode) child, prefix, newPrefix);
            }
        }
        for (Iterator qualifiers = node.iterateQualifier(); qualifiers.hasNext();) {
            renameOriginalPrefix((XMPNode) qualifiers.next(), prefix, newPrefix);
        }
    }

    public boolean deletePDFAIdentificationSchema() {
        return deleteSchema(XMPSchemaRegistryImpl.NS_PDFA_ID);
    }

    public boolean deletePDFUAIdentificationSchema() {
        return deleteSchema(XMPSchemaRegistryImpl.NS_PDFUA_ID);
    }

    private boolean deleteSchema(String schemaNS) {
        boolean isDeleted = false;
        XMPNode schemaRoot = null;
        for (Object child : this.meta.getRoot().getUnmodifiableChildren()) {
            XMPNode xmpNode = (XMPNode) child;
            if (schemaNS.equals(xmpNode.getName())) {
                schemaRoot = xmpNode;
                break;
            }
        }
        if (schemaRoot != null) {
            for (Object child : schemaRoot.getUnmodifiableChildren()) {
                XMPNode xmpNode = (XMPNode) child;
                this.meta.deleteProperty(schemaNS, xmpNode.getName());
                isDeleted = true;
            }
            update();
        }
        return isDeleted;
    }

    public boolean containsPropertiesFromNamespace(String nameSpaceURI) {
        for (VeraPDFXMPNode node : getProperties()) {
            if (nameSpaceURI.equals(node.getNamespaceURI())) {
                return true;
            }
        }
        return false;
    }

    private VeraPDFMeta setSimpleTextProperty(String namespaceURI, String propertyName, String value) throws XMPException {
        if (value == null) {
            this.meta.deleteProperty(namespaceURI, propertyName);
            return this;
        }
        XMPProperty property = this.meta.getProperty(namespaceURI, propertyName);
        if (property != null && !property.getOptions().isSimple()) {
            throw new XMPException("Can not set text value to not simple property", XMPError.BADVALUE);
        }
        this.meta.setProperty(namespaceURI, propertyName, value);
        update();
        return this;
    }

    private String getSimpleTextProperty(String namespaceURI, String propertyName) throws XMPException {
        XMPProperty property = this.meta.getProperty(namespaceURI, propertyName);
        if (property != null && !property.getOptions().isSimple()) {
            throw new XMPException("Required property is not simple", XMPError.BADVALUE);
        }
        return property == null ? null : property.getValue();
    }

    private VeraPDFMeta setDefaultAltTextPropertyForDCSchema(String propertyName, String defaultValue) throws XMPException {
        if (defaultValue == null) {
            throw new IllegalArgumentException("Argument defaultValue can not be null");
        }
        this.meta.setLocalizedText(XMPSchemaRegistryImpl.NS_DC, propertyName, "x", XMPConst.X_DEFAULT, defaultValue);
        update();
        return this;
    }

    private String getDefaultAltTextPropertyForDCSchema(String propertyName) throws XMPException {
        XMPProperty title = this.meta.getLocalizedText(XMPSchemaRegistryImpl.NS_DC, propertyName, "x", XMPConst.X_DEFAULT);
        return title == null ? null : title.getValue();
    }

    public String getTitle() throws XMPException {
        return getDefaultAltTextPropertyForDCSchema("title");
    }

    public VeraPDFMeta setTitle(String title) throws XMPException {
        return setDefaultAltTextPropertyForDCSchema("title", title);
    }

    public List<String> getCreator() throws XMPException {
        XMPProperty creator = this.meta.getProperty(XMPSchemaRegistryImpl.NS_DC, "creator");
        if (creator == null) {
            return null;
        }
        if (!creator.getOptions().isArrayOrdered()) {
            throw new XMPException("Creator property of dublin core schema is not an ordered array", XMPError.BADVALUE);
        }

        int size = this.meta.countArrayItems(XMPSchemaRegistryImpl.NS_DC, "creator");
        List<String> res = new ArrayList<>(size);
        for (int i = 1; i <= size; ++i) {
            XMPProperty item = this.meta.getArrayItem(XMPSchemaRegistryImpl.NS_DC, "creator", i);
            if (item.getOptions().isSimple()) {
                res.add(item.getValue());
            } else {
                throw new XMPException("Some entry in creator property of dublin core schema is not simple text", XMPError.BADVALUE);
            }
        }
        return res;
    }

    public VeraPDFMeta setCreator(List<String> creator) throws XMPException {
        if (creator == null) {
            throw new IllegalArgumentException("Argument creator can not be null");
        }
        this.meta.deleteProperty(XMPSchemaRegistryImpl.NS_DC, "creator");
        for (String entry : creator) {
            this.meta.appendArrayItem(XMPSchemaRegistryImpl.NS_DC, "creator", new PropertyOptions().setArrayOrdered(true),
                    entry, new PropertyOptions());
        }
        update();
        return this;
    }

    public String getDescription() throws XMPException {
        return getDefaultAltTextPropertyForDCSchema("description");
    }

    public VeraPDFMeta setDescription(String description) throws XMPException {
        return setDefaultAltTextPropertyForDCSchema("description", description);
    }

    public String getKeywords() throws XMPException {
        return getSimpleTextProperty(XMPSchemaRegistryImpl.NS_PDF, "Keywords");
    }

    public VeraPDFMeta setKeywords(String keywords) throws XMPException {
        return setSimpleTextProperty(XMPSchemaRegistryImpl.NS_PDF, "Keywords", keywords);
    }

    public String getCreatorTool() throws XMPException {
        return getSimpleTextProperty(XMPSchemaRegistryImpl.NS_XMP, "CreatorTool");
    }

    public VeraPDFMeta setCreatorTool(String creatorTool) throws XMPException {
        return setSimpleTextProperty(XMPSchemaRegistryImpl.NS_XMP, "CreatorTool", creatorTool);
    }

    public String getProducer() throws XMPException {
        return getSimpleTextProperty(XMPSchemaRegistryImpl.NS_PDF, "Producer");
    }

    public VeraPDFMeta setProducer(String producer) throws XMPException {
        return setSimpleTextProperty(XMPSchemaRegistryImpl.NS_PDF, "Producer", producer);
    }

    public Calendar getCreateDate() throws XMPException {
        String value = getSimpleTextProperty(XMPSchemaRegistryImpl.NS_XMP, "CreateDate");
        return value == null ? null : XMPDateTimeFactory.createFromISO8601(value).getCalendar();
    }

    public VeraPDFMeta setCreateDate(Calendar createDate) throws XMPException {
        if (createDate == null) {
            throw new IllegalArgumentException("Argument createDate can not be null");
        }
        XMPDateTime date = XMPDateTimeFactory.createFromCalendar(createDate);
        return setSimpleTextProperty(XMPSchemaRegistryImpl.NS_XMP, "CreateDate", date.getISO8601String());
    }

    public Calendar getModifyDate() throws XMPException {
        String value = getSimpleTextProperty(XMPSchemaRegistryImpl.NS_XMP, "ModifyDate");
        return value == null ? null : XMPDateTimeFactory.createFromISO8601(value).getCalendar();
    }

    public VeraPDFMeta setModifyDate(Calendar modifyDate) throws XMPException {
        if (modifyDate == null) {
            throw new IllegalArgumentException("Argument modifyDate can not be null");
        }
        XMPDateTime date = XMPDateTimeFactory.createFromCalendar(modifyDate);
        return setSimpleTextProperty(XMPSchemaRegistryImpl.NS_XMP, "ModifyDate", date.getISO8601String());
    }

    public Integer getPDFAIdentificationPart() throws XMPException {
        String stringValue = getSimpleTextProperty(XMPSchemaRegistryImpl.NS_PDFA_ID, PART);
        try {
            return stringValue == null ? null : Integer.parseInt(stringValue);
        } catch (NumberFormatException e) {
            throw new XMPException("Property part of PDF/A Identification schema contains not integer value", XMPError.BADVALUE, e);
        }
    }

    public Integer getPDFUAIdentificationPart() throws XMPException {
        String stringValue = getSimpleTextProperty(XMPSchemaRegistryImpl.NS_PDFUA_ID, PART);
        try {
            return stringValue == null ? null : Integer.parseInt(stringValue);
        } catch (NumberFormatException e) {
            throw new XMPException("Property part of PDF/UA Identification schema contains not integer value", XMPError.BADVALUE, e);
        }
    }

    public Set<String> getDeclarations() {
        Set<String> declarationsSet = new HashSet<>();
        VeraPDFXMPNode property = getProperty(PDFA_DECLARATIONS, DECLARATIONS);
        if (property != null) {
            List<VeraPDFXMPNode> declarations = property.getChildren();
            for (VeraPDFXMPNode declaration : declarations) {
                for (VeraPDFXMPNode node : declaration.getChildren()) {
                    if (CONFORMS_TO.equals(node.getName())) {
                        declarationsSet.add(node.getValue());
                    }
                }
            }
        }
        return declarationsSet;
    }

    public boolean containsDeclaration(String conformsTo) {
        VeraPDFXMPNode property = getProperty(PDFA_DECLARATIONS, DECLARATIONS);
        if (property != null) {
            List<VeraPDFXMPNode> declarations = property.getChildren();
            for (VeraPDFXMPNode declaration : declarations) {
                for (VeraPDFXMPNode node : declaration.getChildren()) {
                    if (CONFORMS_TO.equals(node.getName()) && conformsTo.equals(node.getValue())) {
                        return true;
                    }
                }
            } 
        }
        return false;
    }

    public VeraPDFMeta addDeclarations(Set<String> conformsToURIs) throws XMPException {
        if (conformsToURIs == null || conformsToURIs.isEmpty()) {
            throw new IllegalArgumentException("Argument conformsToURIs can not be null or empty");
        }
        if (getProperty(PDFA_DECLARATIONS, DECLARATIONS) == null) {
            meta.setProperty(PDFA_DECLARATIONS, DECLARATIONS, null,
                    new PropertyOptions().setArray(true));
        }

        for (String uri : conformsToURIs) {
            this.meta.appendArrayItem(
                    PDFA_DECLARATIONS,
                    DECLARATIONS,
                    null,
                    null,
                    new PropertyOptions().setStruct(true)
            );
            int idx = this.meta.countArrayItems(PDFA_DECLARATIONS, DECLARATIONS);
            String itemPath = XMPPathFactory.composeArrayItemPath(DECLARATIONS, idx);
            this.meta.setStructField(
                    PDFA_DECLARATIONS,
                    itemPath,
                    PDFA_DECLARATIONS,
                    CONFORMS_TO,
                    uri,
                    null
            );
        }

        update();
        return this;
    }

    public VeraPDFMeta setPDFAIdentificationPart(Integer identificationPart) throws XMPException {
        String value = identificationPart == null ? null : identificationPart.toString();
        return setSimpleTextProperty(XMPSchemaRegistryImpl.NS_PDFA_ID, PART, value);
    }

    public VeraPDFMeta setPDFUAIdentificationPart(Integer identificationPart) throws XMPException {
        String value = identificationPart == null ? null : identificationPart.toString();
        return setSimpleTextProperty(XMPSchemaRegistryImpl.NS_PDFUA_ID, PART, value);
    }

    public String getPDFAIdentificationConformance() throws XMPException {
        return getSimpleTextProperty(XMPSchemaRegistryImpl.NS_PDFA_ID, CONFORMANCE);
    }

    public String getPDFARevisionYear() throws XMPException {
        return getSimpleTextProperty(XMPSchemaRegistryImpl.NS_PDFA_ID, REVISION_YEAR);
    }

    public String getPDFUARevisionYear() throws XMPException {
        return getSimpleTextProperty(XMPSchemaRegistryImpl.NS_PDFUA_ID, REVISION_YEAR);
    }

    public VeraPDFMeta setPDFAIdentificationConformance(String identificationConformance) throws XMPException {
        return setSimpleTextProperty(XMPSchemaRegistryImpl.NS_PDFA_ID, CONFORMANCE, identificationConformance);
    }

    public VeraPDFMeta setIdentificationRevisionYear(String namespaceURI, String identificationConformance) throws XMPException {
        return setSimpleTextProperty(namespaceURI, REVISION_YEAR, identificationConformance);
    }

    public XMPMeta getCloneOfInitialMeta() {
        return (XMPMeta) meta.clone();
    }
}
