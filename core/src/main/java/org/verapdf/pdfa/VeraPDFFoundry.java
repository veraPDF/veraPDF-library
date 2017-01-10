/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
/**
 * 
 */
package org.verapdf.pdfa;

import java.io.InputStream;

import org.verapdf.component.Component;
import org.verapdf.core.EncryptedPdfException;
import org.verapdf.core.ModelParsingException;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;
import org.verapdf.pdfa.validation.validators.ValidatorConfig;

/**
 * The veraPDFFoundry interface provides methods for creating implementations of
 * the classes provided by a PDF Parser and Metadata Fixer implementations.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 21 Sep 2016:12:37:55
 */
public interface VeraPDFFoundry extends Component {
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
	public PDFAParser createParser(InputStream pdfStream) throws ModelParsingException, EncryptedPdfException;

	/**
	 * Method that returns a PDFParser instance, parsing the passed
	 * {@link pdfStream} parameter. The caller must explicitly state the flavour
	 * of the PDF document stream.
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
	public PDFAParser createParser(InputStream pdfStream, PDFAFlavour flavour)
			throws ModelParsingException, EncryptedPdfException;

	public PDFAValidator createValidator(ValidatorConfig config);

	public PDFAValidator createValidator(ValidatorConfig config, ValidationProfile profile);

	public PDFAValidator createValidator(ValidatorConfig config, PDFAFlavour flavour);

	public PDFAValidator createValidator(PDFAFlavour flavour, boolean logSuccess);

	public PDFAValidator createValidator(ValidationProfile profile, boolean logSuccess);

	public PDFAValidator createFailFastValidator(PDFAFlavour flavour, int maxFailures);

	public PDFAValidator createFailFastValidator(ValidationProfile profile, int maxFailures);

	public MetadataFixer createMetadataFixer();

	public PDFAFlavour defaultFlavour();
}
