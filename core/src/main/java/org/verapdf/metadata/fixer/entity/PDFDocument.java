package org.verapdf.metadata.fixer.entity;

import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.MetadataFixerResultImpl;

import java.io.OutputStream;

/**
 * Current interface provide necessary behavior of pdf document
 * for {@link MetadataFixerImpl}
 *
 * @author Evgeniy Muravitskiy
 */
public interface PDFDocument {

	/**
	 * Return pdf document metadata representation. Must return null
	 * if and only if handler having problems with metadata obtain
	 * (exceptions, for example). If metadata is not present in the
	 * document ('Metadata' key in catalog not present or empty) it`s
	 * must be added.
	 *
	 * @return metadata representation or null
	 * @see Metadata
	 */
	Metadata getMetadata();

	/**
	 * Return pdf document information dictionary representation.
	 * Must be not null (empty, for example).
	 *
	 * @return information dictionary representation
	 * @see InfoDictionary
	 */
	InfoDictionary getInfoDictionary();

	/**
	 * @return
	 */
	boolean isNeedToBeUpdated();

	/**
	 * Incremental save of pdf document. Document must be saved if and
	 * only if metadata or information dictionary of document was changed.
	 * In {@link MetadataFixerResultImpl} must set 1 of 3 states:
	 * <ul>
	 * <li>
	 * {@link org.verapdf.pdfa.results.MetadataFixerResult.RepairStatus#FIX_ERROR}
	 * if got problems with document save
	 * </li>
	 * <li>
	 * {@link org.verapdf.pdfa.results.MetadataFixerResult.RepairStatus#NO_ACTION}
	 * if metadata and information dictionary was not changed
	 * </li>
	 * <li>
	 * {@link org.verapdf.pdfa.results.MetadataFixerResult.RepairStatus#SUCCESS}
	 * if document save successful
	 * </li>
	 * </ul>
	 *
	 * @param report result of {@code MetadataFixerImpl} handling
	 * @param output output stream for document save
	 * @return 
	 */
	MetadataFixerResult saveDocumentIncremental(MetadataFixerResult.RepairStatus status, OutputStream output);

}
