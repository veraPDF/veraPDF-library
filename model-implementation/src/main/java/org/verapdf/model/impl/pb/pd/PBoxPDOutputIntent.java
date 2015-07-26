package org.verapdf.model.impl.pb.pd;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.cos.COSStream;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.external.ICCOutputProfile;
import org.verapdf.model.impl.pb.external.PBoxICCOutputProfile;
import org.verapdf.model.pdlayer.PDOutputIntent;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDOutputIntent extends PBoxPDObject implements PDOutputIntent{

    private static final Logger LOGGER = Logger.getLogger(PBoxPDOutputIntent.class);
    /**
     * String name for destination profile
     */
	public static final String DEST_PROFILE = "destProfile";
    /**
     * String name for output intent
     */
	public static final String OUTPUT_INTENT_TYPE = "PDOutputIntent";

	/**
	 * @param simplePDObject
     *            a {@link org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent} used to
     *            populate the instance
	 */
	public PBoxPDOutputIntent(org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent simplePDObject) {
        super(simplePDObject);
        setType(OUTPUT_INTENT_TYPE);
    }

	@Override
    public String getdestOutputProfileRef() {
		COSDictionary dictionary = (COSDictionary) (simplePDObject).getCOSObject();
		COSBase item = dictionary.getItem(COSName.DEST_OUTPUT_PROFILE);
		if (item instanceof COSObject) {
			StringBuilder buffer = new StringBuilder();
			buffer.append(((COSObject) item).getObjectNumber()).append(' ');
			buffer.append(((COSObject) item).getGenerationNumber());
			return buffer.toString();
		}
        return null;
	}

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        List<?extends Object> list;
        switch (link) {
            case DEST_PROFILE:
                list = getDestProfile();
                break;
            default:
                list = super.getLinkedObjects(link);
                break;
        }
        return list;
    }

    private List<ICCOutputProfile> getDestProfile() {
        List<ICCOutputProfile> profile = new ArrayList<>();
        COSBase dict = simplePDObject.getCOSObject();
        String subtype = null;
        if (dict instanceof COSDictionary) {
            subtype = ((COSDictionary) dict).getNameAsString(COSName.S);
        }
        try {
            COSStream dest = ((org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent) simplePDObject).getDestOutputIntent();
            if (dest != null) {
                try (final InputStream unfilteredStream = dest.getUnfilteredStream()) {
    				Long n = Long.valueOf(dest.getLong(COSName.N));
                    profile.add(new PBoxICCOutputProfile(unfilteredStream, subtype, n.longValue() != -1 ? n : null));
                }
            }
        } catch (IOException e) {
            LOGGER.error("Can not read dest output profile. " + e.getMessage(), e);
        }
        return profile;
    }
}
