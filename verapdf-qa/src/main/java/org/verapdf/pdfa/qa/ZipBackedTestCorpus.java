/**
 * 
 */
package org.verapdf.pdfa.qa;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public class ZipBackedTestCorpus extends AbstractTestCorpus<ZipEntry> {
    private final static String PDF_SUFFIX = ".pdf";
    private final ZipFile zipSource;

    private ZipBackedTestCorpus(final String name, final String description,
            final File zipSource) throws ZipException, IOException {
        super(name, description, itemsMapFromZipSource(zipSource));
        this.zipSource = new ZipFile(zipSource);
    }

    /**
     * { @inheritDoc }
     * 
     * @throws IOException
     */
    @Override
    protected InputStream getStreamFromReference(ZipEntry reference)
            throws IOException {
        return this.zipSource.getInputStream(reference);
    }

    /**
     * @param name
     *            a String name for the TestCorpus instance
     * @param description
     *            a textual description of the TestCorpus instance
     * @param zipFile
     *            a {@link File} instance that's a zip file for the corpus
     * @return a TestCorpus instance initialised from the passed params and zip
     *         file
     * @throws IOException
     *             if there's an exception parsing the zip file
     * @throws ZipException
     *             if there's an exception parsing the zip file
     */
    public static TestCorpus fromZipSource(final String name,
            final String description, final File zipFile) throws ZipException,
            IOException {
        return new ZipBackedTestCorpus(name, description, zipFile);
    }

    private static final Map<String, ZipEntry> itemsMapFromZipSource(
            final File zipFile) throws ZipException, IOException {
        Map<String, ZipEntry> itemMap = new HashMap<>();
        try (ZipFile zipSource = new ZipFile(zipFile);) {
            Enumeration<? extends ZipEntry> entries = zipSource.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()
                        || !entry.getName().endsWith(PDF_SUFFIX))
                    continue;
                itemMap.put(entry.getName(), entry);
            }
        }
        return itemMap;
    }
}
