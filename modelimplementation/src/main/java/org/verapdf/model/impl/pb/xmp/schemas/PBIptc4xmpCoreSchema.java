package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.Iptc4xmpCoreSchema;

/**
 * Current class is representation of Iptc4xmpCoreSchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBIptc4xmpCoreSchema extends PBXMPPredefinedSchema implements Iptc4xmpCoreSchema {

    private static final String IPTC4XMPCORESCHEMA = "Iptc4xmpCoreSchema";

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBIptc4xmpCoreSchema(XMPSchema xmpSchema) {
        super(xmpSchema);
        setType(IPTC4XMPCORESCHEMA);
    }

}
