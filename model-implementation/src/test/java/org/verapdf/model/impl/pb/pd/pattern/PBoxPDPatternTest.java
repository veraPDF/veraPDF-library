package org.verapdf.model.impl.pb.pd.pattern;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.pdlayer.PDPattern;
import org.verapdf.model.tools.resources.PDInheritableResources;

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
		actual = getPattern(PDInheritableResources.getInstance(resources), patternCosName);
	}

	private static PDPattern getPattern(PDInheritableResources resources,
										COSName patternCosName)
			throws IOException {
		return ColorSpaceFactory.getPattern(resources.getPattern(patternCosName), resources);
	}

}
