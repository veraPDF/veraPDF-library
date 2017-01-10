/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
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
