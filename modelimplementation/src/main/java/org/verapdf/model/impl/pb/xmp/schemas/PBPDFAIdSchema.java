package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.verapdf.model.xmplayer.PDFAIdSchema;

/**
 * Current class is representation of PDFAIdSchema interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/19/15.
 *
 * @author Maksim Bezrukov
 */
public class PBPDFAIdSchema extends PBXMPSchema implements PDFAIdSchema {

    private static final String CORR  ="corr";

    /**
     * Constructs new object
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBPDFAIdSchema(PDFAIdentificationSchema xmpSchema) {
        super(xmpSchema);
        setType("PDFAIdSchema");
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
        return xmpSchema.getProperty(CORR).toString();
    }

}
