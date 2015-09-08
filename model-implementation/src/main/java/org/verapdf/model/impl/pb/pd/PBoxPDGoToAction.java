package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.interactive.action.PDAction;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.pdlayer.PDGoToAction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBoxPDGoToAction extends PBoxPDAction implements PDGoToAction {

	public static final String GOTO_ACTION_TYPE = "PDGoToAction";

	public static final String D = "D";

	public PBoxPDGoToAction(PDAction simplePDObject) {
		super(simplePDObject, GOTO_ACTION_TYPE);
	}

	public PBoxPDGoToAction(PDAction simplePDObject, String type) {
		super(simplePDObject, type);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (D.equals(link)) {
			return this.getD();
		}
		return super.getLinkedObjects(link);
	}

	public List<CosReal> getD() {
		List<CosReal> result = new ArrayList<>();
		COSDictionary cosDictionary = ((PDAction) simplePDObject).getCOSObject();
		if (cosDictionary != null) {
			COSBase dEntry = cosDictionary.getDictionaryObject(COSName.D);
			if (dEntry != null && dEntry instanceof COSArray) {
				for (COSBase cosBase : (COSArray) dEntry) {
					if (cosBase instanceof COSNumber) {
						result.add(new PBCosReal((COSNumber) cosBase));
					}
				}
			}
		}
		return result;
	}

}
