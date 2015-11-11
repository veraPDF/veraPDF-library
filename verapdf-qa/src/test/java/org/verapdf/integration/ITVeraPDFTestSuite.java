package org.verapdf.integration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.pdfa.validation.Validator;
import org.verapdf.validation.profile.parser.LegacyProfileConverter;

public class ITVeraPDFTestSuite {
    private static ValidationProfile PROFILE_1B;

    @BeforeClass
    public static void initialise() throws ProfileException {
        try {
            URL profileURL = new URL(
                    "https://raw.githubusercontent.com/veraPDF/veraPDF-validation-profiles/integration/PDF_A/PDFA-1B.xml");
            PROFILE_1B = LegacyProfileConverter.fromLegacyStream(
                    profileURL.openStream(), PDFAFlavour.PDFA_1_B);
            
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Error loading corpus", e);
        }
    }

    @Test
    public void performValidation() throws URISyntaxException, IOException {
        URL corpusURL = new URL(
                "https://github.com/veraPDF/veraPDF-corpus/archive/staging.zip");
        File tempZip = File.createTempFile("veraCorpus", "zip");
        try (OutputStream output = new FileOutputStream(tempZip);
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
        ZipFile zipIn = new ZipFile(tempZip);
        Enumeration<? extends ZipEntry> entries = zipIn.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory())
                continue;
            if (entry.getName().endsWith(".pdf")) {
                System.out.println();
                System.out.println("Testing: " + entry.getName());
                try (ModelParser loader = new ModelParser(
                        zipIn.getInputStream(entry))) {
                    ValidationResult result = Validator.validate(PROFILE_1B,
                            loader.getRoot(), false);
                    ValidationResults.toXml(result, System.out, Boolean.TRUE);
                    System.out.println();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
