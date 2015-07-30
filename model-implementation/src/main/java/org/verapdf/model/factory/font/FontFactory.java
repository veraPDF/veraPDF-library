package org.verapdf.model.factory.font;

import org.apache.log4j.Logger;
import org.verapdf.model.impl.pb.pd.font.PBoxPDSimpleFont;
import org.verapdf.model.impl.pb.pd.font.PBoxPDType0Font;
import org.verapdf.model.impl.pb.pd.font.PBoxPDType3Font;
import org.verapdf.model.pdlayer.PDFont;

/**
 * @author Timur Kamalov
 */
public final class FontFactory {

	public static final String TYPE_0 = "Type0";
	public static final String TYPE_1 = "Type1";
	public static final String TYPE_3 = "Type3";
	public static final String TRUE_TYPE = "TrueType";

	public static final Logger logger = Logger.getLogger(FontFactory.class);

	public static PDFont parseFont(org.apache.pdfbox.pdmodel.font.PDFont pdfboxFont) {
		switch (pdfboxFont.getSubType()) {
			case TYPE_0:
				return new PBoxPDType0Font(pdfboxFont);
			case TYPE_1:
				return new PBoxPDSimpleFont(pdfboxFont);
			case TYPE_3:
				return new PBoxPDType3Font(pdfboxFont);
			case TRUE_TYPE:
				return new PBoxPDSimpleFont(pdfboxFont);
			default:
				return null;
		}
	}

}
