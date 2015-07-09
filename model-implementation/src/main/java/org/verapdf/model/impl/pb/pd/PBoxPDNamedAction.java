package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.interactive.action.PDActionNamed;
import org.verapdf.model.pdlayer.PDNamedAction;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDNamedAction extends PBoxPDAction implements PDNamedAction{

	public PBoxPDNamedAction(PDActionNamed simplePDObject) {
		super(simplePDObject);
		setType("PDNamedAction");
	}

	@Override
	public String getN() {
		return ((PDActionNamed) simplePDObject).getN();
	}
}
