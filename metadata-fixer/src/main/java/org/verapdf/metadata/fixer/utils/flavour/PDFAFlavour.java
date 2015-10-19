package org.verapdf.metadata.fixer.utils.flavour;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.verapdf.metadata.fixer.utils.flavour.Part.NO_STANDARD;
import static org.verapdf.metadata.fixer.utils.flavour.Part.ISO_19005_1;
import static org.verapdf.metadata.fixer.utils.flavour.Part.ISO_19005_2;
import static org.verapdf.metadata.fixer.utils.flavour.Part.ISO_19005_3;

import static org.verapdf.metadata.fixer.utils.flavour.Level.NO_LEVEL;
import static org.verapdf.metadata.fixer.utils.flavour.Level.A;
import static org.verapdf.metadata.fixer.utils.flavour.Level.B;
import static org.verapdf.metadata.fixer.utils.flavour.Level.U;

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

	public static PDFAFlavour byFlavourId(String flavourId) {
		// TODO : do we need to handle case when flavourId is incorrect?
		return flavourId == null ? null : FLAVOURS.get(flavourId);
	}

	public static Set<String> getFlavourIds() {
		return FLAVOURS.keySet();
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

}
