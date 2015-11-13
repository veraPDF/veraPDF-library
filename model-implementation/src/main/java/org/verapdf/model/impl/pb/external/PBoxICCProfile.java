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

    private static final Logger LOGGER = Logger.getLogger(PBoxICCProfile.class);

	/** Length of icc output profile header */
    public static final int HEADER_LENGTH = 128;
	/** Offset of device class in header */
    public static final int DEVICE_CLASS_OFFSET = 12;
	/** Offset of color space in header */
    public static final int COLOR_SPACE_OFFSET = 16;
	/** Expected length for device class and so on */
    public static final int REQUIRED_LENGTH = 4;
	/** Expected length version */
    public static final int VERSION_LENGTH = 3;
	/** Offset of version byte */
    public static final int VERSION_BYTE = 8;
	/** Offset of subversion byte */
    public static final int SUBVERSION_BYTE = 9;

    private byte[] profileHeader;
    private InputStream profileStream;
    private Long dictionaryNumberOfColors;

    protected PBoxICCProfile(InputStream profileStream,
            				 Long dictionaryNumberOfColors,
							 String type) throws IOException {
        super(type);
        this.profileStream = profileStream;
        this.dictionaryNumberOfColors = dictionaryNumberOfColors;

        initializeProfileHeader();
    }

    private void initializeProfileHeader() throws IOException {
        int available = this.profileStream.available();
        int size = available > HEADER_LENGTH ? HEADER_LENGTH : available;

        this.profileHeader = new byte[size];
        this.profileStream.mark(size);
        this.profileStream.read(this.profileHeader, 0, size);
        this.profileStream.reset();
		// TODO : finalize it correct, when object will destroy
		this.profileStream.close();
    }

    /**
     * @return string representation of device class or null, if profile length
     *         is too small
     */
    @Override
    public String getdeviceClass() {
        return getSubArray(DEVICE_CLASS_OFFSET, REQUIRED_LENGTH);
    }

    /**
     * @return number of colorants for ICC profile, described in profile
     *         dictionary
     */
    @Override
    public Long getN() {
        return this.dictionaryNumberOfColors;
    }

    /**
     * @return string representation of color space or null, if profile length
     *         is too small
     */
    @Override
    public String getcolorSpace() {
        return getSubArray(COLOR_SPACE_OFFSET, REQUIRED_LENGTH);
    }

    private String getSubArray(int start, int length) {
        if (start + length <= this.profileHeader.length) {
            byte[] buffer = new byte[length];
            System.arraycopy(this.profileHeader, start, buffer, 0, length);
            return new String(buffer);
        }
        LOGGER.error("Length of icc profile less than " + (start + length));
        return null;
    }

    /**
     * @return version of ICC profile or null, if profile length is too small
     */
    @Override
    public Double getversion() {
        if (this.profileHeader.length > SUBVERSION_BYTE) {
            StringBuilder version = new StringBuilder(VERSION_LENGTH);
            version.append(this.profileHeader[VERSION_BYTE] & 0xFF).append('.');
            version.append((this.profileHeader[SUBVERSION_BYTE] >>> REQUIRED_LENGTH) & 0xFF);

            return Double.valueOf(version.toString());
        }
        LOGGER.error("ICC profile contain less than 10 bytes of data.");
        return null;
    }

	/**
	 * Indicate validity of icc profile.
	 * Need to implemented by customer.
	 *
	 * @return true if profile is valid
	 */
    @Override
    public Boolean getisValid() {
        return Boolean.TRUE;
    }

}
