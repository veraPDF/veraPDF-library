package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.external.ICCProfile;
import org.verapdf.model.pdlayer.PDOutputIntent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDOutputIntent extends PBoxPDObject implements PDOutputIntent{

    public PBoxPDOutputIntent(COSObjectable simplePDObject) {
        super(simplePDObject);
        setType("PDOutputIntent");
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

    //TODO : implement this
    private List<ICCProfile> getDestProfile() {
        return new ArrayList<>();
    }
}
