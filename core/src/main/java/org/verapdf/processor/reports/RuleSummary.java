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
package org.verapdf.processor.reports;

import java.util.List;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.verapdf.pdfa.results.TestAssertion.Status;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 10 Nov 2016:08:22:27
 */
@XmlJavaTypeAdapter(RuleSummaryImpl.Adapter.class)
public interface RuleSummary {
    public String getSpecification();
    public String getClause();
    public int getTestNumber();
    public String getStatus();
    public Status getRuleStatus();
    public int getPassedChecks();
    public int getFailedChecks();
    public String getTags();
    public String getDescription();
    public String getObject();
    public String getTest();
    public List<Check> getChecks();
}
