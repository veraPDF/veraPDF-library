/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.features;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Features data of a font for feature extractor
 *
 * @author Maksim Bezrukov
 */
public final class FontFeaturesData extends FeaturesData {

	private static final Logger LOGGER = Logger.getLogger(FontFeaturesData.class.getCanonicalName());

	private final InputStream metadata;
	private final String fontName;
	private final String fontFamily;
	private final String fontStretch;
	private final Double fontWeight;
	private final Integer flags;
	private final List<Double> fontBBox;
	private final Double italicAngle;
	private final Double ascent;
	private final Double descent;
	private final Double leading;
	private final Double capHeight;
	private final Double xHeight;
	private final Double stemV;
	private final Double stemH;
	private final Double avgWidth;
	private final Double maxWidth;
	private final Double missingWidth;
	private final String charSet;

	FontFeaturesData(InputStream metadata,
					 InputStream stream,
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
		super(stream);
		this.metadata = metadata;
		this.fontName = fontName;
		this.fontFamily = fontFamily;
		this.fontStretch = fontStretch;
		this.fontWeight = fontWeight;
		this.flags = flags;
		this.fontBBox = fontBBox == null ? null : new ArrayList<>(fontBBox);
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
	 * @return InputStream metadata stream
	 */
	public InputStream getMetadata() {
		return this.metadata;
	}

	/**
	 * @return parameter FontName from the font descriptor dictionary
	 */
	public String getFontName() {
		return this.fontName;
	}

	/**
	 * @return parameter FontFamily from the font descriptor dictionary
	 */
	public String getFontFamily() {
		return this.fontFamily;
	}

	/**
	 * @return parameter FontStretch from the font descriptor dictionary
	 */
	public String getFontStretch() {
		return this.fontStretch;
	}

	/**
	 * @return parameter FontWeight from the font descriptor dictionary
	 */
	public Double getFontWeight() {
		return this.fontWeight;
	}

	/**
	 * @return parameter Flags from the font descriptor dictionary
	 */
	public Integer getFlags() {
		return this.flags;
	}

	/**
	 * @return parameter FontBBox from the font descriptor dictionary
	 */
	public List<Double> getFontBBox() {
		return this.fontBBox == null ? null : Collections.unmodifiableList(this.fontBBox);
	}

	/**
	 * @return parameter ItalicAngle from the font descriptor dictionary
	 */
	public Double getItalicAngle() {
		return this.italicAngle;
	}

	/**
	 * @return parameter Ascent from the font descriptor dictionary
	 */
	public Double getAscent() {
		return this.ascent;
	}

	/**
	 * @return parameter Descent from the font descriptor dictionary
	 */
	public Double getDescent() {
		return this.descent;
	}

	/**
	 * @return parameter Leading from the font descriptor dictionary
	 */
	public Double getLeading() {
		return this.leading;
	}

	/**
	 * @return parameter CapHeight from the font descriptor dictionary
	 */
	public Double getCapHeight() {
		return this.capHeight;
	}

	/**
	 * @return parameter XHeight from the font descriptor dictionary
	 */
	public Double getXHeight() {
		return this.xHeight;
	}

	/**
	 * @return parameter StemV from the font descriptor dictionary
	 */
	public Double getStemV() {
		return this.stemV;
	}

	/**
	 * @return parameter StemH from the font descriptor dictionary
	 */
	public Double getStemH() {
		return this.stemH;
	}

	/**
	 * @return parameter AvgWidth from the font descriptor dictionary
	 */
	public Double getAvgWidth() {
		return this.avgWidth;
	}

	/**
	 * @return parameter MaxWidth from the font descriptor dictionary
	 */
	public Double getMaxWidth() {
		return this.maxWidth;
	}

	/**
	 * @return parameter MissingWidth from the font descriptor dictionary
	 */
	public Double getMissingWidth() {
		return this.missingWidth;
	}

	/**
	 * @return parameter CharSet from the font descriptor dictionary
	 */
	public String getCharSet() {
		return this.charSet;
	}

	@Override
	public void close() throws IOException {
		if (this.metadata != null) {
			try {
				this.metadata.close();
			} catch (IOException e) {
				LOGGER.log(Level.FINE, "Exception during metadata closing", e);
			}
		}
		super.close();
	}

	@SuppressWarnings("hiding")
	public static final class Builder {

		private InputStream metadata = null;
		private InputStream stream;
		private String fontName = null;
		private String fontFamily = null;
		private String fontStretch = null;
		private Double fontWeight = null;
		private Integer flags = null;
		private List<Double> fontBBox = null;
		private Double italicAngle = null;
		private Double ascent = null;
		private Double descent = null;
		private Double leading = Double.valueOf(0.);
		private Double capHeight = null;
		private Double xHeight = Double.valueOf(0.);
		private Double stemV = null;
		private Double stemH = Double.valueOf(0.);
		private Double avgWidth = Double.valueOf(0.);
		private Double maxWidth = Double.valueOf(0.);
		private Double missingWidth = Double.valueOf(0.);
		private String charSet = null;

		public Builder(InputStream stream) {
			this.stream = stream;
		}

		public FontFeaturesData build() {
			if (this.stream == null) {
				throw new IllegalArgumentException("Font stream can not be null");
			}
			return new FontFeaturesData(this.metadata, this.stream, this.fontName, this.fontFamily, this.fontStretch, this.fontWeight,
					this.flags, this.fontBBox, this.italicAngle, this.ascent, this.descent, this.leading, this.capHeight, this.xHeight, this.stemV,
					this.stemH, this.avgWidth, this.maxWidth, this.missingWidth, this.charSet);
		}

		public Builder metadata(InputStream metadata) {
			this.metadata = metadata;
			return this;
		}

		public Builder stream(InputStream stream) {
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
				this.leading = Double.valueOf(0.0);
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
				this.xHeight = Double.valueOf(0.0);
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
				this.stemH = Double.valueOf(0.0);
			}
			return this;
		}

		public Builder avgWidth(Double avgWidth) {
			if (avgWidth != null) {
				this.avgWidth = avgWidth;
			} else {
				this.avgWidth = Double.valueOf(0.0);
			}
			return this;
		}

		public Builder maxWidth(Double maxWidth) {
			if (maxWidth != null) {
				this.maxWidth = maxWidth;
			} else {
				this.maxWidth = Double.valueOf(0.0);
			}
			return this;
		}

		public Builder missingWidth(Double missingWidth) {
			if (missingWidth != null) {
				this.missingWidth = missingWidth;
			} else {
				this.missingWidth = Double.valueOf(0.0);
			}
			return this;
		}

		public Builder charSet(String charSet) {
			this.charSet = charSet;
			return this;
		}
	}
}
