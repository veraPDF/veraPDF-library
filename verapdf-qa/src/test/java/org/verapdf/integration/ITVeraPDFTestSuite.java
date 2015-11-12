package org.verapdf.integration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.core.ProfileException;
import org.verapdf.model.ModelParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.qa.CorpusItemId;
import org.verapdf.pdfa.qa.CorpusItemIdImpl;
import org.verapdf.pdfa.qa.GitHubBackedProfileDirectory;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.ProfileDirectory;
import org.verapdf.pdfa.validators.Validators;

@SuppressWarnings({"javadoc", "static-method"})
public class ITVeraPDFTestSuite {
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
        try (ZipFile zipIn = new ZipFile(VERA_CORPUS_ZIP_FILE);) {
            Enumeration<? extends ZipEntry> entries = zipIn.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String[] pathParts = entry.getName().split("/");
                if (entry.isDirectory() || PDFAFlavour.fromString(entry.getName()) != PDFAFlavour.PDFA_1_B)
                    continue;
                if (entry.getName().endsWith(".pdf")) {
                    CorpusItemId corpusId = CorpusItemIdImpl.fromFileName(PDFAFlavour.PDFA_1_B.getPart(), pathParts[pathParts.length - 1]);
                    System.out.println();
                    System.out.println("Testing: " + entry.getName());
                    try (ModelParser loader = new ModelParser(
                            zipIn.getInputStream(entry))) {
                        PDFAValidator validator = Validators.validate(PROFILES.getValidationProfileByFlavour(PDFAFlavour.PDFA_1_B),
                                false);
                        ValidationResult result = validator.validate(loader);
                        ValidationResults.toXml(result, System.out, Boolean.TRUE);
                        if (result.isCompliant() != corpusId.getExpectedResult())
                            System.err.println("Unexpected result:" + result.isCompliant() + ", for:" + entry.getName());
//                        assertTrue("Unexpected result:" + result.isCompliant() + ", for:" + entry.getName(), corpusId.getExpectedResult() == result.isCompliant());
                        System.out.println();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
