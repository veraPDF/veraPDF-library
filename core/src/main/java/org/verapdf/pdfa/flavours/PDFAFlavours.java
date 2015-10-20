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
final class PDFAFlavours {
    final static String NONE = "none";
    final static String PDFA_STRING_PREFIX = "PDF/A-"; //$NON-NLS-1$
    final static String ISO = "ISO"; //$NON-NLS-1$
    final static String ISO_PREFIX = ISO + " "; //$NON-NLS-1$
    final static int NONE_ID = 0;
    final static int ISO_19005_ID = 19005;
    final static int ISO_32000_ID = 32000;
    final static String ISO_19005_DESCRIPTION = "Document management -- Electronic document file format for long-term preservation"; //$NON-NLS-1$
    final static String ISO_19005_1_DESCRIPTION = "Use of PDF 1.4"; //$NON-NLS-1$
    final static String ISO_19005_2_DESCRIPTION = "Use of ISO 32000-1"; //$NON-NLS-1$
    final static String ISO_19005_3_DESCRIPTION = "Use of ISO 32000-1 with support for embedded files"; //$NON-NLS-1$
    final static String ISO_32000_DESCRIPTION = "Document management -- Portable document format"; //$NON-NLS-1$
    final static int ISO_19005_1_PART = 1;
    final static int ISO_19005_2_PART = 2;
    final static int ISO_19005_3_PART = 3;
    final static String LEVEL_A_CODE = "a "; //$NON-NLS-1$
    final static String LEVEL_B_CODE = "b "; //$NON-NLS-1$
    final static String LEVEL_U_CODE = "u"; //$NON-NLS-1$
    final static String LEVEL_A_NAME = "accessible"; //$NON-NLS-1$
    final static String LEVEL_B_NAME = "basic"; //$NON-NLS-1$
    final static String LEVEL_U_NAME = "unicode"; //$NON-NLS-1$
    final static String ISO_19005_1_YEAR = "2005"; //$NON-NLS-1$
    final static String ISO_19005_2_YEAR = "2011"; //$NON-NLS-1$
    final static String ISO_19005_3_YEAR = "2012"; //$NON-NLS-1$
    final static String ISO_REFERENCE_SUFFIX = "(E)"; //$NON-NLS-1$
    final static String LEVEL_PREFIX = "Level "; //$NON-NLS-1$

    private PDFAFlavours() {
        throw new AssertionError(
                "Should never enter utility class constructor."); //$NON-NLS-1$
    }

}
