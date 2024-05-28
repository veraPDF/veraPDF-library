/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.features;

/**
 * Enumeration for features object types
 *
 * @author Maksim Bezrukov
 */
public enum FeatureObjectType {
	ACTION("actions", "Actions"),
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
	INTERACTIVE_FORM_FIELDS("interactiveFormField", "Interactive Form Fields", "intFormField"),
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
		this.fullName = fullName.trim();
		this.idPrefix = idPrefix;
	}

	public static FeatureObjectType getFeatureObjectTypeByFullName(String fullName) {
		String name = fullName.trim();
		for (FeatureObjectType feature : FeatureObjectType.values()) {
			if (feature.getFullName().equals(name)) {
				return feature;
			}
		}
		return null;
	}

	public String getNodeName() {
		return this.nodeName;
	}
	
	public String getFullName() {
		return this.fullName;
	}

	public String getIdPrefix() {
		return idPrefix;
	}

	@Override
	public String toString() {
		return this.getNodeName();
	}
}
