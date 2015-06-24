package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.Iptc4xmpCoreSchema;

/**
 * Current class is representation of Iptc4xmpCoreSchema interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/22/15.
 *
 * @author Maksim Bezrukov
 */
public class PBIptc4xmpCoreSchema extends PBXMPSchema implements Iptc4xmpCoreSchema {

    /**
     * Constructs new object
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBIptc4xmpCoreSchema(XMPSchema xmpSchema) {
        super(xmpSchema);
        setType("Iptc4xmpCoreSchema");
    }

}
