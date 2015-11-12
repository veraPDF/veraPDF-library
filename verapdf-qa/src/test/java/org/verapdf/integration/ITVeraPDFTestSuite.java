package org.verapdf.integration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
public class ITVeraPDFTestSuite {
    private final static String ZIP_SEPARATOR = "/";
    private final static String PDF_SUFFIX = ".pdf";
    private static final ProfileDirectory PROFILES = GitHubBackedProfileDirectory.INTEGRATION;
    private static File VERA_CORPUS_ZIP_FILE;

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

    @Test
    public void performPdf1BValidation() throws IOException {
        PDFAFlavour[] filters = new PDFAFlavour[] { PDFAFlavour.PDFA_1_B };
        validateFlavour(PDFAFlavour.PDFA_1_B, Arrays.asList(filters));
    }

    @Test
    public void performPdf1AValidation() throws IOException {
        PDFAFlavour[] filters = new PDFAFlavour[] { PDFAFlavour.PDFA_1_B,
                PDFAFlavour.PDFA_1_A };
        validateFlavour(PDFAFlavour.PDFA_1_A, Arrays.asList(filters));
    }

    @Test
    public void performPdf2BValidation() throws IOException {
        PDFAFlavour[] filters = new PDFAFlavour[] { PDFAFlavour.PDFA_2_B };
        validateFlavour(PDFAFlavour.PDFA_2_B, Arrays.asList(filters));
    }

    @Test
    public void performPdf3BValidation() throws IOException {
        PDFAFlavour[] filters = new PDFAFlavour[] { PDFAFlavour.PDFA_3_B };
        validateFlavour(PDFAFlavour.PDFA_3_B, Arrays.asList(filters));
    }

    private static void validateFlavour(final PDFAFlavour flavour,
            final List<PDFAFlavour> filters) throws ZipException, IOException {

        try (ZipFile zipIn = new ZipFile(VERA_CORPUS_ZIP_FILE);) {
            ValidationProfile profile = PROFILES
                    .getValidationProfileByFlavour(flavour);
            Enumeration<? extends ZipEntry> entries = zipIn.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String[] pathParts = entry.getName().split(ZIP_SEPARATOR);
                if (entry.isDirectory()
                        || !entry.getName().endsWith(PDF_SUFFIX)
                        || !matchesFlavourFilter(entry.getName(), filters)) {
                    continue;
                }
                CorpusItemId corpusId = CorpusItemIdImpl.fromFileName(
                        flavour.getPart(), pathParts[pathParts.length - 1]);
                System.out.println("Testing: " + entry.getName());
                try (ModelParser loader = new ModelParser(
                        zipIn.getInputStream(entry))) {
                    PDFAValidator validator = Validators.validate(profile,
                            false);
                    ValidationResult result = validator.validate(loader);
                    ValidationResults.toXml(result, System.out, Boolean.TRUE);
                    if (result.isCompliant() != corpusId.getExpectedResult())
                        System.err.println("Unexpected result:"
                                + result.isCompliant() + ", for:"
                                + entry.getName());
                    // assertTrue("Unexpected result:" + result.isCompliant() +
                    // ", for:" + entry.getName(), corpusId.getExpectedResult()
                    // == result.isCompliant());
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

    private static boolean matchesFlavourFilter(final String parseForMatches,
            final List<PDFAFlavour> filters) {
        for (PDFAFlavour flavour : filters) {
            if (PDFAFlavour.fromString(parseForMatches) == flavour) {
                return true;
            }
        }
        return false;
    }
}
