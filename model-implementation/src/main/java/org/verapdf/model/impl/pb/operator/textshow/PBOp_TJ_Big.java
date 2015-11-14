package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosArray;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.impl.pb.cos.PBCosArray;
import org.verapdf.model.operator.Op_TJ_Big;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Operator which shows one or more text strings,
 * allowing individual glyph positioning
 *
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TJ_Big extends PBOpTextShow implements Op_TJ_Big {

	/** Type name for {@code PBOp_TJ_Big} */
	public static final String OP_TJ_BIG_TYPE = "Op_TJ_Big";

	/** Name of link to the set of strings and numbers */
    public static final String SPECIAL_STRINGS = "specialStrings";

    public PBOp_TJ_Big(List<COSBase> arguments, GraphicState state,
					   PDInheritableResources resources) {
        super(arguments, state, resources, OP_TJ_BIG_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(
            String link) {
        if (SPECIAL_STRINGS.equals(link)) {
            return this.getSpecialStrings();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosArray> getSpecialStrings() {
		if (!this.arguments.isEmpty()) {
			COSBase base = this.arguments.get(
					this.arguments.size() - 1);
			if (base instanceof COSArray) {
				List<CosArray> array =
						new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				array.add(new PBCosArray((COSArray) base));
				return Collections.unmodifiableList(array);
			}
		}
        return Collections.emptyList();
    }
}
