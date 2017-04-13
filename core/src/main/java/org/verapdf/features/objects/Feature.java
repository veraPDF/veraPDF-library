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
package org.verapdf.features.objects;

import org.verapdf.policy.SchematronOperation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.verapdf.policy.SchematronOperation.*;

/**
 * @author Maksim Bezrukov
 */
public class Feature {

	private final String featureName;
	private final String featureXPath;
	private final FeatureType featureType;

	public Feature(String featureName, String featureXPath, FeatureType featureType) {
		this.featureName = featureName;
		this.featureXPath = featureXPath;
		this.featureType = featureType;
	}


	public String getFeatureName() {
		return featureName;
	}

	public String getFeatureXPath() {
		return featureXPath;
	}

	public FeatureType getFeatureType() {
		return featureType;
	}

	public enum FeatureType {
		BOOLEAN,
		NUMBER,
		STRING;

		public List<SchematronOperation> getPossibleOperations() {
			List<SchematronOperation> operations = new ArrayList<>();
			operations.add(PRESENT);
			operations.add(NOT_PRESENT);
			switch (this) {
				case BOOLEAN:
					operations.add(IS_TRUE);
					operations.add(IS_FALSE);
					break;
				case NUMBER:
					operations.add(IS_EQUAL);
					operations.add(NOT_EQUAL);
					operations.add(IS_GREATER);
					operations.add(IS_GREATER_OR_EQUAL);
					operations.add(IS_LESS);
					operations.add(IS_LESS_OR_EQUAL);
					break;
				case STRING:
					operations.add(IS_EQUAL);
					operations.add(NOT_EQUAL);
					operations.add(STARTS_WITH);
					operations.add(ENDS_WITH);
					operations.add(CONTAINS);
					break;
				default:
					throw new IllegalStateException("Unsupported FeatureType in getOperationsForType: " + this);
			}
			return Collections.unmodifiableList(operations);
		}
	}
}
