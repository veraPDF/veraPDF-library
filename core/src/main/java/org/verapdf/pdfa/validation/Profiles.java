/**
 *
 */
package org.verapdf.pdfa.validation;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.verapdf.pdfa.ValidationProfile;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.flavours.PDFAFlavour.Specification;

/**
 * TODO: Defensive Checks for all parameters & JavaDoc
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public final class Profiles {
    /**
     * @param flavour
     *            the PDF/A flavour supported by this profile represented as a
     *            {@link PDFAFlavour} instance.
     * @param name
     *            a String name for the profile
     * @param description
     *            a short textual description of the profile
     * @param creator
     *            a String identifying the profile's creator
     * @param created
     *            a Date instance signifying the creation date of the profile
     * @param hash
     *            an identifying hash for the profile
     * @param rules
     *            the Set of {@link Rule}s for the profile
     * @param variables
     *            the Set of {@link Variable}s for the profile
     * @return a new ValidationProfile instance.
     * @throws IllegalArgumentException
     *             if any of the passed parameters are null or if any of name,
     *             description or creator are empty.
     */
    public static ValidationProfile profileFromValues(
            final PDFAFlavour flavour, final String name,
            final String description, final String creator, final Date created,
            final String hash, final Set<Rule> rules,
            final Set<Variable> variables) {
        if (flavour == null)
            throw new IllegalArgumentException(
                    "Parameter flavour can not be null.");
        if (name == null)
            throw new IllegalArgumentException(
                    "Parameter name can not be null.");
        if (name.isEmpty())
            throw new IllegalArgumentException(
                    "Parameter name can not be empty.");
        if (description == null)
            throw new IllegalArgumentException(
                    "Parameter description can not be null.");
        if (description.isEmpty())
            throw new IllegalArgumentException(
                    "Parameter description can not be empty.");
        if (creator == null)
            throw new IllegalArgumentException(
                    "Parameter creator can not be null.");
        if (creator.isEmpty())
            throw new IllegalArgumentException(
                    "Parameter creator can not be empty.");
        if (created == null)
            throw new IllegalArgumentException(
                    "Parameter created can not be null.");
        if (hash == null)
            throw new IllegalArgumentException(
                    "Parameter hash can not be null.");
        if (rules == null)
            throw new IllegalArgumentException(
                    "Parameter rules can not be null.");
        if (variables == null)
            throw new IllegalArgumentException(
                    "Parameter variables can not be null.");
        return ValidationProfileImpl.fromValues(flavour, name, description,
                creator, created, hash, rules, variables);
    }

    /**
     * @return the {@link ValidationProfile} default instance
     */
    public static ValidationProfile defaultProfile() {
        return ValidationProfileImpl.defaultInstance();
    }

    /**
     * @return the {@link Reference} default instance
     */
    public static Reference defaultReference() {
        return ReferenceImpl.defaultInstance();
    }

    /**
     * @param specification
     *            a String identifying the specification the {@link Reference}
     *            refers to.
     * @param clause
     *            a String identifying the location referred to within the
     *            specification.
     * @return a new Reference instance
     * @throws IllegalArgumentException
     *             if any of the parameters are null or the specification is
     *             empty
     */
    public static Reference referenceFromValues(final String specification,
            final String clause) {
        if (specification == null)
            throw new IllegalArgumentException(
                    "Parameter specification can not be null.");
        if (specification.isEmpty())
            throw new IllegalArgumentException(
                    "Parameter specification can not be empty.");
        if (clause == null)
            throw new IllegalArgumentException(
                    "Parameter clause can not be null.");
        return ReferenceImpl.fromValues(specification, clause);
    }

    /**
     * @return the {@link RuleId} default instance
     */
    public static RuleId defaultRuleId() {
        return RuleIdImpl.defaultInstance();
    }

    /**
     * @param specification
     *            a {@link Specification} instance identifying the PDF/A
     *            specification part the RuleId is derived
     * @param clause
     *            a String that identifies that clause within the specification
     *            that the RuleId is derived
     * @param testNumber
     *            an <code>int</code> that identifies the test number for the
     *            RuleId
     * @return a new RuleId instance
     * @throws IllegalArgumentException
     *             if any of the parameters are null or the clause is empty
     */
    public static RuleId ruleIdFromValues(final Specification specification,
            final String clause, final int testNumber) {
        if (specification == null)
            throw new IllegalArgumentException(
                    "Parameter specification can not be null.");
        if (clause == null)
            throw new IllegalArgumentException(
                    "Parameter clause can not be null.");
        if (clause.isEmpty())
            throw new IllegalArgumentException(
                    "Parameter clause can not be empty.");
        return RuleIdImpl.fromValues(specification, clause, testNumber);
    }

    /**
     * @return the {@link Rule} default instance
     */
    public static Rule defaultRule() {
        return RuleImpl.defaultInstance();
    }

    /**
     * @param id
     *            the {@link RuleId} id for the {@link Rule}
     * @param object
     *            a String that identifies the Object that the rule applies to
     * @param description
     *            a textual description of the {@link Rule}.
     * @param test
     *            a JavaScript expression that is the test carried out on a
     *            model instance
     * @param references
     *            a list of further {@link Reference}s for this rule
     * @return a new {@link Rule} instance.
     * @throws IllegalArgumentException
     *             if any of the parameters are null or the test, object, or
     *             description is empty
     */
    public static Rule ruleFromValues(final RuleId id, final String object,
            final String description, final String test,
            final List<Reference> references) {
        if (id == null)
            throw new IllegalArgumentException("Parameter id can not be null.");
        if (object == null)
            throw new IllegalArgumentException(
                    "Parameter object can not be null.");
        if (object.isEmpty())
            throw new IllegalArgumentException(
                    "Parameter object can not be empty.");
        if (description == null)
            throw new IllegalArgumentException(
                    "Parameter description can not be null.");
        if (description.isEmpty())
            throw new IllegalArgumentException(
                    "Parameter description can not be empty.");
        if (test == null)
            throw new IllegalArgumentException(
                    "Parameter test can not be null.");
        if (test.isEmpty())
            throw new IllegalArgumentException(
                    "Parameter test can not be empty.");
        if (references == null)
            throw new IllegalArgumentException(
                    "Parameter references can not be null.");
        return RuleImpl.fromValues(RuleIdImpl.fromRuleId(id), object,
                description, test, references);
    }

    /**
     * @return the {@link Variable} default instance
     */
    public static Variable defaultVariable() {
        return VariableImpl.defaultInstance();
    }

    /**
     * @param name
     *            a name for the {@link Variable}
     * @param object
     *            a String identifying the object type for the {@link Variable}
     * @param defaultValue
     *            a String default value for the {@link Variable}
     * @param value
     *            a value for the for the {@link Variable}
     * @return a new {@link Variable} instance
     * @throws IllegalArgumentException
     *             if any of the parameters are null or empty
     */
    public static Variable variableFromValues(final String name,
            final String object, final String defaultValue, final String value) {
        if (name == null)
            throw new IllegalArgumentException("Parameter name cannot be null");
        if (name.isEmpty())
            throw new IllegalArgumentException("Parameter name cannot be empty");
        if (object == null)
            throw new IllegalArgumentException(
                    "Parameter object cannot be null");
        if (object.isEmpty())
            throw new IllegalArgumentException(
                    "Parameter object cannot be empty");
        if (defaultValue == null)
            throw new IllegalArgumentException(
                    "Parameter defaultValue cannot be null");
        if (defaultValue.isEmpty())
            throw new IllegalArgumentException(
                    "Parameter defaultValue cannot be empty");
        if (value == null)
            throw new IllegalArgumentException("Parameter value cannot be null");
        if (value.isEmpty())
            throw new IllegalArgumentException(
                    "Parameter value cannot be empty");
        return VariableImpl.fromValues(name, object, defaultValue, value);
    }

    /**
     * @param toConvert
     *            a {@link ValidationProfile} to convert to an XML String
     * @param prettyXml
     *            set to Boolean.TRUE for pretty formatted XML, Boolean.FALSE
     *            for no space formatting
     * @return a String xml representation of toConvert
     * @throws JAXBException
     *             thrown by JAXB marshaller if there's an error converting the
     *             object
     * @throws IOException
     *             thrown when's there's a problem closing the underlying
     *             StringWriter
     * @throws IllegalArgumentException
     *             if toConvert is null
     */
    public static String profileToXmlString(final ValidationProfile toConvert,
            Boolean prettyXml) throws JAXBException, IOException {
        if (toConvert == null)
            throw new IllegalArgumentException(
                    "Parameter toConvert cannot be null");
        return ValidationProfileImpl.toXml(toConvert, prettyXml);
    }

    /**
     * @param toConvert
     *            a {@link ValidationProfile} to convert to an XML String
     * @param forXmlOutput an OutputStream used to write the generated XML to
     * @param prettyXml
     *            set to Boolean.TRUE for pretty formatted XML, Boolean.FALSE
     *            for no space formatting
     * @throws JAXBException
     *             thrown by JAXB marshaller if there's an error converting the
     *             object
     * @throws IllegalArgumentException
     *             if toConvert is null
     */
    public static void profileToXml(final ValidationProfile toConvert, OutputStream forXmlOutput,
            Boolean prettyXml) throws JAXBException {
        if (toConvert == null)
            throw new IllegalArgumentException(
                    "Parameter toConvert cannot be null");
        ValidationProfileImpl.toXml(toConvert, forXmlOutput, prettyXml);
    }

    /**
     * @param toConvert
     *            a {@link ValidationProfile} to convert to an XML String
     * @param forXmlOutput a Writer used to write the generated XML to
     * @param prettyXml
     *            set to Boolean.TRUE for pretty formatted XML, Boolean.FALSE
     *            for no space formatting
     * @throws JAXBException
     *             thrown by JAXB marshaller if there's an error converting the
     *             object
     * @throws IllegalArgumentException
     *             if toConvert is null
     */
    public static void profileToXml(final ValidationProfile toConvert, Writer forXmlOutput,
            Boolean prettyXml) throws JAXBException {
        if (toConvert == null)
            throw new IllegalArgumentException(
                    "Parameter toConvert cannot be null");
        ValidationProfileImpl.toXml(toConvert, forXmlOutput, prettyXml);
    }

    /**
     * @param profiles
     *            a Set of {@link ValidationProfile}s used to populate the
     *            directory instance
     * @return a ProfileDirectory populated with the {@link ValidationProfile}s
     *         passed in the profiles parameter
     * @throws IllegalArgumentException
     *             if the profiles parameter is null or empty
     */
    public static ProfileDirectory directoryFromProfiles(
            Set<ValidationProfile> profiles) {
        if (profiles == null)
            throw new IllegalArgumentException(
                    "Parameter profiles cannot be null.");
        if (profiles.isEmpty())
            throw new IllegalArgumentException(
                    "Parameter profiles cannot be empty.");
        return ProfileDirectoryImpl.fromProfileSet(profiles);
    }

    /**
     * Creates a lookup Map of {@link ValidationProfile}s from the passed Set.
     *
     * @param profileSet
     *            a set of ValidationProfiles
     * @return a Map<PDFAFlavour, ValidationProfile> from the Set of
     *         {@link ValidationProfile}s
     * @throws IllegalArgumentException
     *             if the profileSet parameter is null or empty
     */
    public static Map<PDFAFlavour, ValidationProfile> profileMapFromSet(
            Set<ValidationProfile> profileSet) {
        if (profileSet == null)
            throw new IllegalArgumentException(
                    "Parameter profileSet cannot be null.");
        if (profileSet.isEmpty())
            throw new IllegalArgumentException(
                    "Parameter profileSet cannot be empty.");
        return ProfileDirectoryImpl.profileMapFromSet(profileSet);
    }

    /**
     * Creates a lookup Map of {@link PDFAFlavour}s by String Id from the passed
     * Set.
     *
     * @param flavours
     *            a Set of {@link PDFAFlavour}s
     * @return a Map created from the passed set where the Map key is the String
     *         ID of the flavour
     * @throws IllegalArgumentException
     *             if the flavours parameter is null or empty
     */
    public static Map<String, PDFAFlavour> flavourMapFromSet(
            Set<PDFAFlavour> flavours) {
        if (flavours == null)
            throw new IllegalArgumentException(
                    "Parameter flavours cannot be null.");
        if (flavours.isEmpty())
            throw new IllegalArgumentException(
                    "Parameter flavours cannot be empty.");
        return ProfileDirectoryImpl.flavourMapFromSet(flavours);
    }
}
