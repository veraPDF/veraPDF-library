package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.interactive.action.PDFormFieldAdditionalActions;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDAction;
import org.verapdf.model.pdlayer.PDFormField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDFormField extends PBoxPDObject implements PDFormField {

	public static final String ADDITIONAL_ACTION = "AA";

	public static final int MAX_NUMBER_OF_ACTIONS = 4;
	public static final String FORM_FIELD_TYPE = "PDFormField";

	public PBoxPDFormField(PDField simplePDObject) {
		super(simplePDObject);
		setType(FORM_FIELD_TYPE);
	}

	@Override
	public String getFT() {
		return ((PDField) simplePDObject).getFieldType();
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case ADDITIONAL_ACTION:
				list = this.getAdditionalAction();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<PDAction> getAdditionalAction() {
		List<PDAction> actions = new ArrayList<>(MAX_NUMBER_OF_ACTIONS);
		PDFormFieldAdditionalActions pbActions = ((PDField) simplePDObject).getActions();
		if (pbActions != null) {
			org.apache.pdfbox.pdmodel.interactive.action.PDAction buffer;

			buffer = pbActions.getC();
			addAction(actions, buffer);

			buffer = pbActions.getF();
			addAction(actions, buffer);

			buffer = pbActions.getK();
			addAction(actions, buffer);

			buffer = pbActions.getV();
			addAction(actions, buffer);
		}

		return actions;
	}

}
