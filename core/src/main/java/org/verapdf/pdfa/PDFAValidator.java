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
package org.verapdf.pdfa;

import org.verapdf.component.Component;
import org.verapdf.core.ValidationException;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;

/**
 * A PDFAValidator performs a series of checks on PDF/A documents to verify that
 * the document conforms to a specific PDF/A flavour.
 *
 * Note that the interface makes no provision for configuration of a validator
 * instance. This is left to the implementer although the veraPDF library API
 * provides a {@link Validators} class. This is designed to allow
 * immutable validator instances, meaning there is no methods provided to change
 * the ValidationProfile, or the pre-configured settings.
 *
 * @author Maksim Bezrukov
 */
public interface PDFAValidator extends Component {

    /**
     * Returns the complete {@link ValidationProfile} enforced by this PDFAValidator.
     *
     * @return this PDFAValidator instance's ValiationProfile
     */
    public ValidationProfile getProfile();

    /**
     * This method validates an InputStream, presumably believed to be a PDF/A
     * document of a specific flavour that matches the ValidationProfile
     * supported by the PDFAValidator instance.
     *
     * @param toValidate
     *            a {@link java.io.InputStream} to be validated
     * @return a {@link ValidationResult} containing the result of valdiation
     *         and details of failed checks and possibly passed checks,
     *         dependent upon configuration.
     * @throws ValidationException 
     * @throws ModelParsingException 
     * @throws IllegalArgumentException
     *             if the toValidate parameter is null PDFAValidationException
     *             if the validation process fails
     */
    public ValidationResult validate(PDFAParser toValidate) throws ValidationException;

    public String getValidationProgressString();

}
