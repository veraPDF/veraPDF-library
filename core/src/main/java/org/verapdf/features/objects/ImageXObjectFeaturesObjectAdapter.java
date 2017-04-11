/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 * <p>
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 * <p>
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 * <p>
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.features.objects;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

/**
 * @author Sergey Shemyakov
 */
public interface ImageXObjectFeaturesObjectAdapter extends FeaturesObjectAdapter {
    String getID();

    Long getWidth();

    Long getHeight();

    String getColorSpaceChild();

    Long getBitsPerComponent();

    boolean getImageMask();

    String getMaskChild();

    boolean isInterpolate();

    Set<String> getAlternatesChild();

    String getSMaskChild();

    Long getStructParent();

    List<String> getFilters();

    InputStream getMetadata();

    InputStream getRawStreamData();

    List<StreamFilterAdapter> getFilterAdapters();

    interface StreamFilterAdapter {
        //CCITTFax
        Long getCCITTK();

        boolean getCCITTEndOfLine();

        boolean getCCITTEncodedByteAlign();

        Long getCCITTColumns();

        Long getCCITTRows();

        boolean getCCITTEndOfBlock();

        boolean getCCITTBlackIs1();

        Long getCCITTDamagedRowsBeforeError();

        //DCT
        Long getDCTColorTransform();

        //LZW
        Long getLZWEarlyChange();

        //Flate
        Long getFlatePredictor();

        Long getFlateColors();

        Long getFlateBitsPerComponent();

        Long getFlateColumns();

        //JBIG2
        InputStream getJBIG2Global();

        //Crypt
        boolean hasCryptFilter();
    }
}
