package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpGeneralGS;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class PBOpGeneralGS extends PBOperator implements OpGeneralGS {

	public static final Integer OPERANDS_COUNT = Integer.valueOf(1);

    protected PBOpGeneralGS(List<COSBase> arguments) {
        super(arguments);
    }

	protected List<CosReal> getLastReal() {
		List<CosReal> cosReals = new ArrayList<>(OPERANDS_COUNT);
		COSBase base = !arguments.isEmpty() ? arguments.get(arguments.size() - 1) : null;
		if (base instanceof COSNumber) {
			cosReals.add(new PBCosReal((COSNumber) base));
		}
		return cosReals;
	}
}
