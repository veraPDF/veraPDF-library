package org.verapdf.model.impl.pb.pd.pattern;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDShadingPattern;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDTilingPattern;
import org.verapdf.model.impl.BaseTest;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBoxPDPatternTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "pd/ColorSpaces.pdf";

	protected static void setUp(String patternType, String patternName)
			throws URISyntaxException, IOException {
		expectedType = TYPES.contains(patternType) ? patternType : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		PDResources resources = document.getPage(0).getResources();
		COSName patternCosName = COSName.getPDFName(patternName);
		actual = getPattern(resources, patternCosName, patternType);
	}

	private static PBoxPDPattern getPattern(PDResources resources,
												   COSName patternCosName,
												   String patternType)
			throws IOException {
		if (PBoxPDShadingPattern.SHADING_PATTERN_TYPE.equals(patternType)) {
			return new PBoxPDShadingPattern((PDShadingPattern)
					resources.getPattern(patternCosName));
		} else {
			return new PBoxPDTilingPattern((PDTilingPattern) resources.getPattern(patternCosName));
		}
	}

}
