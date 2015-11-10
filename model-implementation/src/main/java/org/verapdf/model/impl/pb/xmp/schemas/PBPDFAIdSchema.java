package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.type.AbstractField;
import org.apache.xmpbox.type.TextType;
import org.verapdf.model.xmplayer.PDFAIdSchema;


/**
 * Current class is representation of PDFAIdSchema interface from
 * abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBPDFAIdSchema extends PBXMPSchema implements PDFAIdSchema {

	public static final String PDF_AID_SCHEMA_TYPE = "PDFAIdSchema";

	private static final String CORR = "corr";

	private final Long part;
	private final String conformance;
	private final String amd;
	private final String corr;

	/**
	 * Constructs new object
	 *
	 * @param xmpSchema object from xmpbox represented this schema
	 */
	public PBPDFAIdSchema(PDFAIdentificationSchema xmpSchema) {
		super(xmpSchema, PDF_AID_SCHEMA_TYPE);
		this.part = this.getPart(xmpSchema);
		this.conformance = xmpSchema.getConformance();
		this.amd = xmpSchema.getAmd();
		this.corr = this.getCorr(xmpSchema);
	}

	private Long getPart(PDFAIdentificationSchema xmpSchema) {
		Integer part = xmpSchema.getPart();
		return part == null ? null : Long.valueOf(part.longValue());
	}

	private String getCorr(PDFAIdentificationSchema xmpSchema) {
		AbstractField corr = xmpSchema.getProperty(CORR);

		if (corr instanceof TextType) {
			return ((TextType) corr).getStringValue();
		}
		return null;
	}

	/**
	 * @return property part of the schema
	 */
	@Override
	public Long getpart() {
		return this.part;
	}

	/**
	 * @return property conformance of the schema
	 */
	@Override
	public String getconformance() {
		return this.conformance;
	}

	/**
	 * @return property amd of the schema
	 */
	@Override
	public String getamd() {
		return this.amd;
	}

	/**
	 * @return property corr of the schema
	 */
	@Override
	public String getcorr() {
		return this.corr;
	}

}
