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
    private final String name;
    private final String description;
    private final Map<String, L> itemMap;
    
    protected AbstractTestCorpus(final String name, final String description, final Map<String, L> itemMap) {
        this.name = name;
        this.description = description;
        this.itemMap = new HashMap<>(itemMap);
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
        // TODO Auto-generated method stub
        if (!this.itemMap.containsKey(itemName)) throw new IOException("No element found for name=" + itemName);
        return getStreamFromReference(this.itemMap.get(itemName));
    }
    
    abstract protected InputStream getStreamFromReference(final L reference) throws IOException;
}
