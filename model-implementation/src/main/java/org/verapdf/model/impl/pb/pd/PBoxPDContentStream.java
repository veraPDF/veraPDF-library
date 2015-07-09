package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.operator.OperatorFactory;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.pdlayer.PDContentStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDContentStream extends PBoxPDObject implements PDContentStream {

    public static final Logger logger = Logger.getLogger(PBoxPDContentStream.class);
    public static final String OPERATORS = "operators";

    public PBoxPDContentStream(org.apache.pdfbox.contentstream.PDContentStream contentStream) {
        super(contentStream);
        setType("PDContentStream");
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
            PDFStreamParser streamParser = new PDFStreamParser(contentStream.getContentStream());
            streamParser.parse();
            result = OperatorFactory.parseOperators(streamParser.getTokens());
        } catch (IOException e) {
            logger.error("Error while parsing content stream." + e.getMessage());
        }
        return result;
    }
}
