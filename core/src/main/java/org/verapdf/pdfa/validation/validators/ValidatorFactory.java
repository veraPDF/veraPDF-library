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
package org.verapdf.pdfa.validation.validators;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;

import org.verapdf.core.XmlSerialiser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.profiles.Profiles;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;

/**
 * Static utility class that fills in for a factory for {@link PDFAValidator}s.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public final class ValidatorFactory {
	private ValidatorFactory() {

	}

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
	 * @param logPassedChecks
	 *            {@code boolean} used to configure logging of passed tests by
	 *            the {@code PDFAValidator}. Pass {@code true} to log passed
	 *            tests, {@code false} to only log tests that don't pass.
	 * @return a {@link PDFAValidator} instance initialised from the passed
	 *         parameters
	 */
	public static PDFAValidator createValidator(final PDFAFlavour flavour, final boolean logPassedChecks) {
		if (flavour == null)
			throw new IllegalArgumentException("Parameter (PDFAFlavour flavour) cannot be null.");
		return createValidator(Profiles.getVeraProfileDirectory().getValidationProfileByFlavour(flavour),
				logPassedChecks);
	}

	/**
	 * Creates a new {@link PDFAValidator} instance that uses one of the
	 * {@link ValidationProfile}s packaged as a core library resource, see
	 * {@link Validators#createValidator(PDFAFlavour, boolean)}.
	 * 
	 * @param flavour
	 *            the {@link PDFAFlavour} that's associated with the
	 *            {@code ValidationProfile} to used to initialise the
	 *            {@code PDFAValidator}.
	 * @param logPassedChecks
	 *            {@code boolean} used to configure logging of passed tests by
	 *            the {@code PDFAValidator}. Pass {@code true} to log passed
	 *            tests, {@code false} to only log tests that don't pass.
	 * @param maxFailures
	 *            an {@code int} value that configures the {@code PDFAValidator}
	 *            to abort validation after {@code maxFailures} failed tests. If
	 *            {@code maxFailures} is less than 1 then the
	 *            {@code PDFAValidator} will complete the full validation
	 *            process.
	 * @return a {@link PDFAValidator} instance initialised from the passed
	 *         parameters
	 */
	public static PDFAValidator createValidator(final PDFAFlavour flavour, final boolean logPassedChecks,
			final int maxFailures) {
		if (flavour == null)
			throw new IllegalArgumentException("Parameter (PDFAFlavour flavour) cannot be null.");
		return createValidator(Profiles.getVeraProfileDirectory().getValidationProfileByFlavour(flavour),
				logPassedChecks, maxFailures);
	}

	/**
	 * Creates a new {@link PDFAValidator} initialised with the passed profile
	 * and configured NOT to log passed checks.
	 * 
	 * @param profile
	 *            the {@link ValidationProfile} to be enforced by the returned
	 *            {@code PDFAValidator}.
	 * @return a {@link PDFAValidator} instance initialised from the passed
	 *         parameters
	 */
	public static PDFAValidator createValidator(final ValidationProfile profile) {
		return createValidator(profile, false);
	}

	/**
	 * Creates a new {@link PDFAValidator} initialised with the passed profile
	 * and chosen passed test logging.
	 * 
	 * @param profile
	 *            the {@link ValidationProfile} to be enforced by the returned
	 *            {@code PDFAValidator}.
	 * @param logPassedChecks
	 *            {@code boolean} used to configure logging of passed tests by
	 *            the {@code PDFAValidator}. Pass {@code true} to log passed
	 *            tests, {@code false} to only log tests that don't pass.
	 * @return a {@link PDFAValidator} instance initialised from the passed
	 *         parameters
	 */
	public static PDFAValidator createValidator(final ValidationProfile profile, final boolean logPassedChecks) {
		if (profile == null)
			throw new IllegalArgumentException("Parameter (ValidationProfile profile) cannot be null.");
		return new BaseValidator(profile, logPassedChecks);
	}

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
	public static PDFAValidator createValidator(final ValidationProfile profile, final int maxFailures) {
		return createValidator(profile, false, maxFailures);
	}

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
	public static PDFAValidator createValidator(final PDFAFlavour flavour, final int maxFailures) {
		return createValidator(flavour, false, maxFailures);
	}

	/**
	 * Creates a new {@link PDFAValidator} initialised with the passed profile,
	 * chosen passed test logging and requested fast failing behaviour.
	 * 
	 * @param profile
	 *            the {@link ValidationProfile} to be enforced by the returned
	 *            {@code PDFAValidator}.
	 * @param logPassedChecks
	 *            {@code boolean} used to configure logging of passed tests by
	 *            the {@code PDFAValidator}. Pass {@code true} to log passed
	 *            tests, {@code false} to only log tests that don't pass.
	 * @param maxFailures
	 *            an {@code int} value that configures the {@code PDFAValidator}
	 *            to abort validation after {@code maxFailures} failed tests. If
	 *            {@code maxFailures} is less than 1 then the
	 *            {@code PDFAValidator} will complete the full validation
	 *            process.
	 * @return a {@link PDFAValidator} instance initialised from the passed
	 *         parameters
	 */
	public static PDFAValidator createValidator(final ValidationProfile profile, final boolean logPassedChecks,
			final int maxFailures) {
		if (profile == null)
			throw new IllegalArgumentException("Parameter (ValidationProfile profile) cannot be null.");
		if (maxFailures > 0)
			return new FastFailValidator(profile, logPassedChecks, maxFailures);
		return createValidator(profile, logPassedChecks);
	}

	/**
	 * @return the default {@link ValidatorConfig} instance
	 */
	public static ValidatorConfig defaultConfig() {
		return ValidatorConfigImpl.defaultInstance();
	}

	/**
	 * Create a {@link ValidatorConfig} instance from the passed values.
	 * 
	 * @param flavour
	 *            the {@link PDFAFlavour} used for validation
	 * @param recordPasses
	 *            a {@code boolean} parameter, set {@code true} to record passed
	 *            checks, {@code false} to only record failed checks.
	 * @param maxFails
	 *            an {@code int} value for the number of failed checks
	 *            encountered before ending validation.
	 * @return a new {@link ValidatorConfig} instance created from the passed
	 *         values.
	 */
	public static ValidatorConfig createConfig(final PDFAFlavour flavour, final boolean recordPasses,
			final int maxFails) {
		return ValidatorConfigImpl.fromValues(flavour, recordPasses, maxFails);
	}

	/**
	 * De-serialises a {@link ValidatorConfig} instance from it's XML
	 * representation
	 * 
	 * @param source
	 *            an {@link InputStream} that is an XML representation of a
	 *            {@link ValidatorConfig}
	 * @return a {@link ValidatorConfig} instance created from the passed XML
	 *         stream
	 * @throws JAXBException
	 *             when there's an error de-serialising
	 */
	public static ValidatorConfig createConfig(final InputStream source) throws JAXBException {
		return XmlSerialiser.typeFromXml(ValidatorConfigImpl.class, source);
	}

	/**
	 * Serialises a {@link ValidatorConfig} to XML
	 * 
	 * @param source
	 *            a {@link ValidatorConfig} instance to serialise
	 * @return a {@link String} containing the XML representation of the passed
	 *         {@link ValidatorConfig} instance.
	 * @throws JAXBException
	 *             when there's an error serialising
	 */
	public static String configToXml(final ValidatorConfig source) throws JAXBException {
		return XmlSerialiser.toXml(source, true, false);
	}

	/**
	 * Serialises a {@link ValidatorConfig} instance to its XML representation
	 * that's output to the passed {@link OutputStream}.
	 * 
	 * @param source
	 *            the {@link ValidatorConfig} instance to serialise
	 * @param dest
	 *            an {@link OutputStream} to which the serialised instance will
	 *            be written
	 * @throws JAXBException
	 *             when there's an error serialising
	 */
	public static void configToXml(final ValidatorConfig source, final OutputStream dest) throws JAXBException {
		XmlSerialiser.toXml(source, dest, true, false);
	}
}
