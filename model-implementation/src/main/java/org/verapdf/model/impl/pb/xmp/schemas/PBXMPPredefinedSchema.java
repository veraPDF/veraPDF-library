package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.XMPPredefinedSchema;

/**
 * Current class is representation of XMPPredefinedSchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPPredefinedSchema extends PBXMPSchema implements XMPPredefinedSchema {

    private static final String XMPPREDEFINEDSCHEMA = "XMPPredefinedSchema";

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBXMPPredefinedSchema(XMPSchema xmpSchema) {
        super(xmpSchema);
        setType(XMPPREDEFINEDSCHEMA);
    }

    /**
     * @return String that contains all unknown properties for predefined schema (XMP 2005)
     */
    @Override
    public String getunknownProperties() {
        // TODO: implement this
        return null;
    }

    /**
     * @return String that contains all wrong value type properties for predefined schema
     */
    @Override
    public String getwrongValueTypeProperties() {
        // TODO: implement this
        return null;
    }
}
