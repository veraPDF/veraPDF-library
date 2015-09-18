package org.verapdf.features;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Features data of an object for feature extractor
 *
 * @author Maksim Bezrukov
 */
public class FeaturesData {

	private byte[] metadata;
	private List<byte[]> streams;
	private Map<String, String> properties;

	/**
	 * Constructs new FeaturesData
	 *
	 * @param metadata   byte array represents metadata stream
	 * @param streams    list of byte arrays represent all necessary streams for object
	 * @param properties map of properties for object
	 */
	public FeaturesData(byte[] metadata, List<byte[]> streams, Map<String, String> properties) {
		this.metadata = metadata;
		this.streams = streams == null ? new ArrayList<byte[]>() : streams;
		this.properties = properties == null ? new HashMap<String, String>() : properties;
	}

	/**
	 * @return byte array represents metadata stream
	 */
	public byte[] getMetadata() {
		return metadata;
	}

	/**
	 * @return list of byte arrays represent all necessary streams for object
	 */
	public List<byte[]> getStreams() {
		return streams;
	}

	/**
	 * @return map of properties for object
	 */
	public Map<String, String> getProperties() {
		return properties;
	}
}
