package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.pdlayer.PDAction;
import org.verapdf.model.pdlayer.PDAnnot;
import org.verapdf.model.pdlayer.PDContentStream;

import java.lang.Override;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDAnnot extends PBoxPDObject implements PDAnnot {

    public final static String APPEARANCE = "appearance";
	public static final String C = "C";
	public static final String IC = "IC";
	public static final String A = "A";
	public static final String ADDITIONAL_ACTION = "AA";

	private static final Integer MAXIMAL_COUNT_OF_ACTIONS = Integer.valueOf(1);

	public PBoxPDAnnot(PDAnnotation simplePDObject) {
        super(simplePDObject);
        setType("PDAnnot");
    }

    @Override
    public String getSubtype() {
        return ((PDAnnotation) simplePDObject).getSubtype();
    }

    @Override
    public String getAP() {
		// TODO : implement me
		return null;
    }

    @Override
    public Long getF() {
        // TODO : implement me
        return Long.valueOf(0L);
    }

    @Override
    public Double getCA() {
        // TODO : impelemnt me
        return Double.valueOf(0);
    }

	// TODO : implement this
	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;
		switch (link) {
			case ADDITIONAL_ACTION:
				list = getAdditionalActions();
				break;
			case A:
				list = getA();
				break;
			case IC:
				list = getIC();
				break;
			case C:
				list = getC();
				break;
			case APPEARANCE:
				list = getAppearance();
				break;
			default:
				list = new ArrayList<>();
				break;
		}
		return list;
	}

	// TODO : implement this
	private List<PDAction> getAdditionalActions() {
		List<PDAction> actions = new ArrayList<>();
		return actions;
	}

	// TODO : implement this
	private List<PDAction> getA() {
		List<PDAction> actions = new ArrayList<>(MAXIMAL_COUNT_OF_ACTIONS);
		return actions;
	}

	// TODO : implement this
	private List<CosReal> getIC() {
		List<CosReal> ic = new ArrayList<>();
		return ic;
	}

	// TODO : implement this
	private List<CosReal> getC() {
		List<CosReal> c = new ArrayList<>();
		return c;
	}

	//TODO : implement this
	private List<PDContentStream> getAppearance() {
		List<PDContentStream> appearances = new ArrayList<>();
		return appearances;
	}
}
