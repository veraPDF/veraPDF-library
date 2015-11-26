package org.verapdf.report;

import java.io.OutputStream;
import java.util.Formatter;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.verapdf.features.tools.FeaturesCollection;
import org.verapdf.pdfa.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.ValidationProfile;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement(name = "report")
public class MachineReadableReport {

    private static final Logger LOGGER = Logger
            .getLogger(MachineReadableReport.class);

    private static final long MS_IN_SEC = 1000L;
    private static final int SEC_IN_MIN = 60;
    private static final long MS_IN_MIN = SEC_IN_MIN * MS_IN_SEC;
    private static final int MIN_IN_HOUR = 60;
    private static final long MS_IN_HOUR = MS_IN_MIN * MIN_IN_HOUR;

    @XmlAttribute
    private final String creationDate;
    @XmlAttribute
    private final String processingTime;
    @XmlElement(name = "validationReport")
    private final ValidationReport info;
    // @XmlElement(name = "fixerReport")
    // private final MetadataFixerReport fixerReport;
    @XmlElement
    private final FeaturesReport pdfFeatures;

    private MachineReadableReport() {
        this(
                ValidationReport.fromValues(Profiles.defaultProfile(), null,
                        null), "", "", null);
    }

    private MachineReadableReport(ValidationReport report, String creationDate,
            String processingTime, FeaturesReport featuresReport) {
        this.info = report;
        this.creationDate = creationDate;
        this.processingTime = processingTime;
        this.pdfFeatures = featuresReport;
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

    /**
     * @param validationResult
     * @param fixerResult
     * @param collection
     * @param processingTime
     * @return a MachineReadableReport instance initialised from the passed
     *         values
     */
    public static MachineReadableReport fromValues(ValidationProfile profile,
            ValidationResult validationResult, MetadataFixerResult fixerResult,
            FeaturesCollection collection, long processingTime) {
        ValidationReport validationReport = null;
        if (validationResult != null) {
            validationReport = ValidationReport.fromValues(profile, validationResult,
                    fixerResult);
        }
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

        FeaturesReport featuresReport = FeaturesReport.fromValues(collection);
        return new MachineReadableReport(validationReport, creationDate,
                processingTimeValue, featuresReport);
    }

    /**
     * @param toConvert
     * @param stream
     * @param prettyXml
     * @throws JAXBException
     */
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
