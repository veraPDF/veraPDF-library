/**
 * 
 */
package org.verapdf.processor.reports;

import java.util.Set;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
    public String getDescription();
    public String getObject();
    public String getTest();
    public Set<Check> getChecks();
}
