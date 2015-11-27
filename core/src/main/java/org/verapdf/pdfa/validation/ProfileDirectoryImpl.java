/**
 * 
 */
package org.verapdf.pdfa.validation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.verapdf.core.Directory;
import org.verapdf.core.MapBackedDirectory;
import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
final class ProfileDirectoryImpl implements ProfileDirectory {
    private final static String PROFILE_RESOURCE_ROOT = "org/verapdf/pdfa/validation/pdfa-";
    private final static String XML_SUFFIX = ".xml";
    private static final ProfileDirectoryImpl DEFAULT = makeVeraProfileDir();

    private final Directory<PDFAFlavour, ValidationProfile> profileDir;
    private final Directory<String, PDFAFlavour> flavourDir;

    private ProfileDirectoryImpl(Set<ValidationProfile> profileSet) {
        this(ProfileDirectoryImpl.profileMapFromSet(profileSet));
    }

    private ProfileDirectoryImpl(Map<PDFAFlavour, ValidationProfile> profileMap) {
        this.profileDir = new MapBackedDirectory<>(profileMap);
        this.flavourDir = new MapBackedDirectory<>(
                ProfileDirectoryImpl.flavourMapFromSet(profileMap.keySet()));
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<String> getValidationProfileIds() {
        return Collections.unmodifiableSet(this.flavourDir.getKeys());
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<PDFAFlavour> getPDFAFlavours() {
        return this.profileDir.getKeys();
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public ValidationProfile getValidationProfileById(String profileID) {
        if (profileID == null)
            throw new IllegalArgumentException(
                    "Parameter profileID cannot be null");
        PDFAFlavour flavour = this.flavourDir.getItem(profileID);
        if (flavour == null)
            throw new NoSuchElementException("PDFAFlavour " + flavour
                    + " is not supported by this directory.");
        return this.getValidationProfileByFlavour(flavour);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public ValidationProfile getValidationProfileByFlavour(PDFAFlavour flavour) {
        if (flavour == null)
            throw new IllegalArgumentException(
                    "Parameter flavour cannot be null");
        ValidationProfile profile = this.profileDir.getItem(flavour);
        if (profile == null)
            throw new NoSuchElementException("PDFAFlavour " + flavour
                    + " is not supported by this directory.");
        return profile;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<ValidationProfile> getValidationProfiles() {
        return Collections.unmodifiableSet(new HashSet<>(this.profileDir
                .getItems()));
    }

    static ProfileDirectoryImpl getVeraProfileDirectory() {
        return DEFAULT;
    }

    static ProfileDirectoryImpl fromProfileSet(Set<ValidationProfile> profiles) {
        return new ProfileDirectoryImpl(profiles);
    }

    static Map<PDFAFlavour, ValidationProfile> profileMapFromSet(
            Set<ValidationProfile> profileSet) {
        Map<PDFAFlavour, ValidationProfile> profileMap = new HashMap<>();
        for (ValidationProfile profile : profileSet) {
            profileMap.put(profile.getPDFAFlavour(), profile);
        }
        return Collections.unmodifiableMap(profileMap);
    }

    static Map<String, PDFAFlavour> flavourMapFromSet(Set<PDFAFlavour> flavours) {
        Map<String, PDFAFlavour> flavourMap = new HashMap<>();
        for (PDFAFlavour flavour : flavours) {
            flavourMap.put(flavour.getId(), flavour);
        }
        return Collections.unmodifiableMap(flavourMap);
    }

    private static ProfileDirectoryImpl makeVeraProfileDir() {
        Set<ValidationProfile> profiles = new HashSet<>();
        Set<ValidationProfile> profileSet = new HashSet<>();
        for (PDFAFlavour flavour : PDFAFlavour.values()) {
            String profilePath = PROFILE_RESOURCE_ROOT + flavour.getId()
                    + XML_SUFFIX;
            try (InputStream is = ValidationProfileImpl.class.getClassLoader()
                    .getResourceAsStream(profilePath)) {
                if (is != null)
                    profileSet.add(ValidationProfileImpl.fromXml(is));
            } catch (JAXBException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return ProfileDirectoryImpl.fromProfileSet(profiles);
    }

}
