package org.verapdf.model.impl.pb.pd;

import org.verapdf.model.baselayer.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDAcroForm;

import java.util.ArrayList;
import java.util.List;

/**
 * PDF interactive form
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDAcroForm extends PBoxPDObject implements PDAcroForm{

    public PBoxPDAcroForm(org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm simplePDObject) {
        super(simplePDObject);
        setType("PDAcroForm");
    }

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		return new ArrayList<>();
	}

	// TODO : add support of all features
}
