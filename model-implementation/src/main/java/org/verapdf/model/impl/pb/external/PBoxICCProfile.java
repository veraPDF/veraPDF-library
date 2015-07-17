package org.verapdf.model.impl.pb.external;

import org.apache.log4j.Logger;
import org.verapdf.model.external.ICCProfile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Embedded ICC profile
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxICCProfile extends PBoxExternal implements ICCProfile {

    public static final Logger logger = Logger.getLogger(PBoxICCProfile.class);

	public static final Integer HEADER_LENGTH = Integer.valueOf(128);
    public static final Integer DEVICE_CLASS_OFFSET = Integer.valueOf(12);
    public static final Integer COLOR_SPACE_OFFSET = Integer.valueOf(16);
    public static final Integer REQUIRED_LENGTH = Integer.valueOf(4);
	public static final Integer VERSION_LENGTH = Integer.valueOf(3);
	public static final Integer VERSION_BYTE = Integer.valueOf(8);
	public static final Integer SUBVERSION_BYTE = Integer.valueOf(9);

	private byte[] profileHeader;
	private InputStream profileStream;
	private Long dictionaryNumberOfColors;


	protected PBoxICCProfile(InputStream profileStream, Long dictionaryNumberOfColors) throws IOException {
        super();
		this.profileStream = profileStream;
		this.dictionaryNumberOfColors = dictionaryNumberOfColors;

		initializeProfileHeader();
    }

	private void initializeProfileHeader() throws IOException {
		Integer available = Integer.valueOf(this.profileStream.available());
		Integer size = available > HEADER_LENGTH ? HEADER_LENGTH : available;

		this.profileHeader = new byte[size];
		this.profileStream.mark(size);
		this.profileStream.read(this.profileHeader, 0, size);
		this.profileStream.reset();
	}

	/**
	 * @return string representation of device class or null, if profile length is too small
	 */
	@Override
    public String getdeviceClass() {
        return getSubArray(DEVICE_CLASS_OFFSET, REQUIRED_LENGTH);
    }

	/**
	 * @return number of colorants for ICC profile, described in profile dictionary
	 */
	public Long getN() {
		return dictionaryNumberOfColors;
	}

	/**
	 * @return string representation of color space or null, if profile length is too small
	 */
    @Override
    public String getcolorSpace() {
        return getSubArray(COLOR_SPACE_OFFSET, REQUIRED_LENGTH);
    }

    private String getSubArray(Integer start, Integer length) {
        if (start + length <= profileHeader.length) {
            byte[] buffer = new byte[length];
            System.arraycopy(profileHeader, start, buffer, 0, length);
            return new String(buffer);
        } else {
            logger.error("Length of icc profile less than " + (start + length));
            return null;
        }
    }

	/**
	 * @return version of ICC profile or null, if profile length is too small
	 */
    @Override
    public Double getversion() {
        if (profileHeader.length > SUBVERSION_BYTE) {
            StringBuilder version = new StringBuilder(VERSION_LENGTH);
            version.append(profileHeader[VERSION_BYTE]).append('.');
            version.append(profileHeader[SUBVERSION_BYTE] >>> REQUIRED_LENGTH);

            return Double.valueOf(version.toString());
        } else {
            logger.error("ICC profile contain less than 10 bytes of data.");
            return null;
        }
    }

    // Custom implementation for another users
    public Boolean getisValid() {
        return Boolean.TRUE;
    }
}
