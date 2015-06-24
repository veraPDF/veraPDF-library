package org.verapdf.model.impl.pb.pd.images;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDXImage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDXImage extends PBoxPDXObject implements PDXImage {

    public static final String IMAGE_CS = "imageCS";
    public static final String ALTERNATES = "Alternates";

    public PBoxPDXImage(PDImageXObject simplePDObject) {
        super(simplePDObject);
        setType("PDXImage");
    }

    @Override
    public Boolean getInterpolate() {
        return Boolean.valueOf(((PDImageXObject) simplePDObject).getInterpolate());
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        List<? extends Object> list;

        switch (link) {
            case IMAGE_CS:
                list = getImageCS();
                break;
            case ALTERNATES:
                list = getAlternates();
                break;
            default:
                list = super.getLinkedObjects(link);
                break;
        }

        return list;
    }

    //TODO : implement this
    private List<PDColorSpace> getImageCS() {
        List<PDColorSpace> cs = new ArrayList<>(1);
        // look at pdfbox. how they are choose correct constructor
        return new ArrayList<>();
    }

    //TODO : implement this
    private List<PDXImage> getAlternates() {
        // look at pdfbox
        return new ArrayList<>();
    }
}
