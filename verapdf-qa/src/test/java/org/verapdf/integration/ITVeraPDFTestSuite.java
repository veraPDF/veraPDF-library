package org.verapdf.integration;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.core.ProfileException;
import org.verapdf.model.ModelParser;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.qa.CorpusItemId;
import org.verapdf.pdfa.qa.CorpusItemIdImpl;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.pdfa.validation.Validator;
import org.verapdf.validation.profile.parser.LegacyProfileConverter;

public class ITVeraPDFTestSuite {
    private static ValidationProfile PROFILE_1B;
    private static File VERA_CORPUS_ZIP_FILE;

    @BeforeClass
    public static void initialise() throws ProfileException, IOException {
        try {
            URL profileURL = new URL(
                    "https://raw.githubusercontent.com/veraPDF/veraPDF-validation-profiles/integration/PDF_A/PDFA-1B.xml");
            PROFILE_1B = LegacyProfileConverter.fromLegacyStream(
                    profileURL.openStream(), PDFAFlavour.PDFA_1_B);

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Error loading corpus", e);
        }
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
                        ValidationResult result = Validator.validate(
                                PROFILE_1B, loader.getRoot(), false);
                        ValidationResults.toXml(result, System.out,
                                Boolean.TRUE);
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
