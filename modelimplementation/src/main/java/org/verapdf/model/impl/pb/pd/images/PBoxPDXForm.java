package org.verapdf.model.impl.pb.pd.images;

import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.verapdf.model.pdlayer.PDXForm;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDXForm extends PBoxPDXObject implements PDXForm {

    public PBoxPDXForm(PDFormXObject simplePDObject) {
        super(simplePDObject);
        setType("PDXForm");
    }

    // TODO : implement this
    @Override
    public String getSubtype2() {
        return null;
    }
}
