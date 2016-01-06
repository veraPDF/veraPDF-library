package org.verapdf.model.impl.axl;

import com.adobe.xmp.impl.VeraPDFMeta;
import com.adobe.xmp.impl.VeraPDFXMPNode;
import com.adobe.xmp.impl.XMPSchemaRegistryImpl;
import org.apache.log4j.Logger;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.xmplayer.MainXMPPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Current class is representation of XMPPackage interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class AXLMainXMPPackage extends AXLXMPPackage implements MainXMPPackage {
    private static final Logger LOGGER = Logger
            .getLogger(AXLXMPPackage.class);

    public static final String MAIN_XMP_PACKAGE_TYPE = "MainXMPPackage";

    public static final String IDENTIFICATION = "Identification";

    /**
     * Constructs new object
     *
     * @param xmpMetadata          object from xmpbox represented this package
     * @param isSerializationValid true if metadata is valid
     */
    public AXLMainXMPPackage(VeraPDFMeta xmpMetadata, boolean isSerializationValid) {
        super(xmpMetadata, isSerializationValid, true, null, MAIN_XMP_PACKAGE_TYPE);
    }

    /**
     * @param link name of the link
     * @return List of all objects with link name
     */
    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case IDENTIFICATION:
                return this.getIdentification();
            default:
                return super.getLinkedObjects(link);
        }
    }

    private List<AXLPDFAIdentification> getIdentification() {
        List<AXLPDFAIdentification> res = new ArrayList<>();
        VeraPDFMeta xmpMetadata = this.getXmpMetadata();
        if (xmpMetadata != null) {
            res.add(new AXLPDFAIdentification(xmpMetadata));
        }
        return res;
    }

    @Override
    protected List<AXLXMPProperty> getXMPProperties() {
        if (this.getXmpMetadata() == null) {
            return new ArrayList<>();
        }
        List<VeraPDFXMPNode> properties = this.getXmpMetadata().getProperties();
        List<AXLXMPProperty> res = new ArrayList<>(properties.size());
        for (VeraPDFXMPNode node : properties) {
            if (XMPSchemaRegistryImpl.NS_PDFA_ID.equals(node.getNamespaceURI()) && ("part".equals(node.getName()) || "conformance".equals(node.getName()))) {
                continue;
            }
            res.add(new AXLXMPProperty(node, true, this.getMainPackageSchemasDefinition(), this.getCurrentSchemasDefinitionPDFA_1(), this.getCurrentSchemasDefinitionPDFA_2_3()));
        }
        return res;
    }
}
