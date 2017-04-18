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
package org.verapdf.processor.plugins;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement(name = "plugin")
public class PluginConfig {

	@XmlAttribute
	private final boolean enabled;
	@XmlElement
	private final String name;
	@XmlElement
	private final String version;
	@XmlElement
	private final String description;
	@XmlElement
	@XmlJavaTypeAdapter(PluginConfig.PathAdapter.class)
	private final Path pluginJar;
	@XmlElement(name="attribute")
	@XmlElementWrapper(name = "attributes")
	private final List<Attribute> attributes;

	private PluginConfig(boolean enabled, String name, String version, String description, Path pluginJar, List<Attribute> attributes) {
		this.enabled = enabled;
		this.name = name;
		this.version = version;
		this.description = description;
		this.pluginJar = pluginJar;
		this.attributes = attributes == null ? null : new ArrayList<>(attributes);
	}

	private PluginConfig() {
		this(false, "", "", "", FileSystems.getDefault().getPath(""), Collections.<Attribute>emptyList());  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$
	}

	public static PluginConfig fromValues(boolean enabled, String name, String version, String description, Path pluginFolder, List<Attribute> attributes) {
		return new PluginConfig(enabled, name, version, description, pluginFolder, attributes);
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public String getName() {
		return this.name;
	}

	public String getVersion() {
		return this.version;
	}

	public String getDescription() {
		return this.description;
	}

	public Path getPluginJar() {
		return this.pluginJar;
	}

	public List<Attribute> getAttributes() {
		return this.attributes == null ? Collections.<Attribute>emptyList() : Collections.unmodifiableList(this.attributes);
	}

	private static class PathAdapter extends XmlAdapter<String, Path> {

		@Override
		public Path unmarshal(String v) throws Exception {
			Path path = Paths.get(v);
			return path.toAbsolutePath();
		}

		@Override
		public String marshal(Path v) {
			return v.toAbsolutePath().toString();
		}
	}
}
