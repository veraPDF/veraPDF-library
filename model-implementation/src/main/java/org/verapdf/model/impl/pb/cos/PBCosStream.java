package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.*;
import org.verapdf.model.coslayer.CosStream;

/**
 * PDF Stream type
 *
 * @author Evgeniy Mutavitskiy
 */
public class PBCosStream extends PBCosDict implements CosStream {

	public PBCosStream(COSStream stream) {
		super(stream);
		setType("CosStream");
	}

	/**
	 * length of the stream
	 */
	@Override
	public Long getLength() {
		COSBase number = ((COSStream) this.baseObject).getDictionaryObject(COSName.LENGTH);
		return number instanceof COSNumber ? Long.valueOf(((COSNumber) number).longValue()) : null;
	}

	/**
	 * concatenated (white space separated) names of all filters
	 */
	@Override
	public String getfilters() {
		COSBase base = ((COSStream) baseObject).getFilters();
		return getFilters(base);
	}

	private String getFilters(COSBase base) {
		StringBuilder filters = new StringBuilder();

		if (base == null) {
			return null;
		} else if (base instanceof COSName) {
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
		return filters.substring(0, filters.length() - 1);
	}

	/**
	 * @return string representation of file specification if its present
	 */
	@Override
	public String getF() {
		COSBase fileSpecification = ((COSStream) baseObject).getItem("F");
		return fileSpecification != null ? fileSpecification.toString() : null;
	}

	/**
	 * @return string representation of filters for external file
	 */
	@Override
	public String getFFilter() {
		COSBase base = ((COSStream) baseObject).getDictionaryObject(COSName.F_FILTER);
		return getFilters(base);
	}

	/**
	 * @return string representation of decode parameters for filters applied to external file
	 */
	@Override
	public String getFDecodeParms() {
		COSBase fDecodeParms = ((COSStream) baseObject).getItem("FDecodeParms");
		return fDecodeParms != null ? fDecodeParms.toString() : null;
	}

	/**
	 * true if the spacing around stream / endstream complies with the PDF/A requirements
	 */
	@Override
	public Boolean getspacingCompliesPDFA() {
		return ((COSStream) baseObject).getStreamSpacingsComplyPDFA() &&
				((COSStream) baseObject).getEndStreamSpacingsComplyPDFA();
	}

	/**
	 * true if the value of Length key matches the actual length of the stream
	 */
	@Override
	public Boolean getisLengthCorrect() {
		Long keyValue = getLength();
		Long realValue = ((COSStream) baseObject).getOriginLength();
		return Boolean.valueOf(keyValue != null && keyValue.equals(realValue));
	}
}
