/**
 * 
 */
package org.verapdf.report;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 22 Sep 2016:00:12:37
 */
@XmlRootElement(name = "validationBatch")
public class ValidationBatchReport {
	@XmlElement
	private final BatchSummary batchSummary;
    @XmlElementWrapper
    @XmlElement(name = "task")
	private final List<ValidationSummary> tasks;

	private ValidationBatchReport() {
		this(null, Collections.<ValidationSummary>emptyList());
	}
	private ValidationBatchReport(final BatchSummary batchSummary, final List<ValidationSummary> taskSummaries) {
		this.batchSummary = batchSummary;
		this.tasks = Collections.unmodifiableList(taskSummaries);
	}

	public static ValidationBatchReport fromValues(final BatchSummary batchSummary,
			final List<ValidationSummary> taskSummaries) {
		return new ValidationBatchReport(batchSummary, taskSummaries);
	}

	public BatchSummary getBatchSummary() {
		return this.batchSummary;
	}

	public List<ValidationSummary> getTasks() {
		return Collections.unmodifiableList(this.tasks);
	}

	static String toXml(final ValidationBatchReport toConvert, Boolean prettyXml)
            throws JAXBException, IOException {
        String retVal = "";
        try (StringWriter writer = new StringWriter()) {
            toXml(toConvert, writer, prettyXml);
            retVal = writer.toString();
            return retVal;
        }
    }

    public static void toXml(final ValidationBatchReport toConvert,
            final OutputStream stream, Boolean prettyXml) throws JAXBException {
        Marshaller varMarshaller = getMarshaller(prettyXml);
        varMarshaller.marshal(toConvert, stream);
    }

    static ValidationBatchReport fromXml(final InputStream toConvert)
            throws JAXBException {
        Unmarshaller stringUnmarshaller = getUnmarshaller();
        return (ValidationBatchReport) stringUnmarshaller.unmarshal(toConvert);
    }

    public static void toXml(final ValidationBatchReport toConvert, final Writer writer,
            Boolean prettyXml) throws JAXBException {
        Marshaller varMarshaller = getMarshaller(prettyXml);
        varMarshaller.marshal(toConvert, writer);
    }

    static ValidationBatchReport fromXml(final Reader toConvert)
            throws JAXBException {
        Unmarshaller stringUnmarshaller = getUnmarshaller();
        return (ValidationBatchReport) stringUnmarshaller.unmarshal(toConvert);
    }

    static ValidationBatchReport fromXml(final String toConvert)
            throws JAXBException {
        try (StringReader reader = new StringReader(toConvert)) {
            return fromXml(reader);
        }
    }

    private static Unmarshaller getUnmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext
                .newInstance(ValidationBatchReport.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller;
    }

    private static Marshaller getMarshaller(Boolean setPretty)
            throws JAXBException {
        JAXBContext context = JAXBContext
                .newInstance(ValidationBatchReport.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, setPretty);
        return marshaller;
    }

    public static class Builder {
		private final TaskDetails.TimedFactory timer;
		private final List<ValidationSummary> taskSummaries = new ArrayList<>();
		private int validItems = 0;
		private int invalidItems = 0;

		public Builder(final String name) {
			this.timer = new TaskDetails.TimedFactory(name);
		}

		public void addValidationSummary(final ValidationSummary summary) {
			this.taskSummaries.add(summary);
			if (summary.isValid())
				this.validItems++;
			else
				this.invalidItems++;
		}

		public ValidationBatchReport build() {
			BatchSummary summary = new BatchSummary(this.timer.stop(), this.taskSummaries.size(), this.validItems, this.invalidItems);
			return ValidationBatchReport.fromValues(summary, this.taskSummaries);
		}
	}
}
