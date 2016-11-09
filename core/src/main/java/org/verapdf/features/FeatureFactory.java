/**
 * 
 */
package org.verapdf.features;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumSet;

import javax.xml.bind.JAXBException;

import org.verapdf.core.XmlSerialiser;

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

	public static FeatureExtractorConfig configFromValues(final EnumSet<FeatureObjectType> enabledFeatures) {
		if (enabledFeatures == null)
			throw new NullPointerException("Arg enabledFeatures can not be null");
		return FeatureExtractorConfigImpl.fromFeatureSet(enabledFeatures);
	}

	/**
	 * Converts XML file to PluginsCollectionConfig,
	 * 
	 * @see javax.xml.bind.JAXB for more details
	 */
	public static FeatureExtractorConfig configFromXml(final InputStream source) throws JAXBException {
		if (source == null)
			throw new NullPointerException("Arg source can not be null");
		return XmlSerialiser.typeFromXml(FeatureExtractorConfigImpl.class, source);
	}

	public static void configToXml(final FeatureExtractorConfig config, final OutputStream dest) throws JAXBException {
		if (config == null)
			throw new NullPointerException("Arg config can not be null");
		if (dest == null)
			throw new NullPointerException("Arg dest can not be null");
		XmlSerialiser.toXml(config, dest, true, false);
	}
}
