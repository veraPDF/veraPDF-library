package org.verapdf.pdfa;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;

/**
 * Simple interface for PDF/A metadata repair.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public interface MetadataFixer {

    /**
     * @param toFix
     *            an InputStream from which the PDF/A data to repair can be
     *            read.
     * @param outputRepaired
     *            an OutputStream to which the Fixer instance should write the
     *            repaired PDF/A data.
     * @param result
     *            a {@link ValidationResult} instance for the PDF/A to be
     *            repaired, the toFix InputStream.
     * @return a {@link MetadataFixerResult} that holds the repair status and
     *         records any fixes applied.
     */
    public MetadataFixerResult fixMetadata(InputStream toFix,
            OutputStream outputRepaired, ValidationResult result) throws IOException;

    public MetadataFixerResult fixMetadata(PDFParser parser,
            OutputStream outputRepaired, ValidationResult result);
}
