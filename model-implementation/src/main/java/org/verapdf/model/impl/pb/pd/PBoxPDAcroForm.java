package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.model.impl.pb.cos.PBCosArray;
import org.verapdf.model.impl.pb.cos.PBCosStream;
import org.verapdf.model.pdlayer.PDAcroForm;
import org.verapdf.model.pdlayer.PDFormField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PDF interactive form
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDAcroForm extends PBoxPDObject implements PDAcroForm {

	public static final String ACRO_FORM_TYPE = "PDAcroForm";

    public static final String FORM_FIELDS = "formFields";
	public static final String XFA = "XFA";

	private final boolean needAppearance;

    public PBoxPDAcroForm(
            org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm acroForm) {
        super(acroForm, ACRO_FORM_TYPE);
		this.needAppearance = acroForm.getNeedAppearances();
    }

    @Override
    public Boolean getNeedAppearances() {
        return Boolean.valueOf(this.needAppearance);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case FORM_FIELDS:
				return this.getFormFields();
			case XFA:
				return this.getXFA();
			default:
				return super.getLinkedObjects(link);
		}
    }

	private List<PDFormField> getFormFields() {
        List<PDField> fields = ((org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm) this.simplePDObject)
                .getFields();
		List<PDFormField> formFields =
				new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		for (PDField field : fields) {
			formFields.add(new PBoxPDFormField(field));
        }
		return Collections.unmodifiableList(formFields);
    }

	private List<CosObject> getXFA() {
		org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm form =
				(org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm) this.simplePDObject;
		if (form.hasXFA()) {
			COSBase value = form.getCOSObject().getDictionaryObject(COSName.XFA);
			boolean isStream = value instanceof COSStream;
			if (isStream || value instanceof COSArray) {
				ArrayList<CosObject> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				if (isStream) {
					list.add(new PBCosStream((COSStream) value));
				} else {
					list.add(new PBCosArray((COSArray) value));
				}
				return Collections.unmodifiableList(list);
			}
		}
		return Collections.emptyList();
	}
}
