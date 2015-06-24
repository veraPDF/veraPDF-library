package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.PDFXSchema;

/**
 * Current class is representation of PDFXSchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBPDFXSchema extends PBXMPPredefinedSchema implements PDFXSchema {

    private static final String PDFXSCHEMA = "PDFXSchema";

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBPDFXSchema(XMPSchema xmpSchema) {
        super(xmpSchema);
        setType(PDFXSCHEMA);
    }

}
