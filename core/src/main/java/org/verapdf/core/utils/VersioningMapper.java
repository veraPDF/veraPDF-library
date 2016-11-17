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
	private final static String verPrefixOpen = "_("; //$NON-NLS-1$
	private final static String verPrefixClose = ")"; //$NON-NLS-1$
	final static VersioningMapper defaultInstance = new VersioningMapper();
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
			maxVersion = (vers > maxVersion) ? vers : maxVersion;
		}
		Integer version = Integer.valueOf(maxVersion + 1);
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
		final String verStart = (origName.lastIndexOf(".") < 1) ? origName + verPrefixOpen //$NON-NLS-1$
				: origName.substring(0, origName.lastIndexOf(".")) + verPrefixOpen; //$NON-NLS-1$
		return verStart;
	}

	static String verEnd(final File orig) {
		final String origName = orig.getName();
		final String verEnd = (origName.lastIndexOf(".") < 1) ? verPrefixClose //$NON-NLS-1$
				: verPrefixClose + origName.substring(origName.lastIndexOf(".")); //$NON-NLS-1$
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
