package org.verapdf.model.impl.pb.cos;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.verapdf.model.baselayer.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosFilter;
import org.verapdf.model.coslayer.CosStream;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * PDF Stream type
 *
 * @author Evgeniy Mutavitskiy
 */
public class PBCosStream extends PBCosDict implements CosStream {

	private static final Logger logger = Logger.getLogger(PBCosStream.class);

    public static final String FILTERS = "filters";

	/** Type name for PBCosStream */
	public static final String COS_STREAM_TYPE = "CosStream";
	public static final String F_DECODE_PARMS = "FDecodeParms";

	private final Long length;
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

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case FILTERS:
                return this.getFilters();
            default:
                return super.getLinkedObjects(link);
        }
    }

    private List<CosFilter> getFilters() {
        COSBase filters = ((COSStream) this.baseObject).getFilters();
        if (filters == null) {
            return Collections.emptyList();
        } else {
            List<CosFilter> result = new ArrayList<>();
            COSBase decodeParms = ((COSStream) this.baseObject).getDictionaryObject(COSName.DECODE_PARMS);
            if (filters instanceof COSName) {
                //TODO : check null
                if (decodeParms instanceof COSArray) {
                    decodeParms = ((COSArray) decodeParms).get(0);
                }
                result.add(createFilter((COSName) filters, decodeParms));
            } else if (filters instanceof COSArray) {
                for (int i = 0; i < ((COSArray) filters).size(); i++) {
                    COSBase filter = ((COSArray) filters).get(i);
                    if (filter instanceof COSName) {
                        if (decodeParms == null) {
                            result.add(createFilter((COSName) filter, null));
                        } else if (decodeParms instanceof COSArray && ((COSArray) decodeParms).size() < i) {
                            decodeParms = ((COSArray) decodeParms).get(i);
                            result.add(createFilter((COSName) filter, decodeParms));
                        } else {
                            logger.warn("Invalid decodeParms type. Ignoring decodeParms.");
                        }
                    } else {
                        logger.warn("Invalid value type in filters array. Skipping the filter");
                    }
                }
            }
            return result;
        }
    }

    private static CosFilter createFilter(final COSName filter, final COSBase decodeParms) {
        if (decodeParms == null) {
            return new PBCosFilter(filter, null);
        } else if (decodeParms instanceof COSDictionary) {
            return new PBCosFilter(filter, (COSDictionary) decodeParms);
        } else {
            logger.warn("Invalid decodeParms type. Ignoring decodeParms.");
            return new PBCosFilter(filter, null);
        }
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
