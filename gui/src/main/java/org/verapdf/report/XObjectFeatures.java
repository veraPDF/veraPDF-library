package org.verapdf.report;

import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.tools.FeaturesCollection;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Maksim Bezrukov
 */
public class XObjectFeatures {

	@XmlElement
	private final FeaturesNode images;
	@XmlElement
	private final FeaturesNode forms;
	@XmlElement
	private final FeaturesNode postScripts;
	@XmlElement
	private final FeaturesNode xobjects;

	private XObjectFeatures(FeaturesNode images, FeaturesNode forms, FeaturesNode postScripts, FeaturesNode xobjects) {
		this.images = images;
		this.forms = forms;
		this.postScripts = postScripts;
		this.xobjects = xobjects;
	}

	private XObjectFeatures() {
		this(null, null, null, null);
	}

	static XObjectFeatures fromValues(FeaturesCollection collection) {
		FeaturesNode images = FeaturesNode.fromValues(collection, FeaturesObjectTypesEnum.IMAGE_XOBJECT);
		FeaturesNode forms = FeaturesNode.fromValues(collection, FeaturesObjectTypesEnum.FORM_XOBJECT);
		FeaturesNode postScripts = FeaturesNode.fromValues(collection, FeaturesObjectTypesEnum.POSTSCRIPT_XOBJECT);
		FeaturesNode xobjects = FeaturesNode.fromValues(collection, FeaturesObjectTypesEnum.FAILED_XOBJECT);
		return new XObjectFeatures(images, forms, postScripts, xobjects);
	}
}
