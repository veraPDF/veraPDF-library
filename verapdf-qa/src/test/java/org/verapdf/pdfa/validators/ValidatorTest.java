/**
 * 
 */
package org.verapdf.pdfa.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.verapdf.core.ValidationException;
import org.verapdf.integration.CorpusManager;
import org.verapdf.model.ModelParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.qa.CorpusSampler;
import org.verapdf.pdfa.qa.GitHubBackedProfileDirectory;
import org.verapdf.pdfa.qa.TestCorpus;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.validation.ProfileDirectory;
import org.verapdf.pdfa.validation.ValidationProfile;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class ValidatorTest {
    private static final ProfileDirectory PROFILES = GitHubBackedProfileDirectory.INTEGRATION;

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validators.BaseValidator#getProfile()}.
     */
    @Test
    public final void testGetProfile() {
        for (ValidationProfile profile : PROFILES.getValidationProfiles()) {
            PDFAValidator validator = Validators
                    .createValidator(profile, false);
            assertTrue(profile.equals(validator.getProfile()));
        }
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validators.BaseValidator#validate(org.verapdf.pdfa.ValidationModelParser)}
     * .
     * 
     * @throws IOException
     * @throws JAXBException
     */
    @Test
    public final void testValidateValidationConsistency() throws IOException,
            JAXBException {
        // Grab a random sample of 20 corpus files
        TestCorpus veraCorpus = CorpusManager.getVeraCorpus();
        Set<String> sample = CorpusSampler.randomSample(veraCorpus, 20);
        // / Cycle through sample
        for (String itemName : sample) {
            // Try all profiles
            for (ValidationProfile profile : PROFILES.getValidationProfiles()) {
                // Create a validator for profile
                PDFAValidator validator = Validators.createValidator(profile,
                        false);
                Set<ValidationResult> results = new HashSet<>();
                // Validate a fresh model instance and add the result to the set
                for (int index = 0; index < 2; index++) {
                    try (ModelParser parser = new ModelParser(
                            veraCorpus.getItemStream(itemName))) {
                        ValidationResult result = validator.validate(parser);
                        results.add(result);
                    } catch (ValidationException e) {
                        checkValidationException(itemName, e);
                        results.add(ValidationResults.defaultResult());
                    }
                }
                assertTrue(
                        resultsMessage(veraCorpus.getDetails().getName(),
                                itemName, profile.getPDFAFlavour().toString(),
                                results), results.size() == 1);
            }
        }
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testFailFastValidator() throws IOException, JAXBException {
        // Grab a random sample of 20 corpus files
        TestCorpus veraCorpus = CorpusManager.getVeraCorpus();
        Set<String> sample = CorpusSampler.randomSample(veraCorpus, 20);
        // / Cycle through sample
        for (String itemName : sample) {
            // Try all profiles
            for (ValidationProfile profile : PROFILES.getValidationProfiles()) {
                // Create a validator for the profile and get a result with no
                // failures
                PDFAValidator validator = Validators.createValidator(profile,
                        false);
                ValidationResult result = ValidationResults.defaultResult();
                // Validate a fresh model instance and add the result to the set
                try (ModelParser parser = new ModelParser(
                        veraCorpus.getItemStream(itemName))) {
                    result = validator.validate(parser);
                } catch (ValidationException e) {
                    checkValidationException(itemName, e);
                    continue;
                }
                int failedMax = result.getTestAssertions().size() + 1;
                // Set up a loop to restrict failures
                for (int index = failedMax; index > 0; index--) {
                    PDFAValidator fastFailValidator = Validators
                            .createValidator(profile, false, index);
                    ValidationResult failFastResult = ValidationResults
                            .defaultResult();
                    try (ModelParser parser = new ModelParser(
                            veraCorpus.getItemStream(itemName))) {
                        failFastResult = fastFailValidator.validate(parser);
                    } catch (ValidationException e) {
                        checkValidationException(itemName, e);
                        continue;
                    }
                    if (index == failedMax) {
                        assertTrue(resultsComparisonMessage(veraCorpus
                                        .getDetails().getName(), itemName,
                                        profile.getPDFAFlavour().toString(),
                                        result, failFastResult),
                                result.equals(failFastResult));
                    } else if ((index == (failedMax -1)) && (getMaxFailureOrdinal(result) == result.getTotalAssertions())) {
                        assertTrue(resultsComparisonMessage(veraCorpus
                                .getDetails().getName(), itemName,
                                profile.getPDFAFlavour().toString(),
                                result, failFastResult),
                        result.equals(failFastResult));
                    } else if (index < failedMax) {
                        assertFalse(resultsComparisonMessage(veraCorpus
                                .getDetails().getName(), itemName,
                                profile.getPDFAFlavour().toString(),
                                result, failFastResult), result.equals(failFastResult));

                    }
                }
            }
        }
    }

    /**
     * TODO: Sort the validator consistency issues
     * 
     * @throws IOException
     * @throws ValidationException
     * @throws JAXBException
     */
    // @Test
    public void testModelConsistency() throws IOException, ValidationException,
            JAXBException {
        // Grab a random sample of 10 corpus files
        TestCorpus veraCorpus = CorpusManager.getVeraCorpus();
        Set<String> sample = CorpusSampler.randomSample(veraCorpus, 10);

        // Cycle through all available profile on GitHub
        for (ValidationProfile profile : PROFILES.getValidationProfiles()) {
            for (String itemName : sample) {
                // Create fresh validators for each sample item
                PDFAValidator validator = Validators.createValidator(profile,
                        false);
                PDFAValidator checkValidator = Validators.createValidator(
                        profile, false);
                // Create a new model parser instance
                try (ModelParser parser = new ModelParser(
                        veraCorpus.getItemStream(itemName))) {
                    // Validate model with fresh validator
                    ValidationResult firstResult = validator.validate(parser);
                    // Validate same model with second fresh validator instance
                    ValidationResult checkResult = checkValidator
                            .validate(parser);
                    // Validate model with first validator again
                    ValidationResult secondResult = validator.validate(parser);

                    // The results of the two separate validators should be the
                    // same (this works)
                    assertTrue(
                            resultsComparisonMessage(veraCorpus.getDetails()
                                    .getName(), itemName, profile
                                    .getPDFAFlavour().toString(), firstResult,
                                    secondResult),
                            checkResult.equals(secondResult));
                    // The results of the same validator should be the same
                    // (this doesn't)
                    // The act of validation changes something in the
                    // model......
                    assertTrue(
                            resultsComparisonMessage(veraCorpus.getDetails()
                                    .getName(), itemName, profile
                                    .getPDFAFlavour().toString(), firstResult,
                                    secondResult),
                            firstResult.equals(secondResult));
                }
            }
        }
    }

    private static boolean checkValidationException(final String itemName,
            final ValidationException excep) {
        if (excep.getCause() instanceof NegativeArraySizeException) {
            System.err.println("Expected Exception" + excep.getMessage()
                    + ", while validating" + itemName);
        } else {
            excep.printStackTrace();
            fail("Exception" + excep.getMessage() + ", while validating"
                    + itemName);
        }
        return true;
    }

    private String testContextMessage(final String corpus,
            final String itemName, final String profile) {
        return "corpus=" + corpus + "\nitemName=" + itemName + "\nprofile="
                + profile + "\n";
    }

    private String resultsMessage(final String corpus, final String itemName,
            final String profile, final Set<ValidationResult> results)
            throws JAXBException {
        StringWriter writer = new StringWriter();
        writer.write(testContextMessage(corpus, itemName, profile));
        writer.write("\nSet<ValidationResults>.size()=" + results.size()
                + "\nResults:\n");

        if (results.size() > 0) {
            for (ValidationResult result : results) {
                ValidationResults.toXml(result, writer, Boolean.TRUE);
            }
        }
        return writer.toString();
    }

    private String resultsComparisonMessage(final String corpus,
            final String itemName, final String profile,
            final ValidationResult firstResult,
            final ValidationResult secondResult) throws JAXBException {
        StringWriter writer = new StringWriter();
        writer.write(testContextMessage(corpus, itemName, profile));
        writer.write("\nFirstResult:\n");
        ValidationResults.toXml(firstResult, writer, Boolean.TRUE);
        writer.write("\nSecondResult:\n");
        ValidationResults.toXml(secondResult, writer, Boolean.TRUE);
        return writer.toString();
    }
    
    private int getMaxFailureOrdinal(final ValidationResult result) {
        int maxOrdinal = 0;
        for (TestAssertion assertion : result.getTestAssertions()) {
            if ((assertion.getStatus() == Status.FAILED) && (assertion.getOrdinal() > maxOrdinal))
                maxOrdinal = assertion.getOrdinal();
        }
        return maxOrdinal;
    }
}
