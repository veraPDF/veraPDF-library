package org.verapdf.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.zip.ZipException;

import org.junit.AfterClass;
import org.junit.Test;
import org.verapdf.core.ValidationException;
import org.verapdf.model.ModelParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.qa.CorpusItemId;
import org.verapdf.pdfa.qa.CorpusItemIdImpl;
import org.verapdf.pdfa.qa.GitHubBackedProfileDirectory;
import org.verapdf.pdfa.qa.TestCorpus;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.ProfileDirectory;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.pdfa.validators.Validators;

@SuppressWarnings({ "javadoc", "static-method" })
public class ITVeraCorpusTests {
    // Strings for parsting zip entry names
    private final static String ZIP_SEPARATOR = "/";
    // Directory of validation profiles poulated by download from GitHub
    private static final ProfileDirectory PROFILES = GitHubBackedProfileDirectory.INTEGRATION;
    private static final Map<String, Map<PDFAFlavour, ValidationResult>> RESULTS = new HashMap<>();

    @AfterClass
    public static void outputResults() {
        for (String zipEntryName : RESULTS.keySet()) {
            System.out.println();
            System.out.println(zipEntryName);
            for (PDFAFlavour flavour : RESULTS.get(zipEntryName).keySet()) {
                ValidationResult result = RESULTS.get(zipEntryName)
                        .get(flavour);
                System.out.println(flavour.getId() + " isCompliant="
                        + result.isCompliant() + ", failedTests="
                        + result.getTestAssertions().size());
                for (TestAssertion assertion : result.getTestAssertions()) {
                    System.out.println(assertion.getRuleId()
                            + " "
                            + PROFILES.getValidationProfileByFlavour(flavour)
                                    .getRuleByRuleId(assertion.getRuleId())
                                    .getError().getMessage());
                }
            }
        }
    }

    /**
     * Build PDFA-1B filter and test 1B validation
     * 
     * @throws IOException
     *             when the test loop fails, failing the test
     */
    public void performPdf1BValidation() throws IOException {
        PDFAFlavour[] filters = new PDFAFlavour[] { PDFAFlavour.PDFA_1_B };
        validateFlavour(PDFAFlavour.PDFA_1_B, Arrays.asList(filters));
    }

    /**
     * Build PDFA-1A filter and test 1A validation
     * 
     * @throws IOException
     *             when the test loop fails, failing the test
     */
    public void performPdf1AValidation() throws IOException {
        PDFAFlavour[] filters = new PDFAFlavour[] { PDFAFlavour.PDFA_1_B,
                PDFAFlavour.PDFA_1_A };
        validateFlavour(PDFAFlavour.PDFA_1_A, Arrays.asList(filters));
    }

    /**
     * Build PDFA-2B filter and test 2B validation
     * 
     * @throws IOException
     *             when the test loop fails, failing the test
     */
    public void performPdf2BValidation() throws IOException {
        PDFAFlavour[] filters = new PDFAFlavour[] { PDFAFlavour.PDFA_2_B };
        validateFlavour(PDFAFlavour.PDFA_2_B, Arrays.asList(filters));
    }

    /**
     * Build PDFA-3B filter and test 3B validation
     * 
     * @throws IOException
     *             when the test loop fails, failing the test
     */
    public void performPdf3BValidation() throws IOException {
        PDFAFlavour[] filters = new PDFAFlavour[] { PDFAFlavour.PDFA_3_B };
        validateFlavour(PDFAFlavour.PDFA_3_B, Arrays.asList(filters));
    }

    /**
     * Main test loop for a flavour TODO: This is still a little messy, corpus
     * needs a class abstraction to drive the tests
     * 
     * @param flavour
     *            the flavour to be validated
     * @param filters
     *            a List of flavours used to select appropriate corpus files
     * @throws ZipException
     *             when there's a problem unpacking the corpus zip file
     * @throws IOException
     *             when there's a problem reading a particular zip entry
     */
    @Test
    public void validateAllFlavours() throws ZipException, IOException {
        Map<PDFAFlavour, PDFAValidator> validators = new HashMap<>();
        for (ValidationProfile profile : PROFILES.getValidationProfiles()) {
            validators.put(profile.getPDFAFlavour(),
                    Validators.validate(profile, false));
        }

        TestCorpus veraPDFcorpus = CorpusManager.getVeraCorpus();

        for (String itemName : veraPDFcorpus.getItemNames()) {
            String[] pathParts = itemName.split(ZIP_SEPARATOR);
            String namePart = pathParts[pathParts.length - 1];

            RESULTS.put(namePart, new HashMap<PDFAFlavour, ValidationResult>());
            for (PDFAFlavour flavour : validators.keySet()) {

                // Parse a CorpusItemId from the file name, this gives us an
                // expected pass or fail result.

                // Try with resources for model parser from zip stream
                // TODO: This inner try needs at least refactoring to
                // another a
                // sub-function
                try (ModelParser loader = new ModelParser(
                        veraPDFcorpus.getItemStream(itemName))) {

                    // A "stateless" validator instance for each entry,
                    // guaranteed to
                    // be initialised.
                    // Get the stateless validator result
                    ValidationResult result = validators.get(flavour).validate(
                            loader);
                    RESULTS.get(namePart).put(flavour, result);
                } catch (IOException e) {
                    System.err.println("Error parsing PDF Model for "
                            + itemName);
                    e.printStackTrace();
                } catch (Exception e) {
                    System.err.println("Error carrying out validation for "
                            + itemName);
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    private static void validateFlavour(final PDFAFlavour flavour,
            final List<PDFAFlavour> filters) throws ZipException, IOException {
        // Try with resource with the downlodaded corpus zip file
        try {
            TestCorpus isartorCorpus = CorpusManager.getIsartorCorpus();
            // Get the appropriate validation profile for the flavour
            ValidationProfile profile = PROFILES
                    .getValidationProfileByFlavour(flavour);

            // Stateful validator means a single instance will perform all
            // checks. This is to test that a validator re-initialises itself
            // after validating.
            PDFAValidator statefulValidator = Validators.validate(profile,
                    false);

            // Grab an enumeration of the zip entries and loop through
            for (String itemName : isartorCorpus.getItemNames()) {
                // Grab the next zip entry and split the path parts
                String[] pathParts = itemName.split(ZIP_SEPARATOR);

                // Parse a CorpusItemId from the file name, this gives us an
                // expected pass or fail result.
                CorpusItemId corpusId = CorpusItemIdImpl.fromFileName(
                        flavour.getPart(), pathParts[pathParts.length - 1]);

                // Try with resources for model parser from zip stream
                // TODO: This inner try needs at least refactoring to another a
                // sub-function
                try (ModelParser loader = new ModelParser(
                        isartorCorpus.getItemStream(itemName))) {
                    // A "stateless" validator instance for each entry,
                    // guaranteed to
                    // be initialised.
                    PDFAValidator statelessValidator = Validators.validate(
                            profile, false);
                    // Get the stateless validator result
                    ValidationResult statelessResult = statelessValidator
                            .validate(loader);
                    // Get the stateful validator result
                    ValidationResult statefulResult = statefulValidator
                            .validate(loader);
                    // The results should be equal (this means that the
                    // validator re-initialises correctly)
                    assertTrue(statefulResult.equals(statelessResult));
                    // We don't have instance control so this should always be
                    // false, useful check against assumptions
                    assertFalse(statefulResult == statelessResult);
                } catch (IOException e) {
                    System.err.println("Error parsing PDF Model for "
                            + itemName);
                    e.printStackTrace();
                } catch (ValidationException | NegativeArraySizeException e) {
                    System.err.println("Error carrying out validation for "
                            + itemName);
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                }
            }
        } catch (NoSuchElementException excep) {
            System.err.println("No validation profile found for flavour: "
                    + flavour);
            System.err.println(excep.getMessage());
        }
    }

    /**
     * Tests the passed String {@code parseForMatches} and returns true if
     * {@link PDFAFlavour#fromString(String)} returns one of the flavours in
     * {@code filters}.
     * 
     * @param parseForMatches
     *            string to test for flavour matches
     * @param filters
     *            {@code List} of {@link PDFAFlavour}s to test against for
     *            matches
     * @return true of {@code PDFAFlavour} parsed from {@code parseForMatches}
     *         is contained in {@code filters}.
     */
    private static boolean matchesFlavourFilter(final String parseForMatches,
            final List<PDFAFlavour> filters) {
        PDFAFlavour flavour = PDFAFlavour.fromString(parseForMatches);
        return filters.contains(flavour);
    }
}
