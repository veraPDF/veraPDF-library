package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.AdobePDFSchema;
import org.verapdf.model.xmplayer.PDFSchema;

/**
 * Current class is representation of PDFSchema interface from
 * abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBPDFSchema extends PBXMPPredefinedSchema implements PDFSchema {

	public static final String PDF_SCHEMA_TYPE = "PDFSchema";

	private static final String BASE_URL = "BaseURL";
	private static final String CREATION_DATE = "CreationDate";
	private static final String CREATOR = "Creator";
	private static final String MOD_DATE = "ModDate";
	private static final String SUBJECT = "Subject";
	private static final String TRAPPED = "Trapped";

	/**
	 * Constructs new object
	 *
	 * @param xmpSchema object from xmpbox represented this schema
	 */
	public PBPDFSchema(AdobePDFSchema xmpSchema) {
		super(xmpSchema, PDF_SCHEMA_TYPE);
	}

	/**
	 * @return Author part of the schema
	 */
	@Override
	public String getauthor() {
		return this.getProperty(AUTHOR);
	}

	/**
	 * @return BaseURL part of the schema
	 */
	@Override
	public String getbaseURL() {
		return this.getProperty(BASE_URL);
	}

	/**
	 * @return CreationDate part of the schema
	 */
	@Override
	public String getcreationDate() {
		return this.getProperty(CREATION_DATE);
	}

	/**
	 * @return Creator part of the schema
	 */
	@Override
	public String getcreator() {
		return this.getProperty(CREATOR);
	}

	/**
	 * @return ModDate part of the schema
	 */
	@Override
	public String getmodDate() {
		return this.getProperty(MOD_DATE);
	}

	/**
	 * @return Subject part of the schema
	 */
	@Override
	public String getsubject() {
		return this.getProperty(SUBJECT);
	}

	/**
	 * @return Title part of the schema
	 */
	@Override
	public String gettitle() {
		return this.getProperty(TITLE);
	}

	/**
	 * @return Trapped part of the schema
	 */
	@Override
	public String gettrapped() {
		return this.getProperty(TRAPPED);
	}

}
