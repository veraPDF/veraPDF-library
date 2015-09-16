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
	    super(font, TYPE_0_FONT_TYPE);
	}

	@Override
	public Boolean getareRegistryOrderingCompatible() {
		String parentCIDOrdering = null;
		String parentCIDRegistry = null;
		COSDictionary dictionary = ((org.apache.pdfbox.pdmodel.font.PDType0Font) this.pdFontLike)
				.getCOSObject();
		COSBase encoding = dictionary.getDictionaryObject(COSName.ENCODING);
		if (encoding instanceof COSStream) {
			COSBase cidSystemInfo = ((COSStream) encoding)
					.getDictionaryObject(COSName.CIDSYSTEMINFO);
			if (cidSystemInfo instanceof COSDictionary) {
				parentCIDOrdering = ((COSDictionary) cidSystemInfo)
						.getString(COSName.ORDERING);
				parentCIDRegistry = ((COSDictionary) cidSystemInfo)
						.getString(COSName.REGISTRY);
			}
		}
		CMap cMap = ((org.apache.pdfbox.pdmodel.font.PDType0Font) this.pdFontLike).getCMap();
		String parentCMapOrdering = null;
		String parentCMapRegistry = null;
		if (cMap != null) {
			parentCMapOrdering = cMap.getOrdering();
			parentCMapRegistry = cMap.getRegistry();
		}
		String descOrdering = null;
		String descRegistry = null;
		PDCIDSystemInfo info = ((org.apache.pdfbox.pdmodel.font.PDType0Font) this.pdFontLike)
				.getDescendantFont().getCIDSystemInfo();
		if (info != null) {
			descOrdering = info.getOrdering();
			descRegistry = info.getRegistry();
		}
		if (parentCIDOrdering != null && parentCIDRegistry != null
				&& (parentCIDOrdering.equals(parentCMapOrdering) && parentCIDRegistry.equals(parentCMapRegistry))
				&& (parentCIDOrdering.equals(descOrdering) && parentCIDRegistry.equals(descRegistry))) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case DESCENDANT_FONTS:
				return this.getDescendantFonts();
			case ENCODING:
				return this.getEncoding();
			default:
				return super.getLinkedObjects(link);
		}
	}

    private List<PDCIDFont> getDescendantFonts() {
        List<PDCIDFont> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        org.apache.pdfbox.pdmodel.font.PDCIDFont pdcidFont =
				((org.apache.pdfbox.pdmodel.font.PDType0Font) this.pdFontLike)
                .getDescendantFont();
        if (pdcidFont != null) {
            list.add(new PBoxPDCIDFont(pdcidFont));
        }
        return list;
    }

    private List<PDCMap> getEncoding() {
        List<PDCMap> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        CMap charMap = ((org.apache.pdfbox.pdmodel.font.PDType0Font) this.pdFontLike)
                .getCMap();
        if (charMap != null) {
			COSDictionary cosDictionary = ((org.apache.pdfbox.pdmodel.font.PDType0Font) this.pdFontLike).getCOSObject();
			COSBase cmap = cosDictionary.getDictionaryObject(COSName.ENCODING);
			if (cmap != null && cmap instanceof COSStream) {
				list.add(new PBoxPDCMap(charMap, (COSStream) cmap));
			} else {
				list.add(new PBoxPDCMap(charMap, null));
			}
        }
        return list;
    }

}
