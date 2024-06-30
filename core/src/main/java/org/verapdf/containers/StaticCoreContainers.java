package org.verapdf.containers;

import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.Collections;
import java.util.List;

public class StaticCoreContainers {

    private static final ThreadLocal<List<PDFAFlavour>> flavour = new ThreadLocal<>();

    public static void clearAllContainers() {
        flavour.set(null);
    }

    public static List<PDFAFlavour> getFlavour() {
        return flavour.get();
    }
    
    public static void setFlavour(List<PDFAFlavour> flavour) {
        StaticCoreContainers.flavour.set(flavour);
    }

    public static void setFlavour(PDFAFlavour flavour) {
        StaticCoreContainers.flavour.set(Collections.singletonList(flavour));
    }
}
