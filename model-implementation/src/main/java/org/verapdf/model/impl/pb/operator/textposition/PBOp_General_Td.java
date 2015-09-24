package org.verapdf.model.impl.pb.operator.textposition;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosReal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for text position operators (Td and TD)
 *
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOp_General_Td extends PBOpTextPosition {

	/** Name of link to the horizontal offset for Td and TD operators */
    public static final String HORIZONTAL_OFFSET = "horizontalOffset";

	/** Name of link to the vertical offset for Td and TD operators */
	public static final String VERTICAL_OFFSET = "verticalOffset";

    protected PBOp_General_Td(List<COSBase> arguments, final String opType) {
        super(arguments, opType);
    }

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case VERTICAL_OFFSET:
				return this.getVerticalOffset();
			case HORIZONTAL_OFFSET:
				return this.getHorizontalOffset();
			default:
				return super.getLinkedObjects(link);
		}
	}

    private List<CosReal> getHorizontalOffset() {
		if (this.arguments.size() > 1) {
			COSBase number = this.arguments
					.get(this.arguments.size() - 2);
			if (number instanceof COSNumber) {
				List<CosReal> offset =
						new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				offset.add(new PBCosReal((COSNumber) number));
				return Collections.unmodifiableList(offset);
			}
		}
        return Collections.emptyList();
    }

    private List<CosReal> getVerticalOffset() {
        return this.getLastReal();
    }
}
