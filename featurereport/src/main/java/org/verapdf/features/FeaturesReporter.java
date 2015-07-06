package org.verapdf.features;

import org.verapdf.exceptions.featurereport.FeatureValueException;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.tools.FeaturesCollection;

/**
 * Features reporter
 *
 * @author Maksim Bezrukov
 */
public class FeaturesReporter {

    private FeaturesCollection collection;

    /**
     * Creates new FeaturesReporter
     */
    public FeaturesReporter() {
        collection = new FeaturesCollection();
    }

    /**
     * Reports feature object for feature report
     * @param obj - object for reporting
     * @throws FeatureValueException - occurs when wrong feature feature format found during features parser
     */
    public void report(IFeaturesObject obj) throws FeatureValueException {
        try {
            obj.reportFeatures(collection);
        } catch (FeaturesTreeNodeException ignore) {
            // This exception occurs when wrong node creates for feature tree.
            // The logic of the method guarantees this doesn't occur.
        }
    }

    /**
     * @return collection of featurereport
     */
    public FeaturesCollection getCollection() {
        return collection;
    }
}
