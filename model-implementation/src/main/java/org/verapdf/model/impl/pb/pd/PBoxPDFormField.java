package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.interactive.action.PDFormFieldAdditionalActions;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDAction;
import org.verapdf.model.pdlayer.PDFormField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDFormField extends PBoxPDObject implements PDFormField {

	public static final String FORM_FIELD_TYPE = "PDFormField";

    public static final String ADDITIONAL_ACTION = "AA";

    public static final int MAX_NUMBER_OF_ACTIONS = 4;

    public PBoxPDFormField(PDField simplePDObject) {
        super(simplePDObject, FORM_FIELD_TYPE);
    }

    @Override
    public String getFT() {
        return ((PDField) this.simplePDObject).getFieldType();
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (ADDITIONAL_ACTION.equals(link)) {
            return this.getAdditionalAction();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDAction> getAdditionalAction() {
        PDFormFieldAdditionalActions pbActions = ((PDField) this.simplePDObject)
                .getActions();
        if (pbActions != null) {
			List<PDAction> actions = new ArrayList<>(MAX_NUMBER_OF_ACTIONS);

			org.apache.pdfbox.pdmodel.interactive.action.PDAction buffer;

            buffer = pbActions.getC();
            this.addAction(actions, buffer);

            buffer = pbActions.getF();
            this.addAction(actions, buffer);

            buffer = pbActions.getK();
            this.addAction(actions, buffer);

            buffer = pbActions.getV();
            this.addAction(actions, buffer);

			return Collections.unmodifiableList(actions);
        }

        return Collections.emptyList();
    }

}
