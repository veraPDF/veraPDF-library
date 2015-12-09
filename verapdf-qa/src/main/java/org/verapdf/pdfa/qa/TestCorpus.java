/**
 * 
 */
package org.verapdf.pdfa.qa;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public interface TestCorpus {
    /**
     * @return the name of the TestCorpus instance
     */
    public CorpusDetails getDetails();

    /**
     * @return the number of items held in the corpus
     */
    public int getItemCount();

    /**
     * @return the set of all corpus item names
     */
    public Set<String> getItemNames();

    /**
     * @param flavour
     *            the flavour to select corpus item names for
     * @return the set of corpus item names for the PDFAFlavour {@code flavour}
     */
    public Set<String> getItemNamesForFlavour(PDFAFlavour flavour);

    /**
     * @param itemName
     *            the name of the item to retrieve the input stream for
     * @return an InputStream for the item data
     * @throws IOException
     *             if there's a problem retrieving the stream
     */
    public InputStream getItemStream(String itemName) throws IOException;
    
    
}
