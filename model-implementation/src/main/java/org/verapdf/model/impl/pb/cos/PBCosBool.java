package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBoolean;
import org.verapdf.model.coslayer.CosBool;

/**
 * Current class is representation of CosBool interface of abstract model.
 * This class is analogue of COSBoolean in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosBool extends PBCosObject implements CosBool {

	public PBCosBool(COSBoolean bool) {
		super(bool);
		setType("CosBool");
	}

	/**
	 * Get value of this object
	 */
	@Override
	public Boolean getvalue() {
		return ((COSBoolean) baseObject).getValueAsObject();
	}
}
