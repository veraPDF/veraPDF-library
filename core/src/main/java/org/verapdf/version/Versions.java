/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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
		String strippedVersion = versionString;
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
