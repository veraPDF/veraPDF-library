/*
 * Copyright (c) 2015-2026, veraPDF Consortium.
 * All rights reserved.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *
 * Redistribution and use are permitted under the BSD 3-Clause License.
 * See the LICENSE.txt file distributed with this module for the full
 * license terms and disclaimer.
 */

package org.verapdf.xmp.containers;

import java.util.HashMap;
import java.util.Map;

public class StaticXmpCoreContainers {

    /**
     * a map from a namespace URI to its registered prefix
     */
    private static final ThreadLocal<Map<String, String>> namespaceToPrefixMap = ThreadLocal.withInitial(HashMap::new);

    /**
     * a map from a prefix to the associated namespace URI
     */
    private static final ThreadLocal<Map<String, String>> prefixToNamespaceMap = ThreadLocal.withInitial(HashMap::new);

    /**
     * Clears all namespaces and prefixes.
     */
    public static void clearAllContainers() {
        namespaceToPrefixMap.set(new HashMap<>());
        prefixToNamespaceMap.set(new HashMap<>());
    }

    /**
     * Gets namespaces map.
     *
     * @return a map of namespaces
     */
    public static Map<String, String> getNamespaceToPrefixMap() {
        return namespaceToPrefixMap.get();
    }

    /**
     * Gets prefixes map.
     *
     * @return a map of prefixes
     */
    public static Map<String, String> getPrefixToNamespaceMap() {
        return prefixToNamespaceMap.get();
    }

    /**
     * Sets namespaces.
     *
     * @param namespaceToPrefixMap a map of namespaces
     */
    public static void setNamespaceToPrefixMap(Map<String, String> namespaceToPrefixMap) {
        StaticXmpCoreContainers.namespaceToPrefixMap.set(namespaceToPrefixMap);
    }

    /**
     * Sets prefixes.
     *
     * @param prefixToNamespaceMap a map of prefixes
     */
    public static void setPrefixToNamespaceMap(Map<String, String> prefixToNamespaceMap) {
        StaticXmpCoreContainers.prefixToNamespaceMap.set(prefixToNamespaceMap);
    }
}
