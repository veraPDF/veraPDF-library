/**
 * 
 */
package org.verapdf.pdfa;

import java.io.InputStream;

import org.verapdf.core.EncryptedPdfException;
import org.verapdf.core.ModelParsingException;
import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * The veraPDFFoundry interface provides methods for creating implementations of
 * the classes provided by a PDF Parser and Metadata Fixer implementations.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 21 Sep 2016:12:37:55
 */
public interface VeraPDFFoundry {
	/**
	 * Method that returns a PDFParser instance, parsing the passed
	 * {@link pdfStream} parameter. The parser or parser provider will detect
	 * the flavour of the PDF document stream and provide an appropriate parser.
	 * 
	 * @param pdfStream
	 *            {@link java.io.InputStream} for the PDF document to be parsed.
	 * @return a {@link PDFParser} instance created from the supplied
	 *         InputStream.
	 * @throws ModelParsingException
	 *             when there's a problem parsing the PDF file
	 * @throws EncryptedPdfException
	 *             if the PDF to be parsed is encrypted
	 */
	public PDFParser newPdfParser(InputStream pdfStream) throws ModelParsingException, EncryptedPdfException;

	/**
	 * Method that returns a PDFParser instance, parsing the passed
	 * {@link pdfStream} parameter. The caller must explicitly state
	 * the flavour of the PDF document stream.
	 *
	 * @param pdfStream
	 *            {@link java.io.InputStream} for the PDF document to be parsed.
	 * @param flavour
	 *            a {@link PDFAFlavour} instance indicating parser configuration
	 *            (PDF/A part and conformance level) to be assumed when parsing
	 *            the document.
	 * @return a {@link PDFParser} instance created from the supplied
	 *         InputStream.
	 * @throws ModelParsingException
	 *             when there's a problem parsing the PDF file
	 * @throws EncryptedPdfException
	 *             if the PDF to be parsed is encrypted
	 */
	public PDFParser newPdfParser(InputStream pdfStream, PDFAFlavour flavour)
			throws ModelParsingException, EncryptedPdfException;

	public MetadataFixer newMetadataFixer();
}
