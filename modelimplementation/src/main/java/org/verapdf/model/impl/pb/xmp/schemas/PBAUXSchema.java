package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.AUXSchema;

/**
 * Current class is representation of AUXSchema interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/22/15.
 *
 * @author Maksim Bezrukov
 */
public class PBAUXSchema extends PBXMPSchema implements AUXSchema {

    /**
     * Constructs new object
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBAUXSchema(XMPSchema xmpSchema) {
        super(xmpSchema);
        setType("AUXSchema");
    }

}
