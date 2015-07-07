package org.verapdf.model.impl.pb.pd.pattern;

import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDContentStream;
import org.verapdf.model.pdlayer.PDContentStream;
import org.verapdf.model.pdlayer.PDTilingPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDTillingPattern extends PBoxPDPattern implements PDTilingPattern {

	public static final String CONTENT_STREAM = "contentStream";

	public PBoxPDTillingPattern(org.apache.pdfbox.pdmodel.graphics.pattern.PDTilingPattern simplePDObject) {
		super(simplePDObject);
		setType("PDTilingPattern");
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case CONTENT_STREAM:
				list = getContentStream();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<PDContentStream> getContentStream() {
		List<PDContentStream> contentStreams = new ArrayList<>(1);
		contentStreams.add(new PBoxPDContentStream(contentStream));
		return contentStreams;
	}
}
