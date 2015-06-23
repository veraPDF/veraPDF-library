package org.verapdf.model.impl.pb.pd;

import org.verapdf.model.pdlayer.PDAction;

/**
 * PDF action object
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDAction extends PBoxPDObject implements PDAction {

    public PBoxPDAction(org.apache.pdfbox.pdmodel.interactive.action.PDAction simplePDObject) {
        super(simplePDObject);
        setType("PDAction");
    }

    /**
     * @return
     */
    @Override
    public String getSubtype() {
        return ((org.apache.pdfbox.pdmodel.interactive.action.PDAction) simplePDObject).getSubType();
    }
}
