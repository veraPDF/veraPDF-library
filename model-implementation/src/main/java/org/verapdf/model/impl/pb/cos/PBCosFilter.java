package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.coslayer.CosFilter;
import org.verapdf.model.tools.IDGenerator;

/**
 * @author Timur Kamalov
 */
public class PBCosFilter extends PBCosName implements CosFilter {

	public static final String COS_FILTER_TYPE = "CosFilter";

	private static final String IDENTITY = "Identity";
	private static final String CUSTOM = "Custom";
	private static final String DEFAULT = "Default";

	private final String decodeParms;

	public PBCosFilter(final COSName filterName, final COSDictionary decodeParms) {
		super(filterName, COS_FILTER_TYPE);
		if (filterName.equals(COSName.CRYPT)) {
			if (decodeParms == null) {
				this.decodeParms = IDENTITY;
			} else {
				this.decodeParms = decodeParms.getString(COSName.NAME, IDENTITY);
			}
		} else if (decodeParms == null) {
			this.decodeParms = DEFAULT;
		} else {
			this.decodeParms = CUSTOM;
		}
	}

	@Override
	public String getdecodeParms() {
		return this.decodeParms;
	}

}
