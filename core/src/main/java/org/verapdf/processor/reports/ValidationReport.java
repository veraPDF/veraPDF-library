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
package org.verapdf.processor.reports;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 9 Nov 2016:07:43:07
 */
@XmlJavaTypeAdapter(ValidationReportImpl.Adapter.class)
public interface ValidationReport {
	/**
	 * @return the name of the
	 *         {@link org.verapdf.pdfa.validation.profiles.ValidationProfile}
	 *         used to validate
	 */
	public String getProfileName();

	/**
	 * @return the {@link ValidationDetails} for the validation task
	 */
	public ValidationDetails getDetails();

	/**
	 * @return the validation statement
	 */
	public String getStatement();

	/**
	 * @return true if the PDF was compliant with the profile
	 */
	public boolean isCompliant();

	public String getJobEndStatus();
}
