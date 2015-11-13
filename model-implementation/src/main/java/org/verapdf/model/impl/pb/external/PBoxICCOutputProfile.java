package org.verapdf.model.impl.pb.external;

import org.verapdf.model.external.ICCOutputProfile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Embedded ICC profile used as a destination profile in the output intent
 * dictionary
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxICCOutputProfile extends PBoxICCProfile implements
        ICCOutputProfile {

	/**	Type name for {@code PBoxICCOutputProfile} */
    public static final String ICC_OUTPUT_PROFILE_TYPE = "ICCOutputProfile";

    private String subtype;

	/**
	 * Default constructor
	 *
	 * @param profile icc profile stream
	 * @param subtype subtype value for current profile
	 * @param N number of colors defined in stream dictionary
	 * @throws IOException
	 */
    public PBoxICCOutputProfile(InputStream profile, String subtype, Long N)
            throws IOException {
        super(profile, N, ICC_OUTPUT_PROFILE_TYPE);
        this.subtype = subtype;
    }

    /**
     * @return subtype of output intent, which use current ICC profile
     */
    @Override
    public String getS() {
        return this.subtype;
    }
}
