package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPMediaManagementSchema;
import org.verapdf.model.xmplayer.XMPMMSchema;

/**
 * Current class is representation of XMPMMSchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPMMSchema extends PBXMPPredefinedSchema implements XMPMMSchema {

    private static final String XMPMMSCHEMA = "XMPMMSchema";

    private static final String MANIFEST = "Manifest";

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBXMPMMSchema(XMPMediaManagementSchema xmpSchema) {
        super(xmpSchema);
        setType(XMPMMSCHEMA);
    }

    /**
     * @return property Manifest of the schema
     */
    @Override
    public String getmanifest() {
        return xmpSchema.getProperty(MANIFEST) == null ? null : xmpSchema.getProperty(MANIFEST).toString();
    }
}
