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
