/**
 * 
 */
package org.verapdf.pdfa.validation;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.verapdf.pdfa.ValidationProfile;
import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
final class ProfileDirectoryImpl implements ProfileDirectory {
    private final Map<PDFAFlavour, ValidationProfile> profileMap;
    private final Map<String, PDFAFlavour> flavourMap;

    private ProfileDirectoryImpl(Set<ValidationProfile> profileSet) {
        this(ProfileDirectoryImpl.profileMapFromSet(profileSet));
    }

    private ProfileDirectoryImpl(Map<PDFAFlavour, ValidationProfile> profileMap) {
        this.profileMap = Collections.unmodifiableMap(profileMap);
        this.flavourMap = ProfileDirectoryImpl.flavourMapFromSet(profileMap
                .keySet());
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<String> getValidationProfileIds() {
        return Collections.unmodifiableSet(this.flavourMap.keySet());
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<PDFAFlavour> getPDFAFlavours() {
        return Collections.unmodifiableSet(this.profileMap.keySet());
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public ValidationProfile getValidationProfileById(String profileID) {
        if (profileID == null)
            throw new IllegalArgumentException(
                    "Parameter profileID cannot be null");
        PDFAFlavour flavour = this.flavourMap.get(profileID);
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
        ValidationProfile profile = this.profileMap.get(flavour);
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
        return Collections.unmodifiableSet(new HashSet<>(this.profileMap
                .values()));
    }

    static ProfileDirectory fromProfileSet(
            Set<ValidationProfile> profiles) {
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

    static Map<String, PDFAFlavour> flavourMapFromSet(
            Set<PDFAFlavour> flavours) {
        Map<String, PDFAFlavour> flavourMap = new HashMap<>();
        for (PDFAFlavour flavour : flavours) {
            flavourMap.put(flavour.getId(), flavour);
        }
        return Collections.unmodifiableMap(flavourMap);
    }
}
