/**
 * 
 */
package org.verapdf.pdfa.results;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.validation.RuleId;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public class ValidationResults {
    private ValidationResults() {
        // Disable default constructor
    }

    /**
     * @param flavour
     *            a {@link PDFAFlavour} instance indicating the validation type
     *            performed
     * @param assertions
     *            the Set of TestAssertions reported by during validation
     * @param isCompliant
     *            a boolean that indicating whether the validated PDF/A data was
     *            compliant with the indicated flavour
     * @return a new ValidationResult instance populated from the values
     */
    public static ValidationResult resultFromValues(final PDFAFlavour flavour,
            final Set<TestAssertion> assertions, final boolean isCompliant) {
        return ValidationResultImpl
                .fromValues(flavour, assertions, isCompliant);
    }

    /**
     * @param flavour
     *            a {@link PDFAFlavour} instance indicating the validation type
     *            performed
     * @param assertions
     *            the Set of TestAssertions reported by during validation
     * @param isCompliant
     *            a boolean that indicating whether the validated PDF/A data was
     *            compliant with the indicated flavour
     * @return a new ValidationResult instance populated from the values
     */
    public static ValidationResult resultFromValues(final PDFAFlavour flavour,
            final Set<TestAssertion> assertions) {
        boolean isCompliant = true;
        for (TestAssertion assertion : assertions) {
            if (assertion.getStatus() == Status.FAILED) {
                isCompliant = false;
                break;
            }
        }
        return resultFromValues(flavour, assertions, isCompliant);
    }

    /**
     * @return
     */
    public static ValidationResult defaultResult() {
        return ValidationResultImpl.defaultInstance();
    }

    /**
     * @param ruleId
     * @param status
     * @param message
     * @param location
     * @return
     */
    public static TestAssertion assertionFromValues(final RuleId ruleId,
            final Status status, final String message, final Location location) {
        return TestAssertionImpl.fromValues(ruleId, status, message, location);
    }

    /**
     * @return
     */
    public static TestAssertion defaultAssertion() {
        return TestAssertionImpl.defaultInstance();
    }

    /**
     * @param level
     * @param context
     * @return
     */
    public static Location locationFromValues(final String level,
            final String context) {
        return LocationImpl.fromValues(level, context);
    }

    /**
     * @return
     */
    public static Location defaultLocation() {
        return LocationImpl.defaultInstance();
    }
    
    /**
     * @param toConvert
     * @param prettyXml
     * @return
     * @throws JAXBException
     * @throws IOException
     */
    public static String resultToXml(final ValidationResult toConvert, Boolean prettyXml)
            throws JAXBException, IOException {
        String retVal = "";
        try (StringWriter writer = new StringWriter()) {
            toXml(toConvert, writer, prettyXml);
            retVal = writer.toString();
            return retVal;
        }
    }

    /**
     * @param toConvert
     * @return
     * @throws JAXBException
     */
    public static ValidationResult fromXml(final String toConvert)
            throws JAXBException {
        try (StringReader reader = new StringReader(toConvert)) {
            return fromXml(reader);
        }
    }

    /**
     * @param toConvert
     * @param stream
     * @param prettyXml
     * @throws JAXBException
     */
    public static void toXml(final ValidationResult toConvert,
            final OutputStream stream, Boolean prettyXml) throws JAXBException {
        ValidationResultImpl.toXml(toConvert, stream, prettyXml);
    }

    /**
     * @param toConvert
     * @return
     * @throws JAXBException
     */
    public static ValidationResultImpl fromXml(final InputStream toConvert)
            throws JAXBException {
        return ValidationResultImpl.fromXml(toConvert);
    }

    /**
     * @param toConvert
     * @param writer
     * @param prettyXml
     * @throws JAXBException
     */
    public static void toXml(final ValidationResult toConvert,
            final Writer writer, Boolean prettyXml) throws JAXBException {
        ValidationResultImpl.toXml(toConvert, writer, prettyXml);
    }

    /**
     * @param toConvert
     * @return
     * @throws JAXBException
     */
    public static ValidationResultImpl fromXml(final Reader toConvert)
            throws JAXBException {
        return ValidationResultImpl.fromXml(toConvert);
    }
}
