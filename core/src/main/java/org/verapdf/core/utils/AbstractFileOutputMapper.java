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

import org.verapdf.core.VeraPDFException;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 13 Nov 2016:10:47:10
 */

abstract class AbstractFileOutputMapper implements FileOutputMapper {
	protected static final String defaultPrefix = "veraPDF_";
	protected static final String defaultSuffix = "";
	protected static final String extSplitter = ".";
	private final String prefix;
	private final String suffix;

	protected AbstractFileOutputMapper() {
		this(defaultPrefix);
	}

	protected AbstractFileOutputMapper(final String prefix) {
		this(prefix, defaultSuffix);
	}

	/**
	 * 
	 */
	protected AbstractFileOutputMapper(final String prefix, final String suffix) {
		super();
		this.prefix = prefix;
		this.suffix = suffix;
	}

	@Override
	public String getPrefix() {
		return this.prefix;
	}

	@Override
	public String getSuffix() {
		return this.suffix;
	}

	@Override
	public File mapFile(final File orig) throws VeraPDFException {
		return doMapFile(orig);
	}

	protected abstract File doMapFile(final File orig) throws VeraPDFException;

	protected static String addPrefixAndSuffix(final File orig, final FileOutputMapper mapper) {
		if (orig == null)
			throw new NullPointerException("Arg orig can not be null.");
		String filename = orig.getName();
		String newName = mapper.getPrefix() + filename;
		if (!mapper.getSuffix().isEmpty() && !isExtension(mapper) && hasExtension(filename)) {
			// If we have a suffix and an extensions
			newName = newName.substring(0, newName.lastIndexOf(extSplitter)) + mapper.getSuffix()
					+ filename.substring(filename.lastIndexOf(extSplitter));
		} else
			// else it's safe to add the suffix as is
			newName = newName + mapper.getSuffix();
		return newName;
	}

	private static boolean isExtension(FileOutputMapper toTest) {
		return toTest.getSuffix().startsWith(".");
	}

	private static boolean hasExtension(final String filename) {
		return filename.lastIndexOf(extSplitter) > 0;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AbstractFileOutputMapper [prefix=" + this.prefix + ", suffix=" + this.suffix + "]";
	}
}
