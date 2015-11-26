package org.verapdf.integration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.qa.GitHubBackedProfileDirectory;
import org.verapdf.pdfa.qa.ResultSet;
import org.verapdf.pdfa.qa.ResultSet.Incomplete;
import org.verapdf.pdfa.qa.ResultSet.Result;
import org.verapdf.pdfa.qa.ResultSetImpl;
import org.verapdf.pdfa.qa.TestCorpus;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.ProfileDirectory;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.pdfa.validators.Validators;

@SuppressWarnings({ "javadoc", "static-method" })
public class ITVeraCorpusTests {
    // Directory of validation profiles poulated by download from GitHub
    private static final ProfileDirectory PROFILES = GitHubBackedProfileDirectory.INTEGRATION;
    private static final Map<PDFAFlavour, ResultSet> VERA_RESULTS = new HashMap<>();
    private static final Map<PDFAFlavour, ResultSet> ISARTOR_RESULTS = new HashMap<>();

    // @AfterClass
    public static void outputResults() throws JAXBException {
        outputCorpusResults(VERA_RESULTS.get(PDFAFlavour.PDFA_1_B));
        outputCorpusResults(ISARTOR_RESULTS.get(PDFAFlavour.PDFA_1_B));
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
    public void testVeraPdfCorpus() throws ZipException, IOException {
        TestCorpus veraPDFcorpus = CorpusManager.getVeraCorpus();
        for (ValidationProfile profile : PROFILES.getValidationProfiles()) {
            PDFAValidator validator = Validators.createValidator(profile, false);
            VERA_RESULTS.put(profile.getPDFAFlavour(),
                    ResultSetImpl.validateCorpus(veraPDFcorpus, validator));
        }
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
    public void testIsatorCorpus() throws ZipException, IOException {
        TestCorpus veraPDFcorpus = CorpusManager.getIsartorCorpus();
        for (ValidationProfile profile : PROFILES.getValidationProfiles()) {
            PDFAValidator validator = Validators.createValidator(profile, false);
            ISARTOR_RESULTS.put(profile.getPDFAFlavour(),
                    ResultSetImpl.validateCorpus(veraPDFcorpus, validator));
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
    @SuppressWarnings("unused")
    private static boolean matchesFlavourFilter(final String parseForMatches,
            final List<PDFAFlavour> filters) {
        PDFAFlavour flavour = PDFAFlavour.fromString(parseForMatches);
        return filters.contains(flavour);
    }

    private static void outputCorpusResults(final ResultSet results) throws JAXBException {
        System.out.println();
        System.out.println("Flavour" + results.getValidationProfile().getPDFAFlavour());
        System.out.println(results.getDetails());
        System.out.println(results.getCorpusDetails());
        System.out.println("Results");
        for (Result result : results.getResults()) {
            System.out.println(result.getCorpusItem().getPath());
            ValidationResults.toXml(result.getResult(), System.out,
                    Boolean.TRUE);
        }
        System.out.println("Exceptions");
        for (Incomplete exception : results.getExceptions()) {
            System.out.println(exception.getCorpusItem().getPath());
            System.out.println(exception);
        }
    }
}
