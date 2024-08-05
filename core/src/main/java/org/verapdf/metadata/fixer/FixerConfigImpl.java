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
package org.verapdf.metadata.fixer;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Objects;

/**
 * @author Evgeniy Muravitskiy
 */
@XmlRootElement(name = "fixerConfig")
final class FixerConfigImpl implements MetadataFixerConfig {
	public static final String DEFAULT_PREFIX = "veraFixMd_";  //$NON-NLS-1$
	private static final MetadataFixerConfig defaultInstance = new FixerConfigImpl();
	@XmlAttribute
	private final String fixesPrefix;

	private FixerConfigImpl() {
		this(DEFAULT_PREFIX); //$NON-NLS-1$
	}

	private FixerConfigImpl(final String fixesPrefix) {
		super();
		this.fixesPrefix = fixesPrefix;
	}

	@Override
	public String getFixesPrefix() {
		return this.fixesPrefix;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.fixesPrefix == null) ? 0 : this.fixesPrefix.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FixerConfigImpl)) {
			return false;
		}
		FixerConfigImpl other = (FixerConfigImpl) obj;
		return Objects.equals(this.fixesPrefix, other.fixesPrefix);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FixerConfigImpl [fixesPrefix=" + this.fixesPrefix + "]";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
	}

	static MetadataFixerConfig defaultInstance() {
		return defaultInstance;
	}

	static MetadataFixerConfig fromValues(final String fixesPrefix) {
		return new FixerConfigImpl(fixesPrefix);
	}

	static class Adapter extends XmlAdapter<FixerConfigImpl, MetadataFixerConfig> {
		@Override
		public MetadataFixerConfig unmarshal(FixerConfigImpl fixerConfigImpl) {
			return fixerConfigImpl;
		}

		@Override
		public FixerConfigImpl marshal(MetadataFixerConfig fixerConfig) {
			return (FixerConfigImpl) fixerConfig;
		}
	}
}
