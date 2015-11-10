package org.verapdf.model.tools.resources;

import org.apache.log4j.Logger;
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
public class PDInheritableResources {

	private static final Logger LOGGER = Logger.getLogger(PDInheritableResources.class);

	public static final PDResources EMPTY_RESOURCES = new PDEmptyResources();
	public static final PDInheritableResources EMPTY_EXTENDED_RESOURCES = new PDEmptyInheritableResources();

	private final PDResources currentResources;
	private final PDResources pageResources;

	protected PDInheritableResources(PDResources pageResources, PDResources currentResources) {
		this.pageResources = pageResources;
		this.currentResources = currentResources;
	}

	public PDResources getPageResources() {
		return this.pageResources;
	}

	public PDResources getCurrentResources(){
		return this.currentResources;
	}

	public PDInheritableResources getExtendedResources(PDResources resources) {
		return getInstance(this.pageResources, resources);
	}

	public PDFont getFont(COSName name) throws IOException {
		PDFont font = this.currentResources.getFont(name);
		return font != null ? font : this.pageResources.getFont(name);
	}

	public PDColorSpace getColorSpace(COSName name) throws IOException {
		try {
			/*
				if name is name of device depended color space and
				default color space defined only in page resource
			 	dictionary that wee need to get it from page
			 	resource dictionary
			*/
			if (this.isDefaultColorSpaceUsed(name)) {
				return this.pageResources.getColorSpace(name);
			}
			PDColorSpace colorSpace = this.currentResources.getColorSpace(name);
			if (colorSpace != null) {
				return colorSpace;
			}
		} catch (IOException e) {
			LOGGER.warn("Problems during color space obtain from current resource dictionary. " +
					"Trying to find it in page dictionary", e);
		}
		return this.pageResources.getColorSpace(name);
	}

	public PDExtendedGraphicsState getExtGState(COSName name) {
		PDExtendedGraphicsState state = this.currentResources.getExtGState(name);
		return state != null ? state : this.pageResources.getExtGState(name);
	}

	public PDShading getShading(COSName name) throws IOException {
		PDShading shading = this.currentResources.getShading(name);
		return shading != null ? shading : this.pageResources.getShading(name);
	}

	public PDAbstractPattern getPattern(COSName name) throws IOException {
		PDAbstractPattern pattern = this.currentResources.getPattern(name);
		return pattern != null ? pattern : this.pageResources.getPattern(name);
	}

	public PDXObject getXObject(COSName name) throws IOException {
		PDXObject object = this.currentResources.getXObject(name);
		return object != null ? object : this.pageResources.getXObject(name);
	}

	private boolean isDefaultColorSpaceUsed(COSName name) {
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

	public static PDInheritableResources getInstance(PDResources pageResources) {
		return getInstance(pageResources, pageResources);
	}

	public static PDInheritableResources getInstance(
			PDResources pageResources, PDResources currentResources) {
		pageResources = pageResources != null ? pageResources : EMPTY_RESOURCES;
		currentResources = currentResources != null ? currentResources : EMPTY_RESOURCES;
		return new PDInheritableResources(pageResources, currentResources);
	}

}
