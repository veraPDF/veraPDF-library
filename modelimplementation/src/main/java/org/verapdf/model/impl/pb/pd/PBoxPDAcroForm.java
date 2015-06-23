package org.verapdf.model.impl.pb.pd;

import org.verapdf.model.pdlayer.PDAcroForm;

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
}
