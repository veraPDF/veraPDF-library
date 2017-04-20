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
package org.verapdf.pdfa.validation.profiles;

import org.verapdf.pdfa.flavours.PDFAFlavour.Specification;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * RuleIds are used to identify the individual {@link Rule}s that make up a
 * {@link ValidationProfile}. A RuleId instance can be traced to the PDF/A
 * specification and clause that was the motivation behind it. A RuleId
 * comprises:
 * <ul>
 * <li>a {@link Specification} instance that identifies the particular PDF/A
 * specification referenced.</li>
 * <li>a String clause identifier, for the PDF/A specifications clauses are
 * identified by a sequence of period separated integers, e.g 6.1.3, that
 * reflect the heading identifiers used in the specification document.</li>
 * <li>an int test number, many specification clauses are further sub-divided
 * into a set of tests.</li>
 * </ul>
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@XmlJavaTypeAdapter(RuleIdImpl.Adapter.class)
public interface RuleId {
    /**
     * @return the
     */
    public Specification getSpecification();
    /**
     * @return the specification clause String identifier.
     */
    public String getClause();

    /**
     * @return the test number for this particular rule under its specification
     *         clause.
     */
    public int getTestNumber();

}
