package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.verapdf.model.coslayer.CosFileSpecification;

/**
 * Represent a specific type of Dictionary - File Specification Dictionary.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosFileSpecification extends PBCosDict implements CosFileSpecification {

    /** Type name for PBCosFileSpecification */
	public static final String COS_FILE_SPECIFICATION_TYPE = "CosFileSpecification";

	public PBCosFileSpecification(COSDictionary pdfBoxObject) {
		super(pdfBoxObject, COS_FILE_SPECIFICATION_TYPE);
	}

	/**
	 * string representation of the EF dictionary, or null if EF key is not present in the file specification dictionary
	 */
	@Override
    public String getEF() {
		final COSBase ef = ((COSDictionary) baseObject).getItem("EF");
		return ef != null ? ef.toString() : null;
	}
}
