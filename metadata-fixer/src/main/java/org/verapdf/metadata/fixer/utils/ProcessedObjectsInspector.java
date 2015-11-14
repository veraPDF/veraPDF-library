package org.verapdf.metadata.fixer.utils;

import org.verapdf.metadata.fixer.utils.model.ProcessedObjects;
import org.verapdf.metadata.fixer.utils.parser.ProcessedObjectsParser;
import org.verapdf.metadata.fixer.utils.parser.XMLProcessedObjectsParser;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.validation.Rule;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

/**
 * Current class check status of validation according to failed rules,
 * validation profile and list of Metadata failed rule types.
 * <p>
 * Metadata failed rule type is list of rules that belongs to metadata check
 * rules. This rules can be obtain by {@link ProcessedObjectsParser}. Default
 * implementation of this interface is {@link XMLProcessedObjectsParser}.
 * </p>
 *
 * @author Evgeniy Muravitskiy
 *
 * @see Rule
 * @see ValidationProfile
 * @see ValidationStatus
 */
public class ProcessedObjectsInspector {

    private ProcessedObjectsInspector() {
        // hide default constructor
    }

    /**
     * Return validation status according to failed rules and validation
     * profile. {@link ProcessedObjects} obtain from xml file.
     * @param assertions 
     *
     * @param rules
     *            list of rules
     * @param profile
     *            validation profile
     * @return validation status
     * @throws URISyntaxException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws SAXException 
     */
    public static ValidationStatus validationStatus(
            Set<TestAssertion> assertions, ValidationProfile profile)
            throws URISyntaxException, IOException,
            ParserConfigurationException, SAXException {
        ProcessedObjectsParser parser = XMLProcessedObjectsParser.getInstance();
        return validationStatus(assertions, profile, parser);
    }

    /**
     * Return validation status according to failed rules and validation
     * profile. {@link ProcessedObjects} obtain by
     * {@link ProcessedObjectsParser}.
     * @param assertions 
     *
     * @param rules
     *            list of rules
     * @param profile
     *            validation profile
     * @param parser
     *            parse some data to {@code ProcessedObjects}
     * @return validation status
     * @throws IOException 
     * @throws URISyntaxException 
     * @throws ParserConfigurationException 
     * @throws SAXException 
     */
    public static ValidationStatus validationStatus(
            Set<TestAssertion> assertions, ValidationProfile profile,
            ProcessedObjectsParser parser) throws IOException,
            URISyntaxException, ParserConfigurationException, SAXException {
        return validationStatus(assertions, profile,
                parser.getProcessedObjects());
    }

    /**
     * Return validation status according to failed rules, validation profile
     * and metadata failed rules type
     * @param assertions 
     *
     * @param rules
     *            list of rules
     * @param profile
     *            validation profile
     * @param objects
     *            metadata failed rules type
     * @return validation status
     * @throws IOException 
     * @throws URISyntaxException 
     * @throws ParserConfigurationException 
     * @throws SAXException 
     */
    public static ValidationStatus validationStatus(
            Set<TestAssertion> assertions, ValidationProfile profile,
            ProcessedObjects objects) throws IOException, URISyntaxException,
            ParserConfigurationException, SAXException {
        ValidationStatus status = ValidationStatus.VALID;

        for (TestAssertion assertion : assertions) {
            if (assertion.getStatus() == Status.FAILED) {
                status = checkCurrentRule(profile, objects, assertion, status);
            }
        }

        return status;
    }

    private static ValidationStatus checkCurrentRule(ValidationProfile profile,
            ProcessedObjects objects, TestAssertion rule,
            ValidationStatus status) {
        Rule profileRule = profile.getRuleByRuleId(rule.getRuleId());
        String objectType = profileRule.getObject();
        if (objects.contains(objectType, profileRule.getTest())) {
            return ValidationStatus.INVALID_METADATA.getStatus(status);
        }
        return ValidationStatus.INVALID_STRUCTURE.getStatus(status);
    }

}
