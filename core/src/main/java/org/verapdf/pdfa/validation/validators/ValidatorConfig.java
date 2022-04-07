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
package org.verapdf.pdfa.validation.validators;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.logging.Level;

/**
 * Encapsulates the configuration of the veraPDF PDF/A validator. An instance of
 * this class is passed to the validator to control PDF/A validation behaviour.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 26 Oct 2016:00:04:41
 */
@XmlJavaTypeAdapter(ValidatorConfigImpl.Adapter.class)
public interface ValidatorConfig {
	/**
	 * Indicates whether the validator is configured to record passed checks.
	 * 
	 * @return true if passed checks should be recorded, false if only failed
	 *         checks should be recorde.
	 */
	public boolean isRecordPasses();

	/**
	 * The maximum number of failed validation checks encountered before
	 * validation is terminated.
	 * 
	 * @return the number of failed validation checks before validation is
	 *         terminated.
	 */
	public int getMaxFails();

	/**
	 * Obtain the particular PDF/A specification that the validator enforces.
	 * 
	 * @return the {@link PDFAFlavour} that the validator enforces.
	 */
	public PDFAFlavour getFlavour();

	public PDFAFlavour getDefaultFlavour();

	public boolean isDebug();

	public boolean isLogsEnabled();

	public Level getLoggingLevel();

	public int getMaxNumberOfDisplayedFailedChecks();
}
