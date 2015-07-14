package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.interactive.action.PDActionNamed;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDAction;

import java.util.ArrayList;
import java.util.List;

/**
 * PDF action object
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDAction extends PBoxPDObject implements PDAction {

	public static final String NEXT = "Next";

    public PBoxPDAction(org.apache.pdfbox.pdmodel.interactive.action.PDAction simplePDObject) {
        super(simplePDObject);
        setType("PDAction");
    }

    @Override
    public String getS() {
        return ((org.apache.pdfbox.pdmodel.interactive.action.PDAction) simplePDObject).getSubType();
    }

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case NEXT:
				list = getNext();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<PDAction> getNext() {
		List<PDAction> actions = new ArrayList<>();
		List<org.apache.pdfbox.pdmodel.interactive.action.PDAction> next =
				((org.apache.pdfbox.pdmodel.interactive.action.PDAction) simplePDObject).getNext();
		if (next != null) {
			for (org.apache.pdfbox.pdmodel.interactive.action.PDAction action : next) {
				if (action instanceof PDActionNamed) {
					actions.add(new PBoxPDNamedAction((PDActionNamed) action));
				} else if (action != null) {
					actions.add(new PBoxPDAction(action));
				}
			}
		}
		return actions;
	}
}

