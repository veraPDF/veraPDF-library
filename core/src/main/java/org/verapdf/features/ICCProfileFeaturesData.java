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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Features data of an iccprofile for feature extractor
 *
 * @author Maksim Bezrukov
 */
public final class ICCProfileFeaturesData extends FeaturesData {

	private final InputStream metadata;
	private final Integer n;
	private final List<Double> range;

	private ICCProfileFeaturesData(InputStream metadata, InputStream stream, Integer n, List<Double> range) {
		super(stream);
		this.metadata = metadata;
		this.n = n;
		this.range = range == null ? null : new ArrayList<>(range);
	}

	/**
	 * Creates ICCProfileFeaturesData
	 *
	 * @param metadata metadata stream
	 * @param stream   object stream
	 * @param n        parameter N from the iccprofile dictionary
	 * @param range    parameter Range from the iccprofile dictionary
	 */
	public static ICCProfileFeaturesData newInstance(InputStream metadata, InputStream stream, Integer n, List<Double> range) {
		if (stream == null) {
			throw new IllegalArgumentException("ICCProfile stream can not be null");
		}
		return new ICCProfileFeaturesData(metadata, stream, n, range);
	}

	/**
	 * @return metadata stream
	 */
	public InputStream getMetadata() {
		return this.metadata;
	}

	/**
	 * @return parameter N from the iccprofile dictionary
	 */
	public Integer getN() {
		return this.n;
	}

	/**
	 * @return parameter Range from the iccprofile dictionary
	 */
	public List<Double> getRange() {
		return this.range == null ? null : Collections.unmodifiableList(this.range);
	}
}
