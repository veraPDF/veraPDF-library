/**
 * 
 */
package org.verapdf.pdfa;

import java.io.InputStream;

import org.verapdf.core.EncryptedPdfException;
import org.verapdf.core.ModelParsingException;
import org.verapdf.metadata.fixer.entity.PDFDocument;
import org.verapdf.metadata.fixer.utils.FixerConfig;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;

/**
 * The veraPDFFoundry interface provides methods for creating implementations of
 * the core API classes. Note that the library doesn't include implementations
 * for many of these interfaces, including this one.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 21 Sep 2016:12:37:55
 */
public interface VeraPDFFoundry {
	/**
	 * @param pdfStream
	 * @return
	 * @throws ModelParsingException
	 * @throws EncryptedPdfException
	 */
	public PDFParser newPdfParser(InputStream pdfStream) throws ModelParsingException, EncryptedPdfException;

	/**
	 * @param pdfStream
	 * @param flavour
	 * @return
	 * @throws ModelParsingException
	 * @throws EncryptedPdfException
	 */
	public PDFParser newPdfParser(InputStream pdfStream, PDFAFlavour flavour)
			throws ModelParsingException, EncryptedPdfException;

	public MetadataFixer newMetadataFixer(FixerConfig config);
	
	public FixerConfig newFixerConfig(PDFDocument pdfDocument, ValidationResult validationResult);
	
	public PDFDocument newPdfDocument(InputStream pdfStream, PDFAFlavour flavour)
			throws ModelParsingException, EncryptedPdfException;
}
