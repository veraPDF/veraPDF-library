package org.verapdf.model.impl.pb.external;

import org.verapdf.model.external.ICCOutputProfile;

/**
 * Embedded ICC profile used as a destination profile in the output intent dictionary
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxICCOutputProfile extends PBoxICCProfile implements ICCOutputProfile {

	private String subtype;

    public PBoxICCOutputProfile(byte[] profile, String subtype) {
        super(profile);
        setType("ICCOutputProfile");
		this.subtype = subtype;
    }

	@Override
	public String getS() {
		return subtype;
	}
}
