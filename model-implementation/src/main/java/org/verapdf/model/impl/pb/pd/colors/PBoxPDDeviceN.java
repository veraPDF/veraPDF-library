package org.verapdf.model.impl.pb.pd.colors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDDeviceN;

/**
 * DeviceN color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDeviceN extends PBoxPDColorSpace implements PDDeviceN {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDDeviceN.class);

    /**
     * String name for annotations
     */
	public static final String ALTERNATE = "alternate";
    /**
     * String name for device n type
     */
	public static final String DEVICE_N_TYPE = "PDDeviceN";

	/**
	 * @param simplePDObject
     *            a {@link org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN} used to
     *            populate the instance
	 */
	public PBoxPDDeviceN(org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN simplePDObject) {
		super(simplePDObject);
		setType(DEVICE_N_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case ALTERNATE:
				list = this.getAlternate();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<PDColorSpace> getAlternate() {
		List<PDColorSpace> colorSpace = new ArrayList<>();
		try {
			org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace alternateColorSpace =
					((org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN) simplePDObject).getAlternateColorSpace();
			PDColorSpace space = ColorSpaceFactory.getColorSpace(alternateColorSpace);
			if (space != null) {
				colorSpace.add(space);
			}
		} catch (IOException e) {
			LOGGER.error("Can not get alternate color space from DeviceN. " + e.getMessage(), e);
		}
		return colorSpace;
	}
}
