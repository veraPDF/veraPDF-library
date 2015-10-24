/**
 * 
 */
package org.verapdf.core;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A read/write Registry that supports key and value types.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 * @param <K>
 *            the Registry key type
 * @param <V>
 *            the Registry value type
 */
public interface Registry<K, V> extends Directory<K, V> {
    /**
     * Add a single item to the Registry
     * 
     * @param key
     *            the lookup key for the value to add
     * @param value
     *            the value to add
     * @return the added value
     */
    V registerItem(final K key, final V value);

    /**
     * Add a Map<K, V> of items to the Registry
     * 
     * @param itemMap
     */
    void registerItems(final Map<K, V> itemMap);

    /**
     * Unregister an item from the Registry.
     * 
     * @param key
     *            the lookup key for the value to unregister.
     * @return the removed value or null if no value was associated with the key
     */
    V removeItem(final K key);

    /**
     * Unregister a Set of items from the Registry.
     * 
     * @param keys
     *            the Set of lookup keys to remove.
     */
    void removeItems(final Set<K> keys);

    /**
     * Registers or updates an item in Registry caring not if the item already
     * exists.
     * 
     * @param key
     *            the lookup key of the value to putdate.
     * @param value
     *            the value to be used for the putdate.
     * @return the old value of the item
     */
    V putdateItem(final K key, final V value);

    /**
     * Registers or updates all item in Registry caring not if the items already
     * exist or not.
     * 
     * @param itemMap
     *            a Map of keys & values to add.
     */
    void putdateItems(final Map<K, V> itemMap);

    /**
     * Updates the value associated with the key and throws and exception if the
     * item isn't registered.
     * 
     * @param key
     *            the lookup key of the value to update
     * @param value
     *            the value used in the update
     * @return the old value of the item
     * @throws NoSuchElementException
     *             if no value is associated with the key
     */
    V updateItem(final K key, final V value);

    /**
     * @param itemMap
     * @throws NoSuchElementException
     *             if no value is associated with one of the keys
     */
    void updateItems(final Map<K, V> itemMap);
}
