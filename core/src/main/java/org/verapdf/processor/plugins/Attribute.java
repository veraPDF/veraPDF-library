package org.verapdf.processor.plugins;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement(name = "attribute")
public class Attribute {

	@XmlAttribute
	private final String key;
	@XmlAttribute
	private final String value;

	private Attribute(String key, String value) {
		this.key = key;
		this.value = value;
	}

	private Attribute() {
		this("", "");
	}

	public static Attribute fromValues(String key, String value) {
		return new Attribute(key, value);
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
}
