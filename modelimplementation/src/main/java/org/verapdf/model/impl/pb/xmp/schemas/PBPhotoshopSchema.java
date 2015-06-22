package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.PhotoshopSchema;

/**
 * Current class is representation of PhotoshopSchema interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/22/15.
 *
 * @author Maksim Bezrukov
 */
public class PBPhotoshopSchema extends PBXMPSchema implements org.verapdf.model.xmplayer.PhotoshopSchema {

    private static final String AUTHOR = "Author";
    private static final String COPYRIGHT = "Copyright";
    private static final String TITLE = "Title";

    /**
     * Constructs new object
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBPhotoshopSchema(PhotoshopSchema xmpSchema) {
        super(xmpSchema);
        setType("PhotoshopSchema");
    }

    /**
     * @return property Author of the schema
     */
    @Override
    public String getauthor() {
        return xmpSchema.getProperty(AUTHOR) == null ? null : xmpSchema.getProperty(AUTHOR).toString();
    }

    /**
     * @return Copyright part of the schema
     */
    @Override
    public String getcopyright() {
        return xmpSchema.getProperty(COPYRIGHT) == null ? null : xmpSchema.getProperty(COPYRIGHT).toString();
    }

    /**
     * @return History part of the schema
     */
    @Override
    public String gethistory() {
        return ((PhotoshopSchema) xmpSchema).getHistory();
    }

    /**
     * @return Title part of the schema
     */
    @Override
    public String gettitle() {
        return xmpSchema.getProperty(TITLE) == null ? null : xmpSchema.getProperty(TITLE).toString();
    }
}
