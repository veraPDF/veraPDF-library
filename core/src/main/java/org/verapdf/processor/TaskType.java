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
package org.verapdf.processor;

/**
 * @author Evgeniy Muravitskiy
 */
public enum TaskType {
	NONE("NONE"), PARSE("parsing", "PDF Parsing"), VALIDATE("validation", "PDF/A Validation"), EXTRACT_FEATURES(
			"features", "Feature Extraction"), FIX_METADATA("metadata", "Metadata Repair");

	private final String value;
	private final String fullName;

	TaskType(final String value) {
		this(value, value);
	}

	TaskType(final String value, final String fullName) {
		this.value = value;
		this.fullName = fullName;
	}

	public static TaskType fromString(final String toParse) {
		for (TaskType processingType : TaskType.values()) {
			if (processingType.toString().equalsIgnoreCase(toParse))
				return processingType;
		}
		throw new IllegalArgumentException("String can't be parsed into ProcessingType");
	}

	public String getValue() {
		return this.value;
	}

	public String fullName() {
		return this.fullName;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
