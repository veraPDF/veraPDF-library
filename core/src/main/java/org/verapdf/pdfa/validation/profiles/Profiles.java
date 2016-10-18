/**
 *
 */
package org.verapdf.pdfa.validation.profiles;

import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.flavours.PDFAFlavour.Specification;

import javax.xml.bind.JAXBException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/**
 * Utitlity class that provides helper methods for handling
 * {@link ValidationProfile}s and associated classes.
 * <p>
 * The utility methods generally fall into one of the following categories:
 * <ul>
 * <li>default instance creators, <code>defaultTypeName()</code>, used for
 * testing or when a vanilla instance of a particular type is required.</li>
 * <li>from values instance creators, <code>typeNameFromValues(...)</code>, used
 * to create instances from their contained types.</li>
 * <li>XML helper methods, <code>typeNameToXml(...)</code>, to facilitate XML
 * serialisation to Strings, OutputStreams and Writers.</li>
 * <li>XML helper methods, <code>typeNameFromXml(...)</code>, to facilitate XML
 * deserialisation.</li>
 * </ul>
 * Note that XML serialisation and de-serialisation is achieved through JAXB
 * bindings.
 * </p>
 * <p>
 * TODO: Defensive Checks for all parameters.
 * </p>
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public final class Profiles {
    /**
     * Returns a {@link ValidationProfile} instance initialised with the passed
     * values.
     * 
     * @param flavour
     *            the PDF/A flavour supported by this profile represented as a
     *            {@link PDFAFlavour} instance.
     * @param details
     *            the {@link ProfileDetails} for the profile.
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
            final PDFAFlavour flavour, final ProfileDetails details,
            final String hash, final Set<Rule> rules,
            final Set<Variable> variables) {
        if (flavour == null)
            throw new IllegalArgumentException(
                    "Parameter flavour can not be null.");
        if (details == null)
            throw new IllegalArgumentException(
                    "Parameter name can not be null.");
        if (hash == null)
            throw new IllegalArgumentException(
                    "Parameter hash can not be null.");
        if (rules == null)
            throw new IllegalArgumentException(
                    "Parameter rules can not be null.");
        if (variables == null)
            throw new IllegalArgumentException(
                    "Parameter variables can not be null.");
        return ValidationProfileImpl.fromValues(flavour, details, hash, rules,
                variables);
    }

    /**
     * Returns a {@link ValidationProfile} instance initialised with the passed
     * values.
     *
     * @param flavour
     *            the PDF/A flavour supported by this profile represented as a
     *            {@link PDFAFlavour} instance.
     * @param details
     *            the {@link ProfileDetails} for the profile.
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
    public static ValidationProfile profileFromSortedValues(
            final PDFAFlavour flavour, final ProfileDetails details,
            final String hash, final SortedSet<Rule> rules,
            final Set<Variable> variables) {
        if (flavour == null)
            throw new IllegalArgumentException(
                    "Parameter flavour can not be null.");
        if (details == null)
            throw new IllegalArgumentException(
                    "Parameter name can not be null.");
        if (hash == null)
            throw new IllegalArgumentException(
                    "Parameter hash can not be null.");
        if (rules == null)
            throw new IllegalArgumentException(
                    "Parameter rules can not be null.");
        if (variables == null)
            throw new IllegalArgumentException(
                    "Parameter variables can not be null.");
        return ValidationProfileImpl.fromSortedValues(flavour, details, hash,
                rules, variables);
    }

    /**
     * Returns an immutable default instance of a ValidationProfile. This is a
     * static single instance, i.e.
     * <code>Profiles.defaultProfile() == Profiles.defaultProfile()</code> is
     * always true.
     * 
     * @return the {@link ValidationProfile} default instance
     */
    public static ValidationProfile defaultProfile() {
        return ValidationProfileImpl.defaultInstance();
    }

    /**
     * Returns a {@link ProfileDetails} instance initialised using the passed
     * values.
     * 
     * @param name
     *            a String name that identifies the profile
     * @param description
     *            a short, textual String description of the profile.
     * @param creator
     *            a String that identifies the creator of the profile
     * @param created
     *            a {@link Date} instance indicating when the profile was
     *            created.
     * @return the ProfileDetails instance initialised from the values
     */
    public static ProfileDetails profileDetailsFromValues(final String name,
            final String description, final String creator, final Date created) {
        return ProfileDetailsImpl.fromValues(name, description, creator,
                created);
    }

    /**
     * Returns an immutable default instance of a Reference. This is a static
     * single instance, i.e.
     * <code>Profiles.defaultReference() == Profiles.defaultReference()</code>
     * is always true.
     *
     * @return the {@link Reference} default instance
     */
    public static Reference defaultReference() {
        return ReferenceImpl.defaultInstance();
    }

    /**
     * Returns a {@link Reference} instance initialised with the passed values.
     * 
     * @param specification
     *            a String identifying the specification the {@link Reference}
     *            refers to.
     * @param clause
     *            a String identifying the location referred to within the
     *            specification.
     * @return an appropriately initialised Reference instance
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
     * Returns an immutable default instance of a RuleId. This is a static
     * single instance, i.e.
     * <code>Profiles.defaultRuleId() == Profiles.defaultRuleId()</code> is
     * always true.
     *
     * @return the {@link RuleId} default instance
     */
    public static RuleId defaultRuleId() {
        return RuleIdImpl.defaultInstance();
    }

    /**
     * Returns a {@link RuleId} instance initialised with the passed values.
     * 
     * @param specification
     *            a {@link Specification} instance identifying the PDF/A
     *            specification part the RuleId is derived
     * @param clause
     *            a String that identifies that clause within the specification
     *            that the RuleId is derived
     * @param testNumber
     *            an <code>int</code> that identifies the test number for the
     *            RuleId
     * @return a RuleId instance
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
     * Returns an immutable default instance of a Rule. This is a static single
     * instance, i.e.
     * <code>Profiles.defaultRule() == Profiles.defaultRule()</code> is always
     * true.
     *
     * @return the {@link Rule} default instance
     */
    public static Rule defaultRule() {
        return RuleImpl.defaultInstance();
    }

    /**
     * Returns an immutable default instance of ErrorDetails. This is a static
     * single instance, i.e.
     * <code>Profiles.defaultError() == Profiles.defaultError()</code> is always
     * true.
     *
     * @return the {@link ErrorDetails} default instance
     */
    public static ErrorDetails defaultError() {
        return ErrorDetailsImpl.defaultInstance();
    }

    /**
     * Returns a {@link ErrorDetails} instance initialised with the passed
     * values.
     * 
     * @param message
     *            a String message for the {@link ErrorDetails}
     * @param arguments
     *            a List of String arguments for the {@link ErrorDetails}.
     * @return an {@link ErrorDetails} instance
     * @throws IllegalArgumentException
     *             if any of the parameters are null or message is empty
     */
    public static ErrorDetails errorFromValues(final String message,
            final List<String> arguments) {
        if (message == null)
            throw new IllegalArgumentException(
                    "Parameter message can not be null.");
        if (message.isEmpty())
            throw new IllegalArgumentException(
                    "Parameter message can not be empty.");
        if (arguments == null)
            throw new IllegalArgumentException(
                    "Parameter arguments can not be null.");
        return ErrorDetailsImpl.fromValues(message, arguments);
    }

    /**
     * Returns a {@link Rule} instance initialised with the passed values.
     * 
     * @param id
     *            the {@link RuleId} id for the {@link Rule}
     * @param object
     *            a String that identifies the Object that the rule applies to
     * @param description
     *            a textual description of the {@link Rule}.
     * @param test
     *            a JavaScript expression that is the test carried out on a
     *            model instance
     * @param error
     *            the {@link ErrorDetails} associated with the{@link Rule}.
     * @param references
     *            a list of further {@link Reference}s for this rule
     * @return a {@link Rule} instance.
     * @throws IllegalArgumentException
     *             if any of the parameters are null or the test, object, or
     *             description is empty
     */
    public static Rule ruleFromValues(final RuleId id, final String object,
            final String description, final String test,
            final ErrorDetails error, final List<Reference> references) {
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
        if (error == null)
            throw new IllegalArgumentException(
                    "Parameter error can not be null.");
        if (references == null)
            throw new IllegalArgumentException(
                    "Parameter references can not be null.");
        return RuleImpl.fromValues(RuleIdImpl.fromRuleId(id), object,
                description, test, error, references);
    }

    /**
     * Returns an immutable default instance of a Variable. This is a static
     * single instance, i.e.
     * <code>Profiles.defaultVariable() == Profiles.defaultVariable()</code> is
     * always true.
     *
     * @return the {@link Variable} default instance
     */
    public static Variable defaultVariable() {
        return VariableImpl.defaultInstance();
    }

    /**
     * Returns a {@link Variable} instance initialised with the passed values.
     * 
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
     * Convert a {@link ValidationProfile} instance into an XML String.
     * 
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
    public static String profileToXml(final ValidationProfile toConvert,
            final Boolean prettyXml) throws JAXBException, IOException {
        if (toConvert == null)
            throw new IllegalArgumentException(
                    "Parameter toConvert cannot be null");
        return ValidationProfileImpl.toXml(toConvert, prettyXml);
    }

    /**
     * Convert a {@link ValidationProfile} instance to XML and serialise to the
     * {@link OutputStream} <code>forXMLOutput</code>.
     *
     * @param toConvert
     *            a {@link ValidationProfile} to convert to an XML String
     * @param forXmlOutput
     *            an OutputStream used to write the generated XML to
     * @param prettyXml
     *            set to Boolean.TRUE for pretty formatted XML, Boolean.FALSE
     *            for no space formatting
     * @throws JAXBException
     *             thrown by JAXB marshaller if there's an error converting the
     *             object
     * @throws IllegalArgumentException
     *             if toConvert is null
     */
    public static void profileToXml(final ValidationProfile toConvert,
            final OutputStream forXmlOutput, final Boolean prettyXml)
            throws JAXBException {
        if (toConvert == null)
            throw new IllegalArgumentException(
                    "Parameter toConvert cannot be null");
        ValidationProfileImpl.toXml(toConvert, forXmlOutput, prettyXml);
    }

    /**
     * Attempt to de-serialise and return a {@link ValidationProfile} instance
     * from an XML representation that can be read from <code>toConvert</code>.
     *
     * @param toConvert
     *            an InputStream to an XML representation of a profile
     * @return a new {@link ValidationProfile} instance
     * @throws JAXBException
     *             thrown by JAXB marshaller if there's an error converting the
     *             object
     * @throws IllegalArgumentException
     *             if toConvert is null
     */
    public static ValidationProfile profileFromXml(final InputStream toConvert)
            throws JAXBException {
        if (toConvert == null)
            throw new IllegalArgumentException(
                    "Parameter toConvert cannot be null");
        return ValidationProfileImpl.fromXml(toConvert);
    }

    /**
     * Convert a {@link ValidationProfile} instance to XML and serialise to the
     * {@link Writer} <code>forXMLOutput</code>.
     *
     * @param toConvert
     *            a {@link ValidationProfile} to convert to an XML String
     * @param forXmlOutput
     *            a Writer used to write the generated XML to
     * @param prettyXml
     *            set to Boolean.TRUE for pretty formatted XML, Boolean.FALSE
     *            for no space formatting
     * @throws JAXBException
     *             thrown by JAXB marshaller if there's an error converting the
     *             object
     * @throws IllegalArgumentException
     *             if toConvert is null
     */
    public static void profileToXml(final ValidationProfile toConvert,
            Writer forXmlOutput, Boolean prettyXml) throws JAXBException {
        if (toConvert == null)
            throw new IllegalArgumentException(
                    "Parameter toConvert cannot be null");
        ValidationProfileImpl.toXml(toConvert, forXmlOutput, prettyXml);
    }

    /**
     * Create a {@link ProfileDirectory} from a <code>Set</code> of
     * {@link ValidationProfile}s. Note that the returned directory uses each
     * <code>ValidationProfile</code>'s associated {@link PDFAFlavour} as a
     * directory key. This means that only a single
     * <code>ValidationProfile</code> can be associated with a particular
     * <code>PDFAFlavour</code>. If the <code>Set</code> of Profiles passed in
     * <code>profiles</code> contains multiple <code>ValidationProfile</code>s
     * with the same <code>PDFAFlavour</code> only one will be contained in the
     * returned <code>ProfileDirectory</code>. Which one is indeterminate.
     * 
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
     * Returns a {@link ProfileDirectory} instance that has been pre-populated
     * with the curated {@link ValidationProfile}s supplied with the veraPDF
     * library.
     * <p>
     * While the veraPDF library and associated <code>ValidationProfile</code>s
     * are under development, there is no guarantee that the profiles supplied
     * are complete and accurate. Please check the <a
     * href="https://github.com/veraPDF/veraPDF-validation-profiles">validation
     * profiles GitHub repo</a> to find out the current status of our
     * ValidationProfiles.
     * </p>
     *
     * @return the pre-populated veraPDF ValidationProfile directory
     */
    public static ProfileDirectory getVeraProfileDirectory() {
        return ProfileDirectoryImpl.getVeraProfileDirectory();
    }

    /**
     * Returns the JAXB generated XML schema for the ValidationProfileImpl type.
     * 
     * @return the String representation of the schema
     * @throws JAXBException
     *             if there's a problem marshaling the schema
     * @throws IOException
     *             if there's a problem outputting the result
     */
    public static String getValidationProfileSchema() throws JAXBException,
            IOException {
        return ValidationProfileImpl.getSchema();
    }

    public static class RuleIdComparator implements Comparator<RuleId> {
        @Override
        public int compare(RuleId firstId, RuleId secondId) {
            if (firstId.getClause().equals(secondId.getClause())) {
                return firstId.getTestNumber() - secondId.getTestNumber();
            }
            String[] o1StrArr = firstId.getClause().split("\\.");
            String[] o2StrArr = secondId.getClause().split("\\.");
            int min = Math.min(o1StrArr.length, o2StrArr.length);

            for (int i = 0; i < min; ++i) {
                if (!o1StrArr[i].equals(o2StrArr[i])) {
                    return Integer.parseInt(o1StrArr[i])
                            - Integer.parseInt(o2StrArr[i]);
                }
            }

            return o1StrArr.length - o2StrArr.length;
        }
    }

    public static class RuleComparator implements Comparator<Rule> {
        @Override
        public int compare(Rule firstRule, Rule secondRule) {
            return new RuleIdComparator().compare(firstRule.getRuleId(),
                    secondRule.getRuleId());
        }
    }
}
