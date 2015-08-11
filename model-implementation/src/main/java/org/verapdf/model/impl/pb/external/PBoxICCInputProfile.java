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

    public static final String ICC_INPUT_PROFILE_TYPE = "ICCInputProfile";

    public PBoxICCInputProfile(InputStream profile,
            Long dictionaryNumberOfColors) throws IOException {
        super(profile, dictionaryNumberOfColors, ICC_INPUT_PROFILE_TYPE);
    }
}
