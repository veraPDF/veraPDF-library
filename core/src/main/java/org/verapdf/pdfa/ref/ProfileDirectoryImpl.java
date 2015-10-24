/**
 * 
 */
package org.verapdf.pdfa.ref;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.verapdf.pdfa.ProfileDirectory;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.ValidationProfile;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public class ProfileDirectoryImpl implements ProfileDirectory {
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
    public ValidationProfile getValidationProfileById(String profileID)
            throws NoSuchElementException {
        if (profileID == null) throw new IllegalArgumentException("Parameter profileID cannot be null");
        PDFAFlavour flavour = this.flavourMap.get(profileID);
        if (flavour == null) throw new NoSuchElementException("PDFAFlavour " + flavour + " is not supported by this directory.");
        return this.getValidationProfileByFlavour(flavour);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public ValidationProfile getValidationProfileByFlavour(PDFAFlavour flavour)
            throws NoSuchElementException {
        if (flavour == null) throw new IllegalArgumentException("Parameter flavour cannot be null");
        ValidationProfile profile = this.profileMap.get(flavour);
        if (profile == null) throw new NoSuchElementException("PDFAFlavour " + flavour + " is not supported by this directory.");
        return profile;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<ValidationProfile> getValidationProfiles() {
        return Collections.unmodifiableSet(new HashSet<>(this.profileMap.values()));
    }
    
    /**
     * @param profiles a Set of {@link ValidationProfile}s used to populate the directory instance
     * @return a ProfileDirectory populated with the {@link ValidationProfile}s passed in the profiles parameter
     * @throws IllegalArgumentException if the profiles parameter is null or empty
     */
    public static ProfileDirectory createInstance(Set<ValidationProfile> profiles) {
        if (profiles == null) throw new IllegalArgumentException("Parameter profiles cannot be null.");
        if (profiles.isEmpty()) throw new IllegalArgumentException("Parameter profiles cannot be empty.");
        return new ProfileDirectoryImpl(profiles);
    }

    /**
     * Creates a lookup Map of {@link ValidationProfile}s from the passed Set.
     * 
     * @param profileSet
     *            a set of ValidationProfiles
     * @return a Map<PDFAFlavour, ValidationProfile> from the Set of
     *         {@link ValidationProfile}s
     * @throws IllegalArgumentException if the profileSet parameter is null or empty
     */
    public static Map<PDFAFlavour, ValidationProfile> profileMapFromSet(
            Set<ValidationProfile> profileSet) {
        if (profileSet == null) throw new IllegalArgumentException("Parameter profileSet cannot be null.");
        if (profileSet.isEmpty()) throw new IllegalArgumentException("Parameter profileSet cannot be empty.");
        Map<PDFAFlavour, ValidationProfile> profileMap = new HashMap<>();
        for (ValidationProfile profile : profileSet) {
            profileMap.put(profile.getPDFAFlavour(), profile);
        }
        return Collections.unmodifiableMap(profileMap);
    }

    /**
     * Creates a lookup Map of {@link PDFAFlavour}s by String Id from the passed
     * Set.
     * 
     * @param flavours
     *            a Set of {@link PDFAFlavour}s
     * @return a Map created from the passed set where the Map key is the String
     *         ID of the flavour
     * @throws IllegalArgumentException if the flavours parameter is null or empty
     */
    public static Map<String, PDFAFlavour> flavourMapFromSet(
            Set<PDFAFlavour> flavours) {
        if (flavours == null) throw new IllegalArgumentException("Parameter flavours cannot be null.");
        if (flavours.isEmpty()) throw new IllegalArgumentException("Parameter flavours cannot be empty.");
        Map<String, PDFAFlavour> flavourMap = new HashMap<>();
        for (PDFAFlavour flavour : flavours) {
            flavourMap.put(flavour.getId(), flavour);
        }
        return Collections.unmodifiableMap(flavourMap);
    }
}
