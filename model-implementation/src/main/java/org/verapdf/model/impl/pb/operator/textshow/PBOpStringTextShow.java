package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSString;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosString;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.impl.pb.cos.PBCosString;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for all operators that uses one string as operand
 *
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpStringTextShow extends PBOpTextShow {

	/** Name of link to the showing strings for operators ", ', Tj */
    public static final String SHOW_STRING = "showString";

    protected PBOpStringTextShow(List<COSBase> arguments, GraphicState state,
								 PDInheritableResources resources, final String opType) {
        super(arguments, state, resources, opType);
    }

    @Override
    public List<? extends Object> getLinkedObjects(
            String link) {
        if (SHOW_STRING.equals(link)) {
            return this.getShowString();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosString> getShowString() {
		if (!this.arguments.isEmpty()) {
			COSBase base = this.arguments
					.get(this.arguments.size() - 1);
			if (base instanceof COSString) {
				List<CosString> string =
						new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				string.add(new PBCosString((COSString) base));
				return Collections.unmodifiableList(string);
			}
		}
        return Collections.emptyList();
    }

}
