package org.verapdf.report;

import javax.xml.bind.annotation.XmlElement;

import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;

/**
 * @author Maksim Bezrukov
 */
public class DocumentResourcesFeatures {

	@XmlElement
	private final FeaturesNode graphicsStates;
	@XmlElement
	private final FeaturesNode colorSpaces;
	@XmlElement
	private final FeaturesNode patterns;
	@XmlElement
	private final FeaturesNode shadings;
	@XmlElement
	private final FeaturesNode xobjects;
	@XmlElement
	private final FeaturesNode fonts;
	@XmlElement
	private final FeaturesNode propertiesDicts;

	private DocumentResourcesFeatures(FeaturesNode propertiesDicts, FeaturesNode fonts, FeaturesNode xobjects, FeaturesNode shadings, FeaturesNode patterns, FeaturesNode colorSpaces, FeaturesNode graphicsStates) {
		this.propertiesDicts = propertiesDicts;
		this.fonts = fonts;
		this.xobjects = xobjects;
		this.shadings = shadings;
		this.patterns = patterns;
		this.colorSpaces = colorSpaces;
		this.graphicsStates = graphicsStates;
	}

	private DocumentResourcesFeatures() {
		this(null, null, null, null, null, null, null);
	}

	static DocumentResourcesFeatures fromValues(FeatureExtractionResult collection) {
		FeaturesNode graphicsStates = FeaturesNode.fromValues(collection, FeatureObjectType.EXT_G_STATE);
		FeaturesNode colorSpaces = FeaturesNode.fromValues(collection, FeatureObjectType.COLORSPACE);
		FeaturesNode patterns = FeaturesNode.fromValues(collection, FeatureObjectType.PATTERN);
		FeaturesNode shadings = FeaturesNode.fromValues(collection, FeatureObjectType.SHADING);
		FeaturesNode fonts = FeaturesNode.fromValues(collection, FeatureObjectType.FONT);
		FeaturesNode propertiesDicts = FeaturesNode.fromValues(collection, FeatureObjectType.PROPERTIES);
		FeaturesNode xobjects = FeaturesNode.fromValues(collection, FeatureObjectType.IMAGE_XOBJECT,
				FeatureObjectType.FORM_XOBJECT, FeatureObjectType.POSTSCRIPT_XOBJECT,
				FeatureObjectType.FAILED_XOBJECT);
		if (graphicsStates == null
				&& colorSpaces == null
				&& patterns == null
				&& shadings == null
				&& fonts == null
				&& propertiesDicts == null
				&& xobjects == null) {
			return null;
		}
		return new DocumentResourcesFeatures(propertiesDicts, fonts, xobjects, shadings, patterns, colorSpaces, graphicsStates);
	}
}
