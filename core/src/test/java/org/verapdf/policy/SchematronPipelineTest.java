/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
/**
 * 
 */
package org.verapdf.policy;

import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

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
		assertEquals(0, tempResult.length());
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
		tempResult.delete();
	}

}
