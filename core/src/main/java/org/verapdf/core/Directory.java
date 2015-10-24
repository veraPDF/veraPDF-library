/**
 * 
 */
package org.verapdf.core;

import java.util.Collection;
import java.util.Set;

/**
 * A read only Directory that supports key and value types.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 * @param <K>
 *            the Directory key type
 * @param <V>
 *            the Directory value type
 *
 */
public interface Directory<K, V> {
    /**
     * @param key
     *            the key used to lookup a particular item
     * @return the value instance associated with the key
     */
    V getItem(final K key);

    /**
     * @return the Collection of values contained in the directory
     */
    Collection<V> getItems();

    /**
     * @return the Set of keys contained in the directory
     */
    Set<K> getKeys();

    /**
     * @return the number of items held in the directory
     */
    int size();

    /**
     * @return true if the directory contains no items, false if not.
     */
    boolean isEmpty();
}
