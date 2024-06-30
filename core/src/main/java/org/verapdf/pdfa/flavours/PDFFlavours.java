package org.verapdf.pdfa.flavours;

import java.util.List;

public class PDFFlavours {

    public static boolean isPDFUARelatedFlavour(List<PDFAFlavour> flavours) {
        for (PDFAFlavour flavour : flavours) {
            if (isPDFUARelatedFlavour(flavour)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isPDFUARelatedFlavour(PDFAFlavour flavour) {
        return isFlavourFamily(flavour, PDFAFlavour.SpecificationFamily.PDF_UA) || isWTPDFFlavour(flavour) || isWCAGFlavour(flavour);
    }
    
    public static boolean isPDFUA1RelatedFlavour(PDFAFlavour flavour) {
        return isFlavour(flavour, PDFAFlavour.PDFUA_1) || isWCAGFlavour(flavour);
    }

    public static boolean isPDFUA2RelatedFlavour(List<PDFAFlavour> flavours) {
        for (PDFAFlavour flavour : flavours) {
            if (isPDFUA2RelatedFlavour(flavour)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPDFUA2RelatedFlavour(PDFAFlavour flavour) {
        return isFlavour(flavour, PDFAFlavour.PDFUA_2) || isFlavourPart(flavour, PDFAFlavour.Specification.WTPDF_1_0);
    }

    public static boolean isWCAGFlavour(List<PDFAFlavour> flavours) {
        for (PDFAFlavour flavour : flavours) {
            if (isWCAGFlavour(flavour)) {
                return true;
            }
        }
        return false;
    }


    public static boolean isWCAGFlavour(PDFAFlavour flavour) {
        return isFlavourFamily(flavour, PDFAFlavour.SpecificationFamily.WCAG);
    }

    public static boolean isWTPDFFlavour(List<PDFAFlavour> flavours) {
        for (PDFAFlavour flavour : flavours) {
            if (isWTPDFFlavour(flavour)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWTPDFFlavour(PDFAFlavour flavour) {
        return isFlavourFamily(flavour, PDFAFlavour.SpecificationFamily.WTPDF);
    }

    public static boolean isFlavour(List<PDFAFlavour> flavours, PDFAFlavour checkedFlavour) {
        for (PDFAFlavour flavour : flavours) {
            if (isFlavour(flavour, checkedFlavour)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isFlavour(PDFAFlavour currentFlavour, PDFAFlavour checkedFlavour) {
        return currentFlavour == checkedFlavour;
    }

    public static boolean isFlavourFamily(PDFAFlavour flavour, PDFAFlavour.SpecificationFamily family) {
        return flavour != null && flavour.getPart().getFamily() == family;
    }

    public static boolean isFlavourPart(List<PDFAFlavour> flavours, PDFAFlavour.Specification part) {
        for (PDFAFlavour flavour : flavours) {
            if (isFlavourPart(flavour, part)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isFlavourPart(PDFAFlavour flavour, PDFAFlavour.Specification part) {
        return flavour != null && flavour.getPart() == part;
    }

    public static boolean isPDFSpecification(List<PDFAFlavour> flavours, PDFAFlavour.PDFSpecification pdfSpecification) {
        for (PDFAFlavour flavour : flavours) {
            if (isPDFSpecification(flavour, pdfSpecification)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPDFSpecification(PDFAFlavour flavour, PDFAFlavour.PDFSpecification pdfSpecification) {
        return flavour != null && flavour.getPart().getPdfSpecification() == pdfSpecification;
    }
    
    public static boolean isFlavourISOSeries(PDFAFlavour flavour, PDFAFlavour.IsoStandardSeries isoStandardSeries) {
        return flavour != null && flavour.getPart().getSeries() == isoStandardSeries;
    }
}
