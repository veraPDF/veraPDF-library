package org.verapdf.model.impl.pb.pd.colors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosUnicodeName;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.pb.cos.PBCosUnicodeName;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDSeparation;

/**
 * Separation color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDSeparation extends PBoxPDColorSpace implements PDSeparation {

	public static final String SEPARATION_TYPE = "PDSeparation";

	public static final String ALTERNATE = "alternate";
	public static final String COLORANT_NAME = "colorantName";

	public static final int COLORANT_NAME_POSITION = 1;

	public PBoxPDSeparation(
			org.apache.pdfbox.pdmodel.graphics.color.PDSeparation simplePDObject) {
		super(simplePDObject, SEPARATION_TYPE);
	}

	// TODO : implement me
	@Override
	public Boolean getareTintAndAlternateConsistent() {
		return Boolean.FALSE;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case ALTERNATE:
				return this.getAlternate();
			case COLORANT_NAME:
				return this.getColorantName();
			default:
				return super.getLinkedObjects(link);
		}
	}

	/**
	 * @return a {@link List} of alternate {@link PDColorSpace} objects
	 */
	public List<PDColorSpace> getAlternate() {
		org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace space =
				((org.apache.pdfbox.pdmodel.graphics.color.PDSeparation) this.simplePDObject)
						.getAlternateColorSpace();
		PDColorSpace currentSpace = ColorSpaceFactory.getColorSpace(space);
		if (currentSpace != null) {
			List<PDColorSpace> colorSpace = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			colorSpace.add(currentSpace);
			return Collections.unmodifiableList(colorSpace);
		}
		return Collections.emptyList();
	}

	private List<CosUnicodeName> getColorantName() {
		COSArray array = (COSArray) this.simplePDObject.getCOSObject();
		if (array.size() > COLORANT_NAME_POSITION) {
			COSBase object = array.getObject(COLORANT_NAME_POSITION);
			if (object instanceof COSName) {
				ArrayList<CosUnicodeName> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new PBCosUnicodeName((COSName) object));
				return Collections.unmodifiableList(list);
			}
		}
		return Collections.emptyList();
	}
}
