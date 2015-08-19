package org.verapdf.model.impl.pb.pd.colors;

import org.verapdf.model.pdlayer.PDDeviceCMYK;

/**
 * DeviceCMYK color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDeviceCMYK extends PBoxPDColorSpace implements PDDeviceCMYK {

    private static final PDDeviceCMYK INSTANCE = new PBoxPDDeviceCMYK(
            org.apache.pdfbox.pdmodel.graphics.color.PDDeviceCMYK.INSTANCE);

    public static final String DEVICE_CMYK_TYPE = "PDDeviceCMYK";

    private PBoxPDDeviceCMYK(
            org.apache.pdfbox.pdmodel.graphics.color.PDDeviceCMYK simplePDObject) {
        super(simplePDObject, DEVICE_CMYK_TYPE);
    }

    public static PDDeviceCMYK getInstance() {
        return INSTANCE;
    }
}
