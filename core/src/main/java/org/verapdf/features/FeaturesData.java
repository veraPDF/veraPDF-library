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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Features data of an object for feature extractor
 *
 * @author Maksim Bezrukov
 */
public class FeaturesData implements Closeable {

	private static final Logger LOGGER = Logger.getLogger(FeaturesData.class.getCanonicalName());

	private final InputStream stream;

	/**
	 * Constructs new FeaturesData
	 *
	 * @param stream     object stream
	 */
	protected FeaturesData(InputStream stream) {
		this.stream = stream;
	}

	/**
	 * @return stream of object
	 */
	public InputStream getStream() {
		return this.stream;
	}

	@Override
	public void close() throws IOException {
		if (this.stream != null) {
			try {
				this.stream.close();
			} catch (IOException e) {
				LOGGER.log(Level.FINE, "Exception during stream closing", e);
			}
		}
	}
}
