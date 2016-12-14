package org.verapdf.policy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.Source;

import org.junit.Test;
import org.verapdf.core.VeraPDFException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

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
		assertTrue(tempResult.length() == 0);
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
		assertTrue(tempResult.length() == 0);
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
	}

	@Test
	public void testInsertPolicyReport() throws IOException, VeraPDFException {
		File policyReport = new File(new File("."), "policyResult.xml"); //$NON-NLS-1$ //$NON-NLS-2$
		if (policyReport.isFile()) {
			policyReport.delete();
		}
		policyReport.createNewFile();
		assertTrue(policyReport.length() == 0);
		try (InputStream policyIs = PolicyCheckerTests.class.getResourceAsStream(fontChecksStylesheet);
				InputStream mrrIs = PolicyCheckerTests.class.getResourceAsStream(fontChecksMrr);
				FileOutputStream fosPolicyResult = new FileOutputStream(policyReport)) {
			PolicyChecker.applyPolicy(policyIs, mrrIs, fosPolicyResult, true);
		}
		File mrrFile = new File(policyReport.getParentFile(), "mrr.xml"); //$NON-NLS-1$
		if (mrrFile.isFile()) {
			mrrFile.delete();
		}
		mrrFile.createNewFile();
		try (OutputStream fos = new FileOutputStream(mrrFile)) {
			copyStreamToFile(PolicyCheckerTests.class.getResourceAsStream(fontChecksMrr), fos);
		}
		File tempResult = File.createTempFile("veraPDF", "mergeTest"); //$NON-NLS-1$ //$NON-NLS-2$
		try (OutputStream fos = new FileOutputStream(tempResult)) {
			PolicyChecker.insertPolicyReport(tempResult, mrrFile, fos);
		}
		policyReport.delete();
		mrrFile.delete();
	}

	private void copyStreamToFile(final InputStream in, final OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len = in.read(buffer);
		while (len != -1) {
			out.write(buffer, 0, len);
			len = in.read(buffer);
		}
	}
}
