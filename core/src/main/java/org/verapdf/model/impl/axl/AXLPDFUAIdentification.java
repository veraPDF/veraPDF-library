package org.verapdf.model.impl.axl;

import org.verapdf.xmp.XMPException;
import org.verapdf.xmp.impl.VeraPDFMeta;
import org.verapdf.model.xmplayer.PDFUAIdentification;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AXLPDFUAIdentification extends AXLXMPObject implements PDFUAIdentification {

    public static final String PDFUA_IDENTIFICATION = "PDFUAIdentification";

    private static final Logger LOGGER = Logger
            .getLogger(AXLPDFUAIdentification.class.getName());

    private final VeraPDFMeta metadata;

    public AXLPDFUAIdentification(VeraPDFMeta metadata) {
        super(PDFUA_IDENTIFICATION);
        this.metadata = metadata;
    }

    @Override
    public Long getpart() {
        try {
            Integer part = this.metadata.getUAIdentificationPart();
            return part == null ? null : Long.valueOf(part.longValue());
        } catch (XMPException e) {
            LOGGER.log(Level.FINE, "Can not get identification part", e);
            return null;
        }
    }
}
