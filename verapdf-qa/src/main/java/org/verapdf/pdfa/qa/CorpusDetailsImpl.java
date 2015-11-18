/**
 * 
 */
package org.verapdf.pdfa.qa;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public class CorpusDetailsImpl implements CorpusDetails {
    final String name;
    final String description;

    private CorpusDetailsImpl(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * @param name
     *            the name of the TestCorpus
     * @param description
     *            a textual description of the TestCorpus
     * @return a new CorpusDetails instance initialised with the passed param
     *         values
     */
    static CorpusDetails fromValues(final String name,
            final String description) {
        if (name == null)
            throw new NullPointerException("Parameter name can not be null");
        if (name.isEmpty())
            throw new NullPointerException("Parameter name can not be empty");
        if (description == null)
            throw new NullPointerException(
                    "Parameter description can not be null");
        return new CorpusDetailsImpl(name, description);
    }
}
