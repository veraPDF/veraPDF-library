package org.verapdf.model.factory.operator;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingIntent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
@RunWith(Parameterized.class)
public class OperatorFactoryTest {

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		List<Object[]> parameters = new ArrayList<>();
		parameters.add(new Object[] {new COSString("Test COSBase case."), 0});
		parameters.add(new Object[] {Operator.getOperator("q"), 1});
		parameters.add(new Object[] {RenderingIntent.ABSOLUTE_COLORIMETRIC, 0});
		parameters.add(new Object[] {"Unsupported type of object.", 0});
		return parameters;
	}

	@Parameterized.Parameter
	public Object fInput;

	@Parameterized.Parameter(value = 1)
	public int fExpected;

	@Test
	public void testOperatorsFromTokensMethod() {
		List<Object> input = new ArrayList<>(1);
		input.add(fInput);
		Assert.assertEquals(fExpected, OperatorFactory.operatorsFromTokens(input, null).size());
	}

}
