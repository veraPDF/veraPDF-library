package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPageTextSchema;
import org.verapdf.model.xmplayer.XMPTPgSchema;

/**
 * Current class is representation of XMPTPgSchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPTPgSchema extends PBXMPPredefinedSchema implements XMPTPgSchema {

    private static final String XMP_TPG_SCHEMA = "XMPTPgSchema";

    private static final String COLORANTS = "Colorants";
    private static final String FONTS = "Fonts";
    private static final String PLATE_NAMES = "PlateNames";

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBXMPTPgSchema(XMPageTextSchema xmpSchema) {
        super(xmpSchema, XMP_TPG_SCHEMA);
    }

    /**
     * @return property Colorants of the schema
     */
    @Override
    public String getcolorants() {
        return getXmpSchema().getProperty(COLORANTS) == null ? null : getXmpSchema().getProperty(COLORANTS).toString();
    }

    /**
     * @return property Fonts of the schema
     */
    @Override
    public String getfonts() {
        return getXmpSchema().getProperty(FONTS) == null ? null : getXmpSchema().getProperty(FONTS).toString();
    }

    /**
     * @return property PlateNames of the schema
     */
    @Override
    public String getplateNames() {
        return getXmpSchema().getProperty(PLATE_NAMES) == null ? null : getXmpSchema().getProperty(PLATE_NAMES).toString();
    }
}
