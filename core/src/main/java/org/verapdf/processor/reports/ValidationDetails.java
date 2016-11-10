/**
 * 
 */
package org.verapdf.processor.reports;

import java.util.Set;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 10 Nov 2016:08:19:46
 */

public interface ValidationDetails {
    public int getPassedRules();
    public int getFailedRules();
    public int getPassedChecks();
    public int getFailedChecks();
    public Set<RuleSummary> getRuleSummaries();
}
