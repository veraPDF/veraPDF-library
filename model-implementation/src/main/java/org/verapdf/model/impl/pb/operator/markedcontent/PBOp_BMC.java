package org.verapdf.model.impl.pb.operator.markedcontent;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_BMC;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_BMC extends PBOpMarkedContent implements Op_BMC {

    public static final String OP_BMC_TYPE = "Op_BMC";

    public PBOp_BMC(List<COSBase> arguments) {
        super(arguments, OP_BMC_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        switch (link) {
            case TAG:
                return this.getTag();
            case PROPERTIES:
                return this.getPropertiesDict();
            default:
                return super.getLinkedObjects(link);
        }
    }

}
