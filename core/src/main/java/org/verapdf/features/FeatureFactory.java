/**
 * 
 */
package org.verapdf.features;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumSet;

import javax.xml.bind.JAXBException;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 30 Oct 2016:01:14:07
 */

public final class FeatureFactory {
	private FeatureFactory() {

	}

	public static FeatureExtractorConfig defaultConfig() {
		return FeatureExtractorConfigImpl.defaultInstance();
	}

	public static FeatureExtractorConfig createConfig(final EnumSet<FeatureObjectType> enabledFeatures) {
		if (enabledFeatures == null)
			throw new NullPointerException("Arg enabledFeatures can not be null");
		return FeatureExtractorConfigImpl.fromFeatureSet(enabledFeatures);
	}

	/**
	 * Converts XML file to PluginsCollectionConfig,
	 * 
	 * @see javax.xml.bind.JAXB for more details
	 */
	public static FeatureExtractorConfig createConfig(final InputStream toConvert) throws JAXBException {
		return FeatureExtractorConfigImpl.fromXml(toConvert);
	}

	public static String configToXml(final FeatureExtractorConfig toConvert) throws JAXBException, IOException {
		return FeatureExtractorConfigImpl.toXml(toConvert, Boolean.TRUE);
	}

	public static void configToXml(final FeatureExtractorConfig toConvert, OutputStream dest)
			throws JAXBException {
		FeatureExtractorConfigImpl.toXml(toConvert, dest, Boolean.TRUE);
	}
}
