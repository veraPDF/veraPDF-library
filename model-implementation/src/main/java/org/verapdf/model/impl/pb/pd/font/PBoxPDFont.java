package org.verapdf.model.impl.pb.pd.font;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosUnicodeName;
import org.verapdf.model.external.FontProgram;
import org.verapdf.model.factory.font.FontFactory;
import org.verapdf.model.impl.pb.cos.PBCosUnicodeName;
import org.verapdf.model.impl.pb.external.PBoxFontProgram;
import org.verapdf.model.impl.pb.external.PBoxTrueTypeFontProgram;
import org.verapdf.model.impl.pb.pd.PBoxPDResources;
import org.verapdf.model.pdlayer.PDFont;
import org.verapdf.model.tools.IDGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class PBoxPDFont extends PBoxPDResources implements PDFont {

	public static final String FONT_FILE = "fontFile";
	public static final String BASE_FONT = "BaseFont";

	private final String id;

	protected PBoxPDFont(PDFontLike font, final String type) {
		super(font, type);
		this.id = IDGenerator.generateID(font);
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public String getType() {
		String type = null;
		if (this.pdFontLike instanceof org.apache.pdfbox.pdmodel.font.PDFont) {
			type = ((org.apache.pdfbox.pdmodel.font.PDFont) this.pdFontLike)
					.getType();
		} else if (this.pdFontLike instanceof PDCIDFont) {
			type = ((PDCIDFont) this.pdFontLike).getCOSObject().getNameAsString(
					COSName.TYPE);
		}
		return type;
	}

	@Override
	public String getfontName() {
		return this.pdFontLike.getName();
	}

	@Override
	public String getSubtype() {
		String subtype = null;
		if (this.pdFontLike instanceof org.apache.pdfbox.pdmodel.font.PDFont) {
			subtype = ((org.apache.pdfbox.pdmodel.font.PDFont) this.pdFontLike)
					.getSubType();
		} else if (this.pdFontLike instanceof PDCIDFont) {
			subtype = ((PDCIDFont) this.pdFontLike).getCOSObject().getNameAsString(
					COSName.SUBTYPE);
		}
		return subtype;
	}

	@Override
	public Boolean getisSymbolic() {
		PDFontDescriptor fontDescriptor = this.pdFontLike.getFontDescriptor();
		return Boolean.valueOf(fontDescriptor.isSymbolic());
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case FONT_FILE:
				return this.getFontFile();
			case BASE_FONT:
				return this.getBaseFont();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<FontProgram> getFontFile() {
		if (!getSubtype().equals(FontFactory.TYPE_3)
				&& (this.pdFontLike.isEmbedded())) {
			if (getSubtype().equals(FontFactory.TRUE_TYPE)) {
				PBoxTrueTypeFontProgram trueTypeFontProgram = new PBoxTrueTypeFontProgram(
						((PDTrueTypeFont) this.pdFontLike).getTrueTypeFont(), getisSymbolic());
				return this.getFontProgramList(trueTypeFontProgram);
			} else {
				PDFontDescriptor fontDescriptor = pdFontLike.getFontDescriptor();
				PDStream fontFile;
				if (getSubtype().equals(FontFactory.TYPE_1)) {
					if (this.pdFontLike instanceof PDType1CFont) {
						fontFile = fontDescriptor.getFontFile3();
					} else {
						fontFile = fontDescriptor.getFontFile();
					}
				} else if (getSubtype().equals(FontFactory.CID_FONT_TYPE_2)) {
					fontFile = fontDescriptor.getFontFile2();
				} else {
					fontFile = fontDescriptor.getFontFile3();
				}
				if (fontFile != null) {
					return this.getFontProgramList(new PBoxFontProgram(fontFile));
				}
			}
		}
		return Collections.emptyList();
	}

	private List<FontProgram> getFontProgramList(FontProgram fontProgram) {
		List<FontProgram> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		list.add(fontProgram);
		return Collections.unmodifiableList(list);
	}

	private List<CosUnicodeName> getBaseFont() {
		String name = this.pdFontLike.getName();
		if (name != null) {
			ArrayList<CosUnicodeName> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(new PBCosUnicodeName(COSName.getPDFName(name)));
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

}
