package org.verapdf.metadata.fixer.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Evgeniy Muravitskiy
 */
// TODO : remove this and use source implementation
public enum PDFAFlavour {

	NO_FLAVOUR(Part.NO_STANDARD, Level.NO_LEVEL),

	PDFA_1_A(Part.ISO_19005_1, Level.A),

	PDFA_1_B(Part.ISO_19005_1, Level.B),

	PDFA_2_A(Part.ISO_19005_2, Level.A),

	PDFA_2_B(Part.ISO_19005_2, Level.B),

	PDFA_3_A(Part.ISO_19005_3, Level.A),

	PDFA_3_B(Part.ISO_19005_3, Level.B),

	PDFA_3_U(Part.ISO_19005_3, Level.U);

	private final Part part;
	private final Level level;

	PDFAFlavour(Part part, Level level) {
		this.part = part;
		this.level = level;
	}

	public Part getPart() {
		return part;
	}

	public Level getLevel() {
		return level;
	}

	public String getId() {
		return this.part.getPartNumber() + this.level.getCode();
	}

	public static PDFAFlavour byFlavourId(String flavourId) {
		switch (flavourId) {
			// TODO : for no flavour what`s the abbreviation?
			case "-1f":
				return NO_FLAVOUR;
			case "1a":
				return PDFA_1_A;
			case "1b":
				return PDFA_1_B;
			case "2a":
				return PDFA_2_A;
			case "2b":
				return PDFA_2_B;
			case "3a":
				return PDFA_3_A;
			case "3b":
				return PDFA_3_B;
			case "3u":
				return PDFA_3_U;
			default:
				return null;
		}
	}

	private static final Set<String> FLAVOURS;

	public static Set<String> getFlavourIds() {
		return FLAVOURS;
	}

	static {
		Set<String> buffer = new HashSet<>(8, 1);
		buffer.add("-1f");
		buffer.add("1a");
		buffer.add("1b");
		buffer.add("2a");
		buffer.add("2b");
		buffer.add("3a");
		buffer.add("3b");
		buffer.add("3u");
		FLAVOURS = Collections.unmodifiableSet(buffer);
	}

	public enum Level {

		NO_LEVEL('f'),

		A('a'),

		B('b'),

		U('u');

		private final char value;

		Level(char value) {
			this.value = value;
		}

		public String getCode() {
			return String.valueOf(this.value);
		}

		// TODO : implement me
		public String getName() {
			return null;
		}
	}

	public enum Part {

		NO_STANDARD(-1),

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
		public final String getName() {
			return null;
		}
	}
}
