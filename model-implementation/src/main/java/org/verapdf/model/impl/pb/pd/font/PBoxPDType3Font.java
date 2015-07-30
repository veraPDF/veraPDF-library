package org.verapdf.model.impl.pb.pd.font;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.apache.pdfbox.pdmodel.font.PDType3CharProc;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDContentStream;
import org.verapdf.model.pdlayer.PDContentStream;
import org.verapdf.model.pdlayer.PDType3Font;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBoxPDType3Font extends PBoxPDSimpleFont implements PDType3Font {

	public static final String TYPE3_FONT_TYPE = "PDType3Font";
	public static final String CHAR_STRINGS = "charStrings";

	public PBoxPDType3Font(PDFontLike font) {
		super(font);
		setType(TYPE3_FONT_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case CHAR_STRINGS:
				list = getCharStrings();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<PDContentStream> getCharStrings() {
		List<PDContentStream> list = new ArrayList<>();
		COSDictionary charProcDict = ((org.apache.pdfbox.pdmodel.font.PDType3Font) pdFontLike).getCharProcs();
		for (COSName cosName : charProcDict.keySet()) {
			PDType3CharProc charProc = ((org.apache.pdfbox.pdmodel.font.PDType3Font) pdFontLike).getCharProc(cosName);
			list.add(new PBoxPDContentStream(charProc));
		}
		return list;
	}

}
