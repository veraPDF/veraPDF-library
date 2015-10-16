package org.verapdf.features;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Features data of a font for feature extractor
 *
 * @author Maksim Bezrukov
 */
public final class FontFeaturesData extends FeaturesData {

	private String fontName;
	private String fontFamily;
	private String fontStretch;
	private Double fontWeight;
	private Integer flags;
	private List<Double> fontBBox;
	private Double italicAngle;
	private Double ascent;
	private Double descent;
	private Double leading;
	private Double capHeight;
	private Double xHeight;
	private Double stemV;
	private Double stemH;
	private Double avgWidth;
	private Double maxWidth;
	private Double missingWidth;
	private String charSet;

	private FontFeaturesData(byte[] metadata,
							 byte[] stream,
							 String fontName,
							 String fontFamily,
							 String fontStretch,
							 Double fontWeight,
							 Integer flags,
							 List<Double> fontBBox,
							 Double italicAngle,
							 Double ascent,
							 Double descent,
							 Double leading,
							 Double capHeight,
							 Double xHeight,
							 Double stemV,
							 Double stemH,
							 Double avgWidth,
							 Double maxWidth,
							 Double missingWidth,
							 String charSet) {
		super(metadata, stream);
		this.fontName = fontName;
		this.fontFamily = fontFamily;
		this.fontStretch = fontStretch;
		this.fontWeight = fontWeight;
		this.flags = flags;
		this.fontBBox = new ArrayList<>(fontBBox);
		this.italicAngle = italicAngle;
		this.ascent = ascent;
		this.descent = descent;
		this.leading = leading;
		this.capHeight = capHeight;
		this.xHeight = xHeight;
		this.stemV = stemV;
		this.stemH = stemH;
		this.avgWidth = avgWidth;
		this.maxWidth = maxWidth;
		this.missingWidth = missingWidth;
		this.charSet = charSet;
	}

	/**
	 * @return parameter FontName from the font descriptor dictionary
	 */
	public String getFontName() {
		return fontName;
	}

	/**
	 * @return parameter FontFamily from the font descriptor dictionary
	 */
	public String getFontFamily() {
		return fontFamily;
	}

	/**
	 * @return parameter FontStretch from the font descriptor dictionary
	 */
	public String getFontStretch() {
		return fontStretch;
	}

	/**
	 * @return parameter FontWeight from the font descriptor dictionary
	 */
	public Double getFontWeight() {
		return fontWeight;
	}

	/**
	 * @return parameter Flags from the font descriptor dictionary
	 */
	public Integer getFlags() {
		return flags;
	}

	/**
	 * @return parameter FontBBox from the font descriptor dictionary
	 */
	public List<Double> getFontBBox() {
		return Collections.unmodifiableList(fontBBox);
	}

	/**
	 * @return parameter ItalicAngle from the font descriptor dictionary
	 */
	public Double getItalicAngle() {
		return italicAngle;
	}

	/**
	 * @return parameter Ascent from the font descriptor dictionary
	 */
	public Double getAscent() {
		return ascent;
	}

	/**
	 * @return parameter Descent from the font descriptor dictionary
	 */
	public Double getDescent() {
		return descent;
	}

	/**
	 * @return parameter Leading from the font descriptor dictionary
	 */
	public Double getLeading() {
		return leading;
	}

	/**
	 * @return parameter CapHeight from the font descriptor dictionary
	 */
	public Double getCapHeight() {
		return capHeight;
	}

	/**
	 * @return parameter XHeight from the font descriptor dictionary
	 */
	public Double getXHeight() {
		return xHeight;
	}

	/**
	 * @return parameter StemV from the font descriptor dictionary
	 */
	public Double getStemV() {
		return stemV;
	}

	/**
	 * @return parameter StemH from the font descriptor dictionary
	 */
	public Double getStemH() {
		return stemH;
	}

	/**
	 * @return parameter AvgWidth from the font descriptor dictionary
	 */
	public Double getAvgWidth() {
		return avgWidth;
	}

	/**
	 * @return parameter MaxWidth from the font descriptor dictionary
	 */
	public Double getMaxWidth() {
		return maxWidth;
	}

	/**
	 * @return parameter MissingWidth from the font descriptor dictionary
	 */
	public Double getMissingWidth() {
		return missingWidth;
	}

	/**
	 * @return parameter CharSet from the font descriptor dictionary
	 */
	public String getCharSet() {
		return charSet;
	}

	public static final class Builder {

		private byte[] metadata = null;
		private byte[] stream = null;
		private String fontName = null;
		private String fontFamily = null;
		private String fontStretch = null;
		private Double fontWeight = null;
		private Integer flags = null;
		private List<Double> fontBBox = null;
		private Double italicAngle = null;
		private Double ascent = null;
		private Double descent = null;
		private Double leading = 0.;
		private Double capHeight = null;
		private Double xHeight = 0.;
		private Double stemV = null;
		private Double stemH = 0.;
		private Double avgWidth = 0.;
		private Double maxWidth = 0.;
		private Double missingWidth = 0.;
		private String charSet = null;

		public Builder() {
		}

		public FontFeaturesData build() {
			if (this.stream == null) {
				throw new IllegalArgumentException("Font stream can not be null");
			}
			return new FontFeaturesData(metadata, stream, fontName, fontFamily, fontStretch, fontWeight,
					flags, fontBBox, italicAngle, ascent, descent, leading, capHeight, xHeight, stemV,
					stemH, avgWidth, maxWidth, missingWidth, charSet);
		}

		public Builder metadata(byte[] metadata) {
			this.metadata = metadata;
			return this;
		}

		public Builder stream(byte[] stream) {
			this.stream = stream;
			return this;
		}

		public Builder fontName(String fontName) {
			this.fontName = fontName;
			return this;
		}

		public Builder fontFamily(String fontFamily) {
			this.fontFamily = fontFamily;
			return this;
		}

		public Builder fontStretch(String fontStretch) {
			this.fontStretch = fontStretch;
			return this;
		}

		public Builder fontWeight(Double fontWeight) {
			this.fontWeight = fontWeight;
			return this;
		}

		public Builder flags(Integer flags) {
			this.flags = flags;
			return this;
		}

		public Builder fontBBox(List<Double> fontBBox) {
			this.fontBBox = fontBBox;
			return this;
		}

		public Builder italicAngle(Double italicAngle) {
			this.italicAngle = italicAngle;
			return this;
		}

		public Builder ascent(Double ascent) {
			this.ascent = ascent;
			return this;
		}

		public Builder descent(Double descent) {
			this.descent = descent;
			return this;
		}

		public Builder leading(Double leading) {
			if (leading != null) {
				this.leading = leading;
			} else {
				this.leading = 0.0;
			}
			return this;
		}

		public Builder capHeight(Double capHeight) {
			this.capHeight = capHeight;
			return this;
		}

		public Builder xHeight(Double xHeight) {
			if (xHeight != null) {
				this.xHeight = xHeight;
			} else {
				this.xHeight = 0.0;
			}
			return this;
		}

		public Builder stemV(Double stemV) {
			this.stemV = stemV;
			return this;
		}

		public Builder stemH(Double stemH) {
			if (stemH != null) {
				this.stemH = stemH;
			} else {
				this.stemH = 0.0;
			}
			return this;
		}

		public Builder avgWidth(Double avgWidth) {
			if (avgWidth != null) {
				this.avgWidth = avgWidth;
			} else {
				this.avgWidth = 0.0;
			}
			return this;
		}

		public Builder maxWidth(Double maxWidth) {
			if (maxWidth != null) {
				this.maxWidth = maxWidth;
			} else {
				this.maxWidth = 0.0;
			}
			return this;
		}

		public Builder missingWidth(Double missingWidth) {
			if (missingWidth != null) {
				this.missingWidth = missingWidth;
			} else {
				this.missingWidth = 0.0;
			}
			return this;
		}

		public Builder charSet(String charSet) {
			this.charSet = charSet;
			return this;
		}
	}
}
