package org.verapdf.pdfa.validation;

import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * veraPDF ValidationProfiles encapsulate the validation rules and tests that
 * are enforced by the PDF/A Validator. A validation profile is associated with
 * a particular {@link PDFAFlavour}, that identifies the specific PDF/A
 * specification and conformance level that it is designed to test.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@XmlJavaTypeAdapter(ValidationProfileImpl.Adapter.class)
public interface ValidationProfile {
    /**
     * @return the {@link PDFAFlavour} that identifies the specification part
     *         and conformance level tested by this profile.
     */
    public PDFAFlavour getPDFAFlavour();

    /**
     * @return a human interpretable name for this ValidationProfile
     */
    public String getName();

    /**
     * @return a brief textual description of this ValidationProfile
     */
    public String getDescription();

    /**
     * @return a String that identifies the creator of this ValidationProfile
     */
    public String getCreator();

    /**
     * @return the Date that this ValidationProfile was created
     */
    public Date getDateCreated();

    /**
     * @return a hex-encoded String representation of the SHA-1 digest of this
     *         ValidationProfile
     */
    public String getHexSha1Digest();

    /**
     * @return the full set of Validation {@link Rule}s that are enforce by this
     *         ValidationProfile
     */
    public Set<Rule> getRules();

    /**
     * TODO: A better explanation of Variables and their role.
     * 
     * @return the full set of {@link Variable}s used by this ValidationProfile.
     */
    public Set<Variable> getVariables();
}
