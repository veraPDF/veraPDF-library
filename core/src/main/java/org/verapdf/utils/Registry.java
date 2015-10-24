/**
 * 
 */
package org.verapdf.utils;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Thin wrapper for Maps for convenience implementations.
 *  
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 * @param <K> 
 * @param <V> 
 */
public interface Registry<K, V> extends Directory<K, V> {
    /**
     * @param key the lookup for the item to add
     * @param value
     * @return 
     */
    V addItem(final K key, final V value);
    /**
     * @param itemMap
     */
    void addItems(final Map<K, V> itemMap);
    /**
     * @param key
     * @return 
     */
    V deleteItem(final K key);
    /**
     * @param keys
     */
    void deleteItems(final Set<K> keys);
    /**
     * @param key
     * @param value
     * @return 
     */
    V putdateItem(final K key, final V value);
    /**
     * @param itemMap
     */
    void putdateItems(final Map<K, V> itemMap);
    /**
     * @param key
     * @param value
     * @return the old value of the item
     * @throws NoSuchElementException
     */
    V updateItem(final K key, final V value);
    /**
     * @param itemMap
     * @throws NoSuchElementException
     */
    void updateItems(final Map<K, V> itemMap);
}
