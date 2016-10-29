package org.verapdf.processor.plugins;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		this(false, "", "", "", FileSystems.getDefault().getPath(""), Collections.<Attribute>emptyList());
	}

	public static PluginConfig fromValues(boolean enabled, String name, String version, String description, Path pluginFolder, List<Attribute> attributes) {
		return new PluginConfig(enabled, name, version, description, pluginFolder, attributes);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getDescription() {
		return description;
	}

	public Path getPluginJar() {
		return pluginJar;
	}

	public List<Attribute> getAttributes() {
		return attributes == null ? Collections.<Attribute>emptyList() : Collections.unmodifiableList(attributes);
	}

	private static class PathAdapter extends XmlAdapter<String, Path> {

		@Override
		public Path unmarshal(String v) throws Exception {
			Path path = Paths.get(new URI(v));
			return path.toAbsolutePath();
		}

		@Override
		public String marshal(Path v) {
			return v.toAbsolutePath().toUri().toString();
		}
	}
}
