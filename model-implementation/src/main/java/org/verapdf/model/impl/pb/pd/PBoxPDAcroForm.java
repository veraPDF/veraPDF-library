package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDAcroForm;
import org.verapdf.model.pdlayer.PDFormField;

import java.util.ArrayList;
import java.util.List;

/**
 * PDF interactive form
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDAcroForm extends PBoxPDObject implements PDAcroForm {

    public static final String FORM_FIELDS = "formFields";
    public static final String ACRO_FORM_TYPE = "PDAcroForm";

    public PBoxPDAcroForm(
            org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm simplePDObject) {
        super(simplePDObject);
        setType(ACRO_FORM_TYPE);
    }

    @Override
    public Boolean getNeedAppearances() {
        return Boolean
                .valueOf(((org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm) simplePDObject)
                        .getNeedAppearances());
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (FORM_FIELDS.equals(link)) {
            return getFormFields();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDFormField> getFormFields() {
        List<PDFormField> formFields = new ArrayList<>();
        List<PDField> fields = ((org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm) simplePDObject)
                .getFields();
        for (PDField field : fields) {
            formFields.add(new PBoxPDFormField(field));
        }
        return formFields;
    }
}
