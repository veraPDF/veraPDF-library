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
package org.verapdf.component;

import java.net.URI;

import org.verapdf.ReleaseDetails;

/**
 * Factory class for veraPDF components and associated classes.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 27 Oct 2016:00:13:55
 */

public final class Components {
	private Components() {
	}

	/**
	 * @return the default {@link ComponentDetails} instance.
	 */
	public static ComponentDetails defaultDetails() {
		return ComponentDetailsImpl.defaultInstance();
	}

	/**
	 * Convenience method that creates a {@link ComponentDetails} instance with
	 * the veraPDF library version number and description.
	 * 
	 * @param id
	 *            a preferably unique {@link URI} that identifies the
	 *            {@link ComponentDetails}.
	 * @param name
	 *            the name to be assigned the {@link ComponentDetails}
	 * @return a new {@link ComponentDetails} instance populated using the
	 *         passed parameters
	 */
	public static ComponentDetails libraryDetails(final URI id, final String name) {
		return veraDetails(id, name, ReleaseDetails.getInstance().getVersion(), "Core veraPDF library component.");
	}

	/**
	 * Convenience method that creates a {@link ComponentDetails} instance with
	 * the veraPDF library description.
	 * 
	 * @param id
	 *            a preferably unique {@link URI} that identifies the
	 *            {@link ComponentDetails}.
	 * @param name
	 *            the name to be assigned the {@link ComponentDetails}
	 * @param description
	 *            the description to be assigned to the {@link ComponentDetails}
	 *            instance.
	 * @return a new {@link ComponentDetails} instance populated using the
	 *         passed parameters
	 */
	public static ComponentDetails veraDetails(final URI id, final String name, final String version,
			final String description) {
		return detailsFromValues(id, name, version, "The veraPDF Consortium.", description);
	}

	/**
	 * Create a new new {@link ComponentDetails} instance from the passed values
	 * 
	 * @param id
	 *            a unique {@link URI} identifying the {@link Component}
	 * @param name
	 *            a {@link String} name for the {@link Component}
	 * @param version
	 *            the version of the {@link Component}
	 * @param provider
	 *            the provider of the {@link Component}
	 * @param description
	 *            a description for the {@link Component}
	 * @return a new {@link Component} instance populated from the passed
	 *         values.
	 */
	public static ComponentDetails detailsFromValues(final URI id, final String name, final String version,
			final String provider, final String description) {
		if (id == null)
			throw new NullPointerException(nullMessage("id")); //$NON-NLS-1$
		if (name == null)
			throw new NullPointerException(nullMessage("name")); //$NON-NLS-1$
		if (version == null)
			throw new NullPointerException(nullMessage("version")); //$NON-NLS-1$
		if (provider == null)
			throw new NullPointerException(nullMessage("provider")); //$NON-NLS-1$
		if (description == null)
			throw new NullPointerException(nullMessage("description")); //$NON-NLS-1$
		if (name.isEmpty())
			throw new IllegalArgumentException(emptyMessage("name")); //$NON-NLS-1$
		if (version.isEmpty())
			throw new IllegalArgumentException(emptyMessage("version")); //$NON-NLS-1$
		if (provider.isEmpty())
			throw new IllegalArgumentException(emptyMessage("provider")); //$NON-NLS-1$
		if (description.isEmpty())
			throw new IllegalArgumentException(emptyMessage("description")); //$NON-NLS-1$
		return ComponentDetailsImpl.fromValues(id, name, version, provider, description);
	}

	/**
	 * @return the default {@link AuditDuration} instance
	 */
	public static AuditDuration defaultDuration() {
		return AuditDurationImpl.defaultInstance();
	}

	private static String nullMessage(final String name) {
		return makeMessage(name, "null"); //$NON-NLS-1$
	}

	private static String emptyMessage(final String name) {
		return makeMessage(name, "empty"); //$NON-NLS-1$
	}

	private static String makeMessage(final String name, final String suffix) {
		return "Argument " + name + " can not be " + suffix + "."; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Convenience factory class to facilitate the creation of
	 * {@link AuditDuration}s. Create a new {@link Timer} instance using the
	 * {@link Timer#start} method, then halt the {@link Timer} with the
	 * {@link Timer#stop()} method which returns an {@link AuditDuration}
	 * instance.
	 * 
	 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
	 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
	 * @version 0.1
	 */
	public static class Timer {
		private final long start;

		private Timer() {
			super();
			this.start = System.currentTimeMillis();
		}

		/**
		 * Halt the {@link Timer} and return and {@link AuditDuration}.
		 * 
		 * @return an {@link AuditDuration} that holds the timed interval
		 *         details.
		 */
		public AuditDuration stop() {
			return AuditDurationImpl.fromValues(this.start, System.currentTimeMillis());
		}

		/**
		 * Start timing a duration.
		 * 
		 * @return a new Timer instance with start time set.
		 */
		public static Timer start() {
			return new Timer();
		}
	}

}
