package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.PDFESchema;

/**
 * Current class is representation of PDFESchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBPDFESchema extends PBXMPPredefinedSchema implements PDFESchema {

    private static final String PDFESCHEMA = "PDFESchema";

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBPDFESchema(XMPSchema xmpSchema) {
        super(xmpSchema);
        setType(PDFESCHEMA);
    }

}
