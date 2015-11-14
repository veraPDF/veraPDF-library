package org.verapdf.model.impl.pb.cos;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.verapdf.model.coslayer.CosStream;

/**
 * PDF Stream type
 *
 * @author Evgeniy Mutavitskiy
 */
public class PBCosStream extends PBCosDict implements CosStream {

	private static final Logger logger = Logger.getLogger(PBCosStream.class);

	/** Type name for PBCosStream */
	public static final String COS_STREAM_TYPE = "CosStream";
	public static final String F_DECODE_PARMS = "FDecodeParms";

	private final Long length;
	private final String filters;
	private final String fileSpec;
	private final String fFilter;
	private final String fDecodeParams;
	private final boolean streamKeywordCRLFCompliant;
	private final boolean endstreamKeywordEOLCompliant;
	private final boolean isLengthCorrect;

	/**
     * Default constructor
     * @param stream pdfbox COSStream
     */
	public PBCosStream(COSStream stream) {
        super(stream, COS_STREAM_TYPE);
        this.length = parseLength(stream);
        this.filters = parseFilters(stream.getFilters());
        this.fileSpec = stream.getItem("F") != null ? stream.getItem("F")
                .toString() : null;
        this.fFilter = parseFilters(stream
                .getDictionaryObject(COSName.F_FILTER));
        this.fDecodeParams = stream.getItem(F_DECODE_PARMS) != null ? stream
                .getItem(F_DECODE_PARMS).toString() : null;
        this.streamKeywordCRLFCompliant = stream.isStreamKeywordCRLFCompliant();
		this.endstreamKeywordEOLCompliant = stream.isEndstreamKeywordEOLCompliant();
		this.isLengthCorrect = this.length != null && this.length.equals(stream.getOriginLength());
    }

    /**
     * length of the stream
     */
    @Override
    public Long getLength() {
        return this.length;
    }

    /**
     * concatenated (white space separated) names of all filters
     */
    @Override
    public String getfilters() {
        return this.filters;
    }

    /**
     * @return string representation of file specification if its present
     */
    @Override
    public String getF() {
        return this.fileSpec;
    }

    /**
     * @return string representation of filters for external file
     */
    @Override
    public String getFFilter() {
        return this.fFilter;
    }

    /**
     * @return string representation of decode parameters for filters applied to
     *         external file
     */
    @Override
    public String getFDecodeParms() {
        return this.fDecodeParams;
    }

    /**
     * true if the spacing around stream complies with the PDF/A
     * requirements
     */
    @Override
    public Boolean getstreamKeywordCRLFCompliant() {
        return Boolean.valueOf(this.streamKeywordCRLFCompliant);
    }

	@Override
	public Boolean getendstreamKeywordEOLCompliant() {
		return Boolean.valueOf(this.endstreamKeywordEOLCompliant);
	}

    /**
     * true if the value of Length key matches the actual length of the stream
     */
    @Override
    public Boolean getisLengthCorrect() {
        return Boolean.valueOf(this.isLengthCorrect);
    }

    private static Long parseLength(final COSStream stream) {
        COSBase number = stream.getDictionaryObject(COSName.LENGTH);
        return number instanceof COSNumber ? Long.valueOf(((COSNumber) number)
                .longValue()) : null;
    }

    private static String parseFilters(COSBase base) {
        StringBuilder filters = new StringBuilder();

        if (base == null) {
            return null;
        } else if (base instanceof COSName) {
            return ((COSName) base).getName();
        } else if (base instanceof COSArray) {
            for (COSBase filter : (COSArray) base) {
                if (filter instanceof COSName) {
                    filters.append(((COSName) filter).getName()).append(" ");
                } else {
                    logger.error("Incorrect type for stream filter " +
							filter.getClass().getName());
                }
            }
        } else {
			logger.error("Incorrect type for stream filter " +
					base.getClass().getName());
			return null;
        }
        // need to discard last white space
        return filters.substring(0, filters.length() - 1);
    }
}
