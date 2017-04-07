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
