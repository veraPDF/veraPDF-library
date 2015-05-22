package org.verapdf.model.impl.pb.cos;

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
        setType("CosStream");
    }

    /**  length of the stream
     */
    @Override
    public Long getlength() {
        Long length = 0L;
        COSStream stream = (COSStream) this.baseObject;

        if (stream.getItem(COSName.LENGTH) != null)
            return (long) ((COSInteger) stream.getItem(COSName.LENGTH)).intValue();

        try {
            if (stream.getUnfilteredStream() != null)
                length = (long) (stream.getUnfilteredStream()).available();
            if (stream.getFilteredStream() != null)
                length += stream.getFilteredLength();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return length;
    }

    /**  concatenated (white space separated) names of all filters
     */
    @Override
    public String getfilters() {
        StringBuilder filters = new StringBuilder();
        COSBase base = ((COSStream) baseObject).getFilters();
        if (base == null)
            return filters.toString();

        if (base instanceof COSName) {
            return ((COSName) base).getName();
        } else if (base instanceof COSArray) {
            for (COSBase filter : ((COSArray) base)) {
                if (filter instanceof COSName) {
                    filters.append(((COSName) filter).getName()).append(" ");
                } else {
                    throw new IllegalArgumentException("Can`t find filters for stream");
                }
            }
        } else {
            throw new IllegalArgumentException("Not the corresponding type for filters");
        }
        // need to discard last white space
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
