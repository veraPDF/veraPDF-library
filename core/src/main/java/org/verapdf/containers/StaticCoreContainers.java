package org.verapdf.containers;

import org.verapdf.pdfa.flavours.PDFAFlavour;

public class StaticCoreContainers {

    private static final ThreadLocal<PDFAFlavour> flavour = new ThreadLocal<>();

    public static void clearAllContainers() {
        flavour.set(null);
    }

    public static PDFAFlavour getFlavour() {
        return flavour.get();
    }
    
    public static void setFlavour(PDFAFlavour flavour) {
        StaticCoreContainers.flavour.set(flavour);
    }
}
