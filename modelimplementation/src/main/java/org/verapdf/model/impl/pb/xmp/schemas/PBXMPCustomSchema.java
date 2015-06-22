package org.verapdf.model.impl.pb.xmp.schemas;

import org.verapdf.model.xmplayer.XMPCustomSchema;

/**
 * Current class is representation of XMPSchema interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/19/15.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPCustomSchema extends PBXMPSchema implements XMPCustomSchema {

    private org.apache.xmpbox.schema.XMPSchema xmpSchema;

    /**
     * Constructs new object
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBXMPCustomSchema(org.apache.xmpbox.schema.XMPSchema xmpSchema) {
        super(xmpSchema);
        setType("XMPCustomSchema");
        this.xmpSchema = xmpSchema;
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

    /**
     * @return true if the definition of this custom schema contains at the main xmp package as this schema
     */
    @Override
    public Boolean getisDefinedInMainPackage() {
        // TODO: implement this method
        return Boolean.TRUE;
    }
}
