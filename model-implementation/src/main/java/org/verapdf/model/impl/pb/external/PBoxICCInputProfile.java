package org.verapdf.model.impl.pb.external;

import org.verapdf.model.external.ICCInputProfile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Embedded ICC profile used for ICCBased color spaces
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxICCInputProfile extends PBoxICCProfile implements
        ICCInputProfile {

	/** Type name for {@code PBoxICCInputProfile} */
    public static final String ICC_INPUT_PROFILE_TYPE = "ICCInputProfile";

	/**
	 * Default constructor.
	 *
	 * @param profile stream of profile
	 * @param dictionaryNumberOfColors number of colors defined in stream dictionary
	 * @throws IOException
	 */
    public PBoxICCInputProfile(InputStream profile,
            Long dictionaryNumberOfColors) throws IOException {
        super(profile, dictionaryNumberOfColors, ICC_INPUT_PROFILE_TYPE);
    }
}
