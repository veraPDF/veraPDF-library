/**
 * 
 */
package org.verapdf.pdfa.qa;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public class CorpusSampler {
    private CorpusSampler() {
        /**
         * Disable default constructor
         */
    }

    /**
     * @param corpus
     *            a {@link TestCorpus} instance to sample from
     * @param sampleSize
     *            the size of the sample to take
     * @return the {@code Set<String>} of names that make up the sample
     */
    public static Set<String> randomSample(final TestCorpus corpus,
            final int sampleSize) {
        if (corpus == null)
            throw new NullPointerException("Parameter corpus can not be null");
        if (sampleSize < 1)
            throw new NullPointerException("Parameter sampleSize=" + sampleSize
                    + ", must be > 0");
        Random random = new Random(new Date().getTime());
        int setSize = corpus.getItemNames().size();
        List<String> names = new ArrayList<>(corpus.getItemNames());
        Set<String> sample = new HashSet<>();
        for (int index = 0; index < sampleSize; index++) {
            sample.add(names.get(random.nextInt(setSize)));
        }
        return sample;
    }
}
