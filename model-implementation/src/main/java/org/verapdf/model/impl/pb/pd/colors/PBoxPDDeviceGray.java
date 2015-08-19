package org.verapdf.model.impl.pb.pd.colors;

import org.verapdf.model.pdlayer.PDDeviceGray;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDeviceGray extends PBoxPDColorSpace implements PDDeviceGray {

    private static final PDDeviceGray INSTANCE = new PBoxPDDeviceGray(
            org.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray.INSTANCE);

    public static final String DEVICE_GRAY_TYPE = "PDDeviceGray";

    private PBoxPDDeviceGray(
            org.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray simplePDObject) {
        super(simplePDObject, DEVICE_GRAY_TYPE);
    }

    public static PDDeviceGray getInstance() {
        return INSTANCE;
    }
}
