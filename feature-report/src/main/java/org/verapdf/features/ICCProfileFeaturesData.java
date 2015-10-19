package org.verapdf.features;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Features data of an iccprofile for feature extractor
 *
 * @author Maksim Bezrukov
 */
public final class ICCProfileFeaturesData extends FeaturesData {

	private Integer n;
	private List<Double> range;

	private ICCProfileFeaturesData(byte[] metadata, byte[] stream, Integer n, List<Double> range) {
		super(metadata, stream);
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
