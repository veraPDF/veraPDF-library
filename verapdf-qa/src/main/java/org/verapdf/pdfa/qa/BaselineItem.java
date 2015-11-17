/**
 * 
 */
package org.verapdf.pdfa.qa;

import java.util.Set;

import org.verapdf.pdfa.results.TestAssertion;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public interface BaselineItem {
    /**
     * @return the Corpus item that the baseline item relates to
     */
    public CorpusItem getCorpusItem();

    /**
     * @return the number of of failed assertions expected by the baseline
     */
    public int getExpectedFailedCount();

    /**
     * @return the set of failed test assertions expected by the baseline
     */
    public Set<TestAssertion> getExpectedFailures();
}
