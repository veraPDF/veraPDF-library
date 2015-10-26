package org.verapdf.metadata.fixer.utils;

import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.entity.PDFDocument;
import org.verapdf.metadata.fixer.utils.parser.ProcessedObjectsParser;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.validation.profile.model.ValidationProfile;
import org.verapdf.validation.report.model.Result;

/**
 * @author Evgeniy Muravitskiy
 */
public interface FixerConfig {

    ValidationResult getValidationResult();

	Metadata getMetadata();

	PDFDocument getDocument();

	ProcessedObjectsParser getParser();

	boolean isFixIdentification();

	PDFAFlavour getPDFAFlavour();
}
