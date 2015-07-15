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
    public static final Integer DEVICE_CLASS_OFFSET = Integer.valueOf(12);
    public static final Integer COLOR_SPACE_OFFSET = Integer.valueOf(16);
    public static final Integer REQUIRED_LENGTH = Integer.valueOf(4);

    private byte[] profile;
	private Long N;

	public byte[] getProfile() {
		return profile;
	}

	protected PBoxICCProfile(byte[] profile, Long N) {
        super();
        this.profile = new byte[profile.length];
        System.arraycopy(profile, 0, this.profile, 0, profile.length);
		this.N = N;
    }

    @Override
    public String getdeviceClass() {
        return getSubArray(DEVICE_CLASS_OFFSET, REQUIRED_LENGTH);
    }

	public Long getN() {
		return N;
	}

    @Override
    public String getcolorSpace() {
        return getSubArray(COLOR_SPACE_OFFSET, REQUIRED_LENGTH);
    }

    private String getSubArray(Integer start, Integer length) {
        if (start + length <= profile.length) {
            byte[] buffer = new byte[length];
            System.arraycopy(profile, start, buffer, 0, length);
            return new String(buffer);
        } else {
            logger.error("Length of icc profile less than " + (start + length));
            return null;
        }
    }

    @Override
    public Double getversion() {
        final Integer subversionByte = Integer.valueOf(9);
        if (profile.length > subversionByte) {
            final Integer versionLength = Integer.valueOf(3);
            final Integer vesrsionByte = Integer.valueOf(8);

            StringBuilder version = new StringBuilder(versionLength);
            version.append(profile[vesrsionByte]).append('.');
            version.append(profile[subversionByte] >>> REQUIRED_LENGTH);

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
