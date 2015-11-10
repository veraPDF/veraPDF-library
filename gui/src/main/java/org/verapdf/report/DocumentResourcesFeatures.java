package org.verapdf.report;

import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.tools.FeaturesCollection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class DocumentResourcesFeatures {

	@XmlElementWrapper
	@XmlElement(name = "graphicsState")
	private final List<FeaturesNode> graphicsStates;
	@XmlElementWrapper
	@XmlElement(name = "colorSpace")
	private final List<FeaturesNode> colorSpaces;
	@XmlElementWrapper
	@XmlElement(name = "pattern")
	private final List<FeaturesNode> patterns;
	@XmlElementWrapper
	@XmlElement(name = "shading")
	private final List<FeaturesNode> shadings;
	@XmlElement
	private final XObjectFeatures xobjects;
	@XmlElementWrapper
	@XmlElement(name = "font")
	private final List<FeaturesNode> fonts;
	@XmlElementWrapper
	@XmlElement(name = "propertiesDict")
	private final List<FeaturesNode> propertiesDicts;

	private DocumentResourcesFeatures(List<FeaturesNode> propertiesDicts, List<FeaturesNode> fonts, XObjectFeatures xobjects, List<FeaturesNode> shadings, List<FeaturesNode> patterns, List<FeaturesNode> colorSpaces, List<FeaturesNode> graphicsStates) {
		this.propertiesDicts = propertiesDicts;
		this.fonts = fonts;
		this.xobjects = xobjects;
		this.shadings = shadings;
		this.patterns = patterns;
		this.colorSpaces = colorSpaces;
		this.graphicsStates = graphicsStates;
	}

	public DocumentResourcesFeatures() {
		this(null, null, null, null, null, null, null);
	}

	static DocumentResourcesFeatures fromValues(FeaturesCollection collection) {
		List<FeaturesNode> graphicsStates = FeaturesReport.getListFromType(collection, FeaturesObjectTypesEnum.EXT_G_STATE);
		List<FeaturesNode> colorSpaces = FeaturesReport.getListFromType(collection, FeaturesObjectTypesEnum.COLORSPACE);
		List<FeaturesNode> patterns = FeaturesReport.getListFromType(collection, FeaturesObjectTypesEnum.PATTERN);
		List<FeaturesNode> shadings = FeaturesReport.getListFromType(collection, FeaturesObjectTypesEnum.SHADING);
		List<FeaturesNode> fonts = FeaturesReport.getListFromType(collection, FeaturesObjectTypesEnum.FONT);
		List<FeaturesNode> propertiesDicts = FeaturesReport.getListFromType(collection, FeaturesObjectTypesEnum.PROPERTIES);
		XObjectFeatures xobjects = XObjectFeatures.fromValues(collection);
		return new DocumentResourcesFeatures(propertiesDicts, fonts, xobjects, shadings, patterns, colorSpaces, graphicsStates);
	}
}
