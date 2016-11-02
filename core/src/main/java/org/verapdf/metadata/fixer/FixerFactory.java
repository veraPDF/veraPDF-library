/**
 * 
 */
package org.verapdf.metadata.fixer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;

import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.MetadataFixerResultImpl;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 30 Oct 2016:01:30:05
 */

public final class FixerFactory {
	private static final MetadataFixerResult defaultResult = new MetadataFixerResultImpl.Builder().build();
	private FixerFactory() {

	}

	public static MetadataFixerConfig defaultConfig() {
		return FixerConfigImpl.defaultInstance();
	}

	public static MetadataFixerConfig fromValues(final String fixesPrefix, boolean fixId) {
		return FixerConfigImpl.fromValues(fixesPrefix, fixId);
	}

	public static MetadataFixerConfig createConfig(final InputStream source) throws JAXBException {
		return FixerConfigImpl.fromXml(source);
	}

	public static void configToXml(final MetadataFixerConfig config, final OutputStream dest)
			throws JAXBException {
		FixerConfigImpl.toXml(config, dest, Boolean.TRUE);
	}

	public static String configToXml(final MetadataFixerConfig config, final Boolean prettyXml)
			throws JAXBException, IOException {
		return FixerConfigImpl.toXml(config, prettyXml);
	}

	public static MetadataFixerConfig createConfig(final String toConvert)
			throws JAXBException {
		return FixerConfigImpl.fromXml(toConvert);
	}
	
	public static MetadataFixerResult defaultResult() {
		return defaultResult;
	}
}
