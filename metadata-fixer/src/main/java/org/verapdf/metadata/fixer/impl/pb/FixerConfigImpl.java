package org.verapdf.metadata.fixer.impl.pb;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.entity.PDFDocument;
import org.verapdf.metadata.fixer.impl.pb.model.PDFDocumentImpl;
import org.verapdf.metadata.fixer.utils.FixerConfig;
import org.verapdf.metadata.fixer.utils.PDFAFlavour;
import org.verapdf.metadata.fixer.utils.parser.ProcessedObjectsParser;
import org.verapdf.metadata.fixer.utils.parser.XMLProcessedObjectsParser;
import org.verapdf.validation.profile.model.ValidationProfile;
import org.verapdf.validation.report.model.Result;
import org.verapdf.validation.report.model.ValidationInfo;

/**
 * @author Evgeniy Muravitskiy
 */
public class FixerConfigImpl implements FixerConfig {

	private final PDFDocument document;
	private final ValidationInfo validationResult;
	private final ProcessedObjectsParser parser;

	public FixerConfigImpl(PDDocument document, ValidationInfo validationResult) {
		this(document, validationResult, XMLProcessedObjectsParser.getInstance());
	}

	public FixerConfigImpl(PDDocument document, ValidationInfo validationResult, ProcessedObjectsParser parser) {
		if (document == null) {
			throw new IllegalArgumentException("Document can not be null");
		}
		if (parser == null) {
			throw new IllegalArgumentException("Parser can not be null.");
		}
		this.document = new PDFDocumentImpl(document);
		this.validationResult = validationResult;
		this.parser = parser;
	}

	@Override
	public Result getValidationResult() {
		return this.validationResult == null ? null :
				this.validationResult.getResult();
	}

	@Override
	public ValidationProfile getValidationProfile() {
		return this.validationResult == null ? null :
				this.validationResult.getProfile().getValidationProfile();
	}

	@Override
	public Metadata getMetadata() {
		return this.document.getMetadata();
	}

	@Override
	public PDFDocument getDocument() {
		return this.document;
	}

	@Override
	public ProcessedObjectsParser getParser() {
		return this.parser;
	}

	@Override
	public boolean isFixIdentification() {
		return false;
	}

	@Override
	public PDFAFlavour getPDFAFlavour() {
		return null;
	}

}
