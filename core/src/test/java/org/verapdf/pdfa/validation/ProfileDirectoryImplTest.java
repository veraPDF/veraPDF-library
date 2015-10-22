/**
 * 
 */
package org.verapdf.pdfa.validation;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.verapdf.pdfa.ValidationProfile;
import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class ProfileDirectoryImplTest {
    private static ValidationProfile DEFAULT = Profiles.defaultProfile();
    private static ValidationProfile PDFA_1B = getPDFA1b();
    private static Set<ValidationProfile> DEFAULT_ALONE = getSolitarySet(DEFAULT);
    private static Set<ValidationProfile> PDFA_1B_ALONE = getSolitarySet(PDFA_1B);
    private static Set<ValidationProfile> DUAL_PROFILE = getDualSet();

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.ProfileDirectoryImpl#getValidationProfileIds()}
     * .
     */
    @Test
    public final void testGetValidationProfileIds() {
        ProfileDirectory dir = ProfileDirectoryImpl
                .fromProfileSet(DEFAULT_ALONE);
        assertTrue(dir.getValidationProfileIds().size() == 1);
        dir = ProfileDirectoryImpl.fromProfileSet(PDFA_1B_ALONE);
        assertTrue(dir.getValidationProfileIds().size() == 1);
        dir = ProfileDirectoryImpl.fromProfileSet(DUAL_PROFILE);
        assertTrue(dir.getValidationProfileIds().size() == 2);
        assertTrue(dir.getValidationProfileIds().contains(
                DEFAULT.getPDFAFlavour().getId()));
        assertTrue(dir.getValidationProfileIds().contains(
                PDFA_1B.getPDFAFlavour().getId()));
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.ProfileDirectoryImpl#getPDFAFlavours()}
     * .
     */
    @Test
    public final void testGetPDFAFlavours() {
        ProfileDirectory dir = ProfileDirectoryImpl
                .fromProfileSet(DEFAULT_ALONE);
        assertTrue(dir.getPDFAFlavours().size() == 1);
        dir = ProfileDirectoryImpl.fromProfileSet(PDFA_1B_ALONE);
        assertTrue(dir.getPDFAFlavours().size() == 1);
        dir = ProfileDirectoryImpl.fromProfileSet(DUAL_PROFILE);
        assertTrue(dir.getPDFAFlavours().size() == 2);
        assertTrue(dir.getPDFAFlavours().contains(DEFAULT.getPDFAFlavour()));
        assertTrue(dir.getPDFAFlavours().contains(PDFA_1B.getPDFAFlavour()));
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.ProfileDirectoryImpl#getValidationProfileById(java.lang.String)}
     * .
     */
    @Test
    public final void testGetValidationProfileById() {
        ProfileDirectory dir = ProfileDirectoryImpl
                .fromProfileSet(DEFAULT_ALONE);
        assertTrue(dir.getValidationProfileById(
                DEFAULT.getPDFAFlavour().getId()).getPDFAFlavour() == PDFAFlavour.NO_FLAVOUR);
        dir = ProfileDirectoryImpl.fromProfileSet(PDFA_1B_ALONE);
        assertTrue(dir.getValidationProfileById(
                PDFA_1B.getPDFAFlavour().getId()).getPDFAFlavour() == PDFAFlavour.PDFA_1_B);
        dir = ProfileDirectoryImpl.fromProfileSet(DUAL_PROFILE);
        assertTrue(dir.getValidationProfileById(
                DEFAULT.getPDFAFlavour().getId()).getPDFAFlavour() == PDFAFlavour.NO_FLAVOUR);
        assertTrue(dir.getValidationProfileById(
                PDFA_1B.getPDFAFlavour().getId()).getPDFAFlavour() == PDFAFlavour.PDFA_1_B);
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.ProfileDirectoryImpl#getValidationProfileById(java.lang.String)}
     * .
     */
    @Test(expected = NoSuchElementException.class)
    public final void testGetValidationProfileByIdNoElement() {
        ProfileDirectory dir = ProfileDirectoryImpl
                .fromProfileSet(DEFAULT_ALONE);
        dir.getValidationProfileById(PDFAFlavour.PDFA_2_A.getId());
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.ProfileDirectoryImpl#getValidationProfileByFlavour(org.verapdf.pdfa.flavours.PDFAFlavour)}
     * .
     */
    @Test
    public final void testGetValidationProfileByFlavour() {
        ProfileDirectory dir = ProfileDirectoryImpl
                .fromProfileSet(DEFAULT_ALONE);
        assertTrue(dir.getValidationProfileByFlavour(DEFAULT.getPDFAFlavour())
                .getPDFAFlavour() == PDFAFlavour.NO_FLAVOUR);
        dir = ProfileDirectoryImpl.fromProfileSet(PDFA_1B_ALONE);
        assertTrue(dir.getValidationProfileByFlavour(PDFA_1B.getPDFAFlavour())
                .getPDFAFlavour() == PDFAFlavour.PDFA_1_B);
        dir = ProfileDirectoryImpl.fromProfileSet(DUAL_PROFILE);
        assertTrue(dir.getValidationProfileByFlavour(DEFAULT.getPDFAFlavour())
                .getPDFAFlavour() == PDFAFlavour.NO_FLAVOUR);
        assertTrue(dir.getValidationProfileByFlavour(PDFA_1B.getPDFAFlavour())
                .getPDFAFlavour() == PDFAFlavour.PDFA_1_B);
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.ProfileDirectoryImpl#getValidationProfileByFlavour(org.verapdf.pdfa.flavours.PDFAFlavour)}
     * .
     */
    @Test(expected = NoSuchElementException.class)
    public final void testGetValidationProfileByFlavourNoElement() {
        ProfileDirectory dir = ProfileDirectoryImpl
                .fromProfileSet(DEFAULT_ALONE);
        dir.getValidationProfileByFlavour(PDFAFlavour.PDFA_1_A);
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.ProfileDirectoryImpl#getValidationProfiles()}
     * .
     */
    @Test
    public final void testGetValidationProfiles() {
        ProfileDirectory dir = ProfileDirectoryImpl
                .fromProfileSet(DEFAULT_ALONE);
        assertTrue(dir.getValidationProfiles().contains(DEFAULT));
        dir = ProfileDirectoryImpl.fromProfileSet(PDFA_1B_ALONE);
        assertTrue(dir.getValidationProfiles().contains(PDFA_1B));
        dir = ProfileDirectoryImpl.fromProfileSet(DUAL_PROFILE);
        assertTrue(dir.getValidationProfiles().contains(DEFAULT));
        assertTrue(dir.getValidationProfiles().contains(PDFA_1B));
    }

    private static ValidationProfile getPDFA1b() {
        try (InputStream is = ValidationProfileImpl.class.getClassLoader()
                .getResourceAsStream("org/verapdf/pdfa/validation/pdfa-1b.xml")) {
            return ValidationProfileImpl.fromXml(is);
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
