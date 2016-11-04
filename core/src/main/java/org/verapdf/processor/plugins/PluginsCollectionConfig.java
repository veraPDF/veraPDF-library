package org.verapdf.processor.plugins;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.verapdf.core.XmlSerialiser;

/**
 * @author Maksim Bezrukov
 */

@XmlRootElement(name = "pluginsConfig")
public final class PluginsCollectionConfig {

	@XmlElement(name = "plugin")
	private final List<PluginConfig> plugin;

	public PluginsCollectionConfig() {
		this(Collections.<PluginConfig>emptyList());
	}

	private PluginsCollectionConfig(List<PluginConfig> pluginsConfig) {
		this.plugin = pluginsConfig == null ? Collections.<PluginConfig>emptyList() : new ArrayList<>(pluginsConfig);
	}

	/**
	 * Creates plugins config from list of plugin configs
	 * @param pluginConfigs
	 * @return created plugins config
	 */
	public static PluginsCollectionConfig fromValues(final List<PluginConfig> pluginConfigs) {
		return new PluginsCollectionConfig(pluginConfigs);
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
}