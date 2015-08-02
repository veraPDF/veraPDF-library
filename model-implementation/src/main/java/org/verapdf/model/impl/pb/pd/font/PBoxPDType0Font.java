package org.verapdf.model.impl.pb.pd.font;

import org.apache.fontbox.cmap.CMap;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.font.PDCIDSystemInfo;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDCIDFont;
import org.verapdf.model.pdlayer.PDCMap;
import org.verapdf.model.pdlayer.PDType0Font;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBoxPDType0Font extends PBoxPDFont implements PDType0Font {

	public static final String TYPE_0_FONT_TYPE = "PDType0Font";
	public static final String DESCENDANT_FONTS = "DescendantFonts";
	public static final String ENCODING = "Encoding";

	public PBoxPDType0Font(PDFontLike font) {
		super(font);
		setType(TYPE_0_FONT_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case DESCENDANT_FONTS:
				list = getDescendantFonts();
				break;
			case ENCODING:
				list = getEncoding();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<PDCIDFont> getDescendantFonts() {
		List<PDCIDFont> list = new ArrayList<>();
		org.apache.pdfbox.pdmodel.font.PDCIDFont pdcidFont = ((org.apache.pdfbox.pdmodel.font.PDType0Font) pdFontLike).getDescendantFont();
		if (pdcidFont != null) {
			list.add(new PBoxPDCIDFont(pdcidFont));
		}
		return list;
	}

	private List<PDCMap> getEncoding() {
		List<PDCMap> list = new ArrayList<>();
		CMap charMap = ((org.apache.pdfbox.pdmodel.font.PDType0Font) pdFontLike).getCMap();
		if (charMap != null) {
			list.add(new PBoxPDCMap(charMap));
		}
		return list;
	}


	@Override
	public Boolean getareRegistryOrderingCompatible() {
		String parentOrdering = null;
		String parentRegistry = null;
		COSDictionary dictionary = ((org.apache.pdfbox.pdmodel.font.PDType0Font) pdFontLike).getCOSObject();
		COSBase encoding = dictionary.getDictionaryObject(COSName.ENCODING);
		if (encoding instanceof COSStream) {
			COSBase cidSystemInfo = ((COSStream) encoding).getDictionaryObject(COSName.CIDSYSTEMINFO);
			if (cidSystemInfo instanceof COSDictionary) {
				parentOrdering = ((COSDictionary) cidSystemInfo).getString(COSName.ORDERING);
				parentRegistry = ((COSDictionary) cidSystemInfo).getString(COSName.REGISTRY);
			}
		}
		if (cMap != null) {
			parentOrdering = cMap.getOrdering();
			parentRegistry = cMap.getRegistry();
		}
		String descOrdering = null;
		String descRegistry = null;
		PDCIDSystemInfo info = ((org.apache.pdfbox.pdmodel.font.PDType0Font) pdFontLike).getDescendantFont().getCIDSystemInfo();
		if (info != null) {
			descOrdering = info.getOrdering();
			descRegistry = info.getRegistry();
		}
		if (parentOrdering != null && parentRegistry != null && descOrdering != null && descRegistry != null) {
			if (parentOrdering.equals(descOrdering) && parentRegistry.equals(descRegistry)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
}
