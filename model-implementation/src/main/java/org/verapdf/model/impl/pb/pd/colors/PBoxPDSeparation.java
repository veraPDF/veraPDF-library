package org.verapdf.model.impl.pb.pd.colors;

import java.util.ArrayList;
import java.util.List;

import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDSeparation;

/**
 * Separation color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDSeparation extends PBoxPDColorSpace implements PDSeparation {

	public static final String ALTERNATE = "alternate";
	public static final String SEPARATION_TYPE = "PDSeparation";

	public PBoxPDSeparation(org.apache.pdfbox.pdmodel.graphics.color.PDSeparation simplePDObject) {
		super(simplePDObject);
		setType(SEPARATION_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (ALTERNATE.equals(link)) {
		    return this.getAlternate();
		}
		return super.getLinkedObjects(link);
	}

	/**
	 * @return a {@link List} of alternate {@link PDColorSpace} objects
	 */
	public List<PDColorSpace> getAlternate() {
		List<PDColorSpace> colorSpace = new ArrayList<>();
		org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace space =
				((org.apache.pdfbox.pdmodel.graphics.color.PDSeparation) simplePDObject).getAlternateColorSpace();
		PDColorSpace currentSpace = ColorSpaceFactory.getColorSpace(space);
		if (currentSpace != null) {
			colorSpace.add(currentSpace);
		}
		return colorSpace;
	}
}
