package org.verapdf.features;

import java.util.Arrays;

/**
 * Features data of an object for feature extractor
 *
 * @author Maksim Bezrukov
 */
public class FeaturesData {

	private byte[] metadata;
	private byte[] stream;

	/**
	 * Constructs new FeaturesData
	 *
	 * @param metadata   byte array represents metadata stream
	 * @param stream     byte array represents object stream
	 */
	protected FeaturesData(byte[] metadata, byte[] stream) {
		this.metadata = metadata == null ? null : Arrays.copyOf(metadata, metadata.length);
		this.stream = Arrays.copyOf(stream, stream.length);
	}

	/**
	 * @return byte array represent metadata stream
	 */
	public byte[] getMetadata() {
		return metadata == null ? null : Arrays.copyOf(metadata, metadata.length);
	}

	/**
	 * @return byte array represent streams for object
	 */
	public byte[] getStream() {
		return stream == null ? null : Arrays.copyOf(stream, stream.length);
	}

}
