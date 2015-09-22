package org.verapdf.model.impl.pb.pd.colors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDIndexed;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDIndexed extends PBoxPDColorSpace implements PDIndexed {

	public static final String INDEXED_TYPE = "PDIndexed";

    public static final String BASE = "base";

    public PBoxPDIndexed(
            org.apache.pdfbox.pdmodel.graphics.color.PDIndexed simplePDObject) {
        super(simplePDObject, INDEXED_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (BASE.equals(link)) {
            return this.getBase();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDColorSpace> getBase() {
        org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace baseColorSpace =
				((org.apache.pdfbox.pdmodel.graphics.color.PDIndexed) this.simplePDObject)
                .getBaseColorSpace();
        PDColorSpace colorSpace = ColorSpaceFactory.getColorSpace(baseColorSpace);
        if (colorSpace != null) {
			List<PDColorSpace> base = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			base.add(colorSpace);
			return Collections.unmodifiableList(base);
        }
        return Collections.emptyList();
    }
}
