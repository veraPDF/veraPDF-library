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

import java.util.Set;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 10 Nov 2016:08:19:46
 */
@XmlJavaTypeAdapter(ValidationDetailsImpl.Adapter.class)
public interface ValidationDetails {
	/**
	 * @return the number of rules for which all checks passed during validation
	 */
	public int getPassedRules();

	/**
	 * @return the number of rules for which at least one check failed during
	 *         validation
	 */
	public int getFailedRules();

	/**
	 * @return the number of successful checks during validation
	 */
	public int getPassedChecks();

	/**
	 * @return the number of failed checks during validation
	 */
	public int getFailedChecks();

	/**
	 * @return the {@link Set} of {@link RuleSummary} for rules used during
	 *         validation
	 */
	public Set<RuleSummary> getRuleSummaries();
}
