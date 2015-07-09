package org.verapdf.model.impl.pb.xmp;

import org.verapdf.model.GenericModelObject;
import org.verapdf.model.xmplayer.XMPObject;

/**
 * Current class is representation of XMPObject interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPObject extends GenericModelObject implements XMPObject {

    private static final String XMPOBJECT = "XMPObject";

    private String type = XMPOBJECT;

    /**
     * @return type of the object
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * @return id of the object
     */
    @Override
    public String getID() {
        return null;
    }

    protected void setType(String type) {
        this.type = type;
    }
}
