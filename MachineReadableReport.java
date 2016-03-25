package org.verapdf.report;

import java.io.File;
import java.io.OutputStream;
import java.util.Date;
import java.util.Formatter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.verapdf.features.tools.FeaturesCollection;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.ValidationProfile;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement(name = "report")
public class MachineReadableReport {

    private static final long MS_IN_SEC = 1000L;
    private static final int SEC_IN_MIN = 60;
    private static final long MS_IN_MIN = SEC_IN_MIN * MS_IN_SEC;
    private static final int MIN_IN_HOUR = 60;
    private static final long MS_IN_HOUR = MS_IN_MIN * MIN_IN_HOUR;

    @XmlAttribute
    private final Date creationDate;
    @XmlAttribute
    private final String processingTime;
    @XmlElement
    private final ItemDetails itemDetails;
    @XmlElement
    private final ValidationReport validationReport;
    @XmlElement
    private final MetadataFixesReport metadataFixesReport;
    @XmlElement
    private final FeaturesReport pdfFeaturesReport;

    private MachineReadableReport() {

        this(ItemDetails.DEFAULT, ValidationReport.fromValues(
                Profiles.defaultProfile(), null, false, 0), "", null, null);
    }

        private MachineReadableReport(ItemDetails itemDetails,
            ValidationReport report, String processingTime,
            FeaturesReport featuresReport, MetadataFixesReport metadataFixesReport) {
        this.itemDetails = itemDetails;
        this.validationReport = report;
        this.creationDate = new Date();
        this.processingTime = processingTime;
        this.pdfFeaturesReport = featuresReport;
        this.metadataFixesReport = metadataFixesReport;
    }

    /**
     * @param file 
     * @param profile
     * @param validationResult
     * @param reportPassedChecks
     * @param maxFailuresDisplayed 
     * @param fixerResult
     * @param collection
     * @param processingTime
     * @return a MachineReadableReport instance initialised from the passed
     *         values
     */
    public static MachineReadableReport fromValues(File file, ValidationProfile profile,
            ValidationResult validationResult, boolean reportPassedChecks, int maxFailuresDisplayed,
            MetadataFixerResult fixerResult, FeaturesCollection collection,
            long processingTime) {
        ValidationReport validationReport = null;
        if (validationResult != null) {
            validationReport = ValidationReport.fromValues(profile,
                    validationResult, reportPassedChecks, maxFailuresDisplayed);
        }
        MetadataFixesReport fixesReport = MetadataFixesReport.fromValues(fixerResult);
        FeaturesReport featuresReport = FeaturesReport.fromValues(collection);
        return new MachineReadableReport(ItemDetails.fromFile(file), validationReport,
                getProcessingTime(processingTime), featuresReport, fixesReport);
    }

    /**
     * @param name
     * @param profile
     * @param validationResult
     * @param reportPassedChecks
     * @param fixerResult
     * @param collection
     * @param processingTime
     * @return a MachineReadableReport instance initialised from the passed
     *         values
     */
    public static MachineReadableReport fromValues(String name, ValidationProfile profile,
            ValidationResult validationResult, boolean reportPassedChecks,
            MetadataFixerResult fixerResult, FeaturesCollection collection,
            long processingTime, int maxFailuresDisplayed) {
        ValidationReport validationReport = null;
        if (validationResult != null) {
            validationReport = ValidationReport.fromValues(profile,
                    validationResult, reportPassedChecks, maxFailuresDisplayed);
        }
        FeaturesReport featuresReport = FeaturesReport.fromValues(collection);
        MetadataFixesReport fixesReport = MetadataFixesReport.fromValues(fixerResult);
        return new MachineReadableReport(ItemDetails.fromValues(name), validationReport,
                getProcessingTime(processingTime), featuresReport, fixesReport);
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

    private static String getProcessingTime(long processTime) {
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

}
