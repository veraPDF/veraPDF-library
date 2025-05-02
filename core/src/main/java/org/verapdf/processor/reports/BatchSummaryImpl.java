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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.verapdf.component.AuditDuration;
import org.verapdf.component.Components;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 2 Nov 2016:11:43:50
 */
@XmlRootElement(name = "batchSummary")
final class BatchSummaryImpl implements BatchSummary {
	private static final BatchSummary DEFAULT = new BatchSummaryImpl();
	@XmlElement(name="arlingtonReports")
	private final ValidationBatchSummary validationReports;
	@XmlElement
	private final FeaturesBatchSummary featureReports;
	@XmlElement
	private final MetadataRepairBatchSummary repairReports;
	@XmlElement
	private final AuditDuration duration;
	@XmlAttribute
	private final int totalJobs;
	@XmlAttribute
	private final int failedToParse;
	@XmlAttribute
	private final int encrypted;
	@XmlAttribute
	private final int outOfMemory;
	@XmlAttribute
	private final int veraExceptions;

	private BatchSummaryImpl() {
		this(Components.defaultDuration(), ValidationBatchSummaryImpl.defaultInstance(),
				FeaturesBatchSummary.defaultInstance(), MetadataRepairBatchSummary.defaultInstance(), 0, 0, 0, 0, 0);
	}

	private BatchSummaryImpl(final AuditDuration duration, final ValidationBatchSummary validationSummary,
							 final FeaturesBatchSummary featureSummary, final MetadataRepairBatchSummary repairSummary,
							 final int totalJobs, final int failedToParse, final int encrypted, int outOfMemory, int veraExceptions) {
		super();
		this.duration = duration;
		this.validationReports = validationSummary;
		this.featureReports = featureSummary;
		this.repairReports = repairSummary;
		this.totalJobs = totalJobs;
		this.failedToParse = failedToParse;
		this.encrypted = encrypted;
		this.outOfMemory = outOfMemory;
		this.veraExceptions = veraExceptions;
	}

	@Override
	public AuditDuration getDuration() {
		return this.duration;
	}

	@Override
	public ValidationBatchSummary getValidationSummary() {
		return this.validationReports;
	}

	@Override
	public FeaturesBatchSummary getFeaturesSummary() {
		return this.featureReports;
	}

	@Override
	public MetadataRepairBatchSummary getRepairSummary() {
		return this.repairReports;
	}

	@Override
	public int getTotalJobs() {
		return this.totalJobs;
	}

	@Override
	public int getFailedParsingJobs() {
		return this.failedToParse;
	}

	@Override
	public boolean isMultiJob() {
		return (this.totalJobs > 1);
	}

	@Override
	public int getFailedEncryptedJobs() {
		return this.encrypted;
	}

	@Override
	public int getOutOfMemory() {
		return this.outOfMemory;
	}

	@Override
	public int getVeraExceptions() {
		return this.veraExceptions;
	}

	public static BatchSummary defaultInstance() {
		return DEFAULT;
	}

	static BatchSummary fromValues(final AuditDuration duration, final ValidationBatchSummary validationSummary,
								   final FeaturesBatchSummary featureSummary, final MetadataRepairBatchSummary repairSummary,
								   final int totalJobs, final int failedToParse, final int encrypted, final int outOfMemory, final int veraExceptions) {
		return new BatchSummaryImpl(duration, validationSummary, featureSummary, repairSummary, totalJobs,
				failedToParse, encrypted, outOfMemory, veraExceptions);
	}

	static class Adapter extends XmlAdapter<BatchSummaryImpl, BatchSummary> {
		@Override
		public BatchSummary unmarshal(BatchSummaryImpl summary) {
			return summary;
		}

		@Override
		public BatchSummaryImpl marshal(BatchSummary summary) {
			return (BatchSummaryImpl) summary;
		}
	}
}
