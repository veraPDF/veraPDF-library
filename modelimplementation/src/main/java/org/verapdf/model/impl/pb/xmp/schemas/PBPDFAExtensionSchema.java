package org.verapdf.model.impl.pb.xmp.schemas;


import org.apache.xmpbox.schema.PDFAExtensionSchema;

/**
 * Current class is representation of PDFAExtensionSchema interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/22/15.
 *
 * @author Maksim Bezrukov
 */
public class PBPDFAExtensionSchema extends PBXMPSchema implements org.verapdf.model.xmplayer.PDFAExtensionSchema {

    /**
     * Constructs new object
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBPDFAExtensionSchema(PDFAExtensionSchema xmpSchema) {
        super(xmpSchema);
        setType("PDFAExtensionSchema");
    }

}
