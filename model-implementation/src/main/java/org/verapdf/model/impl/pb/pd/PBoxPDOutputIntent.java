package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.external.ICCOutputProfile;
import org.verapdf.model.impl.pb.external.PBoxICCOutputProfile;
import org.verapdf.model.pdlayer.PDOutputIntent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDOutputIntent extends PBoxPDObject implements PDOutputIntent {

	private static final Logger LOGGER = Logger
			.getLogger(PBoxPDOutputIntent.class);

	public static final String OUTPUT_INTENT_TYPE = "PDOutputIntent";

	public static final String DEST_PROFILE = "destProfile";

	public PBoxPDOutputIntent(
			org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent simplePDObject) {
		super(simplePDObject, OUTPUT_INTENT_TYPE);
	}

	@Override
	public String getdestOutputProfileRef() {
		COSDictionary dictionary = (COSDictionary) this.simplePDObject
				.getCOSObject();
		COSBase item = dictionary.getItem(COSName.DEST_OUTPUT_PROFILE);
		if (item instanceof COSObject) {
			return String.valueOf(((COSObject) item).getObjectNumber()) +
					' ' + ((COSObject) item).getGenerationNumber();
		}
		return null;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (DEST_PROFILE.equals(link)) {
			return this.getDestProfile();
		}
		return super.getLinkedObjects(link);
	}

	private List<ICCOutputProfile> getDestProfile() {
		List<ICCOutputProfile> profile = new ArrayList<>();
		COSBase dict = this.simplePDObject.getCOSObject();
		String subtype = null;
		if (dict instanceof COSDictionary) {
			subtype = ((COSDictionary) dict).getNameAsString(COSName.S);
		}
		try {
			COSStream dest = ((org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent) this.simplePDObject)
					.getDestOutputIntent();
			if (dest != null) {
				final InputStream unfilteredStream = dest.getUnfilteredStream();
				long N = dest.getLong(COSName.N);
				profile.add(new PBoxICCOutputProfile(unfilteredStream, subtype,
						N != -1 ? Long.valueOf(N) : null));
				unfilteredStream.close();
			}
		} catch (IOException e) {
			LOGGER.error("Can not read dest output profile. " + e.getMessage(),
					e);
		}
		return profile;
	}
}
