package org.verapdf.model.factory.operator;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.opclip.PBOp_WStar;
import org.verapdf.model.impl.pb.operator.opclip.PBOp_W_clip;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.tools.constants.Operators;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public final class OperatorFactory {

    public static Operator parseOperator(org.apache.pdfbox.contentstream.operator.Operator pdfBoxOperator, List<COSBase> arguments) {
        String operatorName = pdfBoxOperator.getName();
        switch (operatorName) {
            case Operators.W_CLIP : return new PBOp_W_clip(arguments);
            case Operators.W_STAR_EOCLIP : return new PBOp_WStar(arguments);
            default: return null;
        }
    }

    public static List<Operator> parseOperators(List<Object> pdfBoxTokens) {
        List<Operator> result = new ArrayList<>();
        List<COSBase> arguments = new ArrayList<>();
        for (Object pdfBoxToken : pdfBoxTokens) {
            if (pdfBoxToken instanceof COSBase) {
                arguments.add((COSBase) pdfBoxToken);
            } else if (pdfBoxToken instanceof org.apache.pdfbox.contentstream.operator.Operator) {
                parseOperator((org.apache.pdfbox.contentstream.operator.Operator) pdfBoxToken, arguments);
                arguments.clear();
            } else {
                //TODO : something unexpected encountered.
            }
        }
        return result;
    }

}
