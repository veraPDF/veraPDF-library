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
package org.verapdf.pdfa.validation.profiles;

import org.verapdf.pdfa.flavours.PDFAFlavour;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Set;
import java.util.SortedSet;

/**
 * veraPDF ValidationProfiles encapsulate the validation rules and tests that
 * are enforced by the PDF/A Validator. A validation profile is associated with
 * a particular {@link PDFAFlavour}, that identifies the specific PDF/A
 * specification and conformance level that it is designed to test.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@XmlJavaTypeAdapter(ValidationProfileImpl.Adapter.class)
public interface ValidationProfile {
    /**
     * @return the {@link PDFAFlavour} that identifies the specification part
     *         and conformance level tested by this profile.
     */
    public PDFAFlavour getPDFAFlavour();

    /**
     * @return the {@link ProfileDetails} for this profile.
     */
    public ProfileDetails getDetails();

    /**
     * @return a hex-encoded String representation of the SHA-1 digest of this
     *         ValidationProfile
     */
    public String getHexSha1Digest();

    /**
     * @return the full set of Validation {@link Rule}s that are enforce by this
     *         ValidationProfile
     */
    public Set<Rule> getRules();

    /**
     * @param id
     *            the {@link RuleId} of the {@link Rule} to be retrieved.
     * @return the {@link Rule} associated with this id
     */
    public Rule getRuleByRuleId(final RuleId id);

    /**
     * Retrieve the complete Set of validation {@link Rule}s associated with a
     * PDF object
     * 
     * @param objectName
     *            the String name identifier for the object
     * @return the full set of Validation {@link Rule}s that are associated with
     *         object name
     */
    public Set<Rule> getRulesByObject(final String objectName);

    /**
     * TODO: A better explanation of Variables and their role.
     * 
     * @return the full set of {@link Variable}s used by this ValidationProfile.
     */
    public Set<Variable> getVariables();

    /**
     * TODO: A better explanation of Variables and their role.
     * 
     * @param objectName
     * 
     * @return the full set of {@link Variable}s that are associated with object
     *         name.
     */
    public Set<Variable> getVariablesByObject(final String objectName);

    public SortedSet<String> getTags();
}
