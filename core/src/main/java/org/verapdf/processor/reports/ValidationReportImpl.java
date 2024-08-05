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
package org.verapdf.processor.reports;

import org.verapdf.processor.reports.enums.JobEndStatus;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 10 Nov 2016:08:18:17
 */
@XmlRootElement(name = "validationReport")
final class ValidationReportImpl implements ValidationReport {
	private static final ValidationReportImpl defaultInstance = new ValidationReportImpl();
	@XmlElement
	private final ValidationDetails details;
	@XmlAttribute
	private final String jobEndStatus;
	@XmlAttribute
	private final String profileName;
	@XmlAttribute
	private final String statement;
	@XmlAttribute
	private final boolean isCompliant;

	private ValidationReportImpl() {
		this(ValidationDetailsImpl.defaultInstance(), "Unknown Profile", "Statement", //$NON-NLS-1$ //$NON-NLS-2$
		     false, JobEndStatus.NORMAL.getValue());
	}

	private ValidationReportImpl(final ValidationDetails details, final String profileName, final String statement,
	                             final boolean isCompliant, final String jobEndStatus) {
		super();
		this.details = details;
		this.profileName = profileName;
		this.statement = statement;
		this.isCompliant = isCompliant;
		this.jobEndStatus = jobEndStatus;
	}

	@Override
	public ValidationDetails getDetails() {
		return this.details;
	}

	@Override
	public String getProfileName() {
		return this.profileName;
	}

	@Override
	public String getStatement() {
		return this.statement;
	}

	@Override
	public boolean isCompliant() {
		return this.isCompliant;
	}

	@Override
	public String getJobEndStatus() {
		return this.jobEndStatus;
	}

	static class Adapter extends XmlAdapter<ValidationReportImpl, ValidationReport> {
		@Override
		public ValidationReport unmarshal(ValidationReportImpl report) {
			return report;
		}

		@Override
		public ValidationReportImpl marshal(ValidationReport report) {
			return (ValidationReportImpl) report;
		}
	}

	static final ValidationReport defaultInstance() {
		return defaultInstance;
	}

	static final ValidationReport fromValues(final ValidationDetails details, final String profileName,
	                                         final String statement, final boolean isCompliant, final String jobEndStatus) {
		return new ValidationReportImpl(details, profileName, statement, isCompliant, jobEndStatus);
	}
}
