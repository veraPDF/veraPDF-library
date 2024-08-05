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
/**
 * 
 */
package org.verapdf.metadata.fixer;

import java.io.InputStream;
import java.io.OutputStream;

import jakarta.xml.bind.JAXBException;

import org.verapdf.core.XmlSerialiser;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.MetadataFixerResultImpl;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 30 Oct 2016:01:30:05
 */

public final class FixerFactory {
	private static final MetadataFixerResult defaultResult = new MetadataFixerResultImpl.Builder().build();
	private FixerFactory() {

	}

	public static MetadataFixerConfig defaultConfig() {
		return FixerConfigImpl.defaultInstance();
	}

	public static MetadataFixerConfig configFromValues(final String fixesPrefix) {
		return FixerConfigImpl.fromValues(fixesPrefix);
	}

	public static MetadataFixerConfig configFromXml(final InputStream source) throws JAXBException {
		return XmlSerialiser.typeFromXml(FixerConfigImpl.class, source);
	}
	
	public static void configToXml(MetadataFixerConfig config, final OutputStream dest) throws JAXBException {
		XmlSerialiser.toXml(config, dest, true, false);
	}

	public static MetadataFixerResult defaultResult() {
		return defaultResult;
	}
}
