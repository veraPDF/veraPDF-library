package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSInteger;
import org.verapdf.model.coslayer.CosInteger;
import org.verapdf.model.impl.pb.cos.PBCosInteger;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpGeneralGS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class of general graphic state operators
 *
 * @author Timur Kamalov
 */
public abstract class PBOpGeneralGS extends PBOperator implements OpGeneralGS {

    protected PBOpGeneralGS(List<COSBase> arguments, final String opType) {
        super(arguments, opType);
    }

	protected List<CosInteger> getLastInteger() {
		if (!this.arguments.isEmpty()) {
			COSBase number = this.arguments
					.get(this.arguments.size() - 1);
			if (number instanceof COSInteger) {
				List<CosInteger> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new PBCosInteger((COSInteger) number));
				return Collections.unmodifiableList(list);
			}
		}
		return Collections.emptyList();
	}

}
