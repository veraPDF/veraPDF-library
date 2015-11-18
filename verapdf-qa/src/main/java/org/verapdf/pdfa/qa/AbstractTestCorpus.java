/**
 * 
 */
package org.verapdf.pdfa.qa;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
abstract class AbstractTestCorpus<L> implements TestCorpus {
    private final CorpusDetails details;
    private final Map<String, L> itemMap;

    protected AbstractTestCorpus(final CorpusDetails details,
            final Map<String, L> itemMap) {
        this.details = details;
        this.itemMap = new HashMap<>(itemMap);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public CorpusDetails getDetails() {
        return this.details;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public int getItemCount() {
        return this.itemMap.size();
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<String> getItemNames() {
        return Collections.unmodifiableSet(this.itemMap.keySet());
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<String> getItemNamesForFlavour(PDFAFlavour flavour) {
        // TODO Look at implementing sorting by flavour
        return Collections.unmodifiableSet(this.itemMap.keySet());
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public InputStream getItemStream(String itemName) throws IOException {
        if (!this.itemMap.containsKey(itemName))
            throw new IOException("No element found for name=" + itemName);
        return getStreamFromReference(this.itemMap.get(itemName));
    }

    abstract protected InputStream getStreamFromReference(final L reference)
            throws IOException;
}
