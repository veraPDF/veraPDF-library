/**
 * 
 */
package org.verapdf.policy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 12 Dec 2016:19:20:59
 */

public class SchematronPipelineTest {
	private static final String resourceBase = "/org/verapdf/policy/"; //$NON-NLS-1$
	private static final String basicSchematronPath = resourceBase + "basic.sch"; //$NON-NLS-1$
	private static final String basicXslControlPath = resourceBase + "basic.xsl"; //$NON-NLS-1$

	@Test
	public void testProcessSchematron() throws IOException, TransformerException {

		File tempResult = File.createTempFile("veraPDF", "unitTest"); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(tempResult.length() == 0);
		try (InputStream is = SchematronPipelineTest.class.getResourceAsStream(basicSchematronPath);
				FileOutputStream fos = new FileOutputStream(tempResult)) {
			SchematronPipeline.processSchematron(is, fos);
		}
		assertTrue(tempResult.length() > 0);
		Source test = Input.fromFile(tempResult).build();
		Source control = Input.fromStream(this.getClass().getResourceAsStream(basicXslControlPath)).build();
		Diff myDiff = DiffBuilder.compare(control).checkForSimilar().ignoreComments().ignoreWhitespace().withTest(test)
				.build();
		assertFalse(myDiff.toString(), myDiff.hasDifferences());
	}

}
