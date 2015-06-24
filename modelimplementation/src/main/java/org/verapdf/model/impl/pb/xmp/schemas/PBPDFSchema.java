package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.AdobePDFSchema;
import org.verapdf.model.xmplayer.PDFSchema;

/**
 * Current class is representation of PDFSchema interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/22/15.
 *
 * @author Maksim Bezrukov
 */
public class PBPDFSchema extends PBXMPSchema implements PDFSchema {

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
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBPDFSchema(AdobePDFSchema xmpSchema) {
        super(xmpSchema);
        setType("PDFSchema");
    }

    /**
     * @return Author part of the schema
     */
    @Override
    public String getauthor() {
        return xmpSchema.getProperty(AUTHOR) == null ? null : xmpSchema.getProperty(AUTHOR).toString();
    }

    /**
     * @return BaseURL part of the schema
     */
    @Override
    public String getbaseURL() {
        return xmpSchema.getProperty(BASE_URL) == null ? null : xmpSchema.getProperty(BASE_URL).toString();
    }

    /**
     * @return CreationDate part of the schema
     */
    @Override
    public String getcreationDate() {
        return xmpSchema.getProperty(CREATION_DATE) == null ? null : xmpSchema.getProperty(CREATION_DATE).toString();
    }

    /**
     * @return Creator part of the schema
     */
    @Override
    public String getcreator() {
        return xmpSchema.getProperty(CREATOR) == null ? null : xmpSchema.getProperty(CREATOR).toString();
    }

    /**
     * @return ModDate part of the schema
     */
    @Override
    public String getmodDate() {
        return xmpSchema.getProperty(MOD_DATE) == null ? null : xmpSchema.getProperty(MOD_DATE).toString();
    }

    /**
     * @return Subject part of the schema
     */
    @Override
    public String getsubject() {
        return xmpSchema.getProperty(SUBJECT) == null ? null : xmpSchema.getProperty(SUBJECT).toString();
    }

    /**
     * @return Title part of the schema
     */
    @Override
    public String gettitle() {
        return xmpSchema.getProperty(TITLE) == null ? null : xmpSchema.getProperty(TITLE).toString();
    }

    /**
     * @return Trapped part of the schema
     */
    @Override
    public String gettrapped() {
        return xmpSchema.getProperty(TRAPPED) == null ? null : xmpSchema.getProperty(TRAPPED).toString();
    }
}
