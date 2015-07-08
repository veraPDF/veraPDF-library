package org.verapdf.model.factory.colors;

import org.apache.pdfbox.pdmodel.graphics.color.*;
import org.verapdf.model.impl.pb.pd.colors.*;
import org.verapdf.model.pdlayer.PDColorSpace;

/**
 * Factory for transforming PDColorSpace objects of pdfbox to corresponding
 * PDColorSpace objects of veraPDF-library.
 *
 * @author Evgeniy Muravitskiy
 */
public class ColorSpaceFactory {

	private ColorSpaceFactory() {

	}

    /**
     * Transform object of pdfbox to corresponding object of veraPDF-library
     * ({@link org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace} to
     * {@link PBoxPDColorSpace})
     *
     * @param colorSpace pdfbox color space object
     * @return <? extends PBoxPDColorSpace> object or {@code null} if {@code colorSpace} argument
     * {@code null} or unsupported type
     */
    public static PDColorSpace getColorSpace(org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace colorSpace) {
        if (colorSpace == null) {
            return null;
        } else if (colorSpace instanceof PDCalGray) {
            return new PBoxPDCalGray((PDCalGray) colorSpace);
        } else if (colorSpace instanceof PDCalRGB) {
            return new PBoxPDCalRGB((PDCalRGB) colorSpace);
        } else if (colorSpace instanceof PDDeviceCMYK) {
            return new PBoxPDDeviceCMYK((PDDeviceCMYK) colorSpace);
        } else if (colorSpace instanceof PDDeviceGray) {
            return new PBoxPDDeviceGray((PDDeviceGray) colorSpace);
        } else if (colorSpace instanceof PDDeviceN) {
            return new PBoxPDDeviceN((PDDeviceN) colorSpace);
        } else if (colorSpace instanceof PDDeviceRGB) {
            return new PBoxPDDeviceRGB((PDDeviceRGB) colorSpace);
        } else if (colorSpace instanceof PDICCBased) {
            return new PBoxPDICCBased((PDICCBased) colorSpace);
        } else if (colorSpace instanceof PDLab) {
            return new PBoxPDLab((PDLab) colorSpace);
        } else if (colorSpace instanceof PDSeparation) {
            return new PBoxPDSeparation((PDSeparation) colorSpace);
        } else if (colorSpace instanceof PDIndexed) {
			return new PBoxPDIndexed((PDIndexed) colorSpace);
		} else {
            return null;
        }
    }
}
