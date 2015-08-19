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
public class PBoxPDTilingPattern extends PBoxPDPattern implements
        PDTilingPattern {

    public static final String TILING_PATTERN_TYPE = "PDTilingPattern";

    public static final String CONTENT_STREAM = "contentStream";

    public PBoxPDTilingPattern(
            org.apache.pdfbox.pdmodel.graphics.pattern.PDTilingPattern simplePDObject) {
        super(simplePDObject, TILING_PATTERN_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {

        if (CONTENT_STREAM.equals(link)) {
            return this.getContentStream();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDContentStream> getContentStream() {
        List<PDContentStream> contentStreams = new ArrayList<>(1);
        contentStreams.add(new PBoxPDContentStream(
				(org.apache.pdfbox.contentstream.PDContentStream) simplePDObject));
        return contentStreams;
    }
}
