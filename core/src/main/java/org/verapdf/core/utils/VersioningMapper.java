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
package org.verapdf.core.utils;

import java.io.File;
import java.io.FilenameFilter;

import org.verapdf.core.VeraPDFException;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 13 Nov 2016:23:09:37
 */

final class VersioningMapper implements FileOutputMapper {
	private static final String verPrefixOpen = "_("; //$NON-NLS-1$
	private static final String verPrefixClose = ")"; //$NON-NLS-1$
	static final VersioningMapper defaultInstance = new VersioningMapper();
	protected final FileOutputMapper mapper;

	private VersioningMapper() {
		this(RelativeDirectoryMapper.defaultInstance());
	}

	private VersioningMapper(final FileOutputMapper mapper) {
		super();
		this.mapper = mapper;
	}

	@Override
	public String getPrefix() {
		return this.mapper.getPrefix();
	}

	@Override
	public String getSuffix() {
		return this.mapper.getSuffix();
	}

	@Override
	public File mapFile(final File orig) throws VeraPDFException {
		return version(this.mapper.mapFile(orig));
	}

	private static File version(final File toCheck) {
		if (!toCheck.exists()) {
			return toCheck;
		}
		return newVersion(toCheck);
	}

	private static File newVersion(final File orig) {
		int maxVersion = -1;
		for (File version : getVersions(orig)) {
			int vers = getVersion(orig, version);
			maxVersion = Math.max(vers, maxVersion);
		}
		Integer version = maxVersion + 1;
		String versionedName = String.format("%s%d%s", verStart(orig), version, verEnd(orig)); //$NON-NLS-1$
		return new File(orig.getParentFile(), versionedName);
	}

	static int getVersion(final File orig, final File version) {
		String verPart = version.getName().replace(verStart(orig), "").replace(verEnd(orig), ""); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			return Integer.parseInt(verPart);
		} catch (@SuppressWarnings("unused") NumberFormatException excep) {
			return -1;
		}
	}

	static VersioningMapper defaultInstance() {
		return defaultInstance;
	}

	static VersioningMapper newInstance(final FileOutputMapper mapper) {
		return new VersioningMapper(mapper);
	}

	static String verStart(final File orig) {
		final String origName = orig.getName();
		final String verStart = (origName.lastIndexOf('.') < 1 ? origName : origName.substring(0, origName.lastIndexOf('.'))) + verPrefixOpen; //$NON-NLS-1$
		return verStart;
	}

	static String verEnd(final File orig) {
		final String origName = orig.getName();
		final String verEnd = (origName.lastIndexOf('.') < 1) ? verPrefixClose //$NON-NLS-1$
				: verPrefixClose + origName.substring(origName.lastIndexOf('.')); //$NON-NLS-1$
		return verEnd;
	}

	private static File[] getVersions(final File orig) {
		File parent = orig.getParentFile();
		File[] versions = parent.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {

				if (!name.startsWith(verStart(orig)))
					return false;
				if (!name.endsWith(verEnd(orig)))
					return false;
				return getVersion(orig, new File(dir, name)) >= 0;
			}
		});
		return versions;
	}
}
