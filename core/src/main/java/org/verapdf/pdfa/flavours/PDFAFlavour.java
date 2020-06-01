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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Enums used as id for PDF/A flavours where a flavour uniquely identifies a
 * specific PDF/A Standard part and associated conformance level.
 *
 * The PDF/A Specification:<br/>
 * ISO 19005 - Document Management - Electronic document format for long-term
 * preservation (PDF/A)<br/>
 * comprises 4 parts:
 * <ol>
 * <li>Part 1: PDF/A-1 - Use of PDF 1.4</li>
 * <li>Part 2: PDF/A-2 - Use of ISO 32000-1</li>
 * <li>Part 3: PDF/A-3 - Use of ISO 32000-1 with support for embedded files</li>
 * <li>Part 4: PDF/A-4 - Use of ISO 32000-2</li>
 * <ol>
 * Note that "Use of ISO 32000-1" indicates that PDF/A parts 2 and 3 are based
 * upon PDF 1.7. ISO 32000-1 is the code for the PDF 1.7 ISO standard. The
 * specification parts specify different conformance levels:
 * <ul>
 * <li>Level b - basic</li>
 * <li>Level a - accessible</li>
 * <li>Level u - unicode</li>
 * </ul>
 * Part 1 does not allow a conformance level u (Unicode) so there are eight
 * valid combinations of specification part and level, shown below:
 * <ul>
 * <li>1a</li>
 * <li>1b</li>
 * <li>2a</li>
 * <li>2b</li>
 * <li>2u</li>
 * <li>3a</li>
 * <li>3b</li>
 * <li>3u</li>
 * <li>4</li>
 * </ul>
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public enum PDFAFlavour {
    /** Special ID for the none case */
    NO_FLAVOUR(Specification.NO_STANDARD, Level.NO_LEVEL),
    /** 1a PDF Version 1 Level A */
    PDFA_1_A(Specification.ISO_19005_1, Level.A),
    /** 1b PDF Version 1 Level B */
    PDFA_1_B(Specification.ISO_19005_1, Level.B),
    /** 2a PDF Version 2 Level A */
    PDFA_2_A(Specification.ISO_19005_2, Level.A),
    /** 2b PDF Version 2 Level B */
    PDFA_2_B(Specification.ISO_19005_2, Level.B),
    /** 2u PDF Version 2 Level U */
    PDFA_2_U(Specification.ISO_19005_2, Level.U),
    /** 3a PDF Version 3 Level A */
    PDFA_3_A(Specification.ISO_19005_3, Level.A),
    /** 3b PDF Version 3 Level B */
    PDFA_3_B(Specification.ISO_19005_3, Level.B),
    /** 3u PDF Version 3 Level U */
    PDFA_3_U(Specification.ISO_19005_3, Level.U),
    /** 4 PDF Version 4 */
    PDFA_4(Specification.ISO_19005_4, Level.NO_LEVEL),
    /** ua1 PDF Version 1 */
    PDFUA_1(Specification.ISO_14289_1, Level.NO_LEVEL);

    private final static Map<String, PDFAFlavour> FLAVOUR_LOOKUP = new HashMap<>();
    static {
        for (PDFAFlavour flavour : PDFAFlavour.values()) {
            FLAVOUR_LOOKUP.put(flavour.id, flavour);
        }
    }

    private final Specification part;
    private final Level level;
    private final String id;

    private PDFAFlavour(final Specification standard, final Level level) {
        this((PDFAFlavours.PDFUA.equals(standard.family) ? PDFAFlavours.PDFUA_PREFIX : "") + standard.getPartNumber() + level.getCode(), standard, level);
    }

    private PDFAFlavour(final String id, final Specification standard, final Level level) {
        this.part = standard;
        this.level = level;
        this.id = id;
    }

    /**
     * @return an {@link Specification} instance that identifies the
     *         Specification Part
     */
    public final Specification getPart() {
        return this.part;
    }

    /**
     * @return the {@link Level} instance for this flavour
     */
    public final Level getLevel() {
        return this.level;
    }

    /**
     * @return the two character String id for this flavour, e.g. 1a, 1b, 2a,
     *         etc.
     */
    public final String getId() {
        return this.id;
    }

    @Override
    public final String toString() {
        return this.id;
    }
    /**
     * Enumeration of PDF/A Specification Parts, 1-3 used to provide ids and a
     * standard source of details such as name and description for PDF/A
     * Specifications.
     */
    public enum Specification {
        /** PDF/A Version 1 */
        NO_STANDARD(IsoStandardSeries.NO_SERIES, PDFAFlavours.NONE, PDFAFlavours.NONE_ID,
                PDFAFlavours.NONE, PDFAFlavours.NONE),
        /** PDF/A Version 1 */
        ISO_19005_1(IsoStandardSeries.ISO_19005, PDFAFlavours.PDFA, PDFAFlavours.ISO_19005_1_PART,
                PDFAFlavours.ISO_19005_1_YEAR,
                PDFAFlavours.ISO_19005_1_DESCRIPTION),
        /** PDF/A Version 2 */
        ISO_19005_2(IsoStandardSeries.ISO_19005, PDFAFlavours.PDFA, PDFAFlavours.ISO_19005_2_PART,
                PDFAFlavours.ISO_19005_2_YEAR,
                PDFAFlavours.ISO_19005_2_DESCRIPTION),
        /** PDF/A Version 3 */
        ISO_19005_3(IsoStandardSeries.ISO_19005, PDFAFlavours.PDFA, PDFAFlavours.ISO_19005_3_PART,
                PDFAFlavours.ISO_19005_3_YEAR,
                PDFAFlavours.ISO_19005_3_DESCRIPTION),
        /** PDF/A Version 4 */
        ISO_19005_4(IsoStandardSeries.ISO_19005, PDFAFlavours.PDFA, PDFAFlavours.ISO_19005_4_PART,
                    PDFAFlavours.ISO_19005_4_YEAR,
                    PDFAFlavours.ISO_19005_4_DESCRIPTION),
        /** PDF/UA Version 1 */
        ISO_14289_1(IsoStandardSeries.ISO_14289, PDFAFlavours.PDFUA, PDFAFlavours.ISO_14289_1_PART,
                    PDFAFlavours.ISO_14289_1_YEAR,
                    PDFAFlavours.ISO_14289_1_DESCRIPTION),
        /** WCAG Version 2.1 */
        WCAG_2_1(IsoStandardSeries.NO_SERIES, PDFAFlavours.PDFUA, PDFAFlavours.WCAG_2_1_PART,
                PDFAFlavours.WCAG_2_1_YEAR, PDFAFlavours.WCAG_2_1_DESCRIPTION);

        private final IsoStandardSeries series;
        private final int partNumber;
        private final String id;
        private final String year;
        private final String family;
        private final String name;
        private final String description;

        Specification(final IsoStandardSeries series, final String family, final int partNumber,
                final String year, final String description) {
            this.series = series;
            this.partNumber = partNumber;
            this.year = year;
            this.description = description;
            this.family = family;
            this.name = family + "-" + this.getPartNumber(); //$NON-NLS-1$
            if (!PDFAFlavours.WCAG_2_1_DESCRIPTION.equals(description)) {
                this.id = this.series.getName()
                        + "-" + this.getPartNumber() + ":" + this.getYear(); //$NON-NLS-1$//$NON-NLS-2$
            } else {
                this.id = PDFAFlavours.WCAG_2_1;
            }
        }

        /**
         * @return the part number as an int
         */
        public final int getPartNumber() {
            return this.partNumber;
        }

        /**
         * @return the standard part id as a String
         */
        public final String getId() {
            return this.id;
        }

        /**
         * @return the year for the standard part
         */
        public final String getYear() {
            return this.year;
        }

        /**
         * @return the name for the standard part
         */
        public final String getName() {
            return this.name;
        }

        /**
         * @return the family for the standard part
         */
        public final String getFamily() {
            return this.family;
        }

        /**
         * @return the description
         */
        public String getDescription() {
            return this.description;
        }

        /**
         * @return the {@link IsoStandardSeries} instance indicating the
         *         standard series
         */
        public IsoStandardSeries getSeries() {
            return this.series;
        }

        @Override
        public String toString() {
            return this.id;
        }
    }

    /**
     * Enum type that identifies the different PDF/A Conformance Levels A
     * (accessible), B (basic) & U (unicode).
     *
     */
    public enum Level {
        /** Special identifier for the none case */
        NO_LEVEL(PDFAFlavours.NONE, PDFAFlavours.NONE),
        /** Level A */
        A(PDFAFlavours.LEVEL_A_CODE, PDFAFlavours.LEVEL_A_NAME),
        /** Level B */
        B(PDFAFlavours.LEVEL_B_CODE, PDFAFlavours.LEVEL_B_NAME),
        /** Level U */
        U(PDFAFlavours.LEVEL_U_CODE, PDFAFlavours.LEVEL_U_NAME);

        private final String code;
        private final String name;

        Level(final String code, final String name) {
            this.code = code;
            this.name = name;
        }

        /**
         * @return the PDF/A Level code ("a", "b", or "u")
         */
        public final String getCode() {
            return this.code;
        }

        /**
         * @return the full name of the conformance level
         */
        public final String getName() {
            return this.name;
        }
    }

    /**
     * Enum to for ISO standard identifiers
     */
    public enum IsoStandardSeries {
        /** Special identifier for the none case */
        NO_SERIES(PDFAFlavours.NONE_ID, PDFAFlavours.NONE),
        /** Identifier for PDF/A ISO Standard */
        ISO_19005(PDFAFlavours.ISO_19005_ID, PDFAFlavours.ISO_19005_DESCRIPTION),
        /** Identifier for PDF 1.7 ISO Standard */
        ISO_32000(PDFAFlavours.ISO_32000_ID, PDFAFlavours.ISO_32000_DESCRIPTION),

        ISO_14289(PDFAFlavours.ISO_14289_ID, PDFAFlavours.ISO_14289_DESCRIPTION);

        private final int id;
        private final String name;
        private final String description;

        IsoStandardSeries(final int id, final String description) {
            this.id = id;
            this.name = PDFAFlavours.ISO_PREFIX + this.getId();
            this.description = description;
        }

        /**
         * @return the id
         */
        public int getId() {
            return this.id;
        }

        /**
         * @return the name
         */
        public String getName() {
            return this.name;
        }

        /**
         * @return the description
         */
        public String getDescription() {
            return this.description;
        }

        @Override
        public String toString() {
            return this.getName() + " " + this.getDescription(); //$NON-NLS-1$
        }
    }

    /**
     * Looks up a {@link PDFAFlavour} by two letter flavour identifier. The
     * identifier is a two letter String that identifies a {@link PDFAFlavour},
     * e.g. 1a, 1b, 2a, 2b, 2u, 3a, 3b, 3u, 4. The match is case insensitive so 1A,
     * 1B, etc. are also valid flavour ids.
     *
     * @param flavourId
     *            must be a two character string that exactly matches the
     *            flavour identifier.
     *
     * @return the correct {@link PDFAFlavour} looked up by String id or
     *         {@link PDFAFlavour#NO_FLAVOUR} if id does not match a flavour
     */
    public static PDFAFlavour byFlavourId(final String flavourId) {
        PDFAFlavour flavour = FLAVOUR_LOOKUP.get(flavourId.toLowerCase());
        if (flavour == null) {
            flavour = PDFAFlavour.NO_FLAVOUR;
        }
        return flavour;
    }

    /**
     * Performs a lenient parse of the String <code>toParse</code> to determine
     * whether it contains a valid {@link PDFAFlavour} identifier. Be aware that
     * the identifiers are only 2 character Strings (see
     * {@link PDFAFlavour#byFlavourId(String)} so unintended matches are
     * possible.
     *
     * @param toParse
     *            a String parameter that is parsed to see whether it contains a
     *            PDFAFlavour identifier.
     * @return the correct {@link PDFAFlavour} looked up by parsing
     *         <code>toParse</code> or {@link PDFAFlavour#NO_FLAVOUR} if no
     *         matching flavour was found.
     */
    public static PDFAFlavour fromString(String toParse) {
        for (String id : getFlavourIds()) {
            if (toParse.toLowerCase().contains(id)) {
                return byFlavourId(id);
            }
        }
        return PDFAFlavour.NO_FLAVOUR;
    }

    /**
     * @return the Set of PDFA Flavour String ids
     */
    public static Set<String> getFlavourIds() {
        return FLAVOUR_LOOKUP.keySet();
    }
}
