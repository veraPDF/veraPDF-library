package org.verapdf.model.tools;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.pdmodel.graphics.shading.PDShading;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

import java.io.IOException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PDExtendedResources extends PDResources {

	private static final Logger LOGGER = Logger.getLogger(PDExtendedResources.class);

	public static final PDResources EMPTY = new PDResources();

	private final PDResources currentResources;
	private final PDResources pageResources;

	private PDExtendedResources(PDResources pageResources, COSDictionary currentResources) {
		super(currentResources);
		this.pageResources = pageResources;
		this.currentResources = new PDResources(currentResources);
	}

	public PDResources getPageResources() {
		return this.pageResources;
	}

	@Override
	public PDFont getFont(COSName name) throws IOException {
		try {
			PDFont font = this.currentResources.getFont(name);
			if (font != null) {
				return font;
			}
		} catch (IOException e) {
			LOGGER.warn("Problems during font obtain from current resource dictionary. " +
					"Trying to find it in page dictionary");
		}
		return this.pageResources.getFont(name);
	}

	@Override
	public PDColorSpace getColorSpace(COSName name) throws IOException {
		try {
			if (this.getFromPageResources(name)) {
				return this.pageResources.getColorSpace(name);
			}
			PDColorSpace colorSpace = this.currentResources.getColorSpace(name);
			if (colorSpace != null) {
				return colorSpace;
			}
		} catch (IOException e) {
			LOGGER.warn("Problems during color space obtain from current resource dictionary. " +
					"Trying to find it in page dictionary");
		}
		return this.pageResources.getColorSpace(name);
	}

	@Override
	public PDExtendedGraphicsState getExtGState(COSName name) {
		PDExtendedGraphicsState state = this.currentResources.getExtGState(name);
		return state != null ? state : this.pageResources.getExtGState(name);
	}

	@Override
	public PDShading getShading(COSName name) throws IOException {
		try {
			PDShading shading = this.currentResources.getShading(name);
			if (shading != null) {
				return shading;
			}
		} catch (IOException e) {
			LOGGER.warn("Problems during shading obtain from current resource dictionary. " +
					"Trying to find it in page dictionary");
		}
		return this.pageResources.getShading(name);
	}

	@Override
	public PDAbstractPattern getPattern(COSName name) throws IOException {
		try {
			PDAbstractPattern pattern = this.currentResources.getPattern(name);
			if (pattern != null) {
				return pattern;
			}
		} catch (IOException e) {
			LOGGER.warn("Problems during pattern obtain from current resource dictionary. " +
					"Trying to find it in page dictionary");
		}
		return this.pageResources.getPattern(name);
	}

	@Override
	public PDXObject getXObject(COSName name) throws IOException {
		try {
			PDXObject object = this.currentResources.getXObject(name);
			if (object != null) {
				return object;
			}
		} catch (IOException e) {
			LOGGER.warn("Problems during XObject obtain from current resource dictionary. " +
					"Trying to find it in page dictionary");
		}
		return this.pageResources.getXObject(name);
	}

	private boolean getFromPageResources(COSName name) {
		if (this.isDeviceDepended(name)) {
			COSName value = PDColorSpace.getDefaultValue(this.currentResources, name);
			if (value != null) {
				return false;
			}
			value = PDColorSpace.getDefaultValue(this.pageResources, name);
			if (value != null) {
				return true;
			}
		}
		return false;
	}

	private boolean isDeviceDepended(COSName name) {
		return COSName.DEVICERGB.equals(name) ||
				COSName.DEVICEGRAY.equals(name) || COSName.DEVICECMYK.equals(name);
	}

	public static PDExtendedResources getInstance(PDResources pageResources) {
		return getInstance(pageResources, pageResources);
	}

	public static PDExtendedResources getInstance(
			PDResources pageResources, PDResources currentResources) {
		COSDictionary current = currentResources == null ? new COSDictionary() :
														currentResources.getCOSObject();
		return new PDExtendedResources(pageResources != null ?
				pageResources : EMPTY, current);
	}

}
