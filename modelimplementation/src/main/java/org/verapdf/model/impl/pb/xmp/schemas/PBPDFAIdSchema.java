package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.type.AbstractField;
import org.apache.xmpbox.type.TextType;
import org.verapdf.model.xmplayer.PDFAIdSchema;


/**
 * Current class is representation of PDFAIdSchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBPDFAIdSchema extends PBXMPPredefinedSchema implements PDFAIdSchema {

    private static final String PDFAIDSCHEMA = "PDFAIdSchema";

    private static final String CORR = "corr";

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBPDFAIdSchema(PDFAIdentificationSchema xmpSchema) {
        super(xmpSchema);
        setType(PDFAIDSCHEMA);
    }

    /**
     * @return property part of the schema
     */
    @Override
    public Long getpart() {
        return ((PDFAIdentificationSchema) xmpSchema).getPart() == null ? null : Long.valueOf(((PDFAIdentificationSchema) xmpSchema).getPart());
    }

    /**
     * @return property conformance of the schema
     */
    @Override
    public String getconformance() {
        return ((PDFAIdentificationSchema) xmpSchema).getConformance();
    }

    /**
     * @return property amd of the schema
     */
    @Override
    public String getamd() {
        return ((PDFAIdentificationSchema) xmpSchema).getAmd();
    }

    /**
     * @return property corr of the schema
     */
    @Override
    public String getcorr() {
        AbstractField corr = xmpSchema.getProperty(CORR);

        if (corr instanceof TextType) {
            return ((TextType) corr).getStringValue();
        } else {
            return null;
        }
    }

}
