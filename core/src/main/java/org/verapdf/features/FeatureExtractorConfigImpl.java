package org.verapdf.features;

import java.util.EnumSet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement(name = "featuresConfig")
final class FeatureExtractorConfigImpl implements FeatureExtractorConfig {
	private static final FeatureExtractorConfig DEFAULT = new FeatureExtractorConfigImpl(EnumSet.of(FeatureObjectType.INFORMATION_DICTIONARY));
	@XmlElementWrapper(name="enabledFeatures")
	@XmlElement(name="feature")
	private final EnumSet<FeatureObjectType> enabledFeatures;

	private FeatureExtractorConfigImpl() {
		this(EnumSet.noneOf(FeatureObjectType.class));
	}
	private FeatureExtractorConfigImpl(EnumSet<FeatureObjectType> enabledFeatures) {
		super();
		this.enabledFeatures = EnumSet.copyOf(enabledFeatures);
	}

	@Override
	public boolean isFeatureEnabled(FeatureObjectType type) {
		return this.enabledFeatures.contains(type);
	}

	@Override
	public boolean isAnyFeatureEnabled(EnumSet<FeatureObjectType> types) {
		for (FeatureObjectType type : types) {
			if (this.enabledFeatures.contains(type)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public EnumSet<FeatureObjectType> getEnabledFeatures() {
		return this.enabledFeatures;
	}

	static FeatureExtractorConfig defaultInstance() {
		return DEFAULT;
	}
	
	static FeatureExtractorConfig fromFeatureSet(final EnumSet<FeatureObjectType> enabledFeatures) {
		return new FeatureExtractorConfigImpl(enabledFeatures);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.enabledFeatures == null) ? 0 : this.enabledFeatures.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeatureExtractorConfigImpl other = (FeatureExtractorConfigImpl) obj;
		if (this.enabledFeatures == null) {
			if (other.enabledFeatures != null)
				return false;
		} else if (!this.enabledFeatures.equals(other.enabledFeatures))
			return false;
		return true;
	}

	static class Adapter extends XmlAdapter<FeatureExtractorConfigImpl, FeatureExtractorConfig> {
		@Override
		public FeatureExtractorConfig unmarshal(FeatureExtractorConfigImpl validationConfigImpl) {
			return validationConfigImpl;
		}

		@Override
		public FeatureExtractorConfigImpl marshal(FeatureExtractorConfig validationResult) {
			return (FeatureExtractorConfigImpl) validationResult;
		}
	}

}
