package org.verapdf.report;

import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.tools.FeaturesCollection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class XObjectFeatures {

	@XmlElementWrapper
	@XmlElement(name = "image")
	private final List<FeaturesNode> images;
	@XmlElementWrapper
	@XmlElement(name = "form")
	private final List<FeaturesNode> forms;
	@XmlElementWrapper
	@XmlElement(name = "postScript")
	private final List<FeaturesNode> postScripts;
	@XmlElementWrapper
	@XmlElement(name = "xobject")
	private final List<FeaturesNode> xobjects;

	private XObjectFeatures(List<FeaturesNode> images, List<FeaturesNode> forms, List<FeaturesNode> postScripts, List<FeaturesNode> xobjects) {
		this.images = images;
		this.forms = forms;
		this.postScripts = postScripts;
		this.xobjects = xobjects;
	}

	private XObjectFeatures() {
		this(null, null, null, null);
	}

	static XObjectFeatures fromValues(FeaturesCollection collection) {
		List<FeaturesNode> images = FeaturesReport.getListFromType(collection, FeaturesObjectTypesEnum.IMAGE_XOBJECT);
		List<FeaturesNode> forms = FeaturesReport.getListFromType(collection, FeaturesObjectTypesEnum.FORM_XOBJECT);
		List<FeaturesNode> postScripts = FeaturesReport.getListFromType(collection, FeaturesObjectTypesEnum.POSTSCRIPT_XOBJECT);
		List<FeaturesNode> xobjects = FeaturesReport.getListFromType(collection, FeaturesObjectTypesEnum.FAILED_XOBJECT);
		return new XObjectFeatures(images, forms, postScripts, xobjects);
	}
}
