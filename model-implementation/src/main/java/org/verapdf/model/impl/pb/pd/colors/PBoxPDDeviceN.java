package org.verapdf.model.impl.pb.pd.colors;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosUnicodeName;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.pb.cos.PBCosUnicodeName;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDDeviceN;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DeviceN color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDeviceN extends PBoxPDColorSpace implements PDDeviceN {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDDeviceN.class);

	public static final String DEVICE_N_TYPE = "PDDeviceN";

	public static final String ALTERNATE = "alternate";
	public static final String COLORANT_NAMES = "colorantNames";

	public static final int COLORANT_NAMES_POSITION = 1;

	public PBoxPDDeviceN(
			org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN simplePDObject) {
		super(simplePDObject, DEVICE_N_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case ALTERNATE:
				return this.getAlternate();
			case COLORANT_NAMES:
				return this.getColorantNames();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<PDColorSpace> getAlternate() {
		try {
			org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace alternateColorSpace =
					((org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN) this.simplePDObject)
							.getAlternateColorSpace();
			PDColorSpace space = ColorSpaceFactory.getColorSpace(alternateColorSpace);
			if (space != null) {
				List<PDColorSpace> colorSpace = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				colorSpace.add(space);
				return Collections.unmodifiableList(colorSpace);
			}
		} catch (IOException e) {
			LOGGER.error("Can not get alternate color space from DeviceN. ", e);
		}
		return Collections.emptyList();
	}

	private List<CosUnicodeName> getColorantNames() {
		COSArray array = (COSArray) this.simplePDObject.getCOSObject();
		COSBase colorants = array.getObject(COLORANT_NAMES_POSITION);
		if (colorants instanceof COSArray) {
			ArrayList<CosUnicodeName> list = new ArrayList<>(((COSArray) colorants).size());
			for (COSBase colorant : (COSArray) colorants) {
				if (colorant instanceof COSName) {
					list.add(new PBCosUnicodeName((COSName) colorant));
				}
			}
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}
}
