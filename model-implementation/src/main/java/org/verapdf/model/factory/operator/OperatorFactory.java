package org.verapdf.model.factory.operator;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for converting pdfbox operators to the veraPDF-library operators
 *
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

	/**
	 * Converts pdfbox operators and arguments from content stream
	 * to the corresponding {@link Operator} objects of veraPDF-library
	 *
	 * @param pdfBoxTokens list of {@link COSBase} or
	 * 					   {@link org.apache.pdfbox.contentstream.operator.Operator}
	 * 					   objects
	 * @param resources    resources for a given stream
	 * @return list of {@link Operator} objects of veraPDF-library
	 */
    public static List<Operator> operatorsFromTokens(List<Object> pdfBoxTokens,
													 PDInheritableResources resources) {
        List<Operator> result = new ArrayList<>();
        List<COSBase> arguments = new ArrayList<>();
        OperatorParser parser = new OperatorParser();

        for (Object pdfBoxToken : pdfBoxTokens) {
            if (pdfBoxToken instanceof COSBase) {
                arguments.add((COSBase) pdfBoxToken);
            } else if (pdfBoxToken instanceof org.apache.pdfbox.contentstream.operator.Operator) {
                try {
                    parser.parseOperator(result,
                            (org.apache.pdfbox.contentstream.operator.Operator) pdfBoxToken,
                            resources, arguments);
                } catch (CloneNotSupportedException e) {
					LOGGER.debug("GraphicsState clone issues for pdfBoxToken:" + pdfBoxToken);
					LOGGER.debug(GS_CLONE_MALFUNCTION, e);
				} catch (IOException e) {
					LOGGER.debug(e);
				}
				arguments = new ArrayList<>();
			} else {
                LOGGER.error(MSG_UNEXPECTED_OBJECT_TYPE
                        + pdfBoxToken.getClass().getName());
            }
        }
        return result;
    }

}
