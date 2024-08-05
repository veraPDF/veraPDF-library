/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.pdfa.validation.profiles;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class ProfileDirectoryImplTest {
    private static final ValidationProfile DEFAULT = Profiles.defaultProfile();
    private static final ValidationProfile PDFA_1B = getPDFA1b();
    private static final Set<ValidationProfile> DEFAULT_ALONE = getSolitarySet(DEFAULT);
    private static final Set<ValidationProfile> PDFA_1B_ALONE = getSolitarySet(PDFA_1B);
    private static final Set<ValidationProfile> DUAL_PROFILE = getDualSet();

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.profiles.ProfileDirectoryImpl#getValidationProfileIds()}
     * .
     */
    @Test
    public final void testGetValidationProfileIds() {
        ProfileDirectory dir = Profiles
                .directoryFromProfiles(DEFAULT_ALONE);
        assertEquals(1, dir.getValidationProfileIds().size());
        dir = Profiles.directoryFromProfiles(PDFA_1B_ALONE);
        assertEquals(1, dir.getValidationProfileIds().size());
        dir = Profiles.directoryFromProfiles(DUAL_PROFILE);
        assertEquals(2, dir.getValidationProfileIds().size());
        assertTrue(dir.getValidationProfileIds().contains(
                DEFAULT.getPDFAFlavour().getId()));
        assertTrue(dir.getValidationProfileIds().contains(
                PDFA_1B.getPDFAFlavour().getId()));
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.profiles.ProfileDirectoryImpl#getPDFAFlavours()}
     * .
     */
    @Test
    public final void testGetPDFAFlavours() {
        ProfileDirectory dir = Profiles.directoryFromProfiles(DEFAULT_ALONE);
        assertEquals(1, dir.getPDFAFlavours().size());
        dir = Profiles.directoryFromProfiles(PDFA_1B_ALONE);
        assertEquals(1, dir.getPDFAFlavours().size());
        dir = Profiles.directoryFromProfiles(DUAL_PROFILE);
        assertEquals(2, dir.getPDFAFlavours().size());
        assertTrue(dir.getPDFAFlavours().contains(DEFAULT.getPDFAFlavour()));
        assertTrue(dir.getPDFAFlavours().contains(PDFA_1B.getPDFAFlavour()));
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.profiles.ProfileDirectoryImpl#getValidationProfileById(java.lang.String)}
     * .
     */
    @Test
    public final void testGetValidationProfileById() {
        ProfileDirectory dir = Profiles.directoryFromProfiles(DEFAULT_ALONE);
        assertSame(dir.getValidationProfileById(
                DEFAULT.getPDFAFlavour().getId()).getPDFAFlavour(), PDFAFlavour.NO_FLAVOUR);
        dir = Profiles.directoryFromProfiles(PDFA_1B_ALONE);
        assertSame(dir.getValidationProfileById(
                PDFA_1B.getPDFAFlavour().getId()).getPDFAFlavour(), PDFAFlavour.PDFA_1_B);
        dir = Profiles.directoryFromProfiles(DUAL_PROFILE);
        assertSame(dir.getValidationProfileById(
                DEFAULT.getPDFAFlavour().getId()).getPDFAFlavour(), PDFAFlavour.NO_FLAVOUR);
        assertSame(dir.getValidationProfileById(
                PDFA_1B.getPDFAFlavour().getId()).getPDFAFlavour(), PDFAFlavour.PDFA_1_B);
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.profiles.ProfileDirectoryImpl#getValidationProfileById(java.lang.String)}
     * .
     */
    @Test(expected = NoSuchElementException.class)
    public final void testGetValidationProfileByIdNoElement() {
        ProfileDirectory dir = Profiles.directoryFromProfiles(DEFAULT_ALONE);
        dir.getValidationProfileById(PDFAFlavour.PDFA_2_A.getId());
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.profiles.ProfileDirectoryImpl#getValidationProfileByFlavour(org.verapdf.pdfa.flavours.PDFAFlavour)}
     * .
     */
    @Test
    public final void testGetValidationProfileByFlavour() {
        ProfileDirectory dir = Profiles.directoryFromProfiles(DEFAULT_ALONE);
        assertSame(dir.getValidationProfileByFlavour(DEFAULT.getPDFAFlavour())
                .getPDFAFlavour(), PDFAFlavour.NO_FLAVOUR);
        dir = Profiles.directoryFromProfiles(PDFA_1B_ALONE);
        assertSame(dir.getValidationProfileByFlavour(PDFA_1B.getPDFAFlavour())
                .getPDFAFlavour(), PDFAFlavour.PDFA_1_B);
        dir = Profiles.directoryFromProfiles(DUAL_PROFILE);
        assertSame(dir.getValidationProfileByFlavour(DEFAULT.getPDFAFlavour())
                .getPDFAFlavour(), PDFAFlavour.NO_FLAVOUR);
        assertSame(dir.getValidationProfileByFlavour(PDFA_1B.getPDFAFlavour())
                .getPDFAFlavour(), PDFAFlavour.PDFA_1_B);
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.profiles.ProfileDirectoryImpl#getValidationProfileByFlavour(org.verapdf.pdfa.flavours.PDFAFlavour)}
     * .
     */
    @Test(expected = NoSuchElementException.class)
    public final void testGetValidationProfileByFlavourNoElement() {
        ProfileDirectory dir = Profiles.directoryFromProfiles(DEFAULT_ALONE);
        dir.getValidationProfileByFlavour(PDFAFlavour.PDFA_1_A);
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.profiles.ProfileDirectoryImpl#getValidationProfiles()}
     * .
     */
    @Test
    public final void testGetValidationProfiles() {
        ProfileDirectory dir = Profiles.directoryFromProfiles(DEFAULT_ALONE);
        assertTrue(dir.getValidationProfiles().contains(DEFAULT));
        dir = Profiles.directoryFromProfiles(PDFA_1B_ALONE);
        assertTrue(dir.getValidationProfiles().contains(PDFA_1B));
        dir = Profiles.directoryFromProfiles(DUAL_PROFILE);
        assertTrue(dir.getValidationProfiles().contains(DEFAULT));
        assertTrue(dir.getValidationProfiles().contains(PDFA_1B));
    }

    private static ValidationProfile getPDFA1b() {
        try (InputStream is = ValidationProfileImpl.class.getClassLoader()
                .getResourceAsStream("org/verapdf/pdfa/validation/PDFA-1B.xml")) {
            return Profiles.profileFromXml(is);
        } catch (JAXBException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Set<ValidationProfile> getSolitarySet(
            ValidationProfile profile) {
        Set<ValidationProfile> profiles = new HashSet<>();
        profiles.add(profile);
        return profiles;
    }

    private static Set<ValidationProfile> getDualSet() {
        Set<ValidationProfile> profiles = new HashSet<>();
        profiles.add(DEFAULT);
        profiles.add(PDFA_1B);
        return profiles;
    }
}
