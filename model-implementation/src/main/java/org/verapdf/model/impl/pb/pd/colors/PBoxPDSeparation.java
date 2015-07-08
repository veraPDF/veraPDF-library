package org.verapdf.model.impl.pb.pd.colors;

import org.apache.log4j.Logger;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDSeparation;

import java.util.ArrayList;
import java.util.List;

/**
 * Separation color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDSeparation extends PBoxPDColorSpace implements PDSeparation {

	public static final Logger logger = Logger.getLogger(PBoxPDSeparation.class);

	public static final String ALTERNATE = "alternate";

	public PBoxPDSeparation(org.apache.pdfbox.pdmodel.graphics.color.PDSeparation simplePDObject) {
		super(simplePDObject);
		setType("PDSeparation");
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case ALTERNATE:
				list = getAlternate();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

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
