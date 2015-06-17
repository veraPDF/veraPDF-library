package org.verapdf.model.impl.pb.pd;

import org.verapdf.model.baselayer.Object;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.pdlayer.PDContentStream;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDContentStream extends PBoxPDObject implements PDContentStream {

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

    // TODO : implement this
    private List<Operator> getOperators() {
        return new ArrayList<>();
    }
}
