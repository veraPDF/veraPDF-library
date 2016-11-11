/**
 * 
 */
package org.verapdf.metadata.fixer;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;

import org.verapdf.core.XmlSerialiser;
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

	public static MetadataFixerConfig configFromValues(final String fixesPrefix, boolean fixId) {
		return FixerConfigImpl.fromValues(fixesPrefix, fixId);
	}

	public static MetadataFixerConfig configFromXml(final InputStream source) throws JAXBException {
		return XmlSerialiser.typeFromXml(FixerConfigImpl.class, source);
	}
	
	public static void configToXml(MetadataFixerConfig config, final OutputStream dest) throws JAXBException {
		XmlSerialiser.toXml(config, dest, true, false);
	}

	public static MetadataFixerResult defaultResult() {
		return defaultResult;
	}
}
