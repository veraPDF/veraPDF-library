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
package org.verapdf.containers;

import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.Collections;
import java.util.List;

public class StaticCoreContainers {

    private static final ThreadLocal<List<PDFAFlavour>> flavour = new ThreadLocal<>();

    public static void clearAllContainers() {
        flavour.set(null);
    }

    public static List<PDFAFlavour> getFlavour() {
        return flavour.get();
    }
    
    public static void setFlavour(List<PDFAFlavour> flavour) {
        StaticCoreContainers.flavour.set(flavour);
    }

    public static void setFlavour(PDFAFlavour flavour) {
        StaticCoreContainers.flavour.set(Collections.singletonList(flavour));
    }
}
