/**
 * 
 */
package org.verapdf.pdfa.results;

import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.validation.RuleId;

import javax.xml.bind.JAXBException;

import java.io.*;
import java.util.Set;

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
        if (flavour == null) throw new NullPointerException("flavour cannot be null");
        if (assertions == null) throw new NullPointerException("assertions cannot be null");
        return ValidationResultImpl
                .fromValues(flavour, assertions, isCompliant, assertions.size());
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
     * @param totalAssertions 
     * @return a new ValidationResult instance populated from the values
     */
    public static ValidationResult resultFromValues(final PDFAFlavour flavour,
            final Set<TestAssertion> assertions, final boolean isCompliant, final int totalAssertions) {
        if (flavour == null) throw new NullPointerException("flavour cannot be null");
        if (assertions == null) throw new NullPointerException("assertions cannot be null");
        return ValidationResultImpl
                .fromValues(flavour, assertions, isCompliant, totalAssertions);
    }

    /**
     * @param flavour
     *            a {@link PDFAFlavour} instance indicating the validation type
     *            performed
     * @param assertions
     *            the Set of TestAssertions reported by during validation
     * @return a new ValidationResult instance populated from the values
     */
    public static ValidationResult resultFromValues(final PDFAFlavour flavour,
            final Set<TestAssertion> assertions) {
        if (flavour == null) throw new NullPointerException("flavour cannot be null");
        if (assertions == null) throw new NullPointerException("assertions cannot be null");
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
     * Returns an immutable default instance of a ValidationResult. This is a
     * static single instance, i.e.
     * <code>ValidationResults.defaultResult() == ValidationResults.defaultResult()</code>
     * is always true.
     *
     * @return the default ValidationResult instance
     */
    public static ValidationResult defaultResult() {
        return ValidationResultImpl.defaultInstance();
    }

    /**
     * Creates an immutable TestAssertion instance from the passed parameter
     * values.
     * 
     * @param ruleId
     *            the {@link RuleId} value for
     *            {@link org.verapdf.pdfa.validation.Rule} the assertion refers
     *            to.
     * @param status
     *            the {@link Status} of the assertion.
     * @param message
     *            any String message to be associated with the assertion.
     * @param location
     *            a {@link Location} instance indicating the location within the
     *            PDF document where the test was performed.
     * @return an immutable TestAssertion instance initialised using the passed
     *         values
     */
    public static TestAssertion assertionFromValues(final RuleId ruleId,
            final Status status, final String message, final Location location) {
        return TestAssertionImpl.fromValues(ruleId, status, message, location);
    }

    /**
     * Returns an immutable default instance of a TestAssertion. This is a
     * static single instance, i.e.
     * <code>ValidationResults.defaultAssertion() == ValidationResults.defaultAssertion()</code>
     * is always true.
     *
     * @return the default TestAssertion instance
     */
    public static TestAssertion defaultAssertion() {
        return TestAssertionImpl.defaultInstance();
    }

    /**
     * TODO: Better explanation of level and context. Creates an immutable
     * {@link Location} instance.
     * 
     * @param level
     *            the Locations level, represented as a String
     * @param context
     *            the Locations context, represented as a String
     * @return and immutable Location instance initialised with the passed
     *         values.
     */
    public static Location locationFromValues(final String level,
            final String context) {
        return LocationImpl.fromValues(level, context);
    }

    /**
     * Returns an immutable default instance of a Location. This is a static
     * single instance, i.e.
     * <code>ValidationResults.defaultLocation() == ValidationResults.defaultLocation()</code>
     * is always true.
     *
     * @return the default Location instance
     */
    public static Location defaultLocation() {
        return LocationImpl.defaultInstance();
    }

    /**
     * Converts a {@link ValidationResult} instance into an XML String. The
     * returned String can be de-serialised from XML to a
     * {@link ValidationResult} instance by passing it to the
     * {@link ValidationResults#fromXml(String)} method.
     * 
     * @param toConvert
     *            a ValidationResult instance to covert to XML
     * @param prettyXml
     *            controls formatting of the returned XML, {@link Boolean#TRUE}
     *            requests pretty formatting, e.g. new lines and indentation,
     *            {@link Boolean#FALSE} indicates no formatting is required.
     * @return the XML representation of the ValidationResult passed as
     *         <code>toConvert</code>.
     * @throws JAXBException
     *             this occurs if there's a problem converting the passed type
     *             to XML
     * @throws IOException
     *             if a problem occurs writing the converted result to a String.
     */
    public static String resultToXml(final ValidationResult toConvert,
                                     Boolean prettyXml) throws JAXBException, IOException {
        String retVal = "";
        try (StringWriter writer = new StringWriter()) {
            toXml(toConvert, writer, prettyXml);
            retVal = writer.toString();
            return retVal;
        }
    }

    /**
     * Converts an XML representation of a {@link ValidationResult} into a
     * ValidationResult instance.
     * 
     * @param toConvert
     *            a String holding an XML serialisation of a ValidationResult.
     * @return an immutable ValidationResult instance initialised using the
     *         passed XML String
     * @throws JAXBException
     *             when there's an error converting the XML
     */
    public static ValidationResult fromXml(final String toConvert)
            throws JAXBException {
        try (StringReader reader = new StringReader(toConvert)) {
            return fromXml(reader);
        }
    }

    /**
     * Converts a {@link ValidationResult} instance into XML which is written to
     * the passed <code>stream</code>.
     * 
     * @param toConvert
     *            a ValidationResult instance to covert to XML
     * @param stream
     *            an OutputStream, the destination where the XML representation
     *            of the ValidationResult will be written.
     * @param prettyXml
     *            controls formatting of the returned XML, {@link Boolean#TRUE}
     *            requests pretty formatting, e.g. new lines and indentation,
     *            {@link Boolean#FALSE} indicates no formatting is required.
     * @throws JAXBException
     *             this occurs if there's a problem converting the passed type
     *             to XML
     */
    public static void toXml(final ValidationResult toConvert,
            final OutputStream stream, Boolean prettyXml) throws JAXBException {
        ValidationResultImpl.toXml(toConvert, stream, prettyXml);
    }

    /**
     * Converts an XML representation of a {@link ValidationResult} into a
     * ValidationResult instance.
     * 
     * @param toConvert
     *            an ImputStream holding an XML serialisation of a ValidationResult.
     * @return an immutable ValidationResult instance initialised using the
     *         contents of the passed InputStream <code>toConvert</code>. 
     * @throws JAXBException
     *             when there's an error converting the XML
     */
    public static ValidationResult fromXml(final InputStream toConvert)
            throws JAXBException {
        return ValidationResultImpl.fromXml(toConvert);
    }

    /**
     * Converts a {@link ValidationResult} instance into XML which is written to
     * the passed <code>writer</code>.
     * 
     * @param toConvert
     *            a ValidationResult instance to covert to XML
     * @param writer
     *            an {@link Writer} instance, the destination where the XML representation
     *            of the ValidationResult will be written.
     * @param prettyXml
     *            controls formatting of the returned XML, {@link Boolean#TRUE}
     *            requests pretty formatting, e.g. new lines and indentation,
     *            {@link Boolean#FALSE} indicates no formatting is required.
     * @throws JAXBException
     *             this occurs if there's a problem converting the passed type
     *             to XML
     */
    public static void toXml(final ValidationResult toConvert,
            final Writer writer, Boolean prettyXml) throws JAXBException {
        ValidationResultImpl.toXml(toConvert, writer, prettyXml);
    }

    /**
     * Converts an XML representation of a {@link ValidationResult} into a
     * ValidationResult instance.
     * 
     * @param toConvert
     *            a Reader instance, holding an XML serialisation of a ValidationResult.
     * @return an immutable ValidationResult instance initialised using the
     *         contents of the passed InputStream <code>toConvert</code>. 
     * @throws JAXBException
     *             when there's an error converting the XML
     */
    public static ValidationResult fromXml(final Reader toConvert)
            throws JAXBException {
        return ValidationResultImpl.fromXml(toConvert);
    }
}
