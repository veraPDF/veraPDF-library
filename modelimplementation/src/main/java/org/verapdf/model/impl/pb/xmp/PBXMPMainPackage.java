package org.verapdf.model.impl.pb.xmp;

import org.apache.xmpbox.XMPMetadata;
import org.verapdf.model.xmplayer.XMPMainPackage;

/**
 * Current class is representation of XMPPackage interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/12/15.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPMainPackage extends PBXMPPackage implements XMPMainPackage {

    /**
     * Constructs new object
     * @param xmpMetadata - object from xmpbox represented this package
     */
    public PBXMPMainPackage(XMPMetadata xmpMetadata) {
        super(xmpMetadata);
        setType("XMPMainPackage");
    }

    /**
     * @return true if pdfaid schema is present
     */
    @Override
    public Boolean getispdfaidNSPresent() {
        return xmpMetadata.getPDFIdentificationSchema() == null;
    }
}
