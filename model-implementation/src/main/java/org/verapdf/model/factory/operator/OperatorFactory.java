package org.verapdf.model.factory.operator;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDResources;
import org.verapdf.model.operator.Operator;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for transforming operators of pdfbox library to
 * corresponding operators of veraPDF-library
 *
 * @author Timur Kamalov
 */
public final class OperatorFactory {

    private static final Logger LOGGER = Logger
            .getLogger(OperatorFactory.class);
    private static final String MSG_UNEXPECTED_OBJECT_TYPE = "Unexpected type of object in tokens: ";

    private OperatorFactory() {
        // Disable default constructor
    }

	/**
	 * Transform operators and arguments of stream of pdf document,
	 * implemented by pdfbox library, to corresponding
	 * {@link Operator} objects of veraPDF-library.
	 *
	 * @param pdfBoxTokens list of {@link COSBase} or
	 * 					   {@link org.apache.pdfbox.contentstream.operator.Operator}
	 * 					   objects
	 * @param resources    resources for a given stream
	 * @return list of {@link Operator} objects of veraPDF-library
	 */
    public static List<Operator> operatorsFromTokens(List<Object> pdfBoxTokens,
            										 PDResources resources) {
        List<Operator> result = new ArrayList<>();
        List<COSBase> arguments = new ArrayList<>();
        OperatorParser parser = new OperatorParser();

        for (Object pdfBoxToken : pdfBoxTokens) {
            if (pdfBoxToken instanceof COSBase) {
                arguments.add((COSBase) pdfBoxToken);
            } else if (pdfBoxToken instanceof org.apache.pdfbox.contentstream.operator.Operator) {
                result.add(parser.parseOperator(
                        (org.apache.pdfbox.contentstream.operator.Operator) pdfBoxToken,
                        resources, arguments));
                arguments.clear();
            } else {
                LOGGER.error(MSG_UNEXPECTED_OBJECT_TYPE
                        + pdfBoxToken.getClass().getName());
            }
        }
        return result;
    }

}
