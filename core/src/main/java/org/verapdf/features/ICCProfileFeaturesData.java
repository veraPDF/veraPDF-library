package org.verapdf.features;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Features data of an iccprofile for feature extractor
 *
 * @author Maksim Bezrukov
 */
public final class ICCProfileFeaturesData extends FeaturesData {

	private final InputStream metadata;
	private final Integer n;
	private final List<Double> range;

	private ICCProfileFeaturesData(InputStream metadata, InputStream stream, Integer n, List<Double> range) {
		super(stream);
		this.metadata = metadata;
		this.n = n;
		this.range = range == null ? null : new ArrayList<>(range);
	}

	/**
	 * Creates ICCProfileFeaturesData
	 *
	 * @param metadata metadata stream
	 * @param stream   object stream
	 * @param n        parameter N from the iccprofile dictionary
	 * @param range    parameter Range from the iccprofile dictionary
	 */
	public static ICCProfileFeaturesData newInstance(InputStream metadata, InputStream stream, Integer n, List<Double> range) {
		if (stream == null) {
			throw new IllegalArgumentException("ICCProfile stream can not be null");
		}
		return new ICCProfileFeaturesData(metadata, stream, n, range);
	}

	/**
	 * @return metadata stream
	 */
	public InputStream getMetadata() {
		return this.metadata;
	}

	/**
	 * @return parameter N from the iccprofile dictionary
	 */
	public Integer getN() {
		return this.n;
	}

	/**
	 * @return parameter Range from the iccprofile dictionary
	 */
	public List<Double> getRange() {
		return this.range == null ? null : Collections.unmodifiableList(this.range);
	}
}
