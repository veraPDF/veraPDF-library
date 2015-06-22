package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.StFntSchema;

/**
 * Current class is representation of StFntSchema interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/22/15.
 *
 * @author Maksim Bezrukov
 */
public class PBStFntSchema extends PBXMPSchema implements StFntSchema {

    /**
     * Constructs new object
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBStFntSchema(XMPSchema xmpSchema) {
        super(xmpSchema);
        setType("StFntSchema");
    }

}
