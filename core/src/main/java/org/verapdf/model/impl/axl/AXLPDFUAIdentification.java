package org.verapdf.model.impl.axl;

import org.verapdf.xmp.XMPConst;
import org.verapdf.xmp.XMPException;
import org.verapdf.xmp.impl.VeraPDFMeta;
import org.verapdf.model.xmplayer.PDFUAIdentification;
import org.verapdf.xmp.impl.VeraPDFXMPNode;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AXLPDFUAIdentification extends AXLXMPObject implements PDFUAIdentification {

    public static final String PDFUA_IDENTIFICATION = "PDFUAIdentification";

    private static final Logger LOGGER = Logger.getLogger(AXLPDFUAIdentification.class.getName());

    private final VeraPDFMeta metadata;

    public AXLPDFUAIdentification(VeraPDFMeta metadata) {
        super(PDFUA_IDENTIFICATION);
        this.metadata = metadata;
    }

    @Override
    public Long getpart() {
        try {
            Integer part = this.metadata.getPDFUAIdentificationPart();
            return part == null ? null : Long.valueOf(part.longValue());
        } catch (XMPException e) {
            LOGGER.log(Level.FINE, "Can not get PDF/UA identification part", e);
            return null;
        }
    }

    @Override
    public String getrev() {
        try {
            return this.metadata.getPDFUARevisionYear();
        } catch (XMPException e) {
            LOGGER.log(Level.FINE, "Can not get PDF/UA identification revision year", e);
            return null;
        }
    }

    @Override
    public String getpartPrefix() {
        VeraPDFXMPNode property = this.metadata.getProperty(XMPConst.NS_PDFUA_ID, "part");
        return property == null ? null : property.getPrefix();
    }

    @Override
    public String getamdPrefix() {
        VeraPDFXMPNode property = this.metadata.getProperty(XMPConst.NS_PDFUA_ID, "amd");
        return property == null ? null : property.getPrefix();
    }

    @Override
    public String getcorrPrefix() {
        VeraPDFXMPNode property = this.metadata.getProperty(XMPConst.NS_PDFUA_ID, "corr");
        return property == null ? null : property.getPrefix();
    }

    @Override
    public String getrevPrefix() {
        VeraPDFXMPNode property = this.metadata.getProperty(XMPConst.NS_PDFUA_ID, "rev");
        return property == null ? null : property.getPrefix();
    }
}
