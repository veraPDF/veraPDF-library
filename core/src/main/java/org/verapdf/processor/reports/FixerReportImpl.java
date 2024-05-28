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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import org.verapdf.pdfa.results.MetadataFixerResult;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 9 Nov 2016:07:50:43
 */
@XmlRootElement(name = "metadataRepairReport")
final class FixerReportImpl implements MetadataFixerReport {
	@XmlAttribute
	private final String status;
	@XmlAttribute
	private final int fixCount;
	@XmlElementWrapper(name="fixes")
	@XmlElement(name="fix")
	private final List<String> fixes;
	@XmlElementWrapper(name="errors")
	@XmlElement(name="error")
	private final List<String> errors;

	private FixerReportImpl(final String status, final int fixCount, final List<String> fixes,
			final List<String> errors) {
		super();
		this.status = status;
		this.fixCount = fixCount;
		this.fixes = Collections.unmodifiableList(fixes);
		this.errors = Collections.unmodifiableList(errors);
	}

	private FixerReportImpl() {
		this("", 0, Collections.emptyList(), Collections.emptyList());
	}

	@Override
	public String getStatus() {
		return this.status;
	}

	@Override
	public int getFixCount() {
		return this.fixCount;
	}

	@Override
	public List<String> getFixes() {
		return this.fixes;
	}

	@Override
	public List<String> getErrors() {
		return this.errors;
	}

	static class Adapter extends XmlAdapter<FixerReportImpl, MetadataFixerReport> {
		@Override
		public MetadataFixerReport unmarshal(FixerReportImpl report) {
			return report;
		}

		@Override
		public FixerReportImpl marshal(MetadataFixerReport report) {
			return (FixerReportImpl) report;
		}
	}


	static final MetadataFixerReport fromValues(final String status, final int fixCount, final List<String> fixes,
			final List<String> errors) {
		return new FixerReportImpl(status, fixCount, fixes, errors);
	}
	
	static final MetadataFixerReport fromValues(final MetadataFixerResult fixerResult) {
        int fixCount = 0;
        List<String> fixes = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        switch (fixerResult.getRepairStatus()) {
            case SUCCESS:
            case ID_REMOVED:
            	fixCount = fixerResult.getAppliedFixes().size();
                fixes = new ArrayList<>(fixerResult.getAppliedFixes());
                break;
            case FIX_ERROR:
            	errors = new ArrayList<>(fixerResult.getAppliedFixes());
                break;
            default:
				break;
        }
        String status = fixerResult.getRepairStatus().toString();
		return new FixerReportImpl(status, fixCount, fixes, errors);
    }

}
