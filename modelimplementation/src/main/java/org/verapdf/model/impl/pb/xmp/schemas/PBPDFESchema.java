package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.PDFESchema;

/**
 * Current class is representation of PDFESchema interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/22/15.
 *
 * @author Maksim Bezrukov
 */
public class PBPDFESchema extends PBXMPSchema implements PDFESchema {

    /**
     * Constructs new object
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBPDFESchema(XMPSchema xmpSchema) {
        super(xmpSchema);
        setType("PDFESchema");
    }

}
