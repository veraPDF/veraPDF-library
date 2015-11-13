package org.verapdf.integration;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.xml.bind.JAXBException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.core.ValidationException;
import org.verapdf.model.ModelParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.qa.CorpusItemId;
import org.verapdf.pdfa.qa.CorpusItemIdImpl;
import org.verapdf.pdfa.qa.GitHubBackedProfileDirectory;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.ProfileDirectory;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.pdfa.validators.Validators;

@SuppressWarnings({ "javadoc", "static-method" })
public class ITVeraCorpusTests {
    // Strings for parsting zip entry names
    private final static String ZIP_SEPARATOR = "/";
    private final static String PDF_SUFFIX = ".pdf";
    // Directory of validation profiles poulated by download from GitHub
    private static final ProfileDirectory PROFILES = GitHubBackedProfileDirectory.INTEGRATION;
    // Reference to corpus zip temp file
    private static File VERA_CORPUS_ZIP_FILE;

    /**
     * Download the corpus zip archive to /tmp and keep a reference to it
     * 
     * @throws IOException
     *             when the corpus file cannot be downloaded
     */
    @BeforeClass
    public static void initialise() throws IOException {
        URL corpusURL = new URL(
                "https://github.com/veraPDF/veraPDF-corpus/archive/staging.zip");
        VERA_CORPUS_ZIP_FILE = File.createTempFile("veraCorpus", "zip");
        try (OutputStream output = new FileOutputStream(VERA_CORPUS_ZIP_FILE);
                InputStream corpusInput = corpusURL.openStream();) {
            byte[] buffer = new byte[8 * 1024];
            try {
                int bytesRead;
                while ((bytesRead = corpusInput.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } finally {
                output.close();
            }
        }
    }

    /**
     * Build PDFA-1B filter and test 1B validation
     * 
     * @throws IOException
     *             when the test loop fails, failing the test
     */
    @Test
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
    @Test
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
    @Test
    public void performPdf2BValidation() throws IOException {
        PDFAFlavour[] filters = new PDFAFlavour[] { PDFAFlavour.PDFA_2_B };
        // validateFlavour(PDFAFlavour.PDFA_2_B, Arrays.asList(filters));
    }

    /**
     * Build PDFA-3B filter and test 3B validation
     * 
     * @throws IOException
     *             when the test loop fails, failing the test
     */
    @Test
    public void performPdf3BValidation() throws IOException {
        PDFAFlavour[] filters = new PDFAFlavour[] { PDFAFlavour.PDFA_3_B };
        // validateFlavour(PDFAFlavour.PDFA_3_B, Arrays.asList(filters));
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
    private static void validateFlavour(final PDFAFlavour flavour,
            final List<PDFAFlavour> filters) throws ZipException, IOException {

        // Try with resource with the downlodaded corpus zip file
        try (ZipFile zipIn = new ZipFile(VERA_CORPUS_ZIP_FILE);) {

            // Get the appropriate validation profile for the flavour
            ValidationProfile profile = PROFILES
                    .getValidationProfileByFlavour(flavour);

            // Stateful validator means a single instance will perform all
            // checks. This is to test that a validator re-initialises itself
            // after validating.
            PDFAValidator statefulValidator = Validators.validate(profile,
                    false);

            // Grab an enumeration of the zip entries and loop through
            Enumeration<? extends ZipEntry> entries = zipIn.entries();
            while (entries.hasMoreElements()) {
                // Grab the next zip entry and split the path parts
                ZipEntry entry = entries.nextElement();
                String[] pathParts = entry.getName().split(ZIP_SEPARATOR);

                // Ignore directory entries, non PDFs and then filter by PDFA
                // Flavour.
                if (entry.isDirectory()
                        || !entry.getName().endsWith(PDF_SUFFIX)
                        || !matchesFlavourFilter(entry.getName(), filters)) {
                    continue;
                }

                // Parse a CorpusItemId from the file name, this gives us an
                // expected pass or fail result.
                CorpusItemId corpusId = CorpusItemIdImpl.fromFileName(
                        flavour.getPart(), pathParts[pathParts.length - 1]);
                System.out.println("Testing: " + entry.getName());

                // Try with resources for model parser from zip stream
                // TODO: This inner try needs at least refactoring to another a
                // sub-function
                try (ModelParser loader = new ModelParser(
                        zipIn.getInputStream(entry))) {
                    // A "stateless" validator instance for each entry, guaranteed to
                    // be initialised.
                    PDFAValidator statelessValidator = Validators.validate(
                            profile, false);
                    // Get the stateless validator result
                    ValidationResult statelessResult = statelessValidator
                            .validate(loader);
                    // Get the stateful validator result
                    ValidationResult statefulResult = statefulValidator
                            .validate(loader);
                    // The results should be equal (this means that the validator re-initialises correctly)
                    assertTrue(statefulResult.equals(statelessResult));
                    // We don't have instance control so this should always be false, useful check against assumptions
                    assertFalse(statefulResult == statelessResult);
                    
                    // Output result and repeat
                    ValidationResults.toXml(statelessResult, System.out,
                            Boolean.TRUE);
                    if (statelessResult.isCompliant() != corpusId
                            .getExpectedResult())
                        System.err.println("Unexpected result:"
                                + statelessResult.isCompliant() + ", for:"
                                + entry.getName());
                    System.out.println();
                } catch (IOException e) {
                    System.err.println("Error parsing PDF Model for "
                            + entry.getName());
                    e.printStackTrace();
                } catch (ValidationException | NegativeArraySizeException e) {
                    System.err.println("Error carrying out validation for "
                            + entry.getName());
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                } catch (JAXBException e) {
                    System.err
                            .println("Error marshalling validation result for "
                                    + entry.getName());
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
