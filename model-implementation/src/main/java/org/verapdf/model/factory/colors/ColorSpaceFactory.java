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


	public static final String CAL_GRAY = "CalGray";
	public static final String CAL_RGB = "CalRGB";
	public static final String DEVICE_CMYK = "DeviceCMYK";
	public static final String DEVICE_GRB = "DeviceRGB";
	public static final String DEVICE_GRAY = "DeviceGray";
	public static final String DEVICE_N = "DeviceN";
	public static final String ICC_BASED = "ICCBased";
	public static final String LAB = "Lab";
	public static final String SEPARATION = "Separation";
	public static final String INDEXED = "Indexed";

	private ColorSpaceFactory() {

	}

    /**
     * Transform object of pdfbox to corresponding object of veraPDF-library
     * ({@link org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace} to
     * {@link PBoxPDColorSpace})
     *
     * @param colorSpace pdfbox color space object
     * @return {@code <? extends PBoxPDColorSpace>} object or {@code null} if {@code colorSpace} argument
     * {@code null} or unsupported type
     */
	public static PDColorSpace getColorSpace(org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace colorSpace) {
		if (colorSpace != null) {
			switch (colorSpace.getName()) {
				case CAL_GRAY:
					return new PBoxPDCalGray((PDCalGray) colorSpace);
				case CAL_RGB:
					return new PBoxPDCalRGB((PDCalRGB) colorSpace);
				case DEVICE_N:
					return new PBoxPDDeviceN((PDDeviceN) colorSpace);
				case DEVICE_CMYK:
					return PBoxPDDeviceCMYK.getInstance();
				case DEVICE_GRB:
					return PBoxPDDeviceRGB.getInstance();
				case DEVICE_GRAY:
					return PBoxPDDeviceGray.getInstance();
				case ICC_BASED:
					return new PBoxPDICCBased((PDICCBased) colorSpace);
				case LAB:
					return new PBoxPDLab((PDLab) colorSpace);
				case SEPARATION:
					return new PBoxPDSeparation((PDSeparation) colorSpace);
				case INDEXED:
					return new PBoxPDIndexed((PDIndexed) colorSpace);
			}
		}
		return null;
	}

}
