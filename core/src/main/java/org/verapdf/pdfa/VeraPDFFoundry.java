/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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

import java.io.File;
import java.io.InputStream;
import java.util.List;
import org.verapdf.component.Component;
import org.verapdf.core.EncryptedPdfException;
import org.verapdf.core.ModelParsingException;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;
import org.verapdf.pdfa.validation.validators.ValidatorConfig;

/**
 * The veraPDFFoundry interface provides methods for creating implementations of
 * the classes provided by a PDF/A Parser and Metadata Fixer implementations.
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 * <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 21 Sep 2016:12:37:55
 */
public interface VeraPDFFoundry extends Component {
	/**
	 * Method that returns a PDFAParser instance, parsing the passed
	 * {@param pdfStream} parameter. The parser or parser provider will detect
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
	 * Method that returns a PDFAParser instance, parsing the passed
	 * {@param pdfStream} parameter. The caller must explicitly state the flavour
	 * of the PDF document stream.
	 *
	 * @param pdfStream
	 *            {@link java.io.InputStream} for the PDF document to be parsed.
	 * @param flavour
	 *            a {@link PDFAFlavour} instance indicating parser configuration
	 *            to be assumed when parsing the document.
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

	public PDFAParser createParser(InputStream pdfStream, PDFAFlavour flavour, PDFAFlavour defaultFlavour,
			String password)
			throws ModelParsingException, EncryptedPdfException;

	/**
	 * Method that returns a PDFAParser instance, parsing file passed as
	 * {@param pdfFile} parameter. The caller must explicitly state the flavour
	 * of the PDF document stream.
	 *
	 * @param pdfFile {@link File} with PDF document to be parsed.
	 * @param flavour a {@link PDFAFlavour} instance indicating parser configuration
	 *                to be assumed when parsing the document.
	 *
	 * @return a {@link PDFAParser} instance created from the supplied
	 * InputStream.
	 *
	 * @throws ModelParsingException when there's a problem parsing the PDF file
	 * @throws EncryptedPdfException if the PDF to be parsed is encrypted
	 */
	public PDFAParser createParser(File pdfFile, PDFAFlavour flavour)
			throws ModelParsingException, EncryptedPdfException;

	public PDFAParser createParser(File pdfFile, PDFAFlavour flavour, PDFAFlavour defaultFlavour)
			throws ModelParsingException, EncryptedPdfException;

	public PDFAParser createParser(File pdfFile, PDFAFlavour flavour, String password)
			throws ModelParsingException, EncryptedPdfException;

	public PDFAParser createParser(File pdfFile, PDFAFlavour flavour, PDFAFlavour defaultFlavour, String password)
			throws ModelParsingException, EncryptedPdfException;

	/**
	 * Method that returns a PDFAParser instance, parsing file passed as
	 * {@param pdfFile} parameter. The parser or parser provider will detect
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
	 * @param profile a validation profile
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
	 * Obtain a new {@link PDFAValidator} instance that uses the list of a custom
	 * {@link org.verapdf.pdfa.flavours.PDFAFlavour}s.
	 *
	 * @param config
	 *            a {@link ValidatorConfig} instance used to configure the
	 *            {@link PDFAValidator}
	 * @param flavours
	 *            the list of particular {@link org.verapdf.pdfa.flavours.PDFAFlavour}s
	 *            to validated against.
	 * @return an appropriately configured {@link PDFAValidator} instance.
	 */
	public PDFAValidator createValidator(ValidatorConfig config, List<PDFAFlavour> flavours);

	/**
	 * Creates a new {@link PDFAValidator} instance that uses one of the
	 * {@link ValidationProfile}s packaged as a core library resource.
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
	 * Creates a new {@link PDFAValidator} instance that uses
	 * {@link ValidationProfile}s packaged as a core library resource.
	 *
	 * @param flavours
	 *            list of the {@link PDFAFlavour} that are associated with the
	 *            {@code ValidationProfile} to used to initialise the
	 *            {@code PDFAValidator}.
	 * @return a {@link PDFAValidator} instance initialised from the passed
	 *         parameters
	 */
	public PDFAValidator createValidator(List<PDFAFlavour> flavours);

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

	/**
	 * Creates a new {@link PDFAValidator} initialised with the passed profile and parameters.
	 *
	 * @param flavour						  the {@link PDFAFlavour} that's associated with the
	 *										 {@code ValidationProfile} to used to initialise the
	 *										 {@code PDFAValidator}.
	 * @param maxNumberOfDisplayedFailedChecks a max checks number to show
	 * @param logSuccess					   {@code boolean} used to configure logging of passed tests by
	 *										 the {@code PDFAValidator}. Pass {@code true} to log passed
	 *										 tests, {@code false} to only log tests that don't pass.
	 * @param showErrorMessages				a flag to show error messages
	 * @param showProgress					 a flag to show validation progress
	 *
	 * @return a {@link PDFAValidator} instance initialised from the passed parameters
	 */
	public PDFAValidator createValidator(PDFAFlavour flavour, int maxNumberOfDisplayedFailedChecks,
			boolean logSuccess, boolean showErrorMessages, boolean showProgress);

	/**
	 * Creates a new {@link PDFAValidator} initialised with the passed profile and parameters.
	 *
	 * @param flavour						  the {@link PDFAFlavour} that's associated with the
	 *										 {@code ValidationProfile} to used to initialise the
	 *										 {@code PDFAValidator}.
	 * @param maxNumberOfDisplayedFailedChecks a max checks number to show
	 * @param logSuccess					   {@code boolean} used to configure logging of passed tests by
	 *										 the {@code PDFAValidator}. Pass {@code true} to log passed
	 *										 tests, {@code false} to only log tests that don't pass.
	 * @param showErrorMessages				a flag to show error messages
	 * @param showProgress					 a flag to show validation progress
	 *
	 * @return a {@link PDFAValidator} instance initialised from the passed parameters
	 */
	public PDFAValidator createValidator(List<PDFAFlavour> flavour, int maxNumberOfDisplayedFailedChecks,
			boolean logSuccess, boolean showErrorMessages, boolean showProgress);

	/**
	 * Creates a new {@link PDFAValidator} initialised with the passed profile and parameters.
	 *
	 * @param profile						  the {@link ValidationProfile} to be enforced by the returned
	 *										 {@code PDFAValidator}.
	 * @param maxNumberOfDisplayedFailedChecks a max checks number to show
	 * @param logSuccess					   {@code boolean} used to configure logging of passed tests by
	 *										 the {@code PDFAValidator}. Pass {@code true} to log passed
	 *										 tests, {@code false} to only log tests that don't pass.
	 * @param showErrorMessages				a flag to show error messages
	 * @param showProgress					 a flag to show validation progress
	 *
	 * @return a {@link PDFAValidator} instance initialised from the passed parameters
	 */
	public PDFAValidator createValidator(ValidationProfile profile, int maxNumberOfDisplayedFailedChecks,
			boolean logSuccess, boolean showErrorMessages, boolean showProgress);

	/**
	 * Creates a new {@link PDFAValidator} initialised with the passed profile and parameters.
	 *
	 * @param flavour						  the {@link PDFAFlavour} that's associated with the
	 *										 {@code ValidationProfile} to used to initialise the
	 *										 {@code PDFAValidator}.
	 * @param maxFailures					  an {@code int} value that configures the {@code PDFAValidator} to abort
	 *										 validation after {@code maxFailures} failed tests. If {@code
	 *										 maxFailures} is less than 1 then the {@code PDFAValidator} will
	 *										 complete the full validation process.
	 * @param maxNumberOfDisplayedFailedChecks a max checks number to show
	 * @param logSuccess					   a flag to show success logs
	 * @param showErrorMessages				a flag to show error message
	 * @param showProgress					 a flag to show validation progress
	 *
	 * @return a {@link PDFAValidator} instance initialised from the passed parameters
	 */
	public PDFAValidator createFailFastValidator(PDFAFlavour flavour, int maxFailures,
												 int maxNumberOfDisplayedFailedChecks, boolean logSuccess, 
												 boolean showErrorMessages, boolean showProgress);

	/**
	 * Creates a new {@link PDFAValidator} initialised with the passed profile and parameters, requested fast failing 
	 * behaviour.
	 *
	 * @param flavours						 the list of {@link PDFAFlavour} that's associated with the
	 *										 {@code ValidationProfile} to used to initialise th {@code PDFAValidator}.
	 * @param maxFailures					  an {@code int} value that configures the {@code PDFAValidator} to abort
	 *										 validation after {@code maxFailures} failed tests. If {@code
	 *										 maxFailures} is less than 1 then the {@code PDFAValidator} will
	 *										 complete the full validation process.
	 * @param maxNumberOfDisplayedFailedChecks a max checks number to show
	 * @param logSuccess					   a flag to show success logs
	 * @param showErrorMessages				a flag to show error message
	 * @param showProgress					 a flag to show validation progress
	 *
	 * @return a {@link PDFAValidator} instance initialised from the passed parameters
	 */
	public PDFAValidator createFailFastValidator(List<PDFAFlavour> flavours, int maxFailures, 
												 int maxNumberOfDisplayedFailedChecks, boolean logSuccess, 
												 boolean showErrorMessages, boolean showProgress);

	/**
	 * Creates a new {@link PDFAValidator} initialised with the passed profile and parameters, requested fast failing 
	 * behaviour.
	 *
	 * @param profile						  the {@link ValidationProfile} to be enforced by the returned
	 *										 {@code PDFAValidator}.
	 * @param maxFailures					  an {@code int} value that configures the {@code PDFAValidator} to abort
	 *										 validation after {@code maxFailures} failed tests. If {@code
	 *										 maxFailures} is less than 1 then the {@code PDFAValidator} will complete
	 *										 the full validation process.
	 * @param maxNumberOfDisplayedFailedChecks a max checks number to show
	 * @param logSuccess					   a flag to show success logs
	 * @param showErrorMessages				a flag to show error message
	 * @param showProgress					 a flag to show validation progress
	 *
	 * @return a {@link PDFAValidator} instance initialised from the passed parameters
	 */
	public PDFAValidator createFailFastValidator(ValidationProfile profile, int maxFailures, 
												 int maxNumberOfDisplayedFailedChecks, boolean logSuccess, 
												 boolean showErrorMessages, boolean showProgress);

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

	public String getParserId();
}
