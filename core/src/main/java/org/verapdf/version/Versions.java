/**
 * 
 */
package org.verapdf.version;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 26 May 2017:01:48:14
 */

public final class Versions {
	public static final String PDFBOX_BUILD_INFO = "PDFBOX"; //$NON-NLS-1$
	private static final String pdfBoxBuildInfo = "-" + PDFBOX_BUILD_INFO; //$NON-NLS-1$
	private static final String snapshotBuildInfo = "-SNAPSHOT"; //$NON-NLS-1$
	private static final String versionPrefix = "v"; //$NON-NLS-1$

	/**
	 * 
	 */
	private Versions() {
		throw new AssertionError("Should never be here"); //$NON-NLS-1$
	}

	public static SemanticVersionNumber fromString(final String versionString) {
		if (versionString == null)
			throw new IllegalArgumentException("Argument versionString can not be null"); //$NON-NLS-1$
		if (versionString.isEmpty())
			throw new IllegalArgumentException("Argument versionString can not be empty"); //$NON-NLS-1$
		String strippedVersion = (versionString.endsWith(pdfBoxBuildInfo)) ? versionString.replace(pdfBoxBuildInfo, "") //$NON-NLS-1$
				: versionString;
		strippedVersion = (strippedVersion.endsWith(snapshotBuildInfo)) ? strippedVersion.replace(snapshotBuildInfo, "") //$NON-NLS-1$
				: strippedVersion;
		strippedVersion = strippedVersion.startsWith(versionPrefix) ? strippedVersion.replaceFirst(versionPrefix, "") //$NON-NLS-1$
				: strippedVersion;
		return VersionNumberImpl.fromString(strippedVersion);
	}

	public static SemanticVersionNumber fromStrings(final String[] parts) {
		return VersionNumberImpl.fromStrings(parts);
	}

	public static SemanticVersionNumber fromInts(final int[] parts) {
		return VersionNumberImpl.fromInts(parts);
	}

	public static SemanticVersionNumber fromInts(final int major, final int minor, final int revision) {
		return VersionNumberImpl.fromInts(major, minor, revision);
	}

}
