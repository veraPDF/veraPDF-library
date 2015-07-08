package org.verapdf.model.impl.pb.xmp;

import org.apache.xmpbox.XMPMetadata;
import org.verapdf.model.xmplayer.XMPMainPackage;

/**
 * Current class is representation of XMPPackage interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPMainPackage extends PBXMPPackage implements XMPMainPackage {

    private static final String XMPMAINPACKAGE = "XMPMainPackage";

    /**
     * Constructs new object
     *
     * @param xmpMetadata - object from xmpbox represented this package
     */
    public PBXMPMainPackage(XMPMetadata xmpMetadata) {
        super(xmpMetadata);
        setType(XMPMAINPACKAGE);
    }

    /**
     * @return true if pdfaid schema is present
     */
    @Override
    public Boolean getispdfaidNSPresent() {
        return getXmpMetadata().getPDFIdentificationSchema() == null;
    }
}
