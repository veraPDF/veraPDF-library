/**
 * 
 */
package org.verapdf.pdfa;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
}
