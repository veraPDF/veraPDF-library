package org.verapdf.features;

import java.util.Arrays;

/**
 * Features data of an object for feature extractor
 *
 * @author Maksim Bezrukov
 */
public class FeaturesData {

	private final byte[] stream;

	/**
	 * Constructs new FeaturesData
	 *
	 * @param stream     byte array represents object stream
	 */
	protected FeaturesData(byte[] stream) {
		this.stream = stream == null ? null : Arrays.copyOf(stream, stream.length);
	}

	/**
	 * @return byte array represent streams for object
	 */
	public byte[] getStream() {
		return this.stream == null ? null : Arrays.copyOf(this.stream, this.stream.length);
	}

}
