package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionFactory;
import org.apache.pdfbox.pdmodel.interactive.action.PDAnnotationAdditionalActions;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceEntry;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.pdlayer.PDAction;
import org.verapdf.model.pdlayer.PDAnnot;
import org.verapdf.model.pdlayer.PDContentStream;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDAnnot extends PBoxPDObject implements PDAnnot {

    public static final String ANNOTATION_TYPE = "PDAnnot";

    public static final String APPEARANCE = "appearance";
    public static final String C = "C";
    public static final String IC = "IC";
    public static final String A = "A";
    public static final String ADDITIONAL_ACTION = "AA";

    public static final int MAX_COUNT_OF_ELEMENTS = 1;
    public static final int MAX_COUNT_OF_ACTIONS = 10;

    public PBoxPDAnnot(PDAnnotation simplePDObject) {
        super(simplePDObject);
        setType(ANNOTATION_TYPE);
    }

    @Override
    public String getSubtype() {
        return ((PDAnnotation) simplePDObject).getSubtype();
    }

    @Override
    public String getAP() {
        COSBase ap = ((PDAnnotation) simplePDObject).getCOSObject()
                .getDictionaryObject(COSName.AP);
        return ap != null ? ap.toString() : null;
    }

    @Override
    public Long getF() {
        return Long.valueOf(((PDAnnotation) simplePDObject)
                .getAnnotationFlags());
    }

    @Override
    public Double getCA() {
        COSBase ca = ((PDAnnotation) simplePDObject).getCOSObject()
                .getDictionaryObject(COSName.CA);
        return ca instanceof COSNumber ? Double.valueOf(((COSNumber) ca)
                .doubleValue()) : null;
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        List<? extends Object> list;
        switch (link) {
        case ADDITIONAL_ACTION:
            list = this.getAdditionalActions();
            break;
        case A:
            list = this.getA();
            break;
        case IC:
            list = this.getIC();
            break;
        case C:
            list = this.getC();
            break;
        case APPEARANCE:
            list = this.getAppearance();
            break;
        default:
            list = super.getLinkedObjects(link);
            break;
        }
        return list;
    }

    private List<PDAction> getAdditionalActions() {
        List<PDAction> actions = new ArrayList<>(MAX_COUNT_OF_ACTIONS);
        COSBase actionDictionary = ((PDAnnotation) simplePDObject)
                .getCOSObject().getDictionaryObject(COSName.AA);
        if (actionDictionary instanceof COSDictionary) {
            PDAnnotationAdditionalActions additionalActions = new PDAnnotationAdditionalActions(
                    (COSDictionary) actionDictionary);
            org.apache.pdfbox.pdmodel.interactive.action.PDAction buffer;

            buffer = additionalActions.getBl();
            addAction(actions, buffer);

            buffer = additionalActions.getD();
            addAction(actions, buffer);

            buffer = additionalActions.getE();
            addAction(actions, buffer);

            buffer = additionalActions.getFo();
            addAction(actions, buffer);

            buffer = additionalActions.getPC();
            addAction(actions, buffer);

            buffer = additionalActions.getPI();
            addAction(actions, buffer);

            buffer = additionalActions.getPO();
            addAction(actions, buffer);

            buffer = additionalActions.getPV();
            addAction(actions, buffer);

            buffer = additionalActions.getU();
            addAction(actions, buffer);

            buffer = additionalActions.getX();
            addAction(actions, buffer);

        }
        return actions;
    }

    private List<PDAction> getA() {
        List<PDAction> actions = new ArrayList<>(MAX_COUNT_OF_ELEMENTS);
        COSBase actionDictionary = ((PDAnnotation) simplePDObject)
                .getCOSObject().getDictionaryObject(COSName.A);
        if (actionDictionary instanceof COSDictionary) {
            org.apache.pdfbox.pdmodel.interactive.action.PDAction action = PDActionFactory
                    .createAction((COSDictionary) actionDictionary);
            if (action != null) {
                actions.add(new PBoxPDAction(action));
            }
        }
        return actions;
    }

    private List<CosReal> getIC() {
        return getRealsFromArray(COSName.IC);
    }

    private List<CosReal> getC() {
        return getRealsFromArray(COSName.C);
    }

    private List<CosReal> getRealsFromArray(COSName arrayName) {
        List<CosReal> color = new ArrayList<>();
        COSBase colorArray = ((PDAnnotation) simplePDObject).getCOSObject()
                .getDictionaryObject(arrayName);
        if (colorArray instanceof COSArray) {
            for (COSBase colorValue : (COSArray) colorArray) {
                if (colorValue instanceof COSNumber) {
                    color.add(new PBCosReal((COSNumber) colorValue));
                }
            }
        }
        return color;
    }

    /**
     * @return normal appearance stream (N key in the appearance dictionary) of
     *         the annotation
     */
    private List<PDContentStream> getAppearance() {
        List<PDContentStream> appearances = new ArrayList<>(
                MAX_COUNT_OF_ELEMENTS);
        PDAppearanceDictionary appearanceDictionary = ((PDAnnotation) simplePDObject)
                .getAppearance();
        if (appearanceDictionary != null) {
            PDAppearanceEntry normalAppearance = appearanceDictionary
                    .getNormalAppearance();
            if (normalAppearance != null && normalAppearance.isStream()) {
                appearances.add(new PBoxPDContentStream(normalAppearance
                        .getAppearanceStream()));
            }
        }
        return appearances;
    }
}
