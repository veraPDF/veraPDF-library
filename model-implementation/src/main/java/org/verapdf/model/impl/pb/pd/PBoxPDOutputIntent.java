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
public class PBoxPDOutputIntent extends PBoxPDObject implements PDOutputIntent{

    public static final Logger logger = Logger.getLogger(PBoxPDOutputIntent.class);

    public PBoxPDOutputIntent(org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent simplePDObject) {
        super(simplePDObject);
        setType("PDOutputIntent");
    }

	public String getdestOutputProfileRef() {
		COSDictionary dictionary = (COSDictionary) (simplePDObject).getCOSObject();
		COSBase item = dictionary.getItem(COSName.DEST_OUTPUT_PROFILE);
		if (item instanceof COSObject) {
			StringBuilder buffer = new StringBuilder();
			buffer.append(((COSObject) item).getObjectNumber()).append(' ');
			buffer.append(((COSObject) item).getGenerationNumber());
			return buffer.toString();
		} else {
			return null;
		}
	}

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        List<?extends Object> list;
        switch (link) {
            case "destProfile":
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
        try {
            COSStream dest = ((org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent) simplePDObject)
					.getDestOutputIntent();
            if (dest != null) {
                final InputStream unfilteredStream = dest.getUnfilteredStream();
                final int bound = unfilteredStream.available();
                byte[] bytes = new byte[bound];
                unfilteredStream.read(bytes);
                profile.add(new PBoxICCOutputProfile(bytes));
                unfilteredStream.close();
            }
        } catch (IOException e) {
            logger.error("Can not read dest output profile. " + e.getMessage());
        }
        return profile;
    }
}
