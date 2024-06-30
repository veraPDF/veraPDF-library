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
package org.verapdf.pdfa.validation.profiles;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.List;
import java.util.LinkedList;
import javax.xml.bind.JAXBException;

import org.verapdf.core.Directory;
import org.verapdf.core.MapBackedDirectory;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.flavours.PDFFlavours;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
final class ProfileDirectoryImpl implements ProfileDirectory {
    private static final String PROFILE_RESOURCE_ROOT = "org/verapdf/pdfa/validation/";
    private static final String XML_SUFFIX = ".xml";
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

    @Override
    public List<ValidationProfile> getValidationProfilesByFlavours(List<PDFAFlavour> flavours) {
        List<ValidationProfile> profiles = new LinkedList<>();
        for (PDFAFlavour flavour : flavours) {
            profiles.add(getValidationProfileByFlavour(flavour));
        }
        return profiles;
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
        for (PDFAFlavour flavour : PDFAFlavour.values()) {
            String profilePath = getProfilePath(flavour);
            try (InputStream is = ValidationProfileImpl.class.getClassLoader()
                    .getResourceAsStream(profilePath)) {
                if (is != null)
                    profiles.add(Profiles.profileFromXml(is));
            } catch (JAXBException | IOException e) {
                e.printStackTrace();
            }
        }
        return ProfileDirectoryImpl.fromProfileSet(profiles);
    }
    
    private static String getProfilePath(PDFAFlavour flavour) {
        StringBuilder profilePath = new StringBuilder();
        profilePath.append(PROFILE_RESOURCE_ROOT);

        profilePath.append(flavour.getPart().getFamily().getFamily().replace("/", "")); //$NON-NLS-1$
        if (flavour.getPart().getFamily() != PDFAFlavour.SpecificationFamily.ARLINGTON) {
            profilePath.append("-"); //$NON-NLS-1$
        }
        profilePath.append(flavour.getPart().getPartNumber());
        if (flavour.getPart().getSubpartNumber() != null) {
            profilePath.append("-"); //$NON-NLS-1$
            profilePath.append(flavour.getPart().getSubpartNumber());
        }
        if (PDFFlavours.isWTPDFFlavour(flavour)) {
            profilePath.append("-"); //$NON-NLS-1$
            profilePath.append(flavour.getLevel().getCode());
        } else {
            profilePath.append(flavour.getLevel().getCode().toUpperCase()); //$NON-NLS-1$
        }
        if (PDFFlavours.isFlavour(flavour, PDFAFlavour.PDFUA_2)) {
            profilePath.append("-").append("ISO32005");
        }
        profilePath.append(XML_SUFFIX);
        return profilePath.toString();
    }

}
