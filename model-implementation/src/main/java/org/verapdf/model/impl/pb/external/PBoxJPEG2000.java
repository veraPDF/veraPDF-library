package org.verapdf.model.impl.pb.external;

import org.verapdf.model.external.JPEG2000;

/**
 * @author Evgeniy Muravitskiy
 */
// TODO : implement me
public class PBoxJPEG2000 extends PBoxExternal implements JPEG2000 {

	public static final String JPEG_2000_TYPE = "JPEG2000";

	public PBoxJPEG2000() {
		super(JPEG_2000_TYPE);
	}

	@Override
	public Long getnrColorChannels() {
		return Long.valueOf(-1);
	}

	@Override
	public Long getnrColorSpaceSpecs() {
		return Long.valueOf(-1);
	}

	@Override
	public Long getnrColorSpacesWithApproxField() {
		return Long.valueOf(-1);
	}

	@Override
	public Long getcolrMethod() {
		return Long.valueOf(-1);
	}

	@Override
	public Long getcolrEnumCS() {
		return Long.valueOf(-1);
	}

	@Override
	public Long getbitDepth() {
		return Long.valueOf(-1);
	}

	@Override
	public Boolean getbpccBoxPresent() {
		return Boolean.FALSE;
	}
}
