package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.CRSSchema;

/**
 * Current class is representation of CRSSchema interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/22/15.
 *
 * @author Maksim Bezrukov
 */
public class PBCRSSchema extends PBXMPSchema implements CRSSchema {

    /**
     * Constructs new object
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBCRSSchema(XMPSchema xmpSchema) {
        super(xmpSchema);
        setType("CRSSchema");
    }

}
