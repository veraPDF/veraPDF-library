package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPBasicSchema;

/**
 * Current class is representation of XMPBasicSchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPBasicSchema extends PBXMPPredefinedSchema implements org.verapdf.model.xmplayer.XMPBasicSchema {

    private static final String XMPBASICSCHEMA = "XMPBasicSchema";

    private static final String AUTHOR = "Author";
    private static final String DESCRIPTION = "Description";
    private static final String TITLE = "Title";

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBXMPBasicSchema(org.apache.xmpbox.schema.XMPBasicSchema xmpSchema) {
        super(xmpSchema);
        setType(XMPBASICSCHEMA);
    }

    /**
     * @return property Author of the schema
     */
    @Override
    public String getauthor() {
        return getXmpSchema().getProperty(AUTHOR) == null ? null : getXmpSchema().getProperty(AUTHOR).toString();
    }

    /**
     * @return property Description of the schema
     */
    @Override
    public String getdescription() {
        return getXmpSchema().getProperty(DESCRIPTION) == null ? null : getXmpSchema().getProperty(DESCRIPTION).toString();
    }

    /**
     * @return property Label of the schema
     */
    @Override
    public String getlabel() {
        return ((XMPBasicSchema) getXmpSchema()).getLabel();
    }

    /**
     * @return property Rating of the schema
     */
    @Override
    public Long getrating() {
        return ((XMPBasicSchema) getXmpSchema()).getRating() == null ? null : Long.valueOf(((XMPBasicSchema) getXmpSchema()).getRating().longValue());
    }

    /**
     * @return property Title of the schema
     */
    @Override
    public String gettitle() {
        return getXmpSchema().getProperty(TITLE) == null ? null : getXmpSchema().getProperty(TITLE).toString();
    }
}
