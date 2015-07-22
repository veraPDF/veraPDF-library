package org.verapdf.model.impl.pb.pd.colors;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.external.ICCInputProfile;
import org.verapdf.model.impl.pb.external.PBoxICCInputProfile;
import org.verapdf.model.pdlayer.PDICCBased;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ICCBased color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDICCBased extends PBoxPDColorSpace implements PDICCBased {

	public static final Logger logger = Logger.getLogger(PBoxPDICCBased.class);

	public static final String ICC_PROFILE = "iccProfile";
	public static final String ICC_BASED_TYPE = "PDICCBased";

	public PBoxPDICCBased(org.apache.pdfbox.pdmodel.graphics.color.PDICCBased simplePDObject) {
		super(simplePDObject);
		setType(ICC_BASED_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case ICC_PROFILE:
				list = this.getICCProfile();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<ICCInputProfile> getICCProfile() {
		List<ICCInputProfile> inputProfile = new ArrayList<>();
		try {
			PDStream pdStream = ((org.apache.pdfbox.pdmodel.graphics.color.PDICCBased) simplePDObject).getPDStream();
			InputStream stream = pdStream.createInputStream();
			long N = pdStream.getStream().getLong(COSName.N);
			if (stream != null && stream.available() > 0) {
				inputProfile.add(new PBoxICCInputProfile(stream, N != -1 ? N : null));
			}
		} catch (IOException e) {
			logger.error("Can not get input profile from ICCBased. " + e.getMessage());
		}
		return inputProfile;
	}
}
