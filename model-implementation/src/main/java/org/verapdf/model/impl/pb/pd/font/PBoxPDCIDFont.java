package org.verapdf.model.impl.pb.pd.font;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDCIDFontType2;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosStream;
import org.verapdf.model.impl.pb.cos.PBCosStream;
import org.verapdf.model.pdlayer.PDCIDFont;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBoxPDCIDFont extends PBoxPDFont implements PDCIDFont {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDCIDFont.class);

    public static final String CID_FONT_TYPE = "PDCIDFont";

    public static final String CID_SET = "CIDSet";

    public static final String IDENTITY = "Identity";
    public static final String CUSTOM = "Custom";

    public PBoxPDCIDFont(PDFontLike font) {
        super(font, CID_FONT_TYPE);
    }

	@Override
	public String getCIDToGIDMap() {
		if (this.pdFontLike instanceof PDCIDFontType2) {
			COSBase map = ((PDCIDFontType2) this.pdFontLike).getCOSObject()
					.getDictionaryObject(COSName.CID_TO_GID_MAP);
			if (map instanceof COSStream) {
				return CUSTOM;
			} else if (map instanceof COSName
					&& IDENTITY.equals(((COSName) map).getName())) {
				return IDENTITY;
			}
		}
		return null;
	}

    @Override
    public Boolean getcidSetListsAllGlyphs() {
        try {
            PDStream cidSet = getCIDSetStream();
            if (cidSet != null) {
                InputStream stream = ((COSStream) cidSet.getCOSObject()).getUnfilteredStream();
                int length = cidSet.getLength();
                byte[] cidSetBytes = getCIDsFromCIDSet(stream, length);

                //reverse bit order in bit set (convert to big endian)
                BitSet bitSet = toBitSetBigEndian(cidSetBytes);

                org.apache.pdfbox.pdmodel.font.PDCIDFont cidFont = ((org.apache.pdfbox.pdmodel.font.PDCIDFont) this.pdFontLike);
                for (int i = 1; i < bitSet.size(); i++) {
                    if (bitSet.get(i) && !cidFont.hasGlyph(i)) {
                        return Boolean.FALSE;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while parsing embedded font program. " + e.getMessage(), e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (CID_SET.equals(link)) {
            return this.getCIDSet();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosStream> getCIDSet() {
        PDStream cidSet = getCIDSetStream();
        if (cidSet != null) {
            List<CosStream> res = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
            res.add(new PBCosStream(cidSet.getStream()));
            return Collections.unmodifiableList(res);
        }
        return Collections.emptyList();
    }

    private PDStream getCIDSetStream(){
        PDFontDescriptor fontDescriptor = this.pdFontLike.getFontDescriptor();
        PDStream cidSet;
        if (fontDescriptor != null) {
            cidSet = fontDescriptor.getCIDSet();
            return cidSet;
        }
        return null;
    }

    private byte[] getCIDsFromCIDSet(InputStream cidSet, int length) throws IOException {
        byte[] cidSetBytes = new byte[length];
        cidSet.read(cidSetBytes);
        return cidSetBytes;
    }

    private BitSet toBitSetBigEndian(byte[] source) {
        BitSet bitSet = new BitSet(source.length * 8);
        int i = 0;
        for(int j = 0; j < source.length; j++) {
            int b = source[j] >= 0 ? source[j] : 256 + source[j];
            for(int k = 0; k < 8; k++) {
                bitSet.set(i++, ((b & 0x80) != 0));
                b = b << 1;
            }
        }

        return bitSet;
    }

}
