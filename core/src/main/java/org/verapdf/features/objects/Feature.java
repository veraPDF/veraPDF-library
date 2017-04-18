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

import static org.verapdf.policy.SchematronOperation.CONTAINS;
import static org.verapdf.policy.SchematronOperation.ENDS_WITH;
import static org.verapdf.policy.SchematronOperation.IS_EQUAL;
import static org.verapdf.policy.SchematronOperation.IS_FALSE;
import static org.verapdf.policy.SchematronOperation.IS_GREATER;
import static org.verapdf.policy.SchematronOperation.IS_GREATER_OR_EQUAL;
import static org.verapdf.policy.SchematronOperation.IS_LESS;
import static org.verapdf.policy.SchematronOperation.IS_LESS_OR_EQUAL;
import static org.verapdf.policy.SchematronOperation.IS_TRUE;
import static org.verapdf.policy.SchematronOperation.NOT_EQUAL;
import static org.verapdf.policy.SchematronOperation.NOT_PRESENT;
import static org.verapdf.policy.SchematronOperation.PRESENT;
import static org.verapdf.policy.SchematronOperation.STARTS_WITH;

import java.util.EnumSet;

import org.verapdf.policy.SchematronOperation;

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
		return this.featureName;
	}

	public String getFeatureXPath() {
		return this.featureXPath;
	}

	public FeatureType getFeatureType() {
		return this.featureType;
	}

	public enum FeatureType {
		BOOLEAN(PRESENT, NOT_PRESENT, IS_TRUE, IS_FALSE),
		NUMBER(PRESENT, NOT_PRESENT, IS_EQUAL,
				NOT_EQUAL, IS_GREATER, IS_GREATER_OR_EQUAL, IS_LESS,
				IS_LESS_OR_EQUAL),
		STRING(PRESENT, NOT_PRESENT, IS_EQUAL,
				NOT_EQUAL, STARTS_WITH, ENDS_WITH, CONTAINS);

		private EnumSet<SchematronOperation> legalOperations;

		FeatureType(SchematronOperation op, SchematronOperation... operations) {
			this.legalOperations = EnumSet.of(op, operations);
		}

		public EnumSet<SchematronOperation> getLegalOperations() {
			return EnumSet.copyOf(this.legalOperations);
		}
	}
}
