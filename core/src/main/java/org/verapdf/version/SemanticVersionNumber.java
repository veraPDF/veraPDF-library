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
 * Simple interface for a <a href="http://semver.org/">semantic version
 * number</a>. Currently ignores pre-release and build metadata 
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 25 May 2017:21:49:36
 */
public interface SemanticVersionNumber extends Comparable<SemanticVersionNumber> {
	/**
	 * @return the version number as a String, that is major.minor.patch
	 */
	public String getVersionString();

	/**
	 * @return the int value of the major version number
	 */
	public int getMajor();

	/**
	 * @return the int value of the minor version number
	 */
	public int getMinor();

	/**
	 * @return the int value of the patch number
	 */
	public int getPatch();
}
