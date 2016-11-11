package org.verapdf.pdfa.results;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
		this(RepairStatus.NO_ACTION, new ArrayList<String>());
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
		return this.appliedFixes != null ? this.appliedFixes.equals(strings.appliedFixes)
				: strings.appliedFixes == null;

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
