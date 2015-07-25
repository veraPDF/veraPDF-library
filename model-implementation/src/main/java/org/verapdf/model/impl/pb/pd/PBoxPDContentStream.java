package org.verapdf.model.impl.pb.pd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.operator.OperatorFactory;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.pdlayer.PDContentStream;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDContentStream extends PBoxPDObject implements PDContentStream {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDContentStream.class);
    /**
     * String name for operators
     */
    public static final String OPERATORS = "operators";
    /**
     * String name for PDCOntentStream type
     */
	public static final String CONTENT_STREAM_TYPE = "PDContentStream";

	/**
	 * @param contentStream the {@link org.apache.pdfbox.contentstream.PDContentStream} instance used to instantiate the object
	 */
	public PBoxPDContentStream(org.apache.pdfbox.contentstream.PDContentStream contentStream) {
        super(contentStream);
        setType(CONTENT_STREAM_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        List<? extends Object> list;

        switch (link) {
            case OPERATORS:
                list = getOperators();
                break;
            default:
                list = super.getLinkedObjects(link);
                break;
        }

        return list;
    }

    private List<Operator> getOperators() {
        List<Operator> result = new ArrayList<>();
        try {
            PDFStreamParser streamParser = new PDFStreamParser(contentStream.getContentStream(),true);
            streamParser.parse();
            result = OperatorFactory.parseOperators(streamParser.getTokens(), contentStream.getResources());
        } catch (IOException e) {
            LOGGER.error("Error while parsing content stream. " + e.getMessage(), e);
        }
        return result;
    }
}
