package org.verapdf.model.impl.pb.pd.colors;

import org.apache.log4j.Logger;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
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

    public PBoxPDDeviceN(
            org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN simplePDObject) {
        super(simplePDObject, DEVICE_N_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (ALTERNATE.equals(link)) {
            return this.getAlternate();
        }
        return super.getLinkedObjects(link);
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
}
