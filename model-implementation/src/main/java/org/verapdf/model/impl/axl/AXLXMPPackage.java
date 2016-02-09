package org.verapdf.model.impl.axl;

import com.adobe.xmp.impl.VeraPDFMeta;
import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.tools.xmp.SchemasDefinition;
import org.verapdf.model.tools.xmp.SchemasDefinitionCreator;
import org.verapdf.model.xmplayer.XMPPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Current class is representation of XMPPackage interface from
 * abstract model based on adobe xmp library
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
    private final boolean isMainMetadata;
    private final boolean isClosedChoiceCheck;
    private SchemasDefinition mainPackageSchemasDefinition;
    private SchemasDefinition currentSchemasDefinitionPDFA_1;
    private SchemasDefinition currentSchemasDefinitionPDFA_2_3;

    public AXLXMPPackage(VeraPDFMeta xmpMetadata, boolean isSerializationValid, boolean isClosedChoiceCheck, SchemasDefinition mainPackageSchemasDefinition) {
        this(xmpMetadata, isSerializationValid, false, isClosedChoiceCheck, mainPackageSchemasDefinition, XMP_PACKAGE_TYPE);
    }

    public AXLXMPPackage(VeraPDFMeta xmpMetadata, boolean isSerializationValid, SchemasDefinition mainPackageSchemasDefinition) {
        this(xmpMetadata, isSerializationValid, false, false, mainPackageSchemasDefinition, XMP_PACKAGE_TYPE);
    }

    protected AXLXMPPackage(VeraPDFMeta xmpMetadata, boolean isSerializationValid, boolean isMainMetadata, boolean isClosedChoiceCheck, SchemasDefinition mainPackageSchemasDefinition, final String type) {
        super(type);
        this.xmpMetadata = xmpMetadata;
        this.isSerializationValid = isSerializationValid;
        this.isMainMetadata = isMainMetadata;
        this.isClosedChoiceCheck = isClosedChoiceCheck;
        this.mainPackageSchemasDefinition = mainPackageSchemasDefinition;
    }

    /**
     * @param link name of the link
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
        if (this.xmpMetadata == null || this.xmpMetadata.getExtensionSchemasNode() == null) {
            return new ArrayList<>();
        }
        List<AXLExtensionSchemasContainer> res = new ArrayList<>(1);
        res.add(new AXLExtensionSchemasContainer(
                this.getXmpMetadata().getExtensionSchemasNode(),
                getCurrentSchemasDefinitionPDFA_1().getValidatorsContainer(),
                getCurrentSchemasDefinitionPDFA_2_3().getValidatorsContainer()
        ));
        return res;
    }

    protected List<AXLXMPProperty> getXMPProperties() {
        if (this.getXmpMetadata() == null) {
            return new ArrayList<>();
        }
        List<VeraPDFXMPNode> properties = this.xmpMetadata.getProperties();
        List<AXLXMPProperty> res = new ArrayList<>(properties.size());
        for (VeraPDFXMPNode node : properties) {
            res.add(new AXLXMPProperty(node, this.isMainMetadata, this.isClosedChoiceCheck, this.getMainPackageSchemasDefinition(), this.getCurrentSchemasDefinitionPDFA_1(), this.getCurrentSchemasDefinitionPDFA_2_3()));
        }
        return res;
    }

    protected VeraPDFMeta getXmpMetadata() {
        return this.xmpMetadata;
    }

    @Override
    public Boolean getisSerializationValid() {
        return Boolean.valueOf(this.isSerializationValid);
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
        VeraPDFMeta xmpMetadata = this.getXmpMetadata();
        if (xmpMetadata == null) {
            return null;
        }
        String packetHeader = xmpMetadata.getPacketHeader();
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
        } else {
            return null;
        }
    }

    protected SchemasDefinition getMainPackageSchemasDefinition() {
        if (this.mainPackageSchemasDefinition == null) {
            this.mainPackageSchemasDefinition = SchemasDefinitionCreator.EMPTY_SCHEMAS_DEFINITION;
        }
        return this.mainPackageSchemasDefinition;
    }

    protected SchemasDefinition getCurrentSchemasDefinitionPDFA_1() {
        if (this.currentSchemasDefinitionPDFA_1 == null) {
            if (this.xmpMetadata != null && this.xmpMetadata.getExtensionSchemasNode() != null) {
                this.currentSchemasDefinitionPDFA_1 = SchemasDefinitionCreator.createExtendedSchemasDefinitionForPDFA_1(this.xmpMetadata.getExtensionSchemasNode(), this.isClosedChoiceCheck);
            } else {
                this.currentSchemasDefinitionPDFA_1 = SchemasDefinitionCreator.EMPTY_SCHEMAS_DEFINITION;
            }
        }
        return this.currentSchemasDefinitionPDFA_1;
    }

    protected SchemasDefinition getCurrentSchemasDefinitionPDFA_2_3() {
        if (this.currentSchemasDefinitionPDFA_2_3 == null) {
            if (this.xmpMetadata != null && this.xmpMetadata.getExtensionSchemasNode() != null) {
                this.currentSchemasDefinitionPDFA_2_3 = SchemasDefinitionCreator.createExtendedSchemasDefinitionForPDFA_2_3(this.xmpMetadata.getExtensionSchemasNode(), this.isClosedChoiceCheck);
            } else {
                this.currentSchemasDefinitionPDFA_2_3 = SchemasDefinitionCreator.EMPTY_SCHEMAS_DEFINITION;
            }
        }
        return this.currentSchemasDefinitionPDFA_2_3;
    }
}
