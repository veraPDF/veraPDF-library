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
	public Long nrColorChannels() {
		return null;
	}

	@Override
	public Long nrColorSpaceSpecs() {
		return null;
	}

	@Override
	public Long nrColorSpacesWithApproxField() {
		return null;
	}

	@Override
	public Long colrMethod() {
		return null;
	}

	@Override
	public Long colrEnumCS() {
		return null;
	}

	@Override
	public Long bitDepth() {
		return null;
	}

	@Override
	public Boolean bpccBoxPresent() {
		return null;
	}
}
