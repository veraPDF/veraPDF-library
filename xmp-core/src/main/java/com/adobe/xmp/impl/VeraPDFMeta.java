package com.adobe.xmp.impl;

import com.adobe.xmp.*;
import com.adobe.xmp.options.ParseOptions;
import com.adobe.xmp.options.PropertyOptions;
import com.adobe.xmp.options.SerializeOptions;
import com.adobe.xmp.properties.XMPProperty;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class VeraPDFMeta {

    private XMPMetaImpl meta;
    private VeraPDFXMPNode extensionSchemasNode;
    private List<VeraPDFXMPNode> properties = new ArrayList<>();
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
                if (XMPMetaImpl.NS_PDFA_EXTENSION.equals(namespaceURI) && "schemas".equals(name)) {
                    this.extensionSchemasNode = VeraPDFXMPNode.fromXMPNode(xmpChild);
                } else {
                    this.properties.add(VeraPDFXMPNode.fromXMPNode(xmpChild));
                }
            }
        }
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
        XMPMetaFactory.serialize(veraPDFMeta.meta, out, new SerializeOptions().setUseCanonicalFormat(true));
    }

    public List<VeraPDFXMPNode> getProperties() {
        return Collections.unmodifiableList(properties);
    }

    public String getPacketHeader() {
        return packetHeader;
    }

    public VeraPDFXMPNode getExtensionSchemasNode() {
        return extensionSchemasNode;
    }

    public VeraPDFXMPNode getProperty(String namespaceURI, String name) {
        for (VeraPDFXMPNode node : properties) {
            String nodeNamespaceURI = node.getNamespaceURI();
            boolean isNamespaceURIEquals = namespaceURI == null ? nodeNamespaceURI == null : namespaceURI.equals(nodeNamespaceURI);
            String nodeName = node.getName();
            boolean isNameEquals = name == null ? nodeName == null : name.equals(nodeName);
            if (isNamespaceURIEquals && isNameEquals) {
                return node;
            }
        }
        return null;
    }

    public boolean deleteIdentificationSchema() {
        boolean isDeleted = false;
        XMPNode identificationRoot = null;
        for (Object child : this.meta.getRoot().getUnmodifiableChildren()) {
            XMPNode xmpNode = (XMPNode) child;
            if (XMPSchemaRegistryImpl.NS_PDFA_ID.equals(xmpNode.getName())) {
                identificationRoot = xmpNode;
                break;
            }
        }
        if (identificationRoot != null) {
            for (Object child : identificationRoot.getUnmodifiableChildren()) {
                XMPNode xmpNode = (XMPNode) child;
                this.meta.deleteProperty(XMPSchemaRegistryImpl.NS_PDFA_ID, xmpNode.getName());
                isDeleted = true;
            }
            update();
        }
        return isDeleted;
    }

    private VeraPDFMeta setSimpleTextProperty(String namespaceURI, String propertyName, String value) throws XMPException {
        if (value == null) {
            throw new IllegalArgumentException("Argument value can not be null");
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
            throw new XMPException("Requared property is not simple", XMPError.BADVALUE);
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

    public Integer getIdentificationPart() throws XMPException {
        String stringValue = getSimpleTextProperty(XMPSchemaRegistryImpl.NS_PDFA_ID, "part");
        try {
            return stringValue == null ? null : Integer.parseInt(stringValue);
        } catch (NumberFormatException e) {
            throw new XMPException("Property part of PDFA Identification schema contains not integer value", XMPError.BADVALUE, e);
        }
    }

    public VeraPDFMeta setIdentificationPart(Integer identificationPart) throws XMPException {
        String value = identificationPart == null ? null : identificationPart.toString();
        return setSimpleTextProperty(XMPSchemaRegistryImpl.NS_PDFA_ID, "part", value);
    }

    public String getIdentificationConformance() throws XMPException {
        return getSimpleTextProperty(XMPSchemaRegistryImpl.NS_PDFA_ID, "conformance");
    }

    public VeraPDFMeta setIdentificationConformance(String identificationConformance) throws XMPException {
        return setSimpleTextProperty(XMPSchemaRegistryImpl.NS_PDFA_ID,  "conformance", identificationConformance);
    }

    public XMPMeta getCloneOfInitialMeta() {
        return (XMPMeta) meta.clone();
    }
}
