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
    public static final String SCHEMAS = "schemas";
    public static final String CONFORMANCE = "conformance";
    public static final String PART = "part";
    public static final String REVISION_YEAR = "rev";
    public static final String CORR = "corr";
    public static final String AMD = "amd";
    public static final String DECLARATIONS = "declarations";
    public static final String CONFORMS_TO = "conformsTo";
    public static final String PDFA_DECLARATIONS = "http://pdfa.org/declarations/";

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
        XMPMetaFactory.serialize(veraPDFMeta.meta, out, new SerializeOptions().setUseCanonicalFormat(true));
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
