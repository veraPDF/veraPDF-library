package org.verapdf.model.impl.pb.xmp.schemas;


import org.apache.xmpbox.schema.PDFAExtensionSchema;


/**
 * Current class is representation of PDFAExtensionSchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBPDFAExtensionSchema extends PBXMPPredefinedSchema implements org.verapdf.model.xmplayer.PDFAExtensionSchema {

    private static final String PDFAEXTENSIONSCHEMA = "PDFAExtensionSchema";

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBPDFAExtensionSchema(PDFAExtensionSchema xmpSchema) {
        super(xmpSchema);
        setType(PDFAEXTENSIONSCHEMA);
    }

    /**
     * @return true if pdfaField prefix is correct at all properties
     */
    @Override
    public Boolean getisPDFAFieldPrefixCorrect() {
        // TODO: implement this
        return Boolean.TRUE;
    }

    /**
     * @return true if pdfaProperty prefix is correct at all properties
     */
    @Override
    public Boolean getisPDFAPropertyPrefixCorrect() {
        // TODO: implement this
        return Boolean.TRUE;
    }

    /**
     * @return true if pdfaSchema prefix is correct at all properties
     */
    @Override
    public Boolean getisPDFASchemaPrefixCorrect() {
        // TODO: implement this
        return Boolean.TRUE;
    }

    /**
     * @return true if pdfaType prefix is correct at all properties
     */
    @Override
    public Boolean getisPDFATypePrefixCorrect() {
        // TODO: implement this
        return Boolean.TRUE;
    }
}
