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
package org.verapdf.features;

import java.util.EnumSet;
import java.util.Objects;

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
		return Objects.equals(this.enabledFeatures, other.enabledFeatures);
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
