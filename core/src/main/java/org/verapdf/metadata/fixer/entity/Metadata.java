package org.verapdf.metadata.fixer.entity;

import org.verapdf.metadata.fixer.schemas.AdobePDF;
import org.verapdf.metadata.fixer.schemas.DublinCore;
import org.verapdf.metadata.fixer.schemas.XMPBasic;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.MetadataFixerResultImpl;

/**
 * Current interface provide necessary behavior of pdf metadata
 * for {@link MetadataFixerImpl}
 *
 * @author Evgeniy Muravitskiy
 */
public interface Metadata {

	/**
	 * Add required fields to stream dictionary and add FlateDecode Filter for part 2 and 3 flavours or remove filters for part 1 flavour
	 *
	 * @param resultBuilder report applied changes
	 */
	void checkMetadataStream(MetadataFixerResultImpl.Builder resultBuilder, PDFAFlavour flavour);

	/**
	 * Remove identification schema if {@code MetadataFixerImpl}
	 * can not repair document to valid PDF/A Document.
	 *
	 * @param resultBuilder report applied changes
	 */
	void removePDFIdentificationSchema(MetadataFixerResultImpl.Builder resultBuilder, PDFAFlavour flavour);

	/**
	 * Add PDF/A identification schema if {@code MetadataFixerImpl}
	 * be able to repair document to valid PDF/A document.
	 *
	 * @param resultBuilder  report applied changes
	 * @param flavour the checked flavour
	 */
	void addPDFIdentificationSchema(MetadataFixerResultImpl.Builder resultBuilder, PDFAFlavour flavour);

	/**
	 * Get {@code DublinCore} representation. If current schema not presented
	 * in metadata but Information dictionary consist corresponding values
	 * than must return empty dublin core schema.
	 *
	 * @param info information dictionary representation of current document
	 * @return {@code DublinCore} schema or null
	 */
	DublinCore getDublinCoreSchema(InfoDictionary info);

	/**
	 * Get {@code AdobePDF} representation. If current schema not presented
	 * in metadata but Information dictionary consist corresponding values
	 * than must return empty dublin core schema.
	 *
	 * @param info information dictionary representation of current document
	 * @return {@code AdobePDF} schema or null
	 */
	AdobePDF getAdobePDFSchema(InfoDictionary info);

	/**
	 * Get {@code XMPBasic} representation. If current schema not presented
	 * in metadata but Information dictionary consist corresponding values
	 * than must return empty dublin core schema.
	 *
	 * @param info information dictionary representation of current document
	 * @return {@code XMPBasic} schema or null
	 */
	XMPBasic getXMPBasicSchema(InfoDictionary info);

	/**
	 * @return
	 */
	boolean isNeedToBeUpdated();

	/**
	 * @param needToBeUpdated
	 */
	void setNeedToBeUpdated(boolean needToBeUpdated);

	/**
	 * Update metadata stream from applied changes. If metadata was not
	 * changed than no updates applied
	 *
	 * @throws Exception problems with stream update
	 */
	void updateMetadataStream() throws Exception;

}
