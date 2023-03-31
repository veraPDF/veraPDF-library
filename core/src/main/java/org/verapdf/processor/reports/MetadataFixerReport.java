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
package org.verapdf.processor.reports;

import java.util.List;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Encapsulates the results fo applying the Metadata Fixer
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 9 Nov 2016:07:47:03
 */
@XmlJavaTypeAdapter(FixerReportImpl.Adapter.class)
public interface MetadataFixerReport {
	/**
	 * @return the status of the attempted repair as a {@link String}
	 */
	public String getStatus();

	/**
	 * @return the number of fixes attempted
	 */
	public int getFixCount();

	/**
	 * @return a {@link List} of {@link String}s detailing the fixes applied
	 */
	public List<String> getFixes();

	/**
	 * @return a {@link List} of {@link String}s detailing any errors
	 *         encountered during repair
	 */
	public List<String> getErrors();
}
