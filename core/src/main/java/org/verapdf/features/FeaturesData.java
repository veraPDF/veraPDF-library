package org.verapdf.features;

import java.io.InputStream;

/**
 * Features data of an object for feature extractor
 *
 * @author Maksim Bezrukov
 */
public class FeaturesData {

	private final InputStream stream;

	/**
	 * Constructs new FeaturesData
	 *
	 * @param stream     object stream
	 */
	protected FeaturesData(InputStream stream) {
		this.stream = stream;
	}

	/**
	 * @return stream of object
	 */
	public InputStream getStream() {
		return this.stream;
	}

}
