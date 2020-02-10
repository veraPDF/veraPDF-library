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
package org.verapdf.pdfa.results;

import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.profiles.ProfileDetails;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

/**
 * Created as the result of validating a PDF/A document against a
 * {@link org.verapdf.pdfa.validation.profiles.ValidationProfile}. The class encapsulates
 * the following information:
 * <ul>
 * <li>a boolean flag indicating whether the PDF/A document validated complied
 * with ValidationProfile.</li>
 * <li>the {@link PDFAFlavour} of the profile used to validate the PDF/A
 * document.</li>
 * <li>a Set of {@link TestAssertion}s representing the results of performing
 * individual tests.</li>
 * </ul>
 * A particular {@link ValidationResult} instance is not guaranteed to carry all
 * of the information generated during validation. While the
 * <code>isCompliant()</code> value MUST reflect the result of validation,
 * individual validator implementations are not obliged to record all, or any
 * individual {@link TestAssertion}s. Such implementations may choose to only
 * record failed tests, or may be configured to abort validation after a so many
 * failed tests.
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@XmlJavaTypeAdapter(ValidationResultImpl.Adapter.class)
public interface ValidationResult {
    /**
     * @return true if the PDF/A document complies with the PDF/A specification
     *         / flavour it was validated against.
     */
    public boolean isCompliant();

    /**
     * @return the {@link PDFAFlavour} that identifies the PDF/A specification
     *         part and conformance level enforced by the Validator that
     *         produced this result.
     */
    public PDFAFlavour getPDFAFlavour();

    /**
     * @return the {@link ProfileDetails} identifying the validation profile used
     */
    public ProfileDetails getProfileDetails();

    /**
     * @return the total number of valdiation checks performed 
     */
    public int getTotalAssertions();

    /**
     * @return the list of {@link TestAssertion}s made during PDF/A validation
     */
    public List<TestAssertion> getTestAssertions();

    /**
     * @return validation profile which has been used for generating validation result
     */
    public ValidationProfile getValidationProfile();
}
