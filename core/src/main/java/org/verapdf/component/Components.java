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
		return veraDetails(id, name, ReleaseDetails.getInstance().getVersion());
	}

	public static ComponentDetails veraDetails(final URI id, final String name, final String version) {
		return detailsFromValues(id, name, version, "The veraPDF Consortium", ReleaseDetails.getInstance().getRights());
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
