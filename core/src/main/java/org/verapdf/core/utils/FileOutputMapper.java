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
 * Simple behaviour for mapping file outputs for processor. Rules are simple,
 * set up with a prefix and/or suffix. A prefix is added to the beginning of the
 * file name, a suffix is added either:
 * <ul>
 * <li>Suffixes starting with any character other than a period: '.' character
 * are inserted before the last filename extension, if it exists.</li>
 * <li>Suffixes starting with a period: '.' are appended to the filename after
 * the extension.</li>
 * </ul>
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 13 Nov 2016:10:33:17
 */

public interface FileOutputMapper {
	/**
	 * @return the prefix added to the mapped file name
	 */
	public String getPrefix();

	/**
	 * @return the suffix appended to the mapped name
	 */
	public String getSuffix();

	/**
	 * Map an original source file to an output file using the getPrefix() as
	 * the file name prefix and get suffix as the suffix. The location of the
	 * final file will be subject to the mapping rules of the particular
	 * implementation. Note that the file returned won't necessarily exist and
	 * will be untested regards write capability and the like.
	 * 
	 * @param orig
	 * @return the {@link File} object for the mapped file.
	 * @throws VeraPDFException
	 *             if there's a problem creating the mapped file
	 */
	public File mapFile(File orig) throws VeraPDFException;
}
