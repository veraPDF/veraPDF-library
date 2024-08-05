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

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 17 Apr 2017:21:52:05
 */

public final class FileUtils {
	private static final String dot = "."; //$NON-NLS-1$
	private static final String extensionRegEx = "\\.(?=[^\\.]+$)"; //$NON-NLS-1$

	public static String extFromFileName(final String filename) {
		if (!filename.contains(dot)) {
			return ""; //$NON-NLS-1$
		}
		String[] nameParts = filename.split(extensionRegEx);
		if (nameParts.length > 0) {
			return nameParts[nameParts.length - 1];
		}
		return ""; //$NON-NLS-1$
	}

	public static boolean hasExtNoCase(final String fileName, final String ext) {
		return hasExt(fileName.toLowerCase(), ext.toLowerCase());
	}

	public static boolean hasExt(final String fileName, final String ext) {
		final String fullExt = ext.startsWith(dot) ? ext : dot + ext;
		return fileName.endsWith(fullExt);
	}

	/**
	 * Method to add a file extension to a given path if it doesn't have it and
	 * return the result.
	 * 
	 * @param path
	 *            a {@link String} that carries the file name or path to add the
	 *            extension to.
	 * @param ext
	 *            the extension to add if not present, this should be the
	 *            letters only, the dot separator '.' is not required.
	 * @return the path with the extension added
	 */
	public static String addExt(final String path, final String ext) {
		int lastDot = path.lastIndexOf(dot);
		int lastSep = Math.max(path.lastIndexOf('\\'), path.lastIndexOf('/'));
		return (lastDot > lastSep) ? path.replaceAll(path.substring(lastDot + 1) + '$', ext) : path + dot + ext;
	}

}
