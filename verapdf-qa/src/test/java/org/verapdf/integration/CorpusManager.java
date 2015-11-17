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

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public final class CorpusManager {
    // Reference to corpus zip temp file
    private static File VERA_CORPUS_ZIP_FILE = null;
    private static File ISARTOR_CORPUS_ZIP_FILE = null;

    /**
     * @return
     * @throws IOException
     */
    public static File getVeraCorpusZipFile() throws IOException {
        if (VERA_CORPUS_ZIP_FILE == null) {
            URL corpusURL = new URL(
                    "https://github.com/veraPDF/veraPDF-corpus/archive/staging.zip");
            VERA_CORPUS_ZIP_FILE = createTempFileFromUrl(corpusURL, "veraCorpus");
        }
        return VERA_CORPUS_ZIP_FILE;
    }

    /**
     * @return
     * @throws IOException
     */
    public static File getIsartorCorpusZipFile() throws IOException {
        if (ISARTOR_CORPUS_ZIP_FILE == null) {
            URL corpusURL = new URL(
                    "http://www.pdfa.org/wp-content/uploads/2011/08/isartor-pdfa-2008-08-13.zip");
            ISARTOR_CORPUS_ZIP_FILE = createTempFileFromUrl(corpusURL, "isartorCorpus");
        }
        return ISARTOR_CORPUS_ZIP_FILE;
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
