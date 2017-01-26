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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.verapdf.pdfa.results.TestAssertion;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 10 Nov 2016:08:51:41
 */

final class CheckImpl implements Check {
	@XmlAttribute
	private final String status;
	@XmlElement
	private final String context;

	private CheckImpl(final TestAssertion.Status status, final String context) {
		this.status = status.toString();
		this.context = context;
	}

	private CheckImpl() {
		this(TestAssertion.Status.PASSED, "");
	}
	
	/**
	 * @return the status
	 */
	@Override
	public String getStatus() {
		return this.status;
	}

	/**
	 * @return the context
	 */
	@Override
	public String getContext() {
		return this.context;
	}

	static class Adapter extends XmlAdapter<CheckImpl, Check> {
		@Override
		public Check unmarshal(CheckImpl check) {
			return check;
		}

		@Override
		public CheckImpl marshal(Check check) {
			return (CheckImpl) check;
		}
	}


	static final Check fromValue(final TestAssertion assertion) {
		if (assertion == null) {
			throw new IllegalArgumentException("Argument assertion con not be null");
		}
		return new CheckImpl(assertion.getStatus(), assertion.getLocation().getContext());
	}
}
