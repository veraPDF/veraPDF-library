package org.verapdf.model.impl.axl;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import com.adobe.xmp.impl.VeraPDFXMPNode;
import com.adobe.xmp.impl.XMPSchemaRegistryImpl;
import org.apache.log4j.Logger;
import org.verapdf.model.xmplayer.PDFAIdentification;

/**
 * @author Maksim Bezrukov
 */
public class AXLPDFAIdentification extends AXLXMPObject implements PDFAIdentification {

    private static final Logger LOGGER = Logger
            .getLogger(AXLPDFAIdentification.class);

    public static final String PDFA_IDENTIFICATION = "PDFAIdentification";

    private final VeraPDFMeta metadata;

    public AXLPDFAIdentification(VeraPDFMeta metadata) {
        super(PDFA_IDENTIFICATION);
        this.metadata = metadata;
    }

    @Override
    public Long getpart() {
        try {
            Integer part = this.metadata.getIdentificationPart();
            return part == null ? null : Long.valueOf(part);
        } catch (XMPException e) {
            LOGGER.debug("Can not get identification part", e);
            return null;
        }
    }

    @Override
    public String getconformance() {
        try {
            return this.metadata.getIdentificationConformance();
        } catch (XMPException e) {
            LOGGER.debug("Can not get identification conformance", e);
            return null;
        }
    }

    @Override
    public String getpartPrefix() {
        VeraPDFXMPNode property = this.metadata.getProperty(XMPSchemaRegistryImpl.NS_PDFA_ID, "part");
        return property == null ? null : property.getPrefix();
    }

    @Override
    public String getconformancePrefix() {
        VeraPDFXMPNode property = this.metadata.getProperty(XMPSchemaRegistryImpl.NS_PDFA_ID, "conformance");
        return property == null ? null : property.getPrefix();
    }

    @Override
    public String getamdPrefix() {
        VeraPDFXMPNode property = this.metadata.getProperty(XMPSchemaRegistryImpl.NS_PDFA_ID, "amd");
        return property == null ? null : property.getPrefix();
    }

    @Override
    public String getcorrPrefix() {
        VeraPDFXMPNode property = this.metadata.getProperty(XMPSchemaRegistryImpl.NS_PDFA_ID, "corr");
        return property == null ? null : property.getPrefix();
    }
}
