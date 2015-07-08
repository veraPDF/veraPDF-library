package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDAcroForm;
import org.verapdf.model.pdlayer.PDFormField;

import java.lang.Boolean;
import java.lang.Override;
import java.util.ArrayList;
import java.util.List;

/**
 * PDF interactive form
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDAcroForm extends PBoxPDObject implements PDAcroForm{

	public static final String FORM_FIELDS = "formFields";

    public PBoxPDAcroForm(org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm simplePDObject) {
        super(simplePDObject);
        setType("PDAcroForm");
    }

	@Override
	public Boolean getNeedAppearances() {
		boolean isNeedAppearances = ((org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm) simplePDObject)
				.getNeedAppearances();
		return Boolean.valueOf(isNeedAppearances);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case FORM_FIELDS:
				list = getFormFields();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<PDFormField> getFormFields() {
		List<PDFormField> formFields = new ArrayList<>();
		List<PDField> fields = ((org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm) simplePDObject).getFields();
		for (PDField field : fields) {
			formFields.add(new PBoxPDFormField(field));
		}
		return formFields;
	}
}
