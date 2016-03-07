package org.verapdf.metadata.fixer.utils;

import org.verapdf.metadata.fixer.entity.PDFDocument;
import org.verapdf.metadata.fixer.utils.parser.ProcessedObjectsParser;
import org.verapdf.pdfa.results.ValidationResult;

/**
 * @author Evgeniy Muravitskiy
 */
public interface FixerConfig {

    ValidationResult getValidationResult();

	PDFDocument getDocument();

	ProcessedObjectsParser getParser();

	boolean isFixIdentification();

}
