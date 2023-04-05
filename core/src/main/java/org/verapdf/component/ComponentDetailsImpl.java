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
package org.verapdf.component;

import java.net.URI;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 27 Oct 2016:00:07:40
 */
@XmlRootElement(name = "componentDetails")
class ComponentDetailsImpl implements ComponentDetails {
	private final static URI defaultId = URI.create("http://component.verapdf.org#default");
	private final static ComponentDetailsImpl defaultInstance = new ComponentDetailsImpl();
	@XmlAttribute
	private final URI id;
	@XmlAttribute
	private final String name;
	@XmlAttribute
	private final String version;
	@XmlElement
	private final String provider;
	@XmlElement
	private final String description;

	private ComponentDetailsImpl() {
		this(defaultId, "name", "version", "provider", "description");
	}

	private ComponentDetailsImpl(final URI id, final String name, final String version, final String provider,
			final String description) {
		super();
		this.id = id;
		this.name = name;
		this.version = version;
		this.provider = provider;
		this.description = description;
	}

	/**
	 * @return the defaultid
	 */
	public static URI getDefaultid() {
		return defaultId;
	}

	/**
	 * @return the id
	 */
	@Override
	public URI getId() {
		return this.id;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @return the version
	 */
	@Override
	public String getVersion() {
		return this.version;
	}

	/**
	 * @return the provider
	 */
	@Override
	public String getProvider() {
		return this.provider;
	}

	/**
	 * @return the description
	 */
	@Override
	public String getDescription() {
		return this.description;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.provider == null) ? 0 : this.provider.hashCode());
		result = prime * result + ((this.version == null) ? 0 : this.version.hashCode());
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
		if (!(obj instanceof ComponentDetailsImpl)) {
			return false;
		}
		ComponentDetailsImpl other = (ComponentDetailsImpl) obj;
		if (this.description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!this.description.equals(other.description)) {
			return false;
		}
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.provider == null) {
			if (other.provider != null) {
				return false;
			}
		} else if (!this.provider.equals(other.provider)) {
			return false;
		}
		if (this.version == null) {
			if (other.version != null) {
				return false;
			}
		} else if (!this.version.equals(other.version)) {
			return false;
		}
		return true;
	}

	static ComponentDetails defaultInstance() {
		return defaultInstance;
	}

	static ComponentDetails fromValues(final URI id, final String name, final String version, final String provider,
			final String description) {
		return new ComponentDetailsImpl(id, name, version, provider, description);
	}
}
