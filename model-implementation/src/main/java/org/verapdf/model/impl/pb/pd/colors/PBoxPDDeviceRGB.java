package org.verapdf.model.impl.pb.pd.colors;

import org.verapdf.model.pdlayer.PDDeviceRGB;

/**
 * DeviceRGB color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDeviceRGB extends PBoxPDColorSpace implements PDDeviceRGB {

    private static final PDDeviceRGB INSTANCE = new PBoxPDDeviceRGB(
            org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB.INSTANCE);

    public static final String DEVICE_RGB_TYPE = "PDDeviceRGB";

    private PBoxPDDeviceRGB(
            org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB simplePDObject) {
        super(simplePDObject, DEVICE_RGB_TYPE);
    }

    public static PDDeviceRGB getInstance() {
        return INSTANCE;
    }
}
