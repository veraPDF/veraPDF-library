/**
 * 
 */
package org.verapdf.core;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 * @param <K>
 *            A key or lookup type
 * @param <V>
 *            A value type
 *
 */
public abstract class FileBackedRegistry<K, V> implements Registry<K, V> {
    private final File root;
    private final MapBackedRegistry<K, V> map;

    /**
     * Default constructor creates an empty registry
     * 
     * @param rootDir
     */
    public FileBackedRegistry(final File rootDir) {
        this(rootDir, Collections.EMPTY_MAP);
    }

    /**
     * @param rootDir
     * @param map
     * 
     */
    public FileBackedRegistry(final File rootDir, final Map<K, V> map) {
        super();
        if (rootDir == null)
            throw new IllegalArgumentException("rootDir cannot be null.");
        if (!rootDir.isDirectory())
            throw new IllegalArgumentException("rootDir MUST be a directory.");
        this.root = rootDir;
        this.map = new MapBackedRegistry<>(map);
        this.load();
        this.persist();
    }

    /**
     * @return a File that corresponds to the root directory of the registry
     */
    public File getRoot() {
        return new File(this.root.getPath());
    }

    /**
     * { @inheritDoc }
     *
     * @see org.verapdf.core.MapBackedDirectory#getItem(java.lang.Object)
     */
    @Override
    public V getItem(K key) {
        return this.map.getItem(key);
    }

    /**
     * { @inheritDoc }
     *
     * @see org.verapdf.core.MapBackedRegistry#registerItem(java.lang.Object,
     *      java.lang.Object)
     */
    @Override
    public V registerItem(K key, V value) {
        V retVal = this.map.registerItem(key, value);
        this.persist();
        return retVal;
    }

    /**
     * { @inheritDoc }
     *
     * @see org.verapdf.core.MapBackedDirectory#getItems()
     */
    @Override
    public List<V> getItems() {
        return this.map.getItems();
    }

    /**
     * { @inheritDoc }
     *
     * @see org.verapdf.core.MapBackedRegistry#registerItems(java.util.Map)
     */
    @Override
    public void registerItems(Map<K, V> itemMap) {
        this.map.registerItems(itemMap);
        this.persist();
    }

    /**
     * { @inheritDoc }
     *
     * @see org.verapdf.core.MapBackedDirectory#getKeys()
     */
    @Override
    public Set<K> getKeys() {
        return this.map.getKeys();
    }

    /**
     * { @inheritDoc }
     *
     * @see org.verapdf.core.MapBackedRegistry#removeItem(java.lang.Object)
     */
    @Override
    public V removeItem(K key) {
        V retVal = this.map.removeItem(key);
        this.persist();
        return retVal;
    }

    /**
     * { @inheritDoc }
     *
     * @see org.verapdf.core.MapBackedDirectory#size()
     */
    @Override
    public int size() {
        return this.map.size();
    }

    /**
     * { @inheritDoc }
     *
     * @see org.verapdf.core.MapBackedRegistry#removeItems(java.util.Set)
     */
    @Override
    public void removeItems(Set<K> keys) {
        this.map.removeItems(keys);
        this.persist();
    }

    /**
     * { @inheritDoc }
     *
     * @see org.verapdf.core.MapBackedDirectory#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    /**
     * { @inheritDoc }
     *
     * @see org.verapdf.core.MapBackedRegistry#putdateItem(java.lang.Object,
     *      java.lang.Object)
     */
    @Override
    public V putdateItem(K key, V value) {
        V retVal = this.map.putdateItem(key, value);
        this.persist();
        return retVal;
    }

    /**
     * { @inheritDoc }
     *
     * @see org.verapdf.core.MapBackedDirectory#hashCode()
     */
    @Override
    public int hashCode() {
        return this.map.hashCode();
    }

    /**
     * { @inheritDoc }
     *
     * @see org.verapdf.core.MapBackedRegistry#putdateItems(java.util.Map)
     */
    @Override
    public void putdateItems(Map<K, V> itemMap) {
        this.map.putdateItems(itemMap);
        this.persist();
    }

    /**
     * { @inheritDoc }
     *
     * @see org.verapdf.core.MapBackedDirectory#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return this.map.equals(obj);
    }

    /**
     * { @inheritDoc }
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.map.toString();
    }

    /**
     * { @inheritDoc }
     *
     * @see org.verapdf.core.MapBackedRegistry#updateItem(java.lang.Object,
     *      java.lang.Object)
     */
    @Override
    public V updateItem(K key, V value) {
        V retVal = this.map.updateItem(key, value);
        this.persist();
        return retVal;
    }

    /**
     * { @inheritDoc }
     *
     * @see org.verapdf.core.MapBackedRegistry#updateItems(java.util.Map)
     */
    @Override
    public void updateItems(Map<K, V> itemMap) {
        this.map.updateItems(itemMap);
        this.persist();
    }

    protected abstract void persist();
    protected abstract void load();
}
