package org.verapdf.model.impl.pb.operator.base;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.verapdf.model.GenericModelObject;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.operator.Operator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for operator layer
 * 
 * @author Timur Kamalov
 */
public abstract class PBOperator extends GenericModelObject implements Operator {

    public static final int MAX_NUMBER_OF_ELEMENTS = 1;
    protected final List<COSBase> arguments;

    protected PBOperator(List<COSBase> arguments, final String opType) {
		super(opType);
        this.arguments = arguments;
    }

    protected List<CosReal> getLastReal() {
		if (!this.arguments.isEmpty()) {
			COSBase base = this.arguments.get(this.arguments.size() - 1);
			if (base instanceof COSNumber) {
				List<CosReal> cosReals = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				cosReals.add(new PBCosReal((COSNumber) base));
				return Collections.unmodifiableList(cosReals);
			}
		}
        return Collections.emptyList();
    }

	protected List<CosReal> getListOfReals() {
		List<CosReal> list = new ArrayList<>();
		for (COSBase base : this.arguments) {
			if (base instanceof COSArray) {
				addArrayElements(list, (COSArray) base);
			} else if (base instanceof COSNumber) {
				list.add(new PBCosReal((COSNumber) base));
			}
		}
		return Collections.unmodifiableList(list);
	}

	private static void addArrayElements(List<CosReal> list, COSArray base) {
		for (COSBase arg : base) {
			if (arg instanceof COSNumber) {
				list.add(new PBCosReal((COSNumber) arg));
			}
		}
	}

}
