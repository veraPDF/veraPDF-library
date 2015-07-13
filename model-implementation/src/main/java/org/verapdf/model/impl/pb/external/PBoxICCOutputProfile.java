package org.verapdf.model.impl.pb.external;

import org.verapdf.model.external.ICCOutputProfile;

/**
 * Embedded ICC profile used as a destination profile in the output intent dictionary
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxICCOutputProfile extends PBoxICCProfile implements ICCOutputProfile {

    public PBoxICCOutputProfile(byte[] profile) {
        super(profile);
        setType("ICCOutputProfile");
    }

	@Override
	public String getS() {
		return null;
	}
}
