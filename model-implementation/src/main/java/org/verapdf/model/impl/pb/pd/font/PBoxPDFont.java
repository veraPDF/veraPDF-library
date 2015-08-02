package org.verapdf.model.impl.pb.pd.font;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDCIDFont;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.font.FontFactory;
import org.verapdf.model.impl.pb.external.PBoxFontProgram;
import org.verapdf.model.impl.pb.pd.PBoxPDResources;
import org.verapdf.model.pdlayer.PDFont;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class PBoxPDFont extends PBoxPDResources implements PDFont {

	public static final String FONT_TYPE = "PDFont";
	public static final String FONT_FILE = "fontFile";

	public PBoxPDFont(PDFontLike font) {
		super(font);
		setType(FONT_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case FONT_FILE:
				list = getFontFile();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<PBoxFontProgram> getFontFile() {
		List<PBoxFontProgram> list = new ArrayList<>();
		if (!getSubtype().equals(FontFactory.TYPE_3) && (pdFontLike.isEmbedded())) {
			PDFontDescriptor fontDescriptor = pdFontLike.getFontDescriptor();
			PDStream fontFile;
			if (getSubtype().equals(FontFactory.TYPE_1)) {
				fontFile = fontDescriptor.getFontFile();
			} else if (getSubtype().equals(FontFactory.TRUE_TYPE)) {
				fontFile = fontDescriptor.getFontFile2();
			} else {
				fontFile = fontDescriptor.getFontFile3();
			}
			if (fontFile != null) {
				list.add(new PBoxFontProgram(fontFile));
			}
		}
		return list;
	}

	@Override
	public String getSubtype() {
		String subtype = null;
		if (pdFontLike instanceof org.apache.pdfbox.pdmodel.font.PDFont) {
			subtype = ((org.apache.pdfbox.pdmodel.font.PDFont) pdFontLike).getSubType();
		} else if (pdFontLike instanceof PDCIDFont) {
			subtype = ((PDCIDFont) pdFontLike).getCOSObject().getNameAsString(COSName.SUBTYPE);
		}
		return subtype;
	}

	@Override
	public String getBaseFont() {
		String baseFont = pdFontLike.getName();
		return baseFont;
	}

}
