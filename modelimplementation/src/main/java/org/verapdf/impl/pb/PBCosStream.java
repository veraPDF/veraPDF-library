package org.verapdf.impl.pb;

import org.apache.pdfbox.cos.*;
import org.verapdf.model.coslayer.CosStream;

import java.io.IOException;

/**
 * Created by Evgeniy Muravitskiy on 5/4/15.
 * <p>
 *     PDF Stream type
 * </p>
 */
public class PBCosStream extends PBCosDict implements CosStream {

    public PBCosStream(COSStream stream) {
        super(stream);
    }

    /**  length of the stream
     */
    @Override
    public Integer getlength() {
        int length = 0;
        if (((COSStream) baseObject).getItem(COSName.LENGTH) != null)
            return ((COSInteger) ((COSStream) baseObject).getItem(COSName.LENGTH)).intValue();
        try {
            if (((COSStream) baseObject).getUnfilteredStream() != null)
                length = (((COSStream) baseObject).getUnfilteredStream()).available();
            if (((COSStream) baseObject).getFilteredStream() != null)
                length += ((COSStream) baseObject).getFilteredLength();
        } catch (IOException ignore) {}
        return length;
    }

    /**  concatenated (white space separated) names of all filters
     */
    @Override
    public String getfilters() {
        String filters = "";
        COSBase base = ((COSStream) baseObject).getFilters();
        if (base == null)
            return null;

        if (base instanceof COSName) {
            filters = ((COSName)base).getName();
        } else if (base instanceof COSArray) {
            for (COSBase filter : ((COSArray) base)) {
                if (filter instanceof COSName)
                    filters += ((COSName) filter).getName() + " ";
                else
                    throw new IllegalArgumentException("Can`t find filters for stream");
            }
        } else
            throw new IllegalArgumentException("Not the corresponding type for filters");
        return filters.substring(0, filters.length() - 2);
    }

    /**  true if the spacing around stream / endstream complies with the PDF/A requirements
     */
    @Override
    public Boolean getspacingComplyPDFA() {
        System.err.println("Current feature not supported yet. Result is always true.");
        return true;
    }
}
