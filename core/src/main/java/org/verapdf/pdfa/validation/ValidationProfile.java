package org.verapdf.pdfa.validation;

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
     * @return the {@link ProfileDetails} for this profile.
     */
    public ProfileDetails getDetails();

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
     * @param id
     *            the {@link RuleId} of the {@link Rule} to be retrieved.
     * @return the {@link Rule} associated with this id
     */
    public Rule getRuleByRuleId(final RuleId id);

    /**
     * Retrieve the complete Set of validation {@link Rule}s associated with a
     * PDF object
     * 
     * @param objectName
     *            the String name identifier for the object
     * @return the full set of Validation {@link Rule}s that are associated with
     *         object name
     */
    public Set<Rule> getRulesByObject(final String objectName);

    /**
     * TODO: A better explanation of Variables and their role.
     * 
     * @return the full set of {@link Variable}s used by this ValidationProfile.
     */
    public Set<Variable> getVariables();

    /**
     * TODO: A better explanation of Variables and their role.
     * 
     * @param objectName
     * 
     * @return the full set of {@link Variable}s that are associated with object
     *         name.
     */
    public Set<Variable> getVariablesByObject(final String objectName);
}
