package org.verapdf.model.impl.pb.external;

import org.verapdf.model.external.ICCInputProfile;

/**
 * Embedded ICC profile used for ICCBasec color spaces
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxICCInputProfile extends PBoxICCProfile implements ICCInputProfile {

    public PBoxICCInputProfile(byte[] profile) {
        super(profile);
        setType("ICCInputProfile");
    }

    // TODO : implement this
    @Override
    public String getcolorSpace() {
        return null;
    }
}
