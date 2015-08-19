package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.PhotoshopSchema;

/**
 * Current class is representation of PhotoshopSchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBPhotoshopSchema extends PBXMPPredefinedSchema implements org.verapdf.model.xmplayer.PhotoshopSchema {

    public static final String PHOTOSHOP_SCHEMA = "PhotoshopSchema";

    private static final String AUTHOR = "Author";
    private static final String COPYRIGHT = "Copyright";
    private static final String TITLE = "Title";

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBPhotoshopSchema(PhotoshopSchema xmpSchema) {
        super(xmpSchema, PHOTOSHOP_SCHEMA);
    }

    /**
     * @return property Author of the schema
     */
    @Override
    public String getauthor() {
        return getXmpSchema().getProperty(AUTHOR) == null ? null : getXmpSchema().getProperty(AUTHOR).toString();
    }

    /**
     * @return Copyright part of the schema
     */
    @Override
    public String getcopyright() {
        return getXmpSchema().getProperty(COPYRIGHT) == null ? null : getXmpSchema().getProperty(COPYRIGHT).toString();
    }

    /**
     * @return History part of the schema
     */
    @Override
    public String gethistory() {
        return ((PhotoshopSchema) getXmpSchema()).getHistory();
    }

    /**
     * @return Title part of the schema
     */
    @Override
    public String gettitle() {
        return getXmpSchema().getProperty(TITLE) == null ? null : getXmpSchema().getProperty(TITLE).toString();
    }
}
