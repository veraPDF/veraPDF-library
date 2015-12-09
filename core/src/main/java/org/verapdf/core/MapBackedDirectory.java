/**
 * 
 */
package org.verapdf.core;

import java.util.*;

/**
 * A {@link java.util.Map} backed {@link Directory} implementation.
 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 * @param <K>
 *            A key or lookup type
 * @param <V>
 *            A value type
 *
 */
public class MapBackedDirectory<K, V> implements Directory<K, V> {
    protected final Map<K, V> map;
    
    /**
     * Creates an empty directory backed by an empty Map
     */
    public MapBackedDirectory() {
        this(Collections.<K, V>emptyMap());
    }
    /**
     * @param map
     */
    public MapBackedDirectory(final Map<K, V> map) {
        super();
        this.map = new HashMap<>(map);
    }
    /**
     * { @inheritDoc }
     */
    @Override
    public V getItem(K key) {
        return this.map.get(key);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public List<V> getItems() {
        return Collections.unmodifiableList(new ArrayList<>(this.map.values()));
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<K> getKeys() {
        return Collections.unmodifiableSet(this.map.keySet());
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public int size() {
        return this.map.size();
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }
    /**
     * { @inheritDoc }
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.map == null) ? 0 : this.map.hashCode());
        return result;
    }
    /**
     * { @inheritDoc }
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Directory<?, ?>))
            return false;
        @SuppressWarnings("unchecked")
        Directory<K, V> other = (Directory<K, V>) obj;
        if (this.getItems() == null) {
            if (other.getItems() != null)
                return false;
        } else if (!this.getItems().equals(other.getItems()))
            return false;
        if (this.getKeys() == null) {
            if (other.getKeys() != null)
                return false;
        } else if (!this.getKeys().equals(other.getKeys()))
            return false;
        return true;
    }
}
