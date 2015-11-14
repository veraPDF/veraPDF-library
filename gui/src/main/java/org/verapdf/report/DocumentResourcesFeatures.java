package org.verapdf.report;

import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.tools.FeaturesCollection;

import javax.xml.bind.annotation.XmlElement;

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
	private final XObjectFeatures xobjects;
	@XmlElement
	private final FeaturesNode fonts;
	@XmlElement
	private final FeaturesNode propertiesDicts;

	private DocumentResourcesFeatures(FeaturesNode propertiesDicts, FeaturesNode fonts, XObjectFeatures xobjects, FeaturesNode shadings, FeaturesNode patterns, FeaturesNode colorSpaces, FeaturesNode graphicsStates) {
		this.propertiesDicts = propertiesDicts;
		this.fonts = fonts;
		this.xobjects = xobjects;
		this.shadings = shadings;
		this.patterns = patterns;
		this.colorSpaces = colorSpaces;
		this.graphicsStates = graphicsStates;
	}

	static DocumentResourcesFeatures fromValues(FeaturesCollection collection) {
		FeaturesNode graphicsStates = FeaturesNode.fromValues(collection, FeaturesObjectTypesEnum.EXT_G_STATE);
		FeaturesNode colorSpaces = FeaturesNode.fromValues(collection, FeaturesObjectTypesEnum.COLORSPACE);
		FeaturesNode patterns = FeaturesNode.fromValues(collection, FeaturesObjectTypesEnum.PATTERN);
		FeaturesNode shadings = FeaturesNode.fromValues(collection, FeaturesObjectTypesEnum.SHADING);
		FeaturesNode fonts = FeaturesNode.fromValues(collection, FeaturesObjectTypesEnum.FONT);
		FeaturesNode propertiesDicts = FeaturesNode.fromValues(collection, FeaturesObjectTypesEnum.PROPERTIES);
		XObjectFeatures xobjects = XObjectFeatures.fromValues(collection);
		return new DocumentResourcesFeatures(propertiesDicts, fonts, xobjects, shadings, patterns, colorSpaces, graphicsStates);
	}
}
