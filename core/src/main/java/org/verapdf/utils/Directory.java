/**
 * 
 */
package org.verapdf.utils;

import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 * @param <K> 
 * @param <V> 
 *
 */
public interface Directory<K, V> {
    /**
     * @param key
     * @return
     */
    V getItem(final K key);
    /**
     * @return
     */
    List<V> getItems();
    /**
     * @return
     */
    Set<K> getKeys();
    /**
     * @return
     */
    int size();
    /**
     * @return
     */
    boolean isEmpty();
}
