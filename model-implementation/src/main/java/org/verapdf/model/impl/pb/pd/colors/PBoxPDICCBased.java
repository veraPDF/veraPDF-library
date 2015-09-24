package org.verapdf.model.impl.pb.pd.colors;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.external.ICCInputProfile;
import org.verapdf.model.impl.pb.external.PBoxICCInputProfile;
import org.verapdf.model.pdlayer.PDICCBased;

/**
 * ICCBased color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDICCBased extends PBoxPDColorSpace implements PDICCBased {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDICCBased.class);

	public static final String ICC_BASED_TYPE = "PDICCBased";

    public static final String ICC_PROFILE = "iccProfile";

    public PBoxPDICCBased(
            org.apache.pdfbox.pdmodel.graphics.color.PDICCBased simplePDObject) {
        super(simplePDObject, ICC_BASED_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (ICC_PROFILE.equals(link)) {
            return this.getICCProfile();
        }
        return super.getLinkedObjects(link);
    }

    private List<ICCInputProfile> getICCProfile() {
        try {
            PDStream pdStream = ((org.apache.pdfbox.pdmodel.graphics.color.PDICCBased) this.simplePDObject)
                    .getPDStream();
            InputStream stream = pdStream.createInputStream();
            Long N = pdStream.getStream().getLong(COSName.N);
            if (stream != null && stream.available() > 0) {
				List<ICCInputProfile> inputProfile = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				N = N != -1 ? N : null;
                inputProfile.add(new PBoxICCInputProfile(stream, N));
				return Collections.unmodifiableList(inputProfile);
            }
        } catch (IOException e) {
            LOGGER.error("Can not get input profile from ICCBased. ", e);
        }
        return Collections.emptyList();
    }
}
