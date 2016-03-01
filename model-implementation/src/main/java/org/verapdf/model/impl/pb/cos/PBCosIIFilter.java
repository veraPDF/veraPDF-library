package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.coslayer.CosIIFilter;

/**
 * @author Timur Kamalov
 */
public class PBCosIIFilter extends PBCosName implements CosIIFilter {

	public static final String COS_II_FILTER_TYPE = "CosIIFilter";

	public PBCosIIFilter(final COSName filter) {
		super(filter, COS_II_FILTER_TYPE);
	}

	public PBCosIIFilter(final String filter) {
		super(COSName.getPDFName(filter));
	}

}
