/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
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
        this(Collections.emptyMap());
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
        if (!Objects.equals(this.getItems(), other.getItems())) {
            return false;
        }
        return Objects.equals(this.getKeys(), other.getKeys());
    }
}
