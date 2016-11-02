
/**
 * 
 */
package org.verapdf.pdfa.validation.validators;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 26 Oct 2016:00:11:25
 */
@XmlRootElement(name = "validatorConfig")
final class ValidatorConfigImpl implements ValidatorConfig {
	private final static ValidatorConfigImpl defaultConfig = new ValidatorConfigImpl();
	@XmlAttribute
	private final PDFAFlavour flavour;
	@XmlAttribute
	private final boolean recordPasses;
	@XmlAttribute
	private final int maxFails;

	private ValidatorConfigImpl() {
		this(PDFAFlavour.NO_FLAVOUR, false, -1);
	}

	private ValidatorConfigImpl(final PDFAFlavour flavour, final boolean recordPasses, final int maxFails) {
		super();
		this.flavour = flavour;
		this.recordPasses = recordPasses;
		this.maxFails = maxFails;
	}

	/**
	 * @see org.verapdf.pdfa.validation.validators.ValidatorConfig#isRecordPasses()
	 */
	@Override
	public boolean isRecordPasses() {
		return this.recordPasses;
	}

	/**
	 * @see org.verapdf.pdfa.validation.validators.ValidatorConfig#getMaxFails()
	 */
	@Override
	public int getMaxFails() {
		return this.maxFails;
	}

	/**
	 * @see org.verapdf.pdfa.validation.validators.ValidatorConfig#getFlavour()
	 */
	@Override
	public PDFAFlavour getFlavour() {
		return this.flavour;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.flavour == null) ? 0 : this.flavour.hashCode());
		result = prime * result + this.maxFails;
		result = prime * result + (this.recordPasses ? 1231 : 1237);
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ValidatorConfigImpl)) {
			return false;
		}
		ValidatorConfigImpl other = (ValidatorConfigImpl) obj;
		if (this.flavour != other.flavour) {
			return false;
		}
		if (this.maxFails != other.maxFails) {
			return false;
		}
		if (this.recordPasses != other.recordPasses) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ValidatorConfigImpl [recordPasses=" + this.recordPasses + ", maxFails=" + this.maxFails + ", flavour="
				+ this.flavour + "]";
	}

	static ValidatorConfig defaultInstance() {
		return defaultConfig;
	}

	static ValidatorConfig fromValues(final PDFAFlavour flavour, final boolean recordPasses, final int maxFails) {
		return new ValidatorConfigImpl(flavour, recordPasses, maxFails);
	}

	static String toXml(final ValidatorConfig toConvert, Boolean prettyXml) throws JAXBException, IOException {
		String retVal = "";
		try (StringWriter writer = new StringWriter()) {
			toXml(toConvert, writer, prettyXml);
			retVal = writer.toString();
			return retVal;
		}
	}

	static void toXml(final ValidatorConfig toConvert, final OutputStream stream, Boolean prettyXml)
			throws JAXBException {
		Marshaller varMarshaller = getMarshaller(prettyXml);
		varMarshaller.marshal(toConvert, stream);
	}

	static ValidatorConfigImpl fromXml(final InputStream toConvert) throws JAXBException {
		Unmarshaller stringUnmarshaller = getUnmarshaller();
		return (ValidatorConfigImpl) stringUnmarshaller.unmarshal(toConvert);
	}

	static void toXml(final ValidatorConfig toConvert, final Writer writer, Boolean prettyXml) throws JAXBException {
		Marshaller varMarshaller = getMarshaller(prettyXml);
		varMarshaller.marshal(toConvert, writer);
	}

	static ValidatorConfigImpl fromXml(final Reader toConvert) throws JAXBException {
		Unmarshaller stringUnmarshaller = getUnmarshaller();
		return (ValidatorConfigImpl) stringUnmarshaller.unmarshal(toConvert);
	}

	static ValidatorConfigImpl fromXml(final String toConvert) throws JAXBException {
		try (StringReader reader = new StringReader(toConvert)) {
			return fromXml(reader);
		}
	}

	static class Adapter extends XmlAdapter<ValidatorConfigImpl, ValidatorConfig> {
		@Override
		public ValidatorConfig unmarshal(ValidatorConfigImpl validationConfigImpl) {
			return validationConfigImpl;
		}

		@Override
		public ValidatorConfigImpl marshal(ValidatorConfig validationResult) {
			return (ValidatorConfigImpl) validationResult;
		}
	}

	static String getSchema() throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(ValidatorConfigImpl.class);
		final StringWriter writer = new StringWriter();
		context.generateSchema(new WriterSchemaOutputResolver(writer));
		return writer.toString();
	}

	private static Unmarshaller getUnmarshaller() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(ValidatorConfigImpl.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return unmarshaller;
	}

	private static Marshaller getMarshaller(Boolean setPretty) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(ValidatorConfigImpl.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, setPretty);
		return marshaller;
	}

	private static class WriterSchemaOutputResolver extends SchemaOutputResolver {
		private final Writer out;

		/**
		 * @param out
		 *            a Writer for the generated schema
		 */
		public WriterSchemaOutputResolver(final Writer out) {
			super();
			this.out = out;
		}

		/**
		 * { @inheritDoc }
		 */
		@Override
		public Result createOutput(String namespaceUri, String suggestedFileName) {
			final StreamResult result = new StreamResult(this.out);
			result.setSystemId("no-id");
			return result;
		}
	}
}
