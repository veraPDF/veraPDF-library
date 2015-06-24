package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.PDFASchemaSchema;

/**
 * Current class is representation of PDFASchemaSchema interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/22/15.
 *
 * @author Maksim Bezrukov
 */
public class PBPDFASchemaSchema extends PBXMPSchema implements PDFASchemaSchema {

    /**
     * Constructs new object
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBPDFASchemaSchema(XMPSchema xmpSchema) {
        super(xmpSchema);
        setType("PDFASchemaSchema");
    }

}
