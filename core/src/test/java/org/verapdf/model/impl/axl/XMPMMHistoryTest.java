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
package org.verapdf.model.impl.axl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import org.verapdf.xmp.XMPException;
import org.verapdf.xmp.impl.VeraPDFMeta;

/**
 * @author Maksim Bezrukov
 */
public class XMPMMHistoryTest {

	@Test
	public void test() throws URISyntaxException, XMPException, IOException {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream(("org/verapdf/model/impl/axl/xmpMM-History.xml"))) {
			VeraPDFMeta meta = VeraPDFMeta.parse(in);
			AXLMainXMPPackage pack = new AXLMainXMPPackage(meta, true,
					PDFAFlavour.PDFA_1_B);
			List<? extends Object> list = pack
					.getLinkedObjects(AXLMainXMPPackage.PROPERTIES);
			assertEquals(1, list.size());
			if (list.size() != 0) {
				Object obj = list.get(0);
				assertTrue(obj instanceof AXLXMPMMHistoryProperty);
				AXLXMPMMHistoryProperty historyProperty = (AXLXMPMMHistoryProperty) obj;
				assertTrue(historyProperty.getisValueTypeCorrect().booleanValue());
				assertTrue(historyProperty.getisPredefinedInXMP2004().booleanValue());
				assertTrue(historyProperty.getisPredefinedInXMP2005().booleanValue());
				assertFalse(historyProperty.getisDefinedInCurrentPackage().booleanValue());
				assertFalse(historyProperty.getisDefinedInMainPackage().booleanValue());

				List<? extends Object> resList = historyProperty
						.getLinkedObjects(AXLXMPMMHistoryProperty.RESOURCE_EVENTS);
				assertEquals(1, resList.size());
				if (!resList.isEmpty()) {
					Object object = resList.get(0);
					assertTrue(object instanceof AXLXMPMMHistoryResourceEvent);
					AXLXMPMMHistoryResourceEvent event = (AXLXMPMMHistoryResourceEvent) object;
					assertEquals("created", event.getaction());
					assertEquals("PDF file was created via veraPDF Test Builder", event.getparameters());
					assertNull(event.getwhen());
				}
			}
		}
	}
}
