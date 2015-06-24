package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.PDFAPropertySchema;

/**
 * Current class is representation of PDFAPropertySchema interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/22/15.
 *
 * @author Maksim Bezrukov
 */
public class PBPDFAPropertySchema extends PBXMPSchema implements PDFAPropertySchema {

    /**
     * Constructs new object
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBPDFAPropertySchema(XMPSchema xmpSchema) {
        super(xmpSchema);
        setType("PDFAPropertySchema");
    }

}
