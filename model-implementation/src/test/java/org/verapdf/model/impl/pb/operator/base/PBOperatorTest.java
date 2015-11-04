package org.verapdf.model.impl.pb.operator.base;

import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.cos.COSObjectKey;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDResources;
import org.junit.Assert;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.factory.operator.OperatorFactory;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOperatorTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "operator/Operators.pdf";

	private static final COSObjectKey KEY = new COSObjectKey(15, 0);

	protected static void setUpOperatorsList(String type, String id) throws IOException, URISyntaxException {
		expectedType = TYPES.contains(type) ? type : null;
		expectedID = id;
		setUp(FILE_RELATIVE_PATH);

		COSObject objectFromPool = document.getDocument().getObjectFromPool(KEY);
		COSStream stream = (COSStream) objectFromPool.getObject();
		PDResources resources = document.getPage(0).getResources();

		PDFStreamParser parser = new PDFStreamParser(stream, true);
		parser.parse();

		List<Operator> operators = OperatorFactory.operatorsFromTokens(parser.getTokens(),
				PDInheritableResources.getInstance(resources));
		actual = getActual(operators, expectedType);

		operators.clear();
	}

	private static Object getActual(List<Operator> operators, String type) {
		for (Operator operator : operators) {
			if (type.equals(operator.getObjectType())) {
				return operator;
			}
		}

		return null;
	}

	protected void testLinksToReals(String link, int count, String type) {
		List<? extends Object> linkedObjects = actual.getLinkedObjects(link);

		Assert.assertEquals(count, linkedObjects.size());

		for (Object object : linkedObjects) {
			Assert.assertEquals(type, object.getObjectType());
		}

	}

	protected static void testLinkToDictionary(String link, String type, Number expectedResult) {
		Object object = testObject(link, 1, type);
		Assert.assertEquals(expectedResult, ((CosDict) object).getsize());
	}

	protected static Object testObject(String link, int count, String type) {
		List<? extends Object> linkedObjects = actual.getLinkedObjects(link);

		Assert.assertEquals(count, linkedObjects.size());

		if (count > 0) {
			Object object = linkedObjects.get(0);
			Assert.assertEquals(type, object.getObjectType());
			return object;
		}

		return null;
	}

	protected static void testReal(String link, int expectedValue) {
		CosReal object = (CosReal) testObject(link, 1, PBCosReal.COS_REAL_TYPE);
		Assert.assertNotNull(object);
		Assert.assertEquals(Long.valueOf(expectedValue), object.getintValue());
	}

}
