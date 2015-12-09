/**
 * 
 */
package org.verapdf.pdfa.qa;

import org.verapdf.pdfa.validation.RuleId;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public interface CorpusItemId {
    /**
     * @return the RuleId
     */
    public RuleId getRuleId();
    /**
     * @return the single character test code for the corpus item
     */
    public String getTestCode();
    /**
     * @return the expected result
     */
    public boolean getExpectedResult();
    
}
