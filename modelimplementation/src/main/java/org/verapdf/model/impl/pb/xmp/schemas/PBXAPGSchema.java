package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.XAPGSchema;

/**
 * Current class is representation of XAPGSchema interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/22/15.
 *
 * @author Maksim Bezrukov
 */
public class PBXAPGSchema extends PBXMPSchema implements XAPGSchema {

    /**
     * Constructs new object
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBXAPGSchema(XMPSchema xmpSchema) {
        super(xmpSchema);
        setType("XAPGSchema");
    }

}
