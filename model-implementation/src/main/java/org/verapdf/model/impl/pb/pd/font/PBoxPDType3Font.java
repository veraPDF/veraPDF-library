package org.verapdf.model.impl.pb.pd.font;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.apache.pdfbox.pdmodel.font.PDType3CharProc;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDContentStream;
import org.verapdf.model.pdlayer.PDContentStream;
import org.verapdf.model.pdlayer.PDType3Font;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Timur Kamalov
 */
public class PBoxPDType3Font extends PBoxPDSimpleFont implements PDType3Font {

    public static final String TYPE3_FONT_TYPE = "PDType3Font";

    public static final String CHAR_STRINGS = "charStrings";

	private final PDInheritableResources resources;

	public PBoxPDType3Font(PDFontLike font, PDInheritableResources resources) {
		super(font, TYPE3_FONT_TYPE);
		this.resources = resources;
	}

	@Override
	public Boolean getisStandard() {
		return Boolean.FALSE;
	}

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (CHAR_STRINGS.equals(link)) {
            return this.getCharStrings();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDContentStream> getCharStrings() {
        COSDictionary charProcDict = ((org.apache.pdfbox.pdmodel.font.PDType3Font) this.pdFontLike)
                .getCharProcs();
		if (charProcDict != null) {
			Set<COSName> keySet = charProcDict.keySet();
			List<PDContentStream> list = new ArrayList<>(keySet.size());
			for (COSName cosName : keySet) {
				PDType3CharProc charProc = ((org.apache.pdfbox.pdmodel.font.PDType3Font) this.pdFontLike)
						.getCharProc(cosName);
				list.add(new PBoxPDContentStream(charProc, this.resources));
			}
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
    }

}
