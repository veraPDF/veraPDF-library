/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 * <p>
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 * <p>
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 * <p>
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.features.objects;

import org.verapdf.features.FeatureObjectType;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * @author Maksim Bezrukov
 */
public final class FeaturesStructureContainer {
	private static Map<FeatureObjectType, List<Feature>> featuresStructure =
			new EnumMap<>(FeatureObjectType.class);

	static {
		featuresStructure.put(FeatureObjectType.ANNOTATION, AnnotationFeaturesObject.getFeaturesList());
		featuresStructure.put(FeatureObjectType.COLORSPACE, ColorSpaceFeaturesObject.getFeaturesList());
		featuresStructure.put(FeatureObjectType.DOCUMENT_SECURITY, DocSecurityFeaturesObject.getFeaturesList());
		featuresStructure.put(FeatureObjectType.EMBEDDED_FILE, EmbeddedFileFeaturesObject.getFeaturesList());
		featuresStructure.put(FeatureObjectType.LOW_LEVEL_INFO, LowLvlInfoFeaturesObject.getFeaturesList());
	}

	private FeaturesStructureContainer() {
	}

	public static List<Feature> getFeaturesListForType(FeatureObjectType type) {
		List<Feature> res = featuresStructure.get(type);
		return res == null ? Collections.<Feature>emptyList() : Collections.unmodifiableList(res);
	}
}
