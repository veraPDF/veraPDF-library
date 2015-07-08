package org.verapdf.model.impl.pb.pd;

import org.verapdf.model.pdlayer.PDGroup;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDGroup extends PBoxPDObject implements PDGroup {

	public PBoxPDGroup(org.apache.pdfbox.pdmodel.graphics.form.PDGroup simplePDObject) {
		super(simplePDObject);
		setType("PDGroup");
	}

	@Override
	public String getS() {
		return ((org.apache.pdfbox.pdmodel.graphics.form.PDGroup) simplePDObject).getSubType().getName();
	}
}
