package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDAction;
import org.verapdf.model.pdlayer.PDOutline;

import java.util.ArrayList;
import java.util.List;

/**
 * Outline (or bookmark) of document
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDOutline extends PBoxPDObject implements PDOutline {

	public static final String ACTION = "A";
	public static final String OUTLINE_TYPE = "PDOutline";
	public static final Integer NUMBER_OF_ACTIONS = Integer.valueOf(1);

	public PBoxPDOutline(PDOutlineItem simplePDObject) {
		super(simplePDObject);
		setType(OUTLINE_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case ACTION:
				list = this.getAction();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<PDAction> getAction() {
		List<PDAction> actions = new ArrayList<>(NUMBER_OF_ACTIONS);
		org.apache.pdfbox.pdmodel.interactive.action.PDAction action = ((PDOutlineItem) simplePDObject).getAction();
		super.addAction(actions, action);
		return actions;
	}

}
