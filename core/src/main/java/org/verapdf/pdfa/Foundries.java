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
package org.verapdf.pdfa;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * Factory class that provides convenience methods for handling the veraPDF
 * Foundry.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 26 Oct 2016:22:00:38
 */

public class Foundries {
	private Foundries() {
	}

	private static final Map<URI, VeraFoundryProvider> providers = new ConcurrentHashMap<>();

	/**
	 * return the {@link URI} identifying the default
	 * {@link VeraFoundryProvider}.
	 */
	public static final URI DEFAULT_PROVIDER_ID = URI.create("http://foundry.verapdf.org#default"); //$NON-NLS-1$

	/**
	 * Register the default {@link VeraFoundryProvider}.
	 * 
	 * @param provider
	 *            the {@link VeraFoundryProvider} to register as the default.
	 */
	public static void registerDefaultProvider(final VeraFoundryProvider provider) {
		registerProvider(DEFAULT_PROVIDER_ID, provider);
	}

	/**
	 * Register a {@link VeraFoundryProvider} with a unique {@link URI}
	 * identifier.
	 * 
	 * @param id
	 *            a unique {@link URI} identifying the
	 *            {@link VeraFoundryProvider}.
	 * @param provider
	 *            the {@link VeraFoundryProvider} to register.
	 */
	public static void registerProvider(final URI id, final VeraFoundryProvider provider) {
		if (provider == null)
			throw new NullPointerException("Argument provider can not be null"); //$NON-NLS-1$
		providers.put(id, provider);
	}

	/**
	 * @return the default {@link VeraFoundryProvider} identified by
	 *         {@link Foundries#DEFAULT_PROVIDER_ID}.
	 */
	public static VeraPDFFoundry defaultInstance() {
		return newInstance(DEFAULT_PROVIDER_ID);
	}

	/**
	 * Obtain a new {@link VeraPDFFoundry} instance.
	 * 
	 * @param id
	 *            the {@link URI} that identifies the {@link VeraPDFFoundry}
	 * @return the {@link VeraPDFFoundry} corresponding to the supplied
	 *         {@link URI}
	 */
	public static VeraPDFFoundry newInstance(URI id) {
		VeraFoundryProvider provider = providers.get(id);
		if (provider == null)
			throw new IllegalArgumentException("No provider with URI:" + id); //$NON-NLS-1$
		return provider.getInstance();
	}

	/**
	 * Obtain all registered {@link VeraFoundryProvider} ids
	 * 
	 * @return a {@link Set} containing the {@link URI} ids for all registered
	 *         {@link VeraFoundryProvider}s.
	 */
	public static Set<URI> getProviderIds() {
		return Collections.unmodifiableSet(providers.keySet());
	}

	/**
	 * Set the default {@link PDFAFlavour} used as a fall back by the veraPDF library
	 * @param defaultFlavour the desired default {@link PDFAFlavour}
	 */
	public void setDefaultFlavour(PDFAFlavour defaultFlavour) {
		if (defaultFlavour != PDFAFlavour.NO_FLAVOUR)
			AbstractFoundry.defaultFlavour = defaultFlavour;
	}

	public static boolean defaultParserIsPDFBox() {
		VeraFoundryProvider provider = providers.get(DEFAULT_PROVIDER_ID);
		return provider != null && "PDFBox".equals(provider.getInstance().getParserId());
	}
}
