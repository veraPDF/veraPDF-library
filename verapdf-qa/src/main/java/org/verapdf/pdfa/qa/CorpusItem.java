/**
 * 
 */
package org.verapdf.pdfa.qa;

/**
 * Interface that encapsulates a basic corpus item.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public interface CorpusItem {
    /**
     * @return the {@link CorpusItemId} of the instance.
     */
    public CorpusItemId getId();

    /**
     * @return the {@code String} path of the item
     */
    public String getPath();

    /**
     * @return the SHA-1 digest of the item, if known, an empty string if not
     *         known
     */
    public String getSha1();
}
