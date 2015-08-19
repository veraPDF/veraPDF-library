package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.AdobePDFSchema;
import org.verapdf.model.xmplayer.PDFSchema;

/**
 * Current class is representation of PDFSchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBPDFSchema extends PBXMPPredefinedSchema implements PDFSchema {

    public static final String PDF_SCHEMA = "PDFSchema";

    private static final String AUTHOR = "Author";
    private static final String BASE_URL = "BaseURL";
    private static final String CREATION_DATE = "CreationDate";
    private static final String CREATOR = "Creator";
    private static final String MOD_DATE = "ModDate";
    private static final String SUBJECT = "Subject";
    private static final String TITLE = "Title";
    private static final String TRAPPED = "Trapped";

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBPDFSchema(AdobePDFSchema xmpSchema) {
        super(xmpSchema, PDF_SCHEMA);
    }

    /**
     * @return Author part of the schema
     */
    @Override
    public String getauthor() {
        return getXmpSchema().getProperty(AUTHOR) == null ? null : getXmpSchema().getProperty(AUTHOR).toString();
    }

    /**
     * @return BaseURL part of the schema
     */
    @Override
    public String getbaseURL() {
        return getXmpSchema().getProperty(BASE_URL) == null ? null : getXmpSchema().getProperty(BASE_URL).toString();
    }

    /**
     * @return CreationDate part of the schema
     */
    @Override
    public String getcreationDate() {
        return getXmpSchema().getProperty(CREATION_DATE) == null ? null : getXmpSchema().getProperty(CREATION_DATE).toString();
    }

    /**
     * @return Creator part of the schema
     */
    @Override
    public String getcreator() {
        return getXmpSchema().getProperty(CREATOR) == null ? null : getXmpSchema().getProperty(CREATOR).toString();
    }

    /**
     * @return ModDate part of the schema
     */
    @Override
    public String getmodDate() {
        return getXmpSchema().getProperty(MOD_DATE) == null ? null : getXmpSchema().getProperty(MOD_DATE).toString();
    }

    /**
     * @return Subject part of the schema
     */
    @Override
    public String getsubject() {
        return getXmpSchema().getProperty(SUBJECT) == null ? null : getXmpSchema().getProperty(SUBJECT).toString();
    }

    /**
     * @return Title part of the schema
     */
    @Override
    public String gettitle() {
        return getXmpSchema().getProperty(TITLE) == null ? null : getXmpSchema().getProperty(TITLE).toString();
    }

    /**
     * @return Trapped part of the schema
     */
    @Override
    public String gettrapped() {
        return getXmpSchema().getProperty(TRAPPED) == null ? null : getXmpSchema().getProperty(TRAPPED).toString();
    }
}
