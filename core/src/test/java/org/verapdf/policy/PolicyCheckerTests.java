/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.policy;

import org.junit.Test;
import org.verapdf.core.VeraPDFException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

import javax.xml.transform.Source;
import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PolicyCheckerTests {
	private static final String resourceBase = "/org/verapdf/policy/"; //$NON-NLS-1$
	private static final String fontChecksSchematron = resourceBase + "font-checks.sch"; //$NON-NLS-1$
	private static final String fontChecksStylesheet = resourceBase + "font-checks.xsl"; //$NON-NLS-1$
	private static final String fontChecksMrr = resourceBase + "font-checks-mrr.xml"; //$NON-NLS-1$
	private static final String fontChecksPolicyControlPath = resourceBase + "font-checks-policy-result.xml"; //$NON-NLS-1$
	public static final List<String> allowedExtensions = Arrays.asList("sch", "xsl", "xslt"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	public static final List<String> disallowedExtensions = Arrays.asList("", "sc", "schr", "xs", "xslts"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	public static final List<String> testFileNames = Arrays.asList("", "test", "anotherTest", "sch", "xsl", "xslt"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

	@Test
	public void testApplySchematronPolicy() throws VeraPDFException, IOException {
		File tempResult = File.createTempFile("veraPDF", "unitTest"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(0, tempResult.length());
		try (InputStream policyIs = PolicyCheckerTests.class.getResourceAsStream(fontChecksSchematron);
				InputStream mrrIs = PolicyCheckerTests.class.getResourceAsStream(fontChecksMrr);
				FileOutputStream fosPolicyResult = new FileOutputStream(tempResult)) {
			PolicyChecker.applyPolicy(policyIs, mrrIs, fosPolicyResult, false);
		}
		assertTrue(tempResult.length() > 0);
		Source test = Input.fromFile(tempResult).build();
		Source control = Input.fromStream(this.getClass().getResourceAsStream(fontChecksPolicyControlPath)).build();
		Diff myDiff = DiffBuilder.compare(control).checkForSimilar().ignoreComments().ignoreWhitespace().withTest(test)
				.build();
		assertFalse(myDiff.toString(), myDiff.hasDifferences());
		tempResult.delete();
	}

	@Test
	public void testIsFilenameAllowedExtension() {
		for (String nameRoot : testFileNames) {
			assertFalse(nameRoot + " should be disallowed.", PolicyChecker.isFilenameAllowedExtension(nameRoot)); //$NON-NLS-1$
			for (String ext : allowedExtensions) {
				String filename = nameRoot + "." + ext; //$NON-NLS-1$
				assertTrue(filename + " should be allowed.", PolicyChecker.isFilenameAllowedExtension(filename)); //$NON-NLS-1$
			}
			for (String ext : disallowedExtensions) {
				String filename = nameRoot + "." + ext; //$NON-NLS-1$
				assertFalse(filename + " should be disallowed.", PolicyChecker.isFilenameAllowedExtension(filename)); //$NON-NLS-1$
			}
		}
	}

	@Test
	public void testIsAllowedExtension() {
		for (String ext : allowedExtensions) {
			assertTrue(PolicyChecker.isAllowedExtension(ext));
		}
		for (String ext : disallowedExtensions) {
			assertFalse(PolicyChecker.isAllowedExtension(ext));
		}
	}

	@Test
	public void testApplyPolicyStyleSheet() throws IOException, VeraPDFException {
		File tempResult = File.createTempFile("veraPDF", "unitTest"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(0, tempResult.length());
		try (InputStream policyIs = PolicyCheckerTests.class.getResourceAsStream(fontChecksStylesheet);
				InputStream mrrIs = PolicyCheckerTests.class.getResourceAsStream(fontChecksMrr);
				FileOutputStream fosPolicyResult = new FileOutputStream(tempResult)) {
			PolicyChecker.applyPolicy(policyIs, mrrIs, fosPolicyResult, true);
		}
		assertTrue(tempResult.length() > 0);
		Source test = Input.fromFile(tempResult).build();
		Source control = Input.fromStream(this.getClass().getResourceAsStream(fontChecksPolicyControlPath)).build();
		Diff myDiff = DiffBuilder.compare(control).checkForSimilar().ignoreComments().ignoreWhitespace().withTest(test)
				.build();
		assertFalse(myDiff.toString(), myDiff.hasDifferences());
		tempResult.delete();
	}

	@Test
	public void testInsertPolicyReport() throws IOException, VeraPDFException {
		File policyReport = File.createTempFile("policyReport", "veraPDF"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(0, policyReport.length());
		try (InputStream policyIs = PolicyCheckerTests.class.getResourceAsStream(fontChecksStylesheet);
				InputStream mrrIs = PolicyCheckerTests.class.getResourceAsStream(fontChecksMrr);
				FileOutputStream fosPolicyResult = new FileOutputStream(policyReport)) {
			PolicyChecker.applyPolicy(policyIs, mrrIs, fosPolicyResult, true);
		}
		File mrrFile = File.createTempFile("mrr", "veraPDF"); //$NON-NLS-1$ //$NON-NLS-2$
		try (OutputStream fos = new FileOutputStream(mrrFile)) {
			copyStreamToFile(PolicyCheckerTests.class.getResourceAsStream(fontChecksMrr), fos);
		}
		File tempResult = File.createTempFile("veraPDF", "mergeTest"); //$NON-NLS-1$ //$NON-NLS-2$
		try (OutputStream fos = new FileOutputStream(tempResult)) {
			PolicyChecker.insertPolicyReport(policyReport, mrrFile, fos);
		}
		policyReport.delete();
		mrrFile.delete();
		tempResult.delete();
	}

	private static void copyStreamToFile(final InputStream in, final OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len = in.read(buffer);
		while (len != -1) {
			out.write(buffer, 0, len);
			len = in.read(buffer);
		}
	}
}
