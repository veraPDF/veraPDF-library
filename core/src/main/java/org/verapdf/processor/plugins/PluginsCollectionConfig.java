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

import org.verapdf.core.XmlSerialiser;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */

@XmlRootElement(name = "pluginsConfig")
public final class PluginsCollectionConfig {

	@XmlElement(name = "plugin")
	private final List<PluginConfig> plugin;

	public PluginsCollectionConfig() {
		this(Collections.emptyList());
	}

	private PluginsCollectionConfig(List<PluginConfig> pluginsConfig) {
		this.plugin = pluginsConfig == null ? Collections.emptyList() : new ArrayList<>(pluginsConfig);
	}

	/**
	 * Creates plugins config from list of plugin configs
	 * @param pluginConfigs
	 * @return created plugins config
	 */
	public static PluginsCollectionConfig fromValues(final List<PluginConfig> pluginConfigs) {
		return new PluginsCollectionConfig(pluginConfigs);
	}

	public static PluginsCollectionConfig defaultConfig() {
		return new PluginsCollectionConfig();
	}

	/**
	 * @return plugin configs
	 */
	public List<PluginConfig> getPlugins() {
		return this.plugin == null ? null :Collections.unmodifiableList(this.plugin);
	}
	
	public static PluginsCollectionConfig create(final InputStream source) throws JAXBException {
		return XmlSerialiser.typeFromXml(PluginsCollectionConfig.class, source);
	}

	public static void configToXml(PluginsCollectionConfig config, OutputStream dest) throws JAXBException {
		XmlSerialiser.toXml(config, dest, true, false);
	}
}