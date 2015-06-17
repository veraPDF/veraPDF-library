package org.verapdf.model.impl.pb.external;

import org.verapdf.model.external.ICCProfile;

/**
 * Embedded ICC profile
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxICCProfile extends PBoxExternal implements ICCProfile {

    protected byte[] profile;

    protected PBoxICCProfile(byte[] profile) {
        super();
        this.profile = new byte[profile.length];
        System.arraycopy(profile, 0, this.profile, 0, profile.length);
    }

    //TODO : implement this
    @Override
    public String getdeviceClass() {
        return null;
    }

    //TODO : implement this
    @Override
    public String getcolorSpace() {
        return null;
    }

    //TODO : implement this
    @Override
    public Double getversion() {
        return null;
    }

    //TODO ; implement this
    public Boolean getisValid() {
        return null;
    }

}
