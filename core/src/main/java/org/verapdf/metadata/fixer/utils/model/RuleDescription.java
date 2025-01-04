/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.metadata.fixer.utils.model;

/**
 * Class represent description of the rule. Each rule must be
 * defined by applied object type and test condition
 * or null test (to define all tests for specific object).
 *
 * @author Evgeniy Muravitskiy
 */
public class RuleDescription {

	private final String objectType;
	private final String test;

	/**
	 * Default constructor.
	 *
	 * @param test       test condition for current object type
	 * @param objectType applied object type
	 */
	public RuleDescription(String test, String objectType) {
		this.test = test;
		this.objectType = objectType;
	}

	/**
	 * @return applied object type
	 */
	public String getObjectType() {
		return this.objectType;
	}

	/**
	 * @return test condition for current object type
	 */
	public String getTest() {
		return this.test;
	}

	/**
	 * Compare string representation of rule description
	 * with current rule description
	 *
	 * @param toCompareType passed object type of another rule description
	 * @param toCompareTest passed test of another rule description
	 *
	 * @return true if {@code objectType} and {@code test} are match
	 */
	public boolean compareTo(String toCompareType, String toCompareTest) {
		return this.objectType.equals(toCompareType) && (this.test == null || this.test.equals(toCompareTest));
	}
}
