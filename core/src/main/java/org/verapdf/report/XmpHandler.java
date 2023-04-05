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
/**
 *
 */
package org.verapdf.report;

import org.verapdf.features.tools.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import jakarta.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Class that's initially a placeholder for XMP specific functionality.
 * <p>
 * This isn't the final approach to XMP, two things need to change:
 * <ul>
 * <li>TODO: we need dedicated XMP parsing and reporting classes</li>
 * <li>TODO: the FeatureReporting mechanism needs re-factoring / re-designing</li>
 * </ul>
 * Regarding the FeatureReporting mechanism, it's crying out for a generics
 * based approach but that can come over the re-design.
 * </p>
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public class XmpHandler {
	private static final byte[] UTF8_METADATA_PREFIX_SQ = {0x3C, 0x3F, 0x78,
			0x70, 0x61, 0x63, 0x6B, 0x65, 0x74, 0x20, 0x62, 0x65, 0x67, 0x69,
			0x6E, 0x3D, 0x27, -0x11, -0x45, -0x41, 0x27};
	private static final byte[] UTF8_METADATA_PREFIX_DQ = {0x3C, 0x3F, 0x78,
			0x70, 0x61, 0x63, 0x6B, 0x65, 0x74, 0x20, 0x62, 0x65, 0x67, 0x69,
			0x6E, 0x3D, 0x22, -0x11, -0x45, -0x41, 0x22};
	private static final byte[] UTF8_METADATA_PREFIX_SQ_EMPTY = {0x3C, 0x3F,
			0x78, 0x70, 0x61, 0x63, 0x6B, 0x65, 0x74, 0x20, 0x62, 0x65, 0x67,
			0x69, 0x6E, 0x3D, 0x27, 0x27};
	private static final byte[] UTF8_METADATA_PREFIX_DQ_EMPTY = {0x3C, 0x3F,
			0x78, 0x70, 0x61, 0x63, 0x6B, 0x65, 0x74, 0x20, 0x62, 0x65, 0x67,
			0x69, 0x6E, 0x3D, 0x22, 0x22};
	private static final byte[] UTF16BE_METADATA_PREFIX_SQ = {0x00, 0x3C,
			0x00, 0x3F, 0x00, 0x78, 0x00, 0x70, 0x00, 0x61, 0x00, 0x63, 0x00,
			0x6B, 0x00, 0x65, 0x00, 0x74, 0x00, 0x20, 0x00, 0x62, 0x00, 0x65,
			0x00, 0x67, 0x00, 0x69, 0x00, 0x6E, 0x00, 0x3D, 0x00, 0x27, -0x02,
			-0x01, 0x00, 0x27};
	private static final byte[] UTF16BE_METADATA_PREFIX_DQ = {0x00, 0x3C,
			0x00, 0x3F, 0x00, 0x78, 0x00, 0x70, 0x00, 0x61, 0x00, 0x63, 0x00,
			0x6B, 0x00, 0x65, 0x00, 0x74, 0x00, 0x20, 0x00, 0x62, 0x00, 0x65,
			0x00, 0x67, 0x00, 0x69, 0x00, 0x6E, 0x00, 0x3D, 0x00, 0x22, -0x02,
			-0x01, 0x00, 0x22};
	private static final byte[] UTF16LE_METADATA_PREFIX_SQ = {0x3C, 0x00,
			0x3F, 0x00, 0x78, 0x00, 0x70, 0x00, 0x61, 0x00, 0x63, 0x00, 0x6B,
			0x00, 0x65, 0x00, 0x74, 0x00, 0x20, 0x00, 0x62, 0x00, 0x65, 0x00,
			0x67, 0x00, 0x69, 0x00, 0x6E, 0x00, 0x3D, 0x00, 0x27, 0x00, -0x01,
			-0x02, 0x27, 0x00};
	private static final byte[] UTF16LE_METADATA_PREFIX_DQ = {0x3C, 0x00,
			0x3F, 0x00, 0x78, 0x00, 0x70, 0x00, 0x61, 0x00, 0x63, 0x00, 0x6B,
			0x00, 0x65, 0x00, 0x74, 0x00, 0x20, 0x00, 0x62, 0x00, 0x65, 0x00,
			0x67, 0x00, 0x69, 0x00, 0x6E, 0x00, 0x3D, 0x00, 0x22, 0x00, -0x01,
			-0x02, 0x22, 0x00};
	private static final byte[] UTF32BE_METADATA_PREFIX_SQ = {0x00, 0x00,
			0x00, 0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78, 0x00,
			0x00, 0x00, 0x70, 0x00, 0x00, 0x00, 0x61, 0x00, 0x00, 0x00, 0x63,
			0x00, 0x00, 0x00, 0x6B, 0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00,
			0x74, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x62, 0x00, 0x00,
			0x00, 0x65, 0x00, 0x00, 0x00, 0x67, 0x00, 0x00, 0x00, 0x69, 0x00,
			0x00, 0x00, 0x6E, 0x00, 0x00, 0x00, 0x3D, 0x00, 0x00, 0x00, 0x27,
			0x00, 0x00, -0x02, -0x01, 0x00, 0x00, 0x00, 0x27};
	private static final byte[] UTF32BE_METADATA_PREFIX_DQ = {0x00, 0x00,
			0x00, 0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78, 0x00,
			0x00, 0x00, 0x70, 0x00, 0x00, 0x00, 0x61, 0x00, 0x00, 0x00, 0x63,
			0x00, 0x00, 0x00, 0x6B, 0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00,
			0x74, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x62, 0x00, 0x00,
			0x00, 0x65, 0x00, 0x00, 0x00, 0x67, 0x00, 0x00, 0x00, 0x69, 0x00,
			0x00, 0x00, 0x6E, 0x00, 0x00, 0x00, 0x3D, 0x00, 0x00, 0x00, 0x22,
			0x00, 0x00, -0x02, -0x01, 0x00, 0x00, 0x00, 0x22};
	private static final byte[] UTF32LE_METADATA_PREFIX_SQ = {0x3C, 0x00,
			0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78, 0x00, 0x00, 0x00, 0x70,
			0x00, 0x00, 0x00, 0x61, 0x00, 0x00, 0x00, 0x63, 0x00, 0x00, 0x00,
			0x6B, 0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00, 0x74, 0x00, 0x00,
			0x00, 0x20, 0x00, 0x00, 0x00, 0x62, 0x00, 0x00, 0x00, 0x65, 0x00,
			0x00, 0x00, 0x67, 0x00, 0x00, 0x00, 0x69, 0x00, 0x00, 0x00, 0x6E,
			0x00, 0x00, 0x00, 0x3D, 0x00, 0x00, 0x00, 0x27, 0x00, 0x00, 0x00,
			-0x01, -0x02, 0x00, 0x00, 0x27, 0x00, 0x00, 0x00};
	private static final byte[] UTF32LE_METADATA_PREFIX_DQ = {0x3C, 0x00,
			0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78, 0x00, 0x00, 0x00, 0x70,
			0x00, 0x00, 0x00, 0x61, 0x00, 0x00, 0x00, 0x63, 0x00, 0x00, 0x00,
			0x6B, 0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00, 0x74, 0x00, 0x00,
			0x00, 0x20, 0x00, 0x00, 0x00, 0x62, 0x00, 0x00, 0x00, 0x65, 0x00,
			0x00, 0x00, 0x67, 0x00, 0x00, 0x00, 0x69, 0x00, 0x00, 0x00, 0x6E,
			0x00, 0x00, 0x00, 0x3D, 0x00, 0x00, 0x00, 0x22, 0x00, 0x00, 0x00,
			-0x01, -0x02, 0x00, 0x00, 0x22, 0x00, 0x00, 0x00};

	private XmpHandler() {

	}


	/**
	 * @param metadataNode
	 * @return the parsed String metadata value
	 */
	public static Node parseMetadataRootElement(FeatureTreeNode metadataNode)
			throws SAXException, IOException, ParserConfigurationException {

		InputSource is = getInputSourceWithEncoding(DatatypeConverter
				.parseHexBinary(metadataNode.getValue()));
		if (is == null) {
			return null;
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document metadataDocument = builder.parse(is);
		return metadataDocument.getDocumentElement();
	}

	private static InputSource getInputSourceWithEncoding(byte[] array) {
		if (array != null) {
			for (int i = 0; i < array.length; ++i) {
				String encoding = getEncodingWithBegin(array, i);
				if (encoding != null) {
					ByteArrayInputStream is = new ByteArrayInputStream(
							Arrays.copyOfRange(array, i, array.length));
					InputSource source = new InputSource(is);
					source.setEncoding(encoding);
					return source;
				}
			}
		}

		return null;
	}

	private static String getEncodingWithBegin(byte[] bStream, int beginOffset) {
		if (beginOffset >= 0) {
			if (matchesFrom(bStream, beginOffset, UTF32BE_METADATA_PREFIX_DQ)
					|| matchesFrom(bStream, beginOffset,
					UTF32BE_METADATA_PREFIX_SQ)) {
				return "UTF-32BE";
			} else if (matchesFrom(bStream, beginOffset,
					UTF32LE_METADATA_PREFIX_DQ)
					|| matchesFrom(bStream, beginOffset,
					UTF32LE_METADATA_PREFIX_SQ)) {
				return "UTF-32LE";
			} else if (matchesFrom(bStream, beginOffset,
					UTF16BE_METADATA_PREFIX_DQ)
					|| matchesFrom(bStream, beginOffset,
					UTF16BE_METADATA_PREFIX_SQ)) {
				return "UTF-16BE";
			} else if (matchesFrom(bStream, beginOffset,
					UTF16LE_METADATA_PREFIX_DQ)
					|| matchesFrom(bStream, beginOffset,
					UTF16LE_METADATA_PREFIX_SQ)) {
				return "UTF-16LE";
			} else if (matchesFrom(bStream, beginOffset,
					UTF8_METADATA_PREFIX_DQ)
					|| matchesFrom(bStream, beginOffset,
					UTF8_METADATA_PREFIX_DQ_EMPTY)
					|| matchesFrom(bStream, beginOffset,
					UTF8_METADATA_PREFIX_SQ)
					|| matchesFrom(bStream, beginOffset,
					UTF8_METADATA_PREFIX_SQ_EMPTY)) {
				return "UTF-8";
			}
		}
		return null;
	}

	private static boolean matchesFrom(byte[] source, int from, byte[] match) {
		if (match.length > source.length - from) {
			return false;
		}
		for (int i = 0; i < match.length; i++) {
			if (source[i + from] != match[i]) {
				return false;
			}
		}
		return true;
	}

}
