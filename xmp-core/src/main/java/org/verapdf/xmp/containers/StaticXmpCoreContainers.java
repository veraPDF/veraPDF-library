package org.verapdf.xmp.containers;

import java.util.HashMap;
import java.util.Map;

public class StaticXmpCoreContainers {

    /**
     * a map from a namespace URI to its registered prefix
     */
    private static final ThreadLocal<Map<String, String>> namespaceToPrefixMap = new ThreadLocal<>();

    /**
     * a map from a prefix to the associated namespace URI
     */
    private static final ThreadLocal<Map<String, String>> prefixToNamespaceMap = new ThreadLocal<>();

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
