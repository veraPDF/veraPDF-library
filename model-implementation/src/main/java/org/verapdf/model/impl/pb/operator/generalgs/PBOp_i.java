package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNumber;
import org.verapdf.model.coslayer.CosNumber;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.coslayer.CosRenderingIntent;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.impl.pb.cos.PBCosRenderingIntent;
import org.verapdf.model.operator.Op_i;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_i extends PBOpGeneralGS implements Op_i {

    public static final String FLATNESS = "flatness";
    public static final String OP_I_TYPE = "Op_i";

    public PBOp_i(List<COSBase> arguments) {
        super(arguments);
        setType(OP_I_TYPE);
    }

	@Override
	public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
		List<? extends org.verapdf.model.baselayer.Object> list;

		switch (link) {
			case FLATNESS:
				list = this.getLastReal();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

}
