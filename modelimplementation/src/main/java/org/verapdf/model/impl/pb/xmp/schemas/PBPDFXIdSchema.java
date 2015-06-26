package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.PDFXIdSchema;

/**
 * Current class is representation of PDFXIdSchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBPDFXIdSchema extends PBXMPPredefinedSchema implements PDFXIdSchema {

    private static final String PDFXIDSCHEMA = "PDFXIdSchema";

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBPDFXIdSchema(XMPSchema xmpSchema) {
        super(xmpSchema);
        setType(PDFXIDSCHEMA);
    }

}
