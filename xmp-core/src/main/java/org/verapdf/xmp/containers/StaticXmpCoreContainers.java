package org.verapdf.xmp.containers;

import java.util.*;

public class StaticXmpCoreContainers {

    /**
     * a map from a namespace URI to its registered prefix
     */
    private static final ThreadLocal<Map<String, String>> namespaceToPrefixMap = new ThreadLocal<>();

    /**
     * a map from a prefix to the associated namespace URI
     */
    private static final ThreadLocal<Map<String, String>> prefixToNamespaceMap = new ThreadLocal<>();

    public static void clearAllContainers() {
        namespaceToPrefixMap.set(new HashMap<>());
        prefixToNamespaceMap.set(new HashMap<>());
    }

    public static Map<String, String> getNamespaceToPrefixMap() {
        return namespaceToPrefixMap.get();
    }

    public static Map<String, String> getPrefixToNamespaceMap() {
        return prefixToNamespaceMap.get();
    }

    public static void setNamespaceToPrefixMap(Map<String, String> namespaceToPrefixMap) {
        StaticXmpCoreContainers.namespaceToPrefixMap.set(namespaceToPrefixMap);
    }

    public static void setPrefixToNamespaceMap(Map<String, String> prefixToNamespaceMap) {
        StaticXmpCoreContainers.prefixToNamespaceMap.set(prefixToNamespaceMap);
    }
}
