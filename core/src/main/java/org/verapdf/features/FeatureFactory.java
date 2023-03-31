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
/**
 * 
 */
package org.verapdf.features;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumSet;

import jakarta.xml.bind.JAXBException;

import org.verapdf.core.XmlSerialiser;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 30 Oct 2016:01:14:07
 */

public final class FeatureFactory {
	private FeatureFactory() {

	}

	public static FeatureExtractorConfig defaultConfig() {
		return FeatureExtractorConfigImpl.defaultInstance();
	}

	public static FeatureExtractorConfig configFromValues(final EnumSet<FeatureObjectType> enabledFeatures) {
		if (enabledFeatures == null)
			throw new NullPointerException("Arg enabledFeatures can not be null");
		return FeatureExtractorConfigImpl.fromFeatureSet(enabledFeatures);
	}

	/**
	 * Converts XML file to PluginsCollectionConfig,
	 * 
	 * @see jakarta.xml.bind.JAXB for more details
	 */
	public static FeatureExtractorConfig configFromXml(final InputStream source) throws JAXBException {
		if (source == null)
			throw new NullPointerException("Arg source can not be null");
		return XmlSerialiser.typeFromXml(FeatureExtractorConfigImpl.class, source);
	}

	public static void configToXml(final FeatureExtractorConfig config, final OutputStream dest) throws JAXBException {
		if (config == null)
			throw new NullPointerException("Arg config can not be null");
		if (dest == null)
			throw new NullPointerException("Arg dest can not be null");
		XmlSerialiser.toXml(config, dest, true, false);
	}
}
