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
package org.verapdf.component;

import java.net.URI;

import org.verapdf.ReleaseDetails;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 27 Oct 2016:00:13:55
 */

public final class Components {
	private Components() {
	}

	public static ComponentDetails defaultDetails() {
		return ComponentDetailsImpl.defaultInstance();
	}

	public static ComponentDetails libraryDetails(final URI id, final String name) {
		return veraDetails(id, name, ReleaseDetails.getInstance().getVersion(), "Core veraPDF library component.");
	}

	public static ComponentDetails veraDetails(final URI id, final String name, final String version, final String description) {
		return detailsFromValues(id, name, version, "The veraPDF Consortium.", description);
	}

	public static ComponentDetails detailsFromValues(final URI id, final String name, final String version,
			final String provider, final String description) {
		if (id == null)
			throw new NullPointerException(nullMessage("id"));
		if (name == null)
			throw new NullPointerException(nullMessage("name"));
		if (version == null)
			throw new NullPointerException(nullMessage("version"));
		if (provider == null)
			throw new NullPointerException(nullMessage("provider"));
		if (description == null)
			throw new NullPointerException(nullMessage("description"));
		if (name.isEmpty())
			throw new IllegalArgumentException(emptyMessage("name"));
		if (version.isEmpty())
			throw new IllegalArgumentException(emptyMessage("version"));
		if (provider.isEmpty())
			throw new IllegalArgumentException(emptyMessage("provider"));
		if (description.isEmpty())
			throw new IllegalArgumentException(emptyMessage("description"));
		return ComponentDetailsImpl.fromValues(id, name, version, provider, description);
	}
	
	public static AuditDuration defaultDuration() {
		return AuditDurationImpl.defaultInstance();
	}

	private static String nullMessage(final String name) {
		return makeMessage(name, "null");
	}

	private static String emptyMessage(final String name) {
		return makeMessage(name, "empty");
	}

	private static String makeMessage(final String name, final String suffix) {
		return "Argument " + name + " can not be " + suffix + ".";
	}

	public static class Timer {
		private final long start;

		private Timer() {
			super();
			this.start = System.currentTimeMillis();
		}

		public AuditDuration stop() {
			return AuditDurationImpl.fromValues(this.start, System.currentTimeMillis());
		}

		public static Timer start() {
			return new Timer();
		}
	}

}
