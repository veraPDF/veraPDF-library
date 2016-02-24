package org.verapdf.model.impl.axl;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.tools.xmp.ValidatorsContainer;
import org.verapdf.model.xmplayer.ExtensionSchemaObject;

/**
 * @author Maksim Bezrukov
 */
public abstract class AXLExtensionSchemaObject extends AXLXMPObject implements ExtensionSchemaObject {

    protected VeraPDFXMPNode xmpNode;
    protected final ValidatorsContainer containerForPDFA_1;
    protected final ValidatorsContainer containerForPDFA_2_3;

    public AXLExtensionSchemaObject(String type, VeraPDFXMPNode xmpNode, ValidatorsContainer containerForPDFA_1, ValidatorsContainer containerForPDFA_2_3) {
        super(type);
        this.xmpNode = xmpNode;
        this.containerForPDFA_1 = containerForPDFA_1;
        this.containerForPDFA_2_3 = containerForPDFA_2_3;
    }

    @Override
    public abstract Boolean getcontainsUndefinedFields();

}
