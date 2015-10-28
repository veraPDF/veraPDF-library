package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.model.external.ICCOutputProfile;
import org.verapdf.model.impl.pb.cos.PBCosDict;
import org.verapdf.model.impl.pb.cos.PBCosObject;
import org.verapdf.model.impl.pb.external.PBoxICCOutputProfile;
import org.verapdf.model.pdlayer.PDOutputIntent;
import org.verapdf.model.tools.IDGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDOutputIntent extends PBoxPDObject implements PDOutputIntent {

	private static final Logger LOGGER = Logger
			.getLogger(PBoxPDOutputIntent.class);

	public static final String OUTPUT_INTENT_TYPE = "PDOutputIntent";

	public static final String DEST_PROFILE = "destProfile";
	public static final String DEST_OUTPUT_PROFILE_REF = "DestOutputProfileRef";

	private final String destOutputProfileIndirect;

	public PBoxPDOutputIntent(
			org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent simplePDObject) {
		super(simplePDObject, OUTPUT_INTENT_TYPE);
		this.destOutputProfileIndirect = this.getDestOutputProfileIndirect(simplePDObject);
	}

	private String getDestOutputProfileIndirect(org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent intent) {
		COSDictionary dictionary = (COSDictionary) intent.getCOSObject();
		COSBase item = dictionary.getItem(COSName.DEST_OUTPUT_PROFILE);
		return IDGenerator.generateID(item);
	}

	@Override
	public String getdestOutputProfileIndirect() {
		return this.destOutputProfileIndirect;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case DEST_PROFILE:
				return this.getDestProfile();
			case DEST_OUTPUT_PROFILE_REF:
				return this.getDestOutputProfileRef();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<ICCOutputProfile> getDestProfile() {
		COSBase dict = this.simplePDObject.getCOSObject();
		String subtype = null;
		if (dict instanceof COSDictionary) {
			subtype = ((COSDictionary) dict).getNameAsString(COSName.S);
		}
		try {
			COSStream dest = ((org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent) this.simplePDObject)
					.getDestOutputIntent();
			if (dest != null) {
				List<ICCOutputProfile> profile = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				final InputStream unfilteredStream = dest.getUnfilteredStream();
				long N = dest.getLong(COSName.N);
				profile.add(new PBoxICCOutputProfile(unfilteredStream, subtype,
						N != -1 ? Long.valueOf(N) : null));
				return Collections.unmodifiableList(profile);
			}
		} catch (IOException e) {
			LOGGER.error("Can not read dest output profile. " + e.getMessage(),
					e);
		}
		return Collections.emptyList();
	}

	private List<CosObject> getDestOutputProfileRef() {
		COSDictionary dict = (COSDictionary) this.simplePDObject.getCOSObject();
		COSBase ref = dict.getDictionaryObject(COSName.getPDFName(DEST_OUTPUT_PROFILE_REF));
		CosObject value = PBCosObject.getFromValue(ref);
		if (value != null) {
			ArrayList<CosObject> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(value);
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}
}
