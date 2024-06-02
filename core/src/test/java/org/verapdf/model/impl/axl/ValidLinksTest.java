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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;

/**
 * @author Maksim Bezrukov
 */
@RunWith(Parameterized.class)
public class ValidLinksTest {

    @Parameterized.Parameters
    public static Collection<org.verapdf.model.baselayer.Object> data() {
        return Arrays.asList(new org.verapdf.model.baselayer.Object[]{
                new AXLXMPPackage(null, true, null),
                new AXLMainXMPPackage(null, true),
                new AXLXMPProperty(null, true, false, null, null, null),
                new AXLPDFAIdentification(null),
                new AXLExtensionSchemasContainer(null, null, null),
                new AXLExtensionSchemaDefinition(null, null, null),
                new AXLExtensionSchemaProperty(null, null, null),
                new AXLExtensionSchemaValueType(null, null, null),
                new AXLExtensionSchemaField(null, null, null),
                new AXLXMPMMHistoryProperty(null, true, false, null, null, null),
                new AXLXMPMMHistoryResourceEvent(null)
        });
    }

    @Parameterized.Parameter
    public org.verapdf.model.baselayer.Object object;

    @Test
    public void testLinksOfAXLXMPPackage() {
        List<String> links = this.object.getLinks();
        for (String link : links) {
            assertNotNull(this.object.getLinkedObjects(link));
        }
    }
}
