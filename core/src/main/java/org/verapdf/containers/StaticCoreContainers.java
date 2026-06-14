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
package org.verapdf.containers;

import org.verapdf.extensions.ExtensionObjectType;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class StaticCoreContainers {

    private static final ThreadLocal<List<PDFAFlavour>> flavour = new ThreadLocal<>();
    private static final ThreadLocal<EnumSet<ExtensionObjectType>> enabledExtensions = new ThreadLocal<>();

    public static void clearAllContainers() {
        flavour.set(null);
        enabledExtensions.set(EnumSet.noneOf(ExtensionObjectType.class));
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

    public static EnumSet<ExtensionObjectType> getEnabledExtensions() {
        if (enabledExtensions.get() == null) {
            enabledExtensions.set(EnumSet.noneOf(ExtensionObjectType.class));
        }
        return enabledExtensions.get();
    }

    public static void setEnabledExtensions(EnumSet<ExtensionObjectType> enabledExtensions) {
        StaticCoreContainers.enabledExtensions.set(enabledExtensions);
    }
}
