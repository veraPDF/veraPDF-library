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
package org.verapdf.pdfa.flavours;


/**
 * Static utility class holding PDF/A Flavour and related specification String
 * constants.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public final class PDFAFlavours {
    static final String NONE = "";
    public static final String ARLINGTON = "Arlington"; //$NON-NLS-1$
    static final String ARLINGTON_1 = "ARLINGTON1"; //$NON-NLS-1$
    static final String ARLINGTON_2 = "ARLINGTON2"; //$NON-NLS-1$
    static final String PDFA = "PDF/A"; //$NON-NLS-1$
    static final String PDFUA = "PDF/UA"; //$NON-NLS-1$
    static final String TAGGED_PDF = "Tagged PDF"; //$NON-NLS-1$
    static final String WCAG_2_1 = "WCAG2.1"; //$NON-NLS-1$
    static final String WCAG = "WCAG"; //$NON-NLS-1$
    public static final String ARLINGTON_PREFIX = "arlington"; //$NON-NLS-1$
    public static final float VERSION_1_4 = 1.4f;
    public static final float VERSION_1_7 = 1.7f;
    static final String PDFUA_PREFIX = "ua"; //$NON-NLS-1$
    static final String WCAG2_1_PREFIX = "wcag"; //$NON-NLS-1$
    static final String ISO = "ISO"; //$NON-NLS-1$
    static final String ISO_PREFIX = ISO + " "; //$NON-NLS-1$
    static final int NONE_ID = 0;
    static final int ISO_14289_ID = 14289;
    static final int ISO_19005_ID = 19005;
    static final int ISO_32000_ID = 32000;
    static final int ISO_32000_1_ID = 320001;
    static final int ISO_32000_2_ID = 320002;
    static final int ISO_32005_ID = 32005;
    static final String ISO_14289_DESCRIPTION = "Document management applications — Electronic document file format enhancement for accessibility"; //$NON-NLS-1$
    static final String ISO_14289_1_DESCRIPTION = "Use of ISO 32000-1 (PDF/UA-1)"; //$NON-NLS-1$
    static final String ISO_14289_2_DESCRIPTION = "Use of ISO 32000-2 (PDF/UA-2)"; //$NON-NLS-1$
    static final String ISO_19005_DESCRIPTION = "Document management -- Electronic document file format for long-term preservation"; //$NON-NLS-1$
    static final String ISO_19005_1_DESCRIPTION = "Use of PDF 1.4"; //$NON-NLS-1$
    static final String ISO_19005_2_DESCRIPTION = "Use of ISO 32000-1"; //$NON-NLS-1$
    static final String ISO_19005_3_DESCRIPTION = "Use of ISO 32000-1 with support for embedded files"; //$NON-NLS-1$
    static final String ISO_19005_4_DESCRIPTION = "Use of ISO 32000-2"; //$NON-NLS-1$
    static final String ISO_32000_DESCRIPTION = "Document management -- Portable document format"; //$NON-NLS-1$
    static final String ISO_32005_DESCRIPTION = "Document management -- Portable document format -- PDF 1.7 and 2.0 structure namespace inclusion in ISO 32000-2"; //$NON-NLS-1$// fix
    static final String WCAG_2_1_DESCRIPTION = "Web Content Accessibility Guidelines 2.1"; //$NON-NLS-1$
    static final String WCAG_2_2_DESCRIPTION = "Web Content Accessibility Guidelines 2.2"; //$NON-NLS-1$
    static final int ISO_19005_1_PART = 1;
    static final int ISO_19005_2_PART = 2;
    static final int ISO_19005_3_PART = 3;
    static final int ISO_19005_4_PART = 4;
    static final int ISO_14289_1_PART = 1;
    static final int ISO_14289_2_PART = 2;
    static final int WCAG_2_1_PART = 2;
    static final int WCAG_2_2_PART = 2;
    static final String LEVEL_A_CODE = "a"; //$NON-NLS-1$
    static final String LEVEL_B_CODE = "b"; //$NON-NLS-1$
    static final String LEVEL_U_CODE = "u"; //$NON-NLS-1$
    static final String LEVEL_E_CODE = "e"; //$NON-NLS-1$
    static final String LEVEL_F_CODE = "f"; //$NON-NLS-1$
    static final String LEVEL_A_NAME = "accessible"; //$NON-NLS-1$
    static final String LEVEL_B_NAME = "basic"; //$NON-NLS-1$
    static final String LEVEL_U_NAME = "unicode"; //$NON-NLS-1$
    static final String LEVEL_E_NAME = "engineering"; //$NON-NLS-1$
    static final String LEVEL_F_NAME = "embedded files"; //$NON-NLS-1$
    static final String ISO_14289_1_YEAR = "2014"; //$NON-NLS-1$
    static final String ISO_14289_2_YEAR = "202x"; //$NON-NLS-1$
    static final String ISO_19005_1_YEAR = "2005"; //$NON-NLS-1$
    static final String ISO_19005_2_YEAR = "2011"; //$NON-NLS-1$
    static final String ISO_19005_3_YEAR = "2012"; //$NON-NLS-1$
    static final String ISO_19005_4_YEAR = "2020"; //$NON-NLS-1$
    static final String ISO_32005_YEAR = "202X"; //$NON-NLS-1$
    static final String WCAG_2_1_YEAR = "2018"; //$NON-NLS-1$
    static final String WCAG_2_2_YEAR = "2023"; //$NON-NLS-1$
    static final String ISO_REFERENCE_SUFFIX = "(E)"; //$NON-NLS-1$
    static final String LEVEL_PREFIX = "Level "; //$NON-NLS-1$

    private PDFAFlavours() {
        throw new AssertionError(
                "Should never enter utility class constructor."); //$NON-NLS-1$
    }

}
