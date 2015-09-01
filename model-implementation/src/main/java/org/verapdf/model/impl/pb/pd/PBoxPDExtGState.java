package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.PDFontSetting;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingIntent;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.coslayer.CosRenderingIntent;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.impl.pb.cos.PBCosRenderingIntent;
import org.verapdf.model.pdlayer.PDExtGState;

import java.util.ArrayList;
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

    public PBoxPDExtGState(PDExtendedGraphicsState simplePDObject) {
        super(simplePDObject, EXT_G_STATE_TYPE);
    }

    @Override
    public String getTR() {
        COSDictionary dictionary = ((PDExtendedGraphicsState) this.simplePDObject)
                .getCOSObject();
        COSBase tr = dictionary.getDictionaryObject(COSName.TR);
        return getStringProperty(tr);
    }

    @Override
    public String getTR2() {
        COSDictionary dictionary = ((PDExtendedGraphicsState) this.simplePDObject)
                .getCOSObject();
        COSBase tr2 = dictionary.getDictionaryObject(COSName.getPDFName("TR2"));
        return getStringProperty(tr2);
    }

    @Override
    public String getSMask() {
        COSDictionary dictionary = ((PDExtendedGraphicsState) this.simplePDObject)
                .getCOSObject();
        COSBase sMask = dictionary.getDictionaryObject(COSName.SMASK);
        return getStringProperty(sMask);
    }

    @Override
    public String getBM() {
        COSDictionary dictionary = ((PDExtendedGraphicsState) this.simplePDObject)
                .getCOSObject();
        COSBase sMask = dictionary.getDictionaryObject(COSName.BM);
        return getStringProperty(sMask);
    }

    @Override
    public Double getca() {
        Float nonStrokingAlphaConstant = ((PDExtendedGraphicsState) this.simplePDObject)
                .getNonStrokingAlphaConstant();
        return nonStrokingAlphaConstant != null ? Double
                .valueOf(nonStrokingAlphaConstant.doubleValue()) : null;
    }

    @Override
    public Double getCA() {
        Float strokingAlphaConstant = ((PDExtendedGraphicsState) this.simplePDObject)
                .getStrokingAlphaConstant();
        return strokingAlphaConstant != null ? Double
                .valueOf(strokingAlphaConstant.doubleValue()) : null;
    }

    private static String getStringProperty(COSBase base) {
        if (base != null) {
            return base instanceof COSName ? ((COSName) base).getName() : base
                    .toString();
        }
        return null;
    }

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case RI:
				return this.getRI();
			case FONT_SIZE:
				return this.getFontSize();
			default:
				return super.getLinkedObjects(link);
		}
	}

    private List<CosRenderingIntent> getRI() {
        List<CosRenderingIntent> renderingIntents = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        RenderingIntent renderingIntent = ((PDExtendedGraphicsState) this.simplePDObject)
                .getRenderingIntent();
        if (renderingIntent != null) {
            COSName pdfName = COSName.getPDFName(renderingIntent.stringValue());
            renderingIntents.add(new PBCosRenderingIntent(pdfName));
        }
        return renderingIntents;
    }

    private List<CosReal> getFontSize() {
        List<CosReal> result = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        PDFontSetting fontSetting = ((PDExtendedGraphicsState) this.simplePDObject)
                .getFontSetting();
        if (fontSetting != null) {
            result.add(new PBCosReal(new COSFloat(fontSetting.getFontSize())));
        }

        return result;
    }

}
