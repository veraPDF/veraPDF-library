package org.verapdf.model.impl.axl;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.xmplayer.ExtensionSchemaObject;

/**
 * @author Maksim Bezrukov
 */
public abstract class AXLExtensionSchemaObject extends AXLXMPObject implements ExtensionSchemaObject {

    protected VeraPDFXMPNode xmpNode;

    public AXLExtensionSchemaObject(String type, VeraPDFXMPNode xmpNode) {
        super(type);
        this.xmpNode = xmpNode;
    }

    @Override
    public abstract Boolean getisValueTypeCorrect();
}
