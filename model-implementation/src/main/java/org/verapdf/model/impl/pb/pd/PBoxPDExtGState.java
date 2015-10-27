package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.graphics.PDFontSetting;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingIntent;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.coslayer.CosRenderingIntent;
import org.verapdf.model.impl.pb.cos.PBCosObject;
import org.verapdf.model.pdlayer.PDHalftone;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.impl.pb.cos.PBCosRenderingIntent;
import org.verapdf.model.pdlayer.PDExtGState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class represent extended graphic state
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDExtGState extends PBoxPDResources implements PDExtGState {

	public static final String EXT_G_STATE_TYPE = "PDExtGState";

    public static final String RI = "RI";
    public static final String FONT_SIZE = "fontSize";
	public static final String HALFTONE = "HT";
	public static final String HALFTONE_PHASE = "HTP";

	private final String tr;
	private final String tr2;
	private final String sMask;
	private final String BM;
	private final Double ca;
	private final Double CA;

    public PBoxPDExtGState(PDExtendedGraphicsState state) {
        super(state, EXT_G_STATE_TYPE);
		this.tr = this.getStringProperty(state, COSName.TR);
		this.tr2 = this.getStringProperty(state, COSName.getPDFName("TR2"));
		this.sMask = this.getStringProperty(state, COSName.SMASK);
		this.BM = this.getStringProperty(state, COSName.BM);
		this.ca = this.getDoubleProperty(state, COSName.CA_NS);
		this.CA = this.getDoubleProperty(state, COSName.CA);
    }

	@Override
    public String getTR() {
		return this.tr;
    }

    @Override
    public String getTR2() {
		return this.tr2;
    }

    @Override
    public String getSMask() {
		return this.sMask;
    }

    @Override
    public String getBM() {
        return this.BM;
    }

    @Override
    public Double getca() {
        return this.ca;
    }

    @Override
    public Double getCA() {
        return this.CA;
    }

    private String getStringProperty(PDExtendedGraphicsState state, COSName key) {
		COSBase base = state.getCOSObject().getDictionaryObject(key);
		return base == null ? null : base instanceof COSName ?
				((COSName) base).getName() : base.toString();
    }

	private Double getDoubleProperty(PDExtendedGraphicsState state, COSName key) {
		COSBase base = state.getCOSObject().getDictionaryObject(key);
		return !(base instanceof COSNumber) ? null :
				Double.valueOf(((COSNumber) base).doubleValue());
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case RI:
				return this.getRI();
			case FONT_SIZE:
				return this.getFontSize();
			case HALFTONE:
				return this.getHalftone();
			case HALFTONE_PHASE:
				return this.getHalftonePhase();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<CosRenderingIntent> getRI() {
        RenderingIntent renderingIntent = ((PDExtendedGraphicsState) this.simplePDObject)
                .getRenderingIntent();
        if (renderingIntent != null) {
			List<CosRenderingIntent> renderingIntents = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			COSName pdfName = COSName.getPDFName(renderingIntent.stringValue());
            renderingIntents.add(new PBCosRenderingIntent(pdfName));
			return Collections.unmodifiableList(renderingIntents);
        }
        return Collections.emptyList();
    }

    private List<CosReal> getFontSize() {
        PDFontSetting fontSetting = ((PDExtendedGraphicsState) this.simplePDObject)
                .getFontSetting();
        if (fontSetting != null) {
			List<CosReal> result = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			result.add(new PBCosReal(new COSFloat(fontSetting.getFontSize())));
			return Collections.unmodifiableList(result);
        }

        return Collections.emptyList();
    }

	private List<PDHalftone> getHalftone() {
		COSDictionary dict = ((PDExtendedGraphicsState) this.simplePDObject).getCOSObject();
		COSBase halftone = dict.getDictionaryObject(COSName.getPDFName("HT"));
		boolean isDictionary = halftone instanceof COSDictionary;
		if (isDictionary || halftone instanceof COSName) {
			ArrayList<PDHalftone> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			if (isDictionary) {
				list.add(new PBoxPDHalftone((COSDictionary) halftone));
			} else {
				list.add(new PBoxPDHalftone((COSName) halftone));
			}
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

	private List<CosObject> getHalftonePhase() {
		COSDictionary dict = ((PDExtendedGraphicsState) this.simplePDObject).getCOSObject();
		COSBase halftonePhase = dict.getDictionaryObject(COSName.getPDFName("HTP"));
		CosObject value = PBCosObject.getFromValue(halftonePhase);
		if (value != null) {
			ArrayList<CosObject> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(value);
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

}
