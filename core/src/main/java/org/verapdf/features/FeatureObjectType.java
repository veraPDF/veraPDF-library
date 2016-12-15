package org.verapdf.features;

/**
 * Enumeration for features object types
 *
 * @author Maksim Bezrukov
 */
public enum FeatureObjectType {
	ANNOTATION("annotations", "Annotations", "annot"),
	COLORSPACE("colorSpace", "Color Spaces", "clrsp"),
	DOCUMENT_SECURITY("ds", "Document Security"),
	EMBEDDED_FILE("embeddedFile","Embedded Files"),
	EXT_G_STATE("exGSt", "Graphics States"),
	FONT("font", "Fonts", "fnt"),
	FORM_XOBJECT("formXobject", "Forms", "xobj"),
	ICCPROFILE("iccProfile", "ICC Profiles", "iccProfile"),
	IMAGE_XOBJECT("imageXobject", "Images", "xobj"),
	INFORMATION_DICTIONARY("informationDict", "Information Dictionary"),
	LOW_LEVEL_INFO("lowLevelInfo", "Low Level Info"),
	METADATA("metadata", "Metadata"),
	OUTLINES("outlines", "Outlines"),
	OUTPUTINTENT("outputIntent", "Output Intents"),
	PAGE("page", "Pages"),
	PATTERN("pattern", "Patterns", "ptrn"),
	POSTSCRIPT_XOBJECT("postscriptXobject", "Postscript", "xobj"),
	PROPERTIES("properties", "Properties Dictionaries", "prop"),
	SHADING("shading", "Shadings", "shdng"),
	SIGNATURE("signature", "Signatures"),
	ERROR("error", "Errors");
	
	private final String nodeName;
	private final String fullName;
	private final String idPrefix;
	
	private FeatureObjectType(final String nodeName, final String fullName) {
		this(nodeName, fullName, nodeName);
	}
	
	private FeatureObjectType(final String nodeName, final String fullName, final String idPrefix) {
		this.nodeName = nodeName;
		this.fullName = fullName;
		this.idPrefix = idPrefix;
	}
	
	public String getNodeName() {
		return this.nodeName;
	}
	
	public String getFullName() {
		return this.fullName;
	}

	/**
	 * @return the idPrefix
	 */
	public String getIdPrefix() {
		return idPrefix;
	}
}
