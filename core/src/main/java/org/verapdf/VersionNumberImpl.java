/**
 * 
 */
package org.verapdf;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 25 May 2017:22:01:04
 */

final class VersionNumberImpl implements SemanticVersionNumber {
	private static final String separator = "\\."; //$NON-NLS-1$
	private static final String pdfBoxBuildInfo = "-PDFBOX"; //$NON-NLS-1$
	private static final String snapshotBuildInfo = "-SNAPSHOT"; //$NON-NLS-1$
	private static final String versionPrefix = "v"; //$NON-NLS-1$
	private final int major;
	private final int minor;
	private final int revision;

	@SuppressWarnings("unused")
	private VersionNumberImpl(final String version) {
		this(version.split(separator));
	}

	private VersionNumberImpl(final String[] parts) {
		this(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
	}

	VersionNumberImpl(final int[] parts) {
		this(parts[0], parts[1], parts[2]);
	}

	VersionNumberImpl(final int major, final int minor, final int revision) {
		this.major = major;
		this.minor = minor;
		this.revision = revision;
	}

	@Override
	public String getVersionString() {
		return String.format("%d.%d.%d", Integer.valueOf(this.major), //$NON-NLS-1$
				Integer.valueOf(this.minor), Integer.valueOf(this.revision));
	}

	@Override
	public int getMajor() {
		return this.major;
	}

	@Override
	public int getMinor() {
		return this.minor;
	}

	@Override
	public int getRevision() {
		return this.revision;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.major;
		result = prime * result + this.minor;
		result = prime * result + this.revision;
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof VersionNumberImpl)) {
			return false;
		}
		VersionNumberImpl other = (VersionNumberImpl) obj;
		if (this.major != other.major) {
			return false;
		}
		if (this.minor != other.minor) {
			return false;
		}
		if (this.revision != other.revision) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getVersionString();
	}

	@Override
	public int compareTo(SemanticVersionNumber other) {
		return compare(this, other);
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
		return fromStrings(strippedVersion.split(separator));
	}

	public static SemanticVersionNumber fromStrings(final String[] parts) {
		if (parts == null)
			throw new IllegalArgumentException("Argument parts can not be null"); //$NON-NLS-1$
		int[] intParts = { 0, 0, 0 };
		for (int iLoop = 0; (iLoop < intParts.length) && (iLoop < parts.length); iLoop++) {
			try {
				intParts[iLoop] = Integer.parseInt(parts[iLoop]);
			} catch (NumberFormatException excep) {
				throw new IllegalArgumentException(
						String.format("NumberFormatException raised when converting \"%s\" to an int.", parts[iLoop]), excep); //$NON-NLS-1$
			}
		}
		return fromInts(intParts);
	}

	public static SemanticVersionNumber fromInts(final int[] parts) {
		if (parts == null)
			throw new IllegalArgumentException("Argument parts can not be null"); //$NON-NLS-1$
		if (parts.length != 3)
			throw new IllegalArgumentException("Argument parts must be a three part array"); //$NON-NLS-1$
		return fromInts(parts[0], parts[1], parts[2]);
	}

	public static SemanticVersionNumber fromInts(final int major, final int minor, final int revision) {
		if (major < 0)
			throw new IllegalArgumentException("Argument major can not be < 0"); //$NON-NLS-1$
		if (minor < 0)
			throw new IllegalArgumentException("Argument minor can not be < 0"); //$NON-NLS-1$
		if (revision < 0)
			throw new IllegalArgumentException("Argument revision can not be < 0"); //$NON-NLS-1$
		return new VersionNumberImpl(major, minor, revision);
	}

	private static int compare(final SemanticVersionNumber that, final SemanticVersionNumber other) {
		int majorDiff = that.getMajor() - other.getMajor();
		if (majorDiff != 0)
			return majorDiff;
		int minorDiff = that.getMinor() - other.getMinor();
		if (minorDiff != 0)
			return minorDiff;
		int revisionDiff = that.getRevision() - other.getRevision();
		if (revisionDiff != 0)
			return revisionDiff;
		return 0;
	}
}
