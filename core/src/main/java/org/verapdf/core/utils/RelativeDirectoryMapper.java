/**
 * 
 */
package org.verapdf.core.utils;

import java.io.File;
import java.nio.file.Path;

import org.verapdf.core.VeraPDFException;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 13 Nov 2016:10:46:22
 */

final class RelativeDirectoryMapper extends AbstractFileOutputMapper {
	static final String defaultRelativePath = ".";
	private final static RelativeDirectoryMapper defaultInstance = new RelativeDirectoryMapper();
	private final String relDirPath;

	private RelativeDirectoryMapper() {
		this(defaultRelativePath);
	}

	private RelativeDirectoryMapper(final String relativePath) {
		this(relativePath, defaultPrefix);
	}

	private RelativeDirectoryMapper(final String relativePath, final String prefix) {
		this(relativePath, prefix, defaultSuffix);
	}

	private RelativeDirectoryMapper(final String relativePath, final String prefix, final String suffix) {
		super(prefix, suffix);
		this.relDirPath = relativePath;
	}

	@Override
	protected File doMapFile(final File orig) throws VeraPDFException {
		String newName = addPrefixAndSuffix(orig, this);
		return makeNewFile(orig, this.relDirPath, newName);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RelativeDirectoryMapper [prefix=" + this.getPrefix() + ", suffix=" + this.getSuffix() + ",relDirPath="
				+ this.relDirPath + "]";
	}

	private static File makeNewFile(final File orig, final String relPath, String newName) throws VeraPDFException {
		File parent = orig.getParentFile();
		if (!relPath.equals(defaultRelativePath)) {
			Path parentPath = parent.toPath();
			Path resolvedPath = parentPath.resolve(relPath);
			parent = resolvedPath.toFile();
			if (!parent.exists() && !parent.mkdirs()) {
				throw new VeraPDFException("Cannot create directory: " + parent);
			}
		}
		return new File(parent, newName);
	}

	static final RelativeDirectoryMapper defaultInstance() {
		return defaultInstance;
	}

	static final RelativeDirectoryMapper withPrefixAndSuffix(final String prefix, final String suffix) {
		return new RelativeDirectoryMapper(defaultRelativePath, prefix, suffix);
	}

	static final RelativeDirectoryMapper fromValues(final String relativePath, final String prefix,
			final String suffix) {
		return new RelativeDirectoryMapper(relativePath, prefix, suffix);
	}
}
