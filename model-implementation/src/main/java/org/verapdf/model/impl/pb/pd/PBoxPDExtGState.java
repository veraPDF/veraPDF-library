package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingIntent;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosRenderingIntent;
import org.verapdf.model.impl.pb.cos.PBCosRenderingIntent;
import org.verapdf.model.pdlayer.PDExtGState;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDExtGState extends PBoxPDResources implements PDExtGState {

	private static final String RI = "RI";

	public PBoxPDExtGState(PDExtendedGraphicsState simplePDObject) {
		super(simplePDObject);
		setType("PDExtGState");
	}

	@Override
	public String getTR() {
		COSDictionary dictionary = ((PDExtendedGraphicsState) simplePDObject).getCOSObject();
		COSBase tr = dictionary.getDictionaryObject(COSName.TR);
		return getStringProperty(tr);
	}

	@Override
	public String getTR2() {
		COSDictionary dictionary = ((PDExtendedGraphicsState) simplePDObject).getCOSObject();
		COSBase tr2 = dictionary.getDictionaryObject(COSName.getPDFName("TR2"));
		return getStringProperty(tr2);
	}

	@Override
	public String getSMask() {
		COSDictionary dictionary = ((PDExtendedGraphicsState) simplePDObject).getCOSObject();
		COSBase sMask = dictionary.getDictionaryObject(COSName.SMASK);
		return getStringProperty(sMask);
	}

	@Override
	public String getBM() {
		COSDictionary dictionary = ((PDExtendedGraphicsState) simplePDObject).getCOSObject();
		COSBase sMask = dictionary.getDictionaryObject(COSName.BM);
		return getStringProperty(sMask);
	}

	@Override
	public Double getca() {
		return Double.valueOf(((PDExtendedGraphicsState) simplePDObject).getNonStrokingAlphaConstant());
	}

	@Override
	public Double getCA() {
		return Double.valueOf(((PDExtendedGraphicsState) simplePDObject).getStrokingAlphaConstant());
	}

	private String getStringProperty(COSBase base) {
		if (base != null) {
			return base instanceof COSName ? ((COSName) base).getName() : base.toString();
		} else {
			return null;
		}
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case RI:
				list = getRI();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<CosRenderingIntent> getRI() {
		List<CosRenderingIntent> renderingIntents = new ArrayList<>(1);
		RenderingIntent renderingIntent =
				((PDExtendedGraphicsState) simplePDObject).getRenderingIntent();
		if (renderingIntent != null) {
			COSName pdfName = COSName.getPDFName(renderingIntent.stringValue());
			renderingIntents.add(new PBCosRenderingIntent(pdfName));
		}
		return renderingIntents;
	}
}
