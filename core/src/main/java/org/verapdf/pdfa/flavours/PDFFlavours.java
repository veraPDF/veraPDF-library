/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.pdfa.flavours;

import java.util.List;

public class PDFFlavours {

    public static boolean isOnlyPDFUARelatedFlavour(List<PDFAFlavour> flavours) {
        for (PDFAFlavour flavour : flavours) {
            if (!isPDFUARelatedFlavour(flavour)) {
                return false;
            }
        }
        return true;
    }

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

    public static boolean isOnlyFlavour(List<PDFAFlavour> flavours, PDFAFlavour checkedFlavour) {
        for (PDFAFlavour flavour : flavours) {
            if (!isFlavour(flavour, checkedFlavour)) {
                return false;
            }
        }
        return true;
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
