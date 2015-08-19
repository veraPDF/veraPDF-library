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

    public static final String PDF_AID_SCHEMA = "PDFAIdSchema";

    private static final String CORR = "corr";

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBPDFAIdSchema(PDFAIdentificationSchema xmpSchema) {
        super(xmpSchema, PDF_AID_SCHEMA);
    }

    /**
     * @return property part of the schema
     */
    @Override
    public Long getpart() {
        return ((PDFAIdentificationSchema) getXmpSchema()).getPart() == null ? null : Long.valueOf(((PDFAIdentificationSchema) getXmpSchema()).getPart().longValue());
    }

    /**
     * @return property conformance of the schema
     */
    @Override
    public String getconformance() {
        return ((PDFAIdentificationSchema) getXmpSchema()).getConformance();
    }

    /**
     * @return property amd of the schema
     */
    @Override
    public String getamd() {
        return ((PDFAIdentificationSchema) getXmpSchema()).getAmd();
    }

    /**
     * @return property corr of the schema
     */
    @Override
    public String getcorr() {
        AbstractField corr = getXmpSchema().getProperty(CORR);

        if (corr instanceof TextType) {
            return ((TextType) corr).getStringValue();
        }
        return null;
    }

}
