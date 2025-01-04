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
package org.verapdf.pdfa.validation.profiles;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.Set;

/**
 * Encapsulates a PDF/A Validation Rule including the String property
 * {@link Rule#getTest()} which is the logical expression that is evaluated when
 * applying the test for this rule.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@XmlJavaTypeAdapter(RuleImpl.Adapter.class)
public interface Rule {

    /**
     * @return the RuleID instance that uniquely identifies this rule
     */
    public RuleId getRuleId();

    /**
     * @return the String name of the PDF Object type to which the Rule applies
     */
    public String getObject();

	/**
     * @return the Boolean flag which identifiers
     * if the rule has to be checked after other rules
     */
    public Boolean getDeferred();

    public String getTags();

    public Set<String> getTagsSet();

    /**
     * @return a textual description of the Rule
     */
    public String getDescription();

    /**
     * @return the logical expression that is evaluated when asserting the test
     *         for this rule.
     */
    public String getTest();

    /**
     * @return the {@link ErrorDetails} associated with this Rule
     */
    public ErrorDetails getError();

    /**
     * @return a List of {@link Reference}s to the specification clause(s) from
     *         which the rule is derived.
     */
    public List<Reference> getReferences();
}
