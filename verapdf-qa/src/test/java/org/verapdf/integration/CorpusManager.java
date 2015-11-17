/**
 * 
 */
package org.verapdf.integration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.verapdf.pdfa.qa.TestCorpus;
import org.verapdf.pdfa.qa.ZipBackedTestCorpus;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public final class CorpusManager {
    // Reference to corpus zip temp file
    private static File VERA_CORPUS_ZIP_FILE = null;
    private static File ISARTOR_CORPUS_ZIP_FILE = null;
    private static TestCorpus VERA_CORPUS = null;
    private static TestCorpus ISARTOR_CORPUS = null;

    /**
     * @return a TestCorpus set up from the downloaded verPDF test corpus zip file
     * @throws IOException if an error occurs downloading or parsing the corpus zip file
     */
    public static TestCorpus getVeraCorpus() throws IOException {
        if (VERA_CORPUS_ZIP_FILE == null) {
            URL corpusURL = new URL(
                    "https://github.com/veraPDF/veraPDF-corpus/archive/staging.zip");
            VERA_CORPUS_ZIP_FILE = createTempFileFromUrl(corpusURL, "veraCorpus");
            VERA_CORPUS = ZipBackedTestCorpus.fromZipSource("veraPDF Test Corpus", "Synthetic test files for PDF/A validation.", VERA_CORPUS_ZIP_FILE);
        }
        return VERA_CORPUS;
    }

    /**
     * @return a TestCorpus set up from the downloaded Isartor test corpus zip file
     * @throws IOException if an error occurs downloading or parsing the corpus zip file
     */
    public static TestCorpus getIsartorCorpus() throws IOException {
        if (ISARTOR_CORPUS_ZIP_FILE == null) {
            URL corpusURL = new URL(
                    "http://www.pdfa.org/wp-content/uploads/2011/08/isartor-pdfa-2008-08-13.zip");
            ISARTOR_CORPUS_ZIP_FILE = createTempFileFromUrl(corpusURL, "isartorCorpus");
            ISARTOR_CORPUS = ZipBackedTestCorpus.fromZipSource("Isartor", "Synthetic test files for PDF/A validation.", ISARTOR_CORPUS_ZIP_FILE);
        }
        return ISARTOR_CORPUS;
    }

    private static File createTempFileFromUrl(final URL sourceUrl,
            final String tempPrefix) throws IOException {
        File tempFile = File.createTempFile(tempPrefix, "zip");
        try (OutputStream output = new FileOutputStream(tempFile);
                InputStream corpusInput = sourceUrl.openStream();) {
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = corpusInput.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }
}
