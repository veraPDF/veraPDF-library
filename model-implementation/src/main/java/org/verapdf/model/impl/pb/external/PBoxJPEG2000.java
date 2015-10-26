package org.verapdf.model.impl.pb.external;

import org.verapdf.model.external.JPEG2000;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxJPEG2000 extends PBoxExternal implements JPEG2000 {

	public static final String JPEG_2000_TYPE = "JPEG2000";

	public PBoxJPEG2000() {
		super(JPEG_2000_TYPE);
	}

	@Override
	public Long getnrColorChannels() {
		return null;
	}

	@Override
	public Long getnrColorSpaceSpecs() {
		return null;
	}

	@Override
	public Long getnrColorSpacesWithApproxField() {
		return null;
	}

	@Override
	public Long getcolrMethod() {
		return null;
	}

	@Override
	public Long getcolrEnumCS() {
		return null;
	}

	@Override
	public Long getbitDepth() {
		return null;
	}

	@Override
	public Boolean getbpccBoxPresent() {
		return null;
	}
}
