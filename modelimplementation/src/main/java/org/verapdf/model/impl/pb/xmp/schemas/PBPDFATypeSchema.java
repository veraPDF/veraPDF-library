package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.PDFATypeSchema;

/**
 * Current class is representation of PDFATypeSchema interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/22/15.
 *
 * @author Maksim Bezrukov
 */
public class PBPDFATypeSchema extends PBXMPSchema implements PDFATypeSchema {

    /**
     * Constructs new object
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBPDFATypeSchema(XMPSchema xmpSchema) {
        super(xmpSchema);
        setType("PDFATypeSchema");
    }

}
