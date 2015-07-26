package org.verapdf.model.impl.pb.pd.pattern;

import org.apache.log4j.Logger;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDShading;
import org.verapdf.model.pdlayer.PDShadingPattern;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDShadingPattern extends PBoxPDPattern implements PDShadingPattern {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDShadingPattern.class);

	public static final String SHADING = "shading";

	public PBoxPDShadingPattern(org.apache.pdfbox.pdmodel.graphics.pattern.PDShadingPattern simplePDObject) {
		super(simplePDObject);
		setType("PDShadingPattern");
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		return  SHADING.equals(link) ? getShading() : super.getLinkedObjects(link);
	}

	private List<PDShading> getShading() {
		List<PDShading> shadings = new ArrayList<>();
		try {
			org.apache.pdfbox.pdmodel.graphics.shading.PDShading shading =
					((org.apache.pdfbox.pdmodel.graphics.pattern.PDShadingPattern) simplePDObject).getShading();
			if (shading != null) {
				shadings.add(new PBoxPDShading(shading));
			}
		} catch (IOException e) {
			LOGGER.error("Can`t get shading pattern. " + e.getMessage(), e);
		}
		return shadings;
	}
}
