package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSInteger;
import org.verapdf.model.coslayer.CosInteger;
import org.verapdf.model.impl.pb.cos.PBCosInteger;
import org.verapdf.model.operator.Op_J_line_cap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_J_line_cap extends PBOpGeneralGS implements Op_J_line_cap {

    public static final String OP_J_LINE_CAP_TYPE = "Op_J_line_cap";
    public static final String LINE_CAP = "lineCap";

    public PBOp_J_line_cap(List<COSBase> arguments) {
        super(arguments);
        setType(OP_J_LINE_CAP_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(
            String link) {
        if (LINE_CAP.equals(link)) {
            return this.getLineCap();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosInteger> getLineCap() {
        List<CosInteger> list = new ArrayList<>(OPERANDS_COUNT);
        if (!this.arguments.isEmpty()
                && this.arguments.get(arguments.size() - 1) instanceof COSInteger) {
            list.add(new PBCosInteger((COSInteger) this.arguments.get(arguments
                    .size() - 1)));
        }
        return list;
    }

}
