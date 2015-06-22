package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.PDFAFieldSchema;

/**
 * Current class is representation of PDFAFieldSchema interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/22/15.
 *
 * @author Maksim Bezrukov
 */
public class PBPDFAFieldSchema extends PBXMPSchema implements PDFAFieldSchema {

    /**
     * Constructs new object
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBPDFAFieldSchema(XMPSchema xmpSchema) {
        super(xmpSchema);
        setType("PDFAFieldSchema");
    }

}
