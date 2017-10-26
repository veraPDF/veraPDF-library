/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org> All rights
 * reserved. veraPDF Library core is free software: you can redistribute it
 * and/or modify it under the terms of either: The GNU General public license
 * GPLv3+. You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the
 * source tree. If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html. The Mozilla Public License
 * MPLv2+. You should have received a copy of the Mozilla Public License along
 * with veraPDF Library core as the LICENSE.MPL file in the root of the source
 * tree. If a copy of the MPL was not distributed with this file, you can obtain
 * one at http://mozilla.org/MPL/2.0/.
 */
/**
 * 
 */
package org.verapdf.policy;

import org.verapdf.core.VeraPDFException;
import org.verapdf.core.utils.FileUtils;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * The veraPDF policy checker which is simply an abstraction that makes applying
 * XML schematron to veraPDF reports straightforward.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 12 Dec 2016:17:51:12
 */
public final class PolicyChecker {
	private static final TransformerFactory factory = TransformerFactory.newInstance();
	public static final String SCHEMA_EXT = "sch"; //$NON-NLS-1$
	public static final String XSL_EXT = "xsl"; //$NON-NLS-1$
	public static final String XSLT_EXT = "xslt"; //$NON-NLS-1$
	/**
	 * A {@link List} of allowed extensions for the passed schmeatron rules.
	 * These can be:
	 * <ul>
	 * <li>.sch which is a raw, uncompiled schematron file which will be
	 * compiled to the appropriate XSL by the policy checker.</li>
	 * <li>.xsl which is a pre-compiled schematron represented as an XSLT
	 * file.</li>
	 * <li>.xslt the longer form of the .xsl extension, again a pre-compiled
	 * schematron file.</li>
	 * </ul>
	 */
	public static final List<String> allowedExtensions = Arrays.asList(SCHEMA_EXT, XSL_EXT, XSLT_EXT);
	private static final String resourcePath = "org/verapdf/policy/"; //$NON-NLS-1$
	private static final String mergeXsl = resourcePath + "MergeMrrPolicy" + '.' + XSL_EXT; //$NON-NLS-1$
	private static final Templates cachedMergeXsl = SchematronPipeline.createCachedTransform(mergeXsl);

	private PolicyChecker() {

	}

	/**
	 * Adds a policy report to a veraPDF machine readable report instance,
	 * effectively a merge carried out by an XSLT transform.
	 * 
	 * @param policyReport
	 *            the XML policy report {@link File} to add to the machine
	 *            readable report.
	 * @param mrrReport
	 *            the XML machine readable report {@link File} to add the policy
	 *            report to
	 * @param mergedReport
	 *            and {@link OutputStream} destination for the merged report.
	 * @throws VeraPDFException
	 *             when there's an error merging the reports.
	 */
	public static void insertPolicyReport(final File policyReport, final File mrrReport,
			final OutputStream mergedReport) throws VeraPDFException {
		try {
			Transformer transformer = cachedMergeXsl.newTransformer();
			transformer.setParameter("policyResultPath", policyReport.getAbsolutePath()); //$NON-NLS-1$
			transformer.transform(new StreamSource(mrrReport), new StreamResult(mergedReport));
			return;
		} catch (TransformerException excep) {
			throw new VeraPDFException("Problem merging XML files.", excep); //$NON-NLS-1$
		}
	}

	/**
	 * Apply a veraPDF policy expressed as schematron to a veraPDF report. Note
	 * that the schematron can be a "raw" schematron file
	 * 
	 * @param policy
	 *            a {@link File} that is either a raw schematron file (.sch
	 *            extenstion) or pre-compiled schematron file (.xsl or .xslt
	 *            extenstion), see {@link PolicyChecker#allowedExtensions}
	 * @param xmlReport
	 *            an {@link InputStream} for the veraPDF XML report to apply
	 *            policy to
	 * @param policyReport
	 *            an {@link OutputStream} to which the ouptut policy file will
	 *            be written
	 * @throws VeraPDFException
	 *             when there's a problem applying the policy schematron rules.
	 */
	public static void applyPolicy(final File policy, final InputStream xmlReport, final OutputStream policyReport)
			throws VeraPDFException {
		// Get the file extension
		String ext = FileUtils.extFromFileName(policy.getName());
		if (!isAllowedExtension(ext)) {
			throw new VeraPDFException("Policy file extension must be one of " + SCHEMA_EXT + ", " + XSL_EXT //$NON-NLS-1$//$NON-NLS-2$
					+ ", or " + XSLT_EXT); //$NON-NLS-1$
		}
		boolean isXsl = !ext.equalsIgnoreCase(SCHEMA_EXT);
		try (FileInputStream fis = new FileInputStream(policy)) {
			applyPolicy(fis, xmlReport, policyReport, isXsl);
		} catch (IOException excep) {
			throw new VeraPDFException("IOException applying policy file " + policy.getAbsolutePath(), excep); //$NON-NLS-1$
		}
	}

	/**
	 * Check if a filename has an allowed policy file extension
	 * 
	 * @param filename
	 *            the filename to check
	 * @return true if the filename has a recognised policy extension
	 */
	public static boolean isFilenameAllowedExtension(final String filename) {
		return isAllowedExtension(FileUtils.extFromFileName(filename));
	}

	/**
	 * Check if an extenstion is an allowed policy file extension
	 * 
	 * @param ext
	 *            the extension to check
	 * @return true if the extension is a recognised policy extension
	 */
	public static boolean isAllowedExtension(final String ext) {
		return allowedExtensions.contains(ext.toLowerCase());
	}

	/**
	 * Apply a veraPDF policy expressed as schematron to a veraPDF report. Note
	 * that the schematron can be a "raw" schematron file
	 * 
	 * @param policy
	 *            an {@link InputStream} for either raw schematron or
	 *            pre-compiled schematron XSL.
	 * @param xmlReport
	 *            an {@link InputStream} for the veraPDF XML report to apply
	 *            policy to
	 * @param policyReport
	 *            an {@link OutputStream} to which the ouptut policy file will
	 *            be written
	 * @param isXsl
	 *            set {@code true} if the {@code policy} stream is a
	 *            pre-compiled XSL policy document, false if it's a raw
	 *            Schematron document.
	 * @throws VeraPDFException
	 *             when there's a problem applying the policy schematron rules.
	 */
	public static void applyPolicy(final InputStream policy, final InputStream xmlReport,
			final OutputStream policyReport, boolean isXsl) throws VeraPDFException {
		try {
			if (isXsl) {
				applySchematronXsl(policy, xmlReport, policyReport);
			} else {
				applyRawSchematron(policy, xmlReport, policyReport);
			}
		} catch (IOException | TransformerException excep) {
			throw new VeraPDFException("Exception when applying policy file.", excep); //$NON-NLS-1$
		}
	}

	private static void applyRawSchematron(final InputStream rawSchematron, final InputStream xmlReport,
			final OutputStream policyReport) throws TransformerException, IOException {
		File schemaXsl = createSchematronXslFile(rawSchematron);
		try (FileInputStream fis = new FileInputStream(schemaXsl)) {
			applySchematronXsl(fis, xmlReport, policyReport);
		}
		schemaXsl.delete();
	}

	private static File createSchematronXslFile(final InputStream rawSchematron)
			throws TransformerException, IOException {
		File resXsl = File.createTempFile("veraPDF_", "SchXsl"); //$NON-NLS-1$ //$NON-NLS-2$
		try (FileOutputStream fos = new FileOutputStream(resXsl)) {
			SchematronPipeline.processSchematron(rawSchematron, fos);
		}
		return resXsl;
	}

	private static void applySchematronXsl(final InputStream schematronXsl, final InputStream xmlReport,
			final OutputStream policyReport) throws TransformerException {
		Transformer transformer = factory.newTransformer(new StreamSource(schematronXsl));
		transformer.transform(new StreamSource(xmlReport), new StreamResult(policyReport));
	}
}
