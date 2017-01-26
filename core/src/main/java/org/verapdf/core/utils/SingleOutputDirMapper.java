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

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 16 Nov 2016:07:19:45
 */

public class SingleOutputDirMapper extends AbstractFileOutputMapper {
	private final File outputDir;

	private SingleOutputDirMapper(final File outputDir) {
		this(outputDir, defaultPrefix);
	}

	private SingleOutputDirMapper(final File outputDir, final String prefix) {
		this(outputDir, prefix, defaultSuffix);
	}

	private SingleOutputDirMapper(final File outputDir, final String prefix, final String suffix) {
		super(prefix, suffix);
		this.outputDir = outputDir;
	}

	@Override
	protected File doMapFile(File orig) {
		String newName = addPrefixAndSuffix(orig, this);
		return new File(this.outputDir, newName);
	}

	static SingleOutputDirMapper fromValues(final File outputDir, final String prefix, final String suffix) {
		return new SingleOutputDirMapper(outputDir, prefix, suffix);
	}
}
