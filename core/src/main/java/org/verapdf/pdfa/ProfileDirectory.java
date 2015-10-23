package org.verapdf.pdfa;

import java.util.NoSuchElementException;
import java.util.Set;

import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * A ProfileDirectory provides access to a set of {@link ValidationProfile}s
 * that can be retrieved by String id or {@link PDFAFlavour}.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public interface ProfileDirectory {
    /**
     * @return the Set of ValidationProfile String identifiers for the profiles
     *         held in the directory.
     */
    public Set<String> getValidationProfileIds();

    /**
     * @return the Set of {@link PDFAFlavour} enum instances that identify the
     *         profiles held in the directory.
     */
    public Set<PDFAFlavour> getPDFAFlavours();

    /**
     * @param profileID
     *            a two character String that uniquely identifies a particular
     *            {@link PDFAFlavour}, e.g. 1a, 1b, 2a, etc.
     * @return the {@link ValidationProfile} associated with the profileId
     * @throws NoSuchElementException
     *             when there is no profile associated with the profileID string
     *             IllegalArgumentException if the profileID parameter is null
     * @throws IllegalArgumentException if profileID is null
     */
    public ValidationProfile getValidationProfileById(String profileID)
            throws NoSuchElementException;

    /**
     * @param flavour
     *            a {@link PDFAFlavour} instance that identifies a
     *            {@link ValidationProfile}
     * @return the {@link ValidationProfile} associated with the flavour
     * @throws NoSuchElementException
     *             when there is no profile associated with the flavour
     *             IllegalArgumentException if the flavour parameter is null
     * @throws IllegalArgumentException if flavour is null
     */
    public ValidationProfile getValidationProfileByFlavour(PDFAFlavour flavour)
            throws NoSuchElementException;

    /**
     * @return the full set of {@link ValidationProfile}s held in the directory.
     */
    public Set<ValidationProfile> getValidationProfiles();
}
