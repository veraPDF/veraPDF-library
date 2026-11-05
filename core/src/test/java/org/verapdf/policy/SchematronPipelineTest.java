/*
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2026, veraPDF Consortium <info@verapdf.org>
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

	@Test
	public void testSchematronIncludeCannotReadLocalFiles() throws IOException {
		File included = File.createTempFile("veraPDF", "include"); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			String fragment = "<sch:pattern xmlns:sch=\"http://purl.oclc.org/dsdl/schematron\" id=\"p\">" //$NON-NLS-1$
					+ "<sch:rule context=\"/\"><sch:assert test=\"false()\">LOCAL_INCLUDE_MARKER</sch:assert>" //$NON-NLS-1$
					+ "</sch:rule></sch:pattern>"; //$NON-NLS-1$
			try (OutputStream os = new FileOutputStream(included)) {
				os.write(fragment.getBytes("UTF-8")); //$NON-NLS-1$
			}
			String includePath = included.getAbsolutePath().replace('\\', '/').replace("&", "&amp;") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					.replace("<", "&lt;").replace("\"", "&quot;"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			String schematron = "<sch:schema xmlns:sch=\"http://purl.oclc.org/dsdl/schematron\">" //$NON-NLS-1$
					+ "<sch:include href=\"" + includePath + "\"/></sch:schema>"; //$NON-NLS-1$ //$NON-NLS-2$
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			try {
				SchematronPipeline.processSchematron(new ByteArrayInputStream(schematron.getBytes("UTF-8")), result); //$NON-NLS-1$
			} catch (TransformerException e) {
				// rejecting the schematron is acceptable
			}
			assertFalse(result.toString("UTF-8").contains("LOCAL_INCLUDE_MARKER")); //$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			included.delete();
		}
	}

}
