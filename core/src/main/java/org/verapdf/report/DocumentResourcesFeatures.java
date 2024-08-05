/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.report;

import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;

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
				FeatureObjectType.FORM_XOBJECT, FeatureObjectType.POSTSCRIPT_XOBJECT);
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

	public FeaturesNode getPropertiesDicts() {
		return propertiesDicts;
	}

	public FeaturesNode getXobjects() {
		return xobjects;
	}

	public FeaturesNode getFonts() {
		return fonts;
	}

	public FeaturesNode getShadings() {
		return shadings;
	}

	public FeaturesNode getPatterns() {
		return patterns;
	}

	public FeaturesNode getColorSpaces() {
		return colorSpaces;
	}

	public FeaturesNode getGraphicsStates() {
		return graphicsStates;
	}
}
