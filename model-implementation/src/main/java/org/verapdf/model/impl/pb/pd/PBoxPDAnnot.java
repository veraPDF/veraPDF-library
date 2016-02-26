package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionFactory;
import org.apache.pdfbox.pdmodel.interactive.action.PDAnnotationAdditionalActions;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceEntry;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDAction;
import org.verapdf.model.pdlayer.PDAction;
import org.verapdf.model.pdlayer.PDAnnot;
import org.verapdf.model.pdlayer.PDContentStream;
import org.verapdf.model.tools.resources.PDInheritableResources;

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
	public static final int X_AXIS = 0;
	public static final int Y_AXIS = 1;

	private final PDInheritableResources resources;

	private final boolean isFKeyPresent;

	private final String subtype;
	private final String ap;
	private final int annotationFlag;
	private final Double ca;
	private final String nType;
	private final String ft;
	private final Double width;
	private final Double height;

	public PBoxPDAnnot(PDAnnotation annot, PDInheritableResources resources) {
		super(annot, ANNOTATION_TYPE);
		this.resources = resources;
		this.subtype = annot.getSubtype();
		this.ap = this.getAP(annot);

		COSDictionary annotDict = annot.getCOSObject();
		this.isFKeyPresent = annotDict.containsKey(COSName.F);

		this.annotationFlag = annot.getAnnotationFlags();
		this.ca = this.getCA(annot);
		this.nType = this.getN_type(annot);
		this.ft = this.getFT(annot);
		this.width = this.getWidth(annot);
		this.height = this.getHeight(annot);
	}

	private String getAP(PDAnnotation annot) {
		COSBase ap = annot.getCOSObject().getDictionaryObject(COSName.AP);
		if (ap != null && ap instanceof COSDictionary) {
			StringBuilder result = new StringBuilder();
			for (COSName key : ((COSDictionary) ap).keySet()) {
				result.append(key.getName());
				result.append(' ');
			}
			//remove last whitespace character
			return result.length() <= 0 ? result.toString() :
					result.substring(0, result.length() - 1);
		}
		return null;
	}

	private Double getCA(PDAnnotation annot) {
		COSBase ca = annot.getCOSObject().getDictionaryObject(COSName.CA);
		return !(ca instanceof COSNumber) ? null :
				Double.valueOf(((COSNumber) ca).doubleValue());
	}

	private String getN_type(PDAnnotation annot) {
		PDAppearanceDictionary appearanceDictionary = annot.getAppearance();
		if (appearanceDictionary != null) {
			PDAppearanceEntry normalAppearance =
					appearanceDictionary.getNormalAppearance();
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

	private String getFT(PDAnnotation annot) {
		COSBase ft = annot.getCOSObject().getDictionaryObject(COSName.FT);
		return ft instanceof COSName ? ((COSName) ft).getName() : null;
	}

	private Double getWidth(PDAnnotation annot) {
		return this.getDifference(annot, X_AXIS);
	}

	private Double getHeight(PDAnnotation annot) {
		return this.getDifference(annot, Y_AXIS);
	}

	private Double getDifference(PDAnnotation annot, int shift) {
		COSBase array = annot.getCOSObject().getDictionaryObject(COSName.RECT);
		if (array instanceof COSArray && ((COSArray) array).size() == 4) {
			COSBase less = ((COSArray) array).getObject(shift);
			COSBase great = ((COSArray) array).getObject(2 + shift);
			if (less instanceof COSNumber && great instanceof COSNumber) {
				return Double.valueOf(
						((COSNumber) great).doubleValue() - ((COSNumber) less).doubleValue());
			}
		}
		return null;
	}

	@Override
    public String getSubtype() {
        return this.subtype;
    }

    @Override
    public String getAP() {
        return this.ap;
    }

    @Override
    public Long getF() {
		return isFKeyPresent ? Long.valueOf(this.annotationFlag) : null;
    }

    @Override
    public Double getCA() {
        return this.ca;
    }

    @Override
    public String getN_type() {
		return this.nType;
    }

    @Override
    public String getFT() {
        return this.ft;
    }

	public Double getwidth() {
		return this.width;
	}

	public Double getheight() {
		return this.height;
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
				PDAppearanceStream stream = normalAppearance.getAppearanceStream();
				appearances.add(new PBoxPDContentStream(stream, this.resources));
				return Collections.unmodifiableList(appearances);
            }
        }
        return Collections.emptyList();
    }
}
