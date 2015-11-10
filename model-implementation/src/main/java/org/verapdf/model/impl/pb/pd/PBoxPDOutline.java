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

	public static final String OUTLINE_TYPE = "PDOutline";

    public static final String ACTION = "A";

    private final String id;

    public PBoxPDOutline(PDOutlineItem simplePDObject, String id) {
        super(simplePDObject, OUTLINE_TYPE);
        this.id = id;
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (ACTION.equals(link)) {
            return this.getAction();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDAction> getAction() {
        List<PDAction> actions = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        org.apache.pdfbox.pdmodel.interactive.action.PDAction action =
				((PDOutlineItem) this.simplePDObject).getAction();
        this.addAction(actions, action);
        return actions;
    }

}
