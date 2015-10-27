package org.verapdf.model.impl.pb.pd.actions;

import org.apache.pdfbox.pdmodel.interactive.action.PDActionRemoteGoTo;
import org.verapdf.model.pdlayer.PDGoToRemoteAction;

/**
 * @author Timur Kamalov
 */
public class PBoxPDGoToRemoteAction extends PBoxPDGoToAction implements PDGoToRemoteAction {

	public static final String GOTO_REMOTE_ACTION_TYPE = "PDGoToRemoteAction";

	public PBoxPDGoToRemoteAction(PDActionRemoteGoTo simplePDObject) {
		super(simplePDObject, GOTO_REMOTE_ACTION_TYPE);
	}


}
