package org.verapdf.model.factory.colors;

import org.apache.pdfbox.pdmodel.graphics.color.*;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDShadingPattern;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDTilingPattern;
import org.verapdf.model.impl.pb.pd.colors.*;
import org.verapdf.model.impl.pb.pd.pattern.PBoxPDShadingPattern;
import org.verapdf.model.impl.pb.pd.pattern.PBoxPDTilingPattern;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDPattern;
import org.verapdf.model.tools.resources.PDInheritableResources;

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
	public static final String PATTERN = "Pattern";

	private ColorSpaceFactory() {
		// disable default constructor
	}

	/**
	 * Transform object of pdfbox to corresponding object of veraPDF-library (
	 * {@link org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace} to
	 * {@link PBoxPDColorSpace}).
	 *
	 * @param colorSpace pdfbox color space object
	 * @return {@code <? extends PBoxPDColorSpace>} object or {@code null} if
	 * {@code colorSpace} argument {@code null} or unsupported type
	 */
	public static PDColorSpace getColorSpace(
			org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace colorSpace) {
		return getColorSpace(colorSpace, null, PDInheritableResources.EMPTY_EXTENDED_RESOURCES);
	}

	/**
	 * Transform object of pdfbox to corresponding object of veraPDF-library (
	 * {@link org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace} to
	 * {@link PBoxPDColorSpace}). If color space is pattern color space, than
	 * transform to Pattern object.
	 *
	 * @param colorSpace pdfbox color space object
	 * @param pattern    pattern of pattern color space
	 * @return {@code <? extends PBoxPDColorSpace>} object or {@code null} if
	 * {@code colorSpace} argument is {@code null},{@code pattern}
	 * argument is {@code null} or unsupported type of color space
	 */
	public static PDColorSpace getColorSpace(
			org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace colorSpace,
			PDAbstractPattern pattern, PDInheritableResources resources) {
		if (colorSpace == null) {
			return null;
		}
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
			case PATTERN:
				return getPattern(pattern, resources);
			default:
				return null;
		}
	}

	/**
	 * Transform object of pdfbox to corresponding object of veraPDF-library (
	 * {@link org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern} to
	 * {@link org.verapdf.model.impl.pb.pd.pattern.PBoxPDPattern})
	 *
	 * @param pattern   pdfbox pattern object
	 * @param resources page resources for tiling pattern
	 * @return {@code <? extends PDPattern>} object or {@code null} if
	 * {@code pattern} argument is {@code null}
	 */
	public static PDPattern getPattern(PDAbstractPattern pattern, PDInheritableResources resources) {
		if (pattern != null) {
			if (pattern.getPatternType() == PDAbstractPattern.TYPE_SHADING_PATTERN) {
				return new PBoxPDShadingPattern((PDShadingPattern) pattern);
			} else if (pattern.getPatternType() == PDAbstractPattern.TYPE_TILING_PATTERN) {
				PDTilingPattern tiling = (PDTilingPattern) pattern;
				PDInheritableResources pdResources = resources.getExtendedResources(tiling.getResources());
				return new PBoxPDTilingPattern(tiling, pdResources);
			}
		}
		return null;
	}

}
