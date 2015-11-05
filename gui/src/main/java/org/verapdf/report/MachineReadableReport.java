package org.verapdf.report;

import org.apache.log4j.Logger;
import org.verapdf.pdfa.results.ValidationResult;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.OutputStream;
import java.util.Formatter;
import java.util.GregorianCalendar;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement(name = "report")
public class MachineReadableReport {

	private static final Logger LOGGER = Logger.getLogger(MachineReadableReport.class);

	private static final long MS_IN_SEC = 1000L;
	private static final int SEC_IN_MIN = 60;
	private static final long MS_IN_MIN = SEC_IN_MIN * MS_IN_SEC;
	private static final int MIN_IN_HOUR = 60;
	private static final long MS_IN_HOUR = MS_IN_MIN * MIN_IN_HOUR;

	@XmlElement(name = "validationInfo")
	private final ValidationInfo info;
	@XmlAttribute
	private final String creationDate;
	@XmlAttribute
	private final String processingTime;

	public MachineReadableReport() {
		this(new ValidationInfo(), "", "");
	}

	public MachineReadableReport(ValidationInfo info, String creationDate, String processingTime) {
		this.info = info;
		this.creationDate = creationDate;
		this.processingTime = processingTime;
	}

	private static String getProcessingTimeAsString(long processTime) {
		long processingTime = processTime;

		Long hours = Long.valueOf(processingTime / MS_IN_HOUR);
		processingTime %= MS_IN_HOUR;

		Long mins = Long.valueOf(processingTime / MS_IN_MIN);
		processingTime %= MS_IN_MIN;

		Long sec = Long.valueOf(processingTime / MS_IN_SEC);
		processingTime %= MS_IN_SEC;

		Long ms = Long.valueOf(processingTime);

		String res;

		try (Formatter formatter = new Formatter()) {
			formatter.format("%02d:", hours);
			formatter.format("%02d:", mins);
			formatter.format("%02d.", sec);
			formatter.format("%03d", ms);
			res = formatter.toString();
		}

		return res;
	}

	public static MachineReadableReport fromValues(ValidationResult result, long processingTime) {
		if (result == null) {
			throw new IllegalArgumentException("Argument result con not be null");
		}

		ValidationInfo info = ValidationInfo.fromValues(result);
		String creationDate = null;
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		try {
			XMLGregorianCalendar now = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gregorianCalendar);
			creationDate = now.toXMLFormat();
		} catch (DatatypeConfigurationException e) {
			LOGGER.error(e);
		}
		String processingTimeValue = getProcessingTimeAsString(processingTime);

		return new MachineReadableReport(info, creationDate, processingTimeValue);
	}

	public static void toXml(final MachineReadableReport toConvert,
							 final OutputStream stream, Boolean prettyXml) throws JAXBException {
		Marshaller varMarshaller = getMarshaller(prettyXml);
		varMarshaller.marshal(toConvert, stream);
	}

	private static Marshaller getMarshaller(Boolean setPretty)
			throws JAXBException {
		JAXBContext context = JAXBContext
				.newInstance(MachineReadableReport.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, setPretty);
		return marshaller;
	}

}
