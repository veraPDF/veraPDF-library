package org.verapdf.pdfa.flavours;

public class PDFFlavours {
    
    public static boolean isPDFUARelatedFlavour(PDFAFlavour flavour) {
        return isFlavourFamily(flavour, PDFAFlavour.SpecificationFamily.PDF_UA) || isWTPDFFlavour(flavour) || isWCAGFlavour(flavour);
    }
    
    public static boolean isPDFUA1RelatedFlavour(PDFAFlavour flavour) {
        return isFlavour(flavour, PDFAFlavour.PDFUA_1) || isWCAGFlavour(flavour);
    }

    public static boolean isPDFUA2RelatedFlavour(PDFAFlavour flavour) {
        return isFlavour(flavour, PDFAFlavour.PDFUA_2) || isFlavourPart(flavour, PDFAFlavour.Specification.WTPDF_1_0);
    }

    public static boolean isWCAGFlavour(PDFAFlavour flavour) {
        return isFlavourFamily(flavour, PDFAFlavour.SpecificationFamily.WCAG);
    }
    
    public static boolean isWTPDFFlavour(PDFAFlavour flavour) {
        return isFlavourFamily(flavour, PDFAFlavour.SpecificationFamily.WTPDF);
    }
    
    public static boolean isFlavour(PDFAFlavour currentFlavour, PDFAFlavour checkedFlavour) {
        return currentFlavour == checkedFlavour;
    }

    public static boolean isFlavourFamily(PDFAFlavour flavour, PDFAFlavour.SpecificationFamily family) {
        return flavour != null && flavour.getPart().getFamily() == family;
    }
    
    public static boolean isFlavourPart(PDFAFlavour flavour, PDFAFlavour.Specification part) {
        return flavour != null && flavour.getPart() == part;
    }

    public static boolean isPDFSpecification(PDFAFlavour flavour, PDFAFlavour.PDFSpecification pdfSpecification) {
        return flavour != null && flavour.getPart().getPdfSpecification() == pdfSpecification;
    }
    
    public static boolean isFlavourISOSeries(PDFAFlavour flavour, PDFAFlavour.IsoStandardSeries isoStandardSeries) {
        return flavour != null && flavour.getPart().getSeries() == isoStandardSeries;
    }
}
