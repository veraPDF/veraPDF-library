package org.verapdf.model.factory.operator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingIntent;
import org.verapdf.model.operator.Operator;

/**
 * @author Timur Kamalov
 */
public final class OperatorFactory {

    private static final Logger LOGGER = Logger
            .getLogger(OperatorFactory.class);
    private static final String MSG_UNEXPECTED_OBJECT_TYPE = "Unexpected type of object in tokens: ";
    private static final String GS_CLONE_MALFUNCTION = "GraphicsState clone function threw CloneNotSupportedException.";

    private OperatorFactory() {
        // Disable default constructor
    }


    public static List<Operator> operatorsFromTokens(List<Object> pdfBoxTokens,
            PDResources resources) {
        List<Operator> result = new ArrayList<>();
        List<COSBase> arguments = new ArrayList<>();
        Iterator<Object> tokenIterator = pdfBoxTokens.iterator();

        OperatorParser parser = new OperatorParser();

        while (tokenIterator.hasNext()) {
            Object pdfBoxToken = tokenIterator.next();
            if (pdfBoxToken instanceof COSBase) {
                arguments.add((COSBase) pdfBoxToken);
            } else if (pdfBoxToken instanceof org.apache.pdfbox.contentstream.operator.Operator) {
                try {
                    result.add(parser.parseOperator(
                            (org.apache.pdfbox.contentstream.operator.Operator) pdfBoxToken,
                            resources, arguments));
                } catch (CloneNotSupportedException e) {
                    LOGGER.debug("GraphicsState clone issues for pdfBoxToken:" + pdfBoxToken);
                    LOGGER.debug(GS_CLONE_MALFUNCTION, e);
                }
                arguments.clear();
            } else if (pdfBoxToken instanceof RenderingIntent) {
                String value = ((RenderingIntent) pdfBoxToken).stringValue();
                arguments.add(COSName.getPDFName(value));
            } else {
                LOGGER.error(MSG_UNEXPECTED_OBJECT_TYPE
                        + pdfBoxToken.getClass().getName());
            }
        }
        return result;
    }

}
