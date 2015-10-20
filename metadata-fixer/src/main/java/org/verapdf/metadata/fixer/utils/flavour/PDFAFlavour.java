package org.verapdf.metadata.fixer.utils.flavour;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.verapdf.metadata.fixer.utils.flavour.PDFAFlavour.Part.NO_STANDARD;
import static org.verapdf.metadata.fixer.utils.flavour.PDFAFlavour.Part.ISO_19005_1;
import static org.verapdf.metadata.fixer.utils.flavour.PDFAFlavour.Part.ISO_19005_2;
import static org.verapdf.metadata.fixer.utils.flavour.PDFAFlavour.Part.ISO_19005_3;

import static org.verapdf.metadata.fixer.utils.flavour.PDFAFlavour.Level.NO_LEVEL;
import static org.verapdf.metadata.fixer.utils.flavour.PDFAFlavour.Level.A;
import static org.verapdf.metadata.fixer.utils.flavour.PDFAFlavour.Level.B;
import static org.verapdf.metadata.fixer.utils.flavour.PDFAFlavour.Level.U;

/**
 * @author Evgeniy Muravitskiy
 */
// TODO : remove this and use source implementation
public enum PDFAFlavour {

	NO_FLAVOUR(NO_STANDARD, NO_LEVEL),

	PDFA_1_A(ISO_19005_1, A),

	PDFA_1_B(ISO_19005_1, B),

	PDFA_2_A(ISO_19005_2, A),

	PDFA_2_B(ISO_19005_2, B),

	PDFA_3_A(ISO_19005_3, A),

	PDFA_3_B(ISO_19005_3, B),

	PDFA_3_U(ISO_19005_3, U);

	private static final Map<String, PDFAFlavour> FLAVOURS;

	public static Set<String> getFlavourIds() {
		return FLAVOURS.keySet();
	}

	public static PDFAFlavour byFlavourId(String flavourId) {
		// TODO : do we need to handle case when flavourId is incorrect?
		return flavourId == null ? null : FLAVOURS.get(flavourId.toUpperCase());
	}

	static {
		Map<String, PDFAFlavour> buffer = new HashMap<>(8, 1);

		buffer.put(NO_FLAVOUR.getId(), NO_FLAVOUR);
		buffer.put(PDFA_1_A.getId()  , PDFA_1_A);
		buffer.put(PDFA_1_B.getId()  , PDFA_1_B);
		buffer.put(PDFA_2_A.getId()  , PDFA_2_A);
		buffer.put(PDFA_2_B.getId()  , PDFA_2_B);
		buffer.put(PDFA_3_A.getId()  , PDFA_3_A);
		buffer.put(PDFA_3_B.getId()  , PDFA_3_B);
		buffer.put(PDFA_3_U.getId()  , PDFA_3_U);

		FLAVOURS = Collections.unmodifiableMap(buffer);
	}

	private final Part part;
	private final Level level;

	PDFAFlavour(Part part, Level level) {
		this.part = part;
		this.level = level;
	}

	public Part getPart() {
		return this.part;
	}

	public Level getLevel() {
		return this.level;
	}

	public String getId() {
		return this.part.getPartNumber() + this.level.getCode();
	}

	public enum Part {

		NO_STANDARD(0),

		ISO_19005_1(1),

		ISO_19005_2(2),

		ISO_19005_3(3);

		private final int value;

		Part(int value) {
			this.value = value;
		}

		public final int getPartNumber() {
			return this.value;
		}

		// TODO : implement me
		public final String getId() {
			throw new IllegalComponentStateException("Implement this method");
		}

		// TODO : implement me
		public final String getName() {
			throw new IllegalComponentStateException("Implement this method");
		}

		// TODO : implement me
		public final String getDescription() {
			throw new IllegalComponentStateException("Implement this method");
		}

		// TODO : implement me
		public final String getYear() {
			throw new IllegalComponentStateException("Implement this method");
		}

	}

	public enum Level {

		NO_LEVEL('F'),

		A('A'),

		B('B'),

		U('U');

		private final char value;

		Level(char value) {
			this.value = value;
		}

		// TODO : implement me
		public String getName() {
			throw new IllegalComponentStateException("Implement this method");
		}

		public String getCode() {
			return String.valueOf(this.value);
		}

	}

	public enum IsoStandardSeries {

		ISO_19005,

		ISO_32000,

		NO_SERIES;

		// TODO : implement me
		public final String getId() {
			throw new IllegalComponentStateException("Implement this method");
		}

		// TODO : implement me
		public final String getName() {
			throw new IllegalComponentStateException("Implement this method");
		}

		// TODO : implement me
		public final String getDescription() {
			throw new IllegalComponentStateException("Implement this method");
		}
	}

}
