/**
 * 
 */
package org.verapdf.core;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A {@link java.util.Map} backed {@link Registry} implementation.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 * @param <K>
 *            A key or lookup type
 * @param <V>
 *            A value type
 *
 */
public class MapBackedRegistry<K, V> extends MapBackedDirectory<K, V> implements
        Registry<K, V> {
    /**
     * Creates an empty registry instance, initialised with an empty map
     */
    public MapBackedRegistry() {
        this(new HashMap<K, V>());
    }

    /**
     * Creates a registry instance initialise using the passed map
     * 
     * @param map
     *            a <code>Map<K, V> instance used to initialise the registry.
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
