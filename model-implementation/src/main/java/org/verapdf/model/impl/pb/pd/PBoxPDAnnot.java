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
import org.verapdf.model.impl.pb.pd.actions.PBoxPDAction;
import org.verapdf.model.pdlayer.PDAction;
import org.verapdf.model.pdlayer.PDAnnot;
import org.verapdf.model.pdlayer.PDContentStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDAnnot extends PBoxPDObject implements PDAnnot {

    public static final String ANNOTATION_TYPE = "PDAnnot";

    public static final String DICT = "Dict";
    public static final String STREAM = "Stream";

    public static final String APPEARANCE = "appearance";
    public static final String C = "C";
    public static final String IC = "IC";
    public static final String A = "A";
    public static final String ADDITIONAL_ACTION = "AA";

    public static final int MAX_COUNT_OF_ACTIONS = 10;
    public static final String NAMED_KEYWORD = "Named";

    public PBoxPDAnnot(PDAnnotation simplePDObject) {
        super(simplePDObject, ANNOTATION_TYPE);
    }

    @Override
    public String getSubtype() {
        return ((PDAnnotation) this.simplePDObject).getSubtype();
    }

    @Override
    public String getAP() {
        COSBase ap = ((PDAnnotation) this.simplePDObject).getCOSObject()
                .getDictionaryObject(COSName.AP);
        if (ap != null && ap instanceof COSDictionary) {
            StringBuilder result = new StringBuilder();
            for (COSName key : ((COSDictionary) ap).keySet()) {
                result.append(key.getName());
                result.append(' ');
            }
            //remove last whitespace character
            return result.length() > 0 ? result.substring(0, result.length() - 1) : result.toString();

        }
        return null;
    }

    @Override
    public Long getF() {
        return Long.valueOf(((PDAnnotation) this.simplePDObject)
                .getAnnotationFlags());
    }

    @Override
    public Double getCA() {
        COSBase ca = ((PDAnnotation) this.simplePDObject).getCOSObject()
                .getDictionaryObject(COSName.CA);
        return ca instanceof COSNumber ? Double.valueOf(((COSNumber) ca)
                .doubleValue()) : null;
    }

    @Override
    public String getN_type() {
        PDAppearanceDictionary appearanceDictionary = ((PDAnnotation) this.simplePDObject).getAppearance();
        if (appearanceDictionary != null) {
            PDAppearanceEntry normalAppearance = appearanceDictionary.getNormalAppearance();
            if (normalAppearance == null) {
                return null;
            } else if (normalAppearance.isSubDictionary()) {
                return DICT;
            } else {
                return STREAM;
            }
        } else {
            return null;
        }
    }

    @Override
    public String getFT() {
        COSBase ft = ((PDAnnotation) this.simplePDObject).getCOSObject().getDictionaryObject(COSName.FT);
        if (ft != null && ft instanceof COSName) {
            return ((COSName) ft).getName();
        } else {
            return null;
        }
    }

    @Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case ADDITIONAL_ACTION:
				return this.getAdditionalActions();
			case A:
				return this.getA();
			case IC:
				return this.getIC();
			case C:
				return this.getC();
			case APPEARANCE:
				return this.getAppearance();
			default:
				return super.getLinkedObjects(link);
		}
	}

    private List<PDAction> getAdditionalActions() {
        COSBase actionDictionary = ((PDAnnotation) simplePDObject)
                .getCOSObject().getDictionaryObject(COSName.AA);
        if (actionDictionary instanceof COSDictionary) {
			List<PDAction> actions = new ArrayList<>(MAX_COUNT_OF_ACTIONS);

			PDAnnotationAdditionalActions additionalActions = new PDAnnotationAdditionalActions(
                    (COSDictionary) actionDictionary);
            org.apache.pdfbox.pdmodel.interactive.action.PDAction buffer;

            buffer = additionalActions.getBl();
            this.addAction(actions, buffer);

            buffer = additionalActions.getD();
            this.addAction(actions, buffer);

            buffer = additionalActions.getE();
            this.addAction(actions, buffer);

            buffer = additionalActions.getFo();
            this.addAction(actions, buffer);

            buffer = additionalActions.getPC();
            this.addAction(actions, buffer);

            buffer = additionalActions.getPI();
            this.addAction(actions, buffer);

            buffer = additionalActions.getPO();
            this.addAction(actions, buffer);

            buffer = additionalActions.getPV();
            this.addAction(actions, buffer);

            buffer = additionalActions.getU();
            this.addAction(actions, buffer);

            buffer = additionalActions.getX();
            this.addAction(actions, buffer);

			return Collections.unmodifiableList(actions);
        }
        return Collections.emptyList();
    }

	private List<PDAction> getA() {
		COSBase actionDictionary = ((PDAnnotation) this.simplePDObject)
				.getCOSObject().getDictionaryObject(COSName.A);
		if (actionDictionary instanceof COSDictionary) {
			org.apache.pdfbox.pdmodel.interactive.action.PDAction action = PDActionFactory
					.createAction((COSDictionary) actionDictionary);
			PDAction result = PBoxPDAction.getAction(action);
			if (result != null) {
				List<PDAction> actions =
						new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				actions.add(result);
				return Collections.unmodifiableList(actions);
			}
		}
		return Collections.emptyList();
	}

    private List<CosReal> getIC() {
        return this.getRealsFromArray(COSName.IC);
    }

    private List<CosReal> getC() {
        return this.getRealsFromArray(COSName.C);
    }

    private List<CosReal> getRealsFromArray(COSName arrayName) {
        COSBase colorArray = ((PDAnnotation) this.simplePDObject).getCOSObject()
                .getDictionaryObject(arrayName);
        if (colorArray instanceof COSArray) {
			List<CosReal> color = new ArrayList<>(((COSArray) colorArray).size());
			for (COSBase colorValue : (COSArray) colorArray) {
                if (colorValue instanceof COSNumber) {
                    color.add(new PBCosReal((COSNumber) colorValue));
                }
            }
			return Collections.unmodifiableList(color);
        }
        return Collections.emptyList();
    }

    /**
     * @return normal appearance stream (N key in the appearance dictionary) of
     *         the annotation
     */
    private List<PDContentStream> getAppearance() {
        PDAppearanceDictionary appearanceDictionary = ((PDAnnotation) this.simplePDObject)
                .getAppearance();
        if (appearanceDictionary != null) {
            PDAppearanceEntry normalAppearance = appearanceDictionary
                    .getNormalAppearance();
            if (normalAppearance != null && normalAppearance.isStream()) {
				List<PDContentStream> appearances =
						new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				appearances.add(new PBoxPDContentStream(normalAppearance
                        .getAppearanceStream()));
				return Collections.unmodifiableList(appearances);
            }
        }
        return Collections.emptyList();
    }
}
