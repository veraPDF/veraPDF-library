package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPRightsManagementSchema;
import org.verapdf.model.xmplayer.XMPRightsSchema;

/**
 * Current class is representation of XMPRightsSchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPRightsSchema extends PBXMPPredefinedSchema implements XMPRightsSchema {

    private static final String XMPRIGHTSSCHEMA = "XMPRightsSchema";

    private static final String COPYRIGHT = "Copyright";

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBXMPRightsSchema(XMPRightsManagementSchema xmpSchema) {
        super(xmpSchema);
        setType(XMPRIGHTSSCHEMA);
    }

    /**
     * @return property Copyright of the schema
     */
    @Override
    public String getcopyright() {
        return getXmpSchema().getProperty(COPYRIGHT) == null ? null : getXmpSchema().getProperty(COPYRIGHT).toString();
    }
}
