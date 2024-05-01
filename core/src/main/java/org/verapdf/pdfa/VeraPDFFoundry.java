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

import org.verapdf.component.Component;
import org.verapdf.core.EncryptedPdfException;
import org.verapdf.core.ModelParsingException;
import org.verapdf.extensions.ExtensionObjectType;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;
import org.verapdf.pdfa.validation.validators.ValidatorConfig;

import java.io.File;
import java.io.InputStream;
import java.util.EnumSet;

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
	 * @return a {@link PDFAParser} instance created from the supplied
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
	 * @return a {@link PDFAParser} instance created from the supplied
	 *         InputStream.
	 * @throws ModelParsingException
	 *             when there's a problem parsing the PDF file
	 * @throws EncryptedPdfException
	 *             if the PDF to be parsed is encrypted
	 */
	public PDFAParser createParser(InputStream pdfStream, PDFAFlavour flavour)
			throws ModelParsingException, EncryptedPdfException;

	public PDFAParser createParser(InputStream pdfStream, PDFAFlavour flavour, PDFAFlavour defaultFlavour)
			throws ModelParsingException, EncryptedPdfException;

	public PDFAParser createParser(InputStream pdfStream, PDFAFlavour flavour, String password)
			throws ModelParsingException, EncryptedPdfException;

	public PDFAParser createParser(InputStream pdfStream, PDFAFlavour flavour, PDFAFlavour defaultFlavour, String password, 
								   EnumSet<ExtensionObjectType> enabledExtensions)
			throws ModelParsingException, EncryptedPdfException;

	/**
	 * Method that returns a PDFParser instance, parsing file passed as
	 * {@link pdfFile} parameter. The caller must explicitly state the flavour
	 * of the PDF document stream.
	 *
	 * @param pdfFile {@link File} with PDF document to be parsed.
	 * @param flavour a {@link PDFAFlavour} instance indicating parser configuration
	 *                (PDF/A part and conformance level) to be assumed when parsing
	 *                the document.
	 * @return a {@link PDFAParser} instance created from the supplied
	 * InputStream.
	 * @throws ModelParsingException when there's a problem parsing the PDF file
	 * @throws EncryptedPdfException if the PDF to be parsed is encrypted
	 */
	public PDFAParser createParser(File pdfFile, PDFAFlavour flavour)
			throws ModelParsingException, EncryptedPdfException;

	public PDFAParser createParser(File pdfFile, PDFAFlavour flavour, PDFAFlavour defaultFlavour)
			throws ModelParsingException, EncryptedPdfException;

	public PDFAParser createParser(File pdfFile, PDFAFlavour flavour, String password)
			throws ModelParsingException, EncryptedPdfException;

	public PDFAParser createParser(File pdfFile, PDFAFlavour flavour, PDFAFlavour defaultFlavour, String password, 
								   EnumSet<ExtensionObjectType> enabledExtensions)
			throws ModelParsingException, EncryptedPdfException;

	/**
	 * Method that returns a PDFParser instance, parsing file passed as
	 * {@link pdfFile} parameter. The parser or parser provider will detect
	 * the flavour of the PDF document stream and provide an appropriate parser.
	 *
	 * @param pdfFile
	 *            {@link java.io.File} with the PDF document to be parsed.
	 * @return a {@link PDFAParser} instance created from the supplied
	 *         InputStream.
	 * @throws ModelParsingException
	 *             when there's a problem parsing the PDF file
	 * @throws EncryptedPdfException
	 *             if the PDF to be parsed is encrypted
	 */
	public PDFAParser createParser(File pdfFile)
		throws ModelParsingException, EncryptedPdfException;

		/**
		 * Obtain a new {@link PDFAValidator} instance.
		 *
		 * @param config
		 *            a {@link ValidatorConfig} instance used to configure the
		 *            {@link PDFAValidator}
		 * @return an appropriately configured {@link PDFAValidator} instance.
		 */
	public PDFAValidator createValidator(ValidatorConfig config);

	/**
	 * Obtain a new {@link PDFAValidator} instance that uses a custom
	 * {@link org.verapdf.pdfa.validation.profiles.ValidationProfile} instance.
	 *
	 * @param config
	 *            a {@link ValidatorConfig} instance used to configure the
	 *            {@link PDFAValidator}
	 * @param profile
	 * @return an appropriately configured {@link PDFAValidator} instance.
	 */
	public PDFAValidator createValidator(ValidatorConfig config, ValidationProfile profile);

	/**
	 * Obtain a new {@link PDFAValidator} instance that uses a custom
	 * {@link org.verapdf.pdfa.flavours.PDFAFlavour}.
	 *
	 * @param config
	 *            a {@link ValidatorConfig} instance used to configure the
	 *            {@link PDFAValidator}
	 * @param flavour
	 *            the particular {@link org.verapdf.pdfa.flavours.PDFAFlavour}
	 *            to validated against.
	 * @return an appropriately configured {@link PDFAValidator} instance.
	 */
	public PDFAValidator createValidator(ValidatorConfig config, PDFAFlavour flavour);

	/**
	 * Creates a new {@link PDFAValidator} instance that uses one of the
	 * {@link ValidationProfile}s packaged as a core library resource. While
	 * these profiles are not guaranteed to be up to date, they are available
	 * when offline. A {@link ProfileDirectory} populated with the pre-loaded
	 * profiles can be obtained by calling
	 * {@link Profiles#getVeraProfileDirectory()}.
	 *
	 * @param flavour
	 *            the {@link PDFAFlavour} that's associated with the
	 *            {@code ValidationProfile} to used to initialise the
	 *            {@code PDFAValidator}.
	 * @param logSuccess
	 *            {@code boolean} used to configure logging of passed tests by
	 *            the {@code PDFAValidator}. Pass {@code true} to log passed
	 *            tests, {@code false} to only log tests that don't pass.
	 * @return a {@link PDFAValidator} instance initialised from the passed
	 *         parameters
	 */
	public PDFAValidator createValidator(PDFAFlavour flavour, boolean logSuccess);

	/**
	 * Creates a new {@link PDFAValidator} initialised with the passed profile
	 * and chosen passed test logging.
	 *
	 * @param profile
	 *            the {@link ValidationProfile} to be enforced by the returned
	 *            {@code PDFAValidator}.
	 * @param logSuccess
	 *            {@code boolean} used to configure logging of passed tests by
	 *            the {@code PDFAValidator}. Pass {@code true} to log passed
	 *            tests, {@code false} to only log tests that don't pass.
	 * @return a {@link PDFAValidator} instance initialised from the passed
	 *         parameters
	 */
	public PDFAValidator createValidator(ValidationProfile profile, boolean logSuccess);

	public PDFAValidator createValidator(PDFAFlavour flavour, int maxNumberOfDisplayedFailedChecks,
										 boolean logSuccess, boolean showErrorMessages, boolean showProgress);

	public PDFAValidator createValidator(ValidationProfile profile, int maxNumberOfDisplayedFailedChecks,
										 boolean logSuccess, boolean showErrorMessages, boolean showProgress);

	/**
	 * Creates a new {@link PDFAValidator} initialised with the passed profile,
	 * requested fast failing behaviour and configured NOT to log passed checks.
	 *
	 * @param flavour
	 *            the {@link PDFAFlavour} that's associated with the
	 *            {@code ValidationProfile} to used to initialise the
	 *            {@code PDFAValidator}.
	 * @param maxFailures
	 *            an {@code int} value that configures the {@code PDFAValidator}
	 *            to abort validation after {@code maxFailures} failed tests. If
	 *            {@code maxFailures} is less than 1 then the
	 *            {@code PDFAValidator} will complete the full validation
	 *            process.
	 * @return a {@link PDFAValidator} instance initialised from the passed
	 *         parameters
	 */
	public PDFAValidator createFailFastValidator(PDFAFlavour flavour, int maxFailures, int maxNumberOfDisplayedFailedChecks,
												 boolean logSuccess, boolean showErrorMessages, boolean showProgress);

	/**
	 * Creates a new {@link PDFAValidator} initialised with the passed profile,
	 * requested fast failing behaviour and configured NOT to log passed checks.
	 *
	 * @param profile
	 *            the {@link ValidationProfile} to be enforced by the returned
	 *            {@code PDFAValidator}.
	 * @param maxFailures
	 *            an {@code int} value that configures the {@code PDFAValidator}
	 *            to abort validation after {@code maxFailures} failed tests. If
	 *            {@code maxFailures} is less than 1 then the
	 *            {@code PDFAValidator} will complete the full validation
	 *            process.
	 * @return a {@link PDFAValidator} instance initialised from the passed
	 *         parameters
	 */
	public PDFAValidator createFailFastValidator(ValidationProfile profile, int maxFailures, int maxNumberOfDisplayedFailedChecks,
												 boolean logSuccess, boolean showErrorMessages, boolean showProgress);

	/**
	 * Obtain a new {@link MetadataFixer} instance.
	 *
	 * @return a {@link MetadataFixer} instance.
	 */
	public MetadataFixer createMetadataFixer();

	/**
	 * @return the default {@link PDFAFlavour} set for this {@link VeraPDFFoundry}
	 */
	public PDFAFlavour defaultFlavour();

	/**
	 * @return parser id
	 */
	public String getParserId();
}
