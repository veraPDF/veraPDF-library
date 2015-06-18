package org.verapdf.model.impl.pb.external;

import org.apache.log4j.Logger;
import org.verapdf.model.external.ICCProfile;

/**
 * Embedded ICC profile
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxICCProfile extends PBoxExternal implements ICCProfile {

    public static final Logger logger = Logger.getLogger(PBoxICCProfile.class);

    protected byte[] profile;

    protected PBoxICCProfile(byte[] profile) {
        super();
        this.profile = new byte[profile.length];
        System.arraycopy(profile, 0, this.profile, 0, profile.length);
    }

    @Override
    public String getdeviceClass() {
        return getSubArray(12, 4);
    }

    @Override
    public String getcolorSpace() {
        return getSubArray(16, 4);
    }

    private String getSubArray(Integer start, Integer length) {
        if (start + length <= profile.length) {
            byte[] buffer = new byte[4];
            System.arraycopy(profile, start, buffer, 0, length);
            return new String(buffer).trim();
        } else {
            logger.error("Length of icc profile less than " + (start + length));
            return null;
        }
    }

    @Override
    public Double getversion() {
        if (profile.length > 9) {
            StringBuilder version = new StringBuilder(3);
            version.append(profile[8]).append('.');
            version.append(profile[9] >>> 4);
            return Double.valueOf(version.toString());
        } else {
            logger.error("ICC profile contain less than 10 bytes of data.");
            return Double.valueOf(-1);
        }
    }

    // Custom implementation for another users
    public Boolean getisValid() {
        return Boolean.TRUE;
    }
}
