/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
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
 * @version 0.1 Created 13 Nov 2016:22:14:34
 */

final class RelativeRootMapper extends AbstractFileOutputMapper {
	private final Path srcRoot;
	private final Path destRoot;

	private RelativeRootMapper(final Path src, final Path dest) {
		this(src, dest, defaultPrefix);
	}

	private RelativeRootMapper(final Path src, final Path dest, final String prefix) {
		this(src, dest, prefix, defaultSuffix);
	}

	private RelativeRootMapper(final Path src, final Path dest, final String prefix, final String suffix) {
		super(prefix, suffix);
		this.srcRoot = src.normalize();
		this.destRoot = dest.normalize();
	}

	@Override
	protected File doMapFile(File orig) throws VeraPDFException {
		String newName = addPrefixAndSuffix(orig, this);
		return makeNewFile(orig, this.srcRoot, this.destRoot, newName);
	}

	private static File makeNewFile(final File orig, final Path src, final Path dest, String newName)
			throws VeraPDFException {
		Path relSrc = orig.getParentFile().toPath().relativize(src);
		Path absDest = dest.resolve(relSrc);
		File parent = absDest.toFile();
		if (!parent.exists() && !parent.mkdirs()) {
			throw new VeraPDFException("Cannot create directory: " + parent);
		}
		return new File(parent, newName);
	}

	static RelativeRootMapper fromValues(final Path src, final Path dest, final String prefix, final String suffix) {
		return new RelativeRootMapper(src, dest, prefix, suffix);
	}
}
