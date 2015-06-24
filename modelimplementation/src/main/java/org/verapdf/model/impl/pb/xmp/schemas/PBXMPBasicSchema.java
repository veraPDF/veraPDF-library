package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPBasicSchema;

/**
 * Current class is representation of XMPBasicSchema interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/22/15.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPBasicSchema extends PBXMPSchema implements org.verapdf.model.xmplayer.XMPBasicSchema {

    private static final String AUTHOR = "Author";
    private static final String DESCRIPTION = "Description";
    private static final String TITLE = "Title";

    /**
     * Constructs new object
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBXMPBasicSchema(org.apache.xmpbox.schema.XMPBasicSchema xmpSchema) {
        super(xmpSchema);
        setType("XMPBasicSchema");
    }

    /**
     * @return property Author of the schema
     */
    @Override
    public String getauthor() {
        return xmpSchema.getProperty(AUTHOR) == null ? null : xmpSchema.getProperty(AUTHOR).toString();
    }

    /**
     * @return property Description of the schema
     */
    @Override
    public String getdescription() {
        return xmpSchema.getProperty(DESCRIPTION) == null ? null : xmpSchema.getProperty(DESCRIPTION).toString();
    }

    /**
     * @return property Label of the schema
     */
    @Override
    public String getlabel() {
        return ((XMPBasicSchema) xmpSchema).getLabel();
    }

    /**
     * @return property Rating of the schema
     */
    @Override
    public Long getrating() {
        return ((XMPBasicSchema) xmpSchema).getRating() == null ? null : Long.valueOf(((XMPBasicSchema) xmpSchema).getRating());
    }

    /**
     * @return property Title of the schema
     */
    @Override
    public String gettitle() {
        return xmpSchema.getProperty(TITLE) == null ? null : xmpSchema.getProperty(TITLE).toString();
    }
}
