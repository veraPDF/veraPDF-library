package org.verapdf.model.impl.pb.pd.actions;

import org.apache.pdfbox.pdmodel.interactive.action.PDActionNamed;
import org.verapdf.model.pdlayer.PDNamedAction;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDNamedAction extends PBoxPDAction implements PDNamedAction {

    public static final String NAMED_ACTION_TYPE = "PDNamedAction";

    public PBoxPDNamedAction(PDActionNamed simplePDObject) {
        super(simplePDObject, NAMED_ACTION_TYPE);
    }

    @Override
    public String getN() {
        return ((PDActionNamed) this.simplePDObject).getN();
    }
}
