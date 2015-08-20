package org.verapdf.model.impl.pb.xmp.schemas;

import org.verapdf.model.xmplayer.XMPCustomSchema;

/**
 * Current class is representation of XMPSchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPCustomSchema extends PBXMPSchema implements XMPCustomSchema {

    private static final String XMP_CUSTOM_SCHEMA = "XMPCustomSchema";

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBXMPCustomSchema(org.apache.xmpbox.schema.XMPSchema xmpSchema) {
        super(xmpSchema, XMP_CUSTOM_SCHEMA);
    }

    /**
     * @return true if definition of this custom schema is correct
     */
    @Override
    public Boolean getisDefinitionCorrect() {
        // TODO: implement this method
        return Boolean.TRUE;
    }

    /**
     * @return true if the definition of this custom schema contains at the same xmp package as this schema
     */
    @Override
    public Boolean getisDefinedInThisPackage() {
        // TODO: implement this method
        return Boolean.TRUE;
    }

}
