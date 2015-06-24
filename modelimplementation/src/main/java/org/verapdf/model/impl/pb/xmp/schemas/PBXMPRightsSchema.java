package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPRightsManagementSchema;
import org.verapdf.model.xmplayer.XMPRightsSchema;

/**
 * Current class is representation of XMPRightsSchema interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/22/15.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPRightsSchema extends PBXMPSchema implements XMPRightsSchema {

    private static final String COPYRIGHT = "Copyright";

    /**
     * Constructs new object
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBXMPRightsSchema(XMPRightsManagementSchema xmpSchema) {
        super(xmpSchema);
        setType("XMPRightsSchema");
    }

    /**
     * @return property Copyright of the schema
     */
    @Override
    public String getcopyright() {
        return xmpSchema.getProperty(COPYRIGHT) == null ? null : xmpSchema.getProperty(COPYRIGHT).toString();
    }
}
