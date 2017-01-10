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
package org.verapdf.pdfa;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 26 Oct 2016:22:00:38
 */

public class Foundries {
	private Foundries() {
	}

	private static Map<URI, VeraFoundryProvider> providers = new ConcurrentHashMap<>();
	public static final URI DEFAULT_PROVIDER_ID = URI.create("http://foundry.verapdf.org#default");

	public static void registerDefaultProvider(final VeraFoundryProvider provider) {
		registerProvider(DEFAULT_PROVIDER_ID, provider);
	}

	public static void registerProvider(final URI id, final VeraFoundryProvider provider) {
		if (provider == null)
			throw new NullPointerException("Argument provider can not be null");
		providers.put(id, provider);
	}

	public static VeraPDFFoundry defaultInstance() {
		return newInstance(DEFAULT_PROVIDER_ID);
	}

	public static VeraPDFFoundry newInstance(URI id) {
		VeraFoundryProvider provider = providers.get(id);
		if (provider == null)
			throw new IllegalArgumentException("No provider with URI:" + id);
		return provider.getInstance();
	}

	public static Set<URI> getProviderIds() {
		return Collections.unmodifiableSet(providers.keySet());
	}

	public void setDefaultFlavour(PDFAFlavour defaultFlavour) {
		if (defaultFlavour != PDFAFlavour.NO_FLAVOUR)
			AbstractFoundry.defaultFlavour = defaultFlavour;
	}
}
