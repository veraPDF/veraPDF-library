package org.verapdf.processor.plugins;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.*;
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
	public static PluginsCollectionConfig fromValues(List<PluginConfig> pluginConfigs) {
		return new PluginsCollectionConfig(pluginConfigs);
	}

	/**
	 * @return plugin configs
	 */
	public List<PluginConfig> getPlugins() {
		return this.plugin == null ? null :Collections.unmodifiableList(this.plugin);
	}

	/**
	 *	Converts PluginsCollectionConfig to XML,
	 *	@see javax.xml.bind.JAXB for more details
	 */
	public static String toXml(final PluginsCollectionConfig toConvert, Boolean prettyXml)
			throws JAXBException, IOException {
		String retVal = "";
		try (StringWriter writer = new StringWriter()) {
			toXml(toConvert, writer, prettyXml);
			retVal = writer.toString();
			return retVal;
		}
	}

	/**
	 *	Converts XML file to PluginsCollectionConfig,
	 *	@see javax.xml.bind.JAXB for more details
	 */
	public static PluginsCollectionConfig fromXml(final String toConvert)
			throws JAXBException {
		try (StringReader reader = new StringReader(toConvert)) {
			return fromXml(reader);
		}
	}

	/**
	 *	Converts PluginsCollectionConfig to XML,
	 *	@see javax.xml.bind.JAXB for more details
	 */
	public static void toXml(final PluginsCollectionConfig toConvert,
							 final OutputStream stream, Boolean prettyXml) throws JAXBException {
		Marshaller varMarshaller = getMarshaller(prettyXml);
		varMarshaller.marshal(toConvert, stream);
	}

	/**
	 *	Converts XML file to PluginsCollectionConfig,
	 *	@see javax.xml.bind.JAXB for more details
	 */
	public static PluginsCollectionConfig fromXml(final InputStream toConvert)
			throws JAXBException {
		Unmarshaller stringUnmarshaller = getUnmarshaller();
		return (PluginsCollectionConfig) stringUnmarshaller.unmarshal(toConvert);
	}

	static void toXml(final PluginsCollectionConfig toConvert, final Writer writer,
					  Boolean prettyXml) throws JAXBException {
		Marshaller varMarshaller = getMarshaller(prettyXml);
		varMarshaller.marshal(toConvert, writer);
	}

	static PluginsCollectionConfig fromXml(final Reader toConvert)
			throws JAXBException {
		Unmarshaller stringUnmarshaller = getUnmarshaller();
		return (PluginsCollectionConfig) stringUnmarshaller.unmarshal(toConvert);
	}

	private static Unmarshaller getUnmarshaller() throws JAXBException {
		JAXBContext context = JAXBContext
				.newInstance(PluginsCollectionConfig.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return unmarshaller;
	}

	private static Marshaller getMarshaller(Boolean setPretty)
			throws JAXBException {
		JAXBContext context = JAXBContext
				.newInstance(PluginsCollectionConfig.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, setPretty);
		return marshaller;
	}
}