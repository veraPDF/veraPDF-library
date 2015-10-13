package org.verapdf.metadata.fixer.entity;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Evgeniy Muravitskiy
 */
public interface PDFDocument {

	Metadata getMetadata();

	InfoDictionary getInfoDictionary();

	boolean isNeedToBeUpdated();

	void saveDocumentIncremental(FixReport report, OutputStream output) throws IOException;
}
