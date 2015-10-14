package org.verapdf.metadata.fixer.entity;

import org.verapdf.metadata.fixer.MetadataFixerResult;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Evgeniy Muravitskiy
 */
public interface PDFDocument {

	Metadata getMetadata();

	InfoDictionary getInfoDictionary();

	boolean isNeedToBeUpdated();

	void saveDocumentIncremental(MetadataFixerResult report, OutputStream output) throws IOException;
}
