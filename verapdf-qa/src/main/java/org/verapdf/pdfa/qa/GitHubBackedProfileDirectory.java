/**
 * 
 */
package org.verapdf.pdfa.qa;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.verapdf.core.ProfileException;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.ProfileDirectory;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.validation.profile.parser.LegacyProfileConverter;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public enum GitHubBackedProfileDirectory implements ProfileDirectory {
    /**
     * Integration branch profiles
     */
    INTEGRATION("integration");
    /**
     * This would work for Master branch profiles
     * 
     * MASTER("master");
     */
    private static final String GITHUB_ROOT = "https://raw.githubusercontent.com/veraPDF/veraPDF-validation-profiles/";
    private static final String PROFILE_PATH_PART = "/PDF_A/";
    private static final String PROFILE_PREFIX = "PDFA-";
    private static final String XML_SUFFIX = ".xml";
    private final String branchName;
    private final ProfileDirectory profiles;

    GitHubBackedProfileDirectory(final String branchName) {
        this.branchName = branchName;
        this.profiles = Profiles
                .directoryFromProfiles(fromGitHubBranch(this.branchName));
    }

    /**
     * @return the GitHub branch name that the instance is populated from
     */
    public String getBranchName() {
        return this.branchName;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<String> getValidationProfileIds() {
        return this.profiles.getValidationProfileIds();
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<PDFAFlavour> getPDFAFlavours() {
        return this.profiles.getPDFAFlavours();
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public ValidationProfile getValidationProfileById(final String profileID) {
        return this.profiles.getValidationProfileById(profileID);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public ValidationProfile getValidationProfileByFlavour(
            final PDFAFlavour flavour) {
        return this.profiles.getValidationProfileByFlavour(flavour);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<ValidationProfile> getValidationProfiles() {
        return this.profiles.getValidationProfiles();
    }

    private static Set<ValidationProfile> fromGitHubBranch(
            final String branchName) {
        String pathPrefix = GITHUB_ROOT + branchName + PROFILE_PATH_PART
                + PROFILE_PREFIX;
        Set<ValidationProfile> profileSet = new HashSet<>();
        for (PDFAFlavour flavour : PDFAFlavour.values()) {
            String profileURLString = pathPrefix
                    + flavour.getId().toUpperCase() + XML_SUFFIX;
            try {
                URL profileURL = new URL(profileURLString);
                ValidationProfile profile = LegacyProfileConverter
                        .fromLegacyStream(profileURL.openStream());
                profileSet.add(profile);
            } catch (ProfileException | IOException e) {
                // Do nothing
            }
        }
        return profileSet;
    }
    
    /**
     * Simple main that simply outputs all loaded from INTEGRATION to System.out.
     * @param args main args, NOT processed.
     * @throws JAXBException when converting the profile to XML goes wrong. It shouldn't.
     */
    public static void main(final String[] args) throws JAXBException {
        for (ValidationProfile profile : INTEGRATION.getValidationProfiles()) {
            Profiles.profileToXml(profile, System.out, Boolean.TRUE);
        }
    }
}
