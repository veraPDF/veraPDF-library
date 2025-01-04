/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.report;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Maksim Bezrukov
 */
public final class XsltTransformer {

	private static final Logger LOGGER = Logger.getLogger(XsltTransformer.class.getCanonicalName());

	private static final TransformerFactory factory = getTransformerFactory();
	
	private XsltTransformer() {
	}

	/**
	 * Creates html validation report
	 *
	 * @param source
	 *            an {@link InputStream} instance that is the source Machine
	 *            Readable Report.
	 * @param destination
	 *            an {@link PrintWriter} to write the HTML report to.
	 * @throws TransformerException
	 *             if an unrecoverable error occurs during the course of the
	 *             transformation
	 * @throws TransformerException
	 *             file system exceptions
	 */
	public static void transform(InputStream source, InputStream xslt, PrintWriter destination,
			Map<String, String> arguments) throws TransformerException {

		Transformer transformer = factory.newTransformer(new StreamSource(xslt));

		if (arguments != null) {
			for (Map.Entry<String, String> entry : arguments.entrySet()) {
				if (entry.getKey() != null && entry.getValue() != null) {
					transformer.setParameter(entry.getKey(), entry.getValue());
				}
			}
		}

		transformer.transform(new StreamSource(source), new StreamResult(destination));
	}

	private static TransformerFactory getTransformerFactory() {
		TransformerFactory fact = TransformerFactory.newInstance();
		try {
			fact.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			fact.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "file");
		} catch (TransformerConfigurationException e) {
			LOGGER.log(Level.WARNING, "Unable to secure xsl transformer");
		}
		return fact;
	}
}
