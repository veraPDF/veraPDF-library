package org.verapdf.model.impl.axl;

import com.adobe.xmp.impl.VeraPDFMeta;
import com.adobe.xmp.impl.VeraPDFXMPNode;
import com.adobe.xmp.impl.XMPSchemaRegistryImpl;
import org.apache.log4j.Logger;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.xmplayer.MainXMPPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Current class is representation of XMPPackage interface from abstract model based on adobe xmp library
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
     * @param xmpMetadata          object that represents this package
     * @param isSerializationValid true if metadata is valid
     */
    public AXLMainXMPPackage(VeraPDFMeta xmpMetadata, boolean isSerializationValid) {
        super(xmpMetadata, isSerializationValid, true, false, null, MAIN_XMP_PACKAGE_TYPE);
    }

    public AXLMainXMPPackage(VeraPDFMeta xmpMetadata, boolean isSerializationValid, boolean isClosedChoiceCheck) {
        super(xmpMetadata, isSerializationValid, true, isClosedChoiceCheck, null, MAIN_XMP_PACKAGE_TYPE);
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
        VeraPDFMeta xmpMetadata = this.getXmpMetadata();
        if (xmpMetadata != null) {
            for (VeraPDFXMPNode node : xmpMetadata.getProperties()) {
                if (XMPSchemaRegistryImpl.NS_PDFA_ID.equals(node.getNamespaceURI())) {
                    List<AXLPDFAIdentification> res = new ArrayList<>(1);
                    res.add(new AXLPDFAIdentification(xmpMetadata));
                    return res;
                }
            }
        }
        return Collections.emptyList();
    }
}
