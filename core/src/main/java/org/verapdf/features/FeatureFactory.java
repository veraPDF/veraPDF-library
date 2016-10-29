/**
 * 
 */
package org.verapdf.features;

import java.io.InputStream;
import java.util.EnumSet;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

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
	 *	Converts XML file to PluginsCollectionConfig,
	 *	@see javax.xml.bind.JAXB for more details
	 */
	public static FeatureExtractorConfig fromXml(final InputStream toConvert)
			throws JAXBException {
		return FeatureExtractorConfigImpl.fromXml(toConvert);
	}

}
