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
package org.verapdf.pdfa.results;

import java.util.*;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Evgeniy Muravitskiy
 */
@XmlRootElement(name = "fixerResult")
public final class MetadataFixerResultImpl implements MetadataFixerResult {
	@XmlAttribute
	private final RepairStatus status;
	@XmlElementWrapper
	@XmlElement(name = "fix")
	private final List<String> appliedFixes;

	private MetadataFixerResultImpl() {
		this(RepairStatus.NO_ACTION, new ArrayList<>());
	}

	private MetadataFixerResultImpl(final RepairStatus status, final List<String> fixes) {
		super();
		this.status = status;
		this.appliedFixes = new ArrayList<>(fixes);
	}

	@Override
	public RepairStatus getRepairStatus() {
		return this.status;
	}

	@Override
	public List<String> getAppliedFixes() {
		return Collections.unmodifiableList(this.appliedFixes);
	}

	@Override
	public Iterator<String> iterator() {
		return this.appliedFixes.iterator();
	}

	/**
	 * @param status
	 * @param fixes
	 * @return
	 */
	public static MetadataFixerResult fromValues(final RepairStatus status, final List<String> fixes) {
		return new MetadataFixerResultImpl(status, fixes);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof MetadataFixerResultImpl))
			return false;

		MetadataFixerResultImpl strings = (MetadataFixerResultImpl) o;

		if (this.status != strings.status)
			return false;
		return Objects.equals(this.appliedFixes, strings.appliedFixes);

	}

	@Override
	public int hashCode() {
		int result = this.status != null ? this.status.hashCode() : 0;
		result = 31 * result + (this.appliedFixes != null ? this.appliedFixes.hashCode() : 0);
		return result;
	}

	/**
	 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
	 */
	@SuppressWarnings("hiding")
	public static class Builder {
		private RepairStatus status = RepairStatus.NO_ACTION;
		private final List<String> fixes = new ArrayList<>();

		/**
		 * @param status
		 *            the
		 *            {@link org.verapdf.pdfa.results.MetadataFixerResult.RepairStatus}
		 *            to set for the Builder.
		 * @return the Builder instance.
		 */
		public Builder status(final RepairStatus status) {
			this.status = status;
			return this;
		}

		/**
		 * @return the current status
		 */
		public RepairStatus getStatus() {
			return this.status;
		}

		/**
		 * @param fix
		 *            a fix to add for the builder
		 * @return the Builder instance
		 */
		public Builder addFix(final String fix) {
			this.fixes.add(fix);
			return this;
		}

		/**
		 * @return a {@link MetadataFixerResult} instance built from the values
		 */
		public MetadataFixerResult build() {
			return MetadataFixerResultImpl.fromValues(this.status, this.fixes);
		}
	}

	static class Adapter extends XmlAdapter<MetadataFixerResultImpl, MetadataFixerResult> {
		@Override
		public MetadataFixerResult unmarshal(MetadataFixerResultImpl mdfResult) {
			return mdfResult;
		}

		@Override
		public MetadataFixerResultImpl marshal(MetadataFixerResult procResult) {
			return (MetadataFixerResultImpl) procResult;
		}
	}
}
