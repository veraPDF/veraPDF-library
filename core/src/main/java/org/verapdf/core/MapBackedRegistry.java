/**
 * 
 */
package org.verapdf.core;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 * @param <K> 
 * @param <V> 
 *
 */
public class MapBackedRegistry<K, V> extends MapBackedDirectory<K, V>
        implements Registry<K, V> {
    /**
     * @param map
     */
    public MapBackedRegistry(final Map<K, V> map) {
        super(map);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public V registerItem(final K key, final V value) {
        return this.map.put(key, value);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public void registerItems(final Map<K, V> itemMap) {
        this.map.putAll(itemMap);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public V removeItem(final K key) {
        return this.map.remove(key);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public void removeItems(final Set<K> keys) {
        for (K key : keys) {
            this.map.remove(key);
        }
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public V putdateItem(final K key, final V value) {
        return this.map.put(key, value);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public void putdateItems(final Map<K, V> itemMap) {
        this.map.putAll(itemMap);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public V updateItem(final K key, final V value) {
        // If the item doesn't exist then a no such element exception
        if (!this.map.containsKey(key)) {
            throw new NoSuchElementException(); 
        }
        return this.map.put(key, value);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public void updateItems(final Map<K, V> itemMap) {
        for (K key : itemMap.keySet()) {
            updateItem(key, itemMap.get(key));
        }
    }

}
