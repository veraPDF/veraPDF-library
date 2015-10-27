package org.verapdf.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Features data of an iccprofile for feature extractor
 *
 * @author Maksim Bezrukov
 */
public final class ICCProfileFeaturesData extends FeaturesData {

	private final byte[] metadata;
	private final Integer n;
	private final List<Double> range;

	private ICCProfileFeaturesData(byte[] metadata, byte[] stream, Integer n, List<Double> range) {
		super(stream);
		this.metadata = metadata == null ? null : Arrays.copyOf(metadata, metadata.length);
		this.n = n;
		this.range = range == null ? null : new ArrayList<>(range);
	}

	/**
	 * Creates ICCProfileFeaturesData
	 *
	 * @param metadata byte array represents metadata stream
	 * @param stream   byte array represents object stream
	 * @param n        parameter N from the iccprofile dictionary
	 * @param range    parameter Range from the iccprofile dictionary
	 */
	public static ICCProfileFeaturesData newInstance(byte[] metadata, byte[] stream, Integer n, List<Double> range) {
		if (stream == null) {
			throw new IllegalArgumentException("ICCProfile stream can not be null");
		}
		return new ICCProfileFeaturesData(metadata, stream, n, range);
	}

	/**
	 * @return byte array represent metadata stream
	 */
	public byte[] getMetadata() {
		return metadata == null ? null : Arrays.copyOf(metadata, metadata.length);
	}

	/**
	 * @return parameter N from the iccprofile dictionary
	 */
	public Integer getN() {
		return n;
	}

	/**
	 * @return parameter Range from the iccprofile dictionary
	 */
	public List<Double> getRange() {
		return range == null ? null : Collections.unmodifiableList(range);
	}
}
