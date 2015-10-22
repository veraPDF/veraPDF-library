package org.verapdf.metadata.fixer.impl.pb;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.entity.PDFDocument;
import org.verapdf.metadata.fixer.impl.pb.model.PDFDocumentImpl;
import org.verapdf.metadata.fixer.utils.FixerConfig;
import org.verapdf.metadata.fixer.utils.flavour.PDFAFlavour;
import org.verapdf.metadata.fixer.utils.parser.ProcessedObjectsParser;
import org.verapdf.metadata.fixer.utils.parser.XMLProcessedObjectsParser;
import org.verapdf.validation.profile.model.ValidationProfile;
import org.verapdf.validation.report.model.Result;
import org.verapdf.validation.report.model.ValidationInfo;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Evgeniy Muravitskiy
 */
public class FixerConfigImpl implements FixerConfig {

	private final PDFDocument document;
	private final ValidationInfo validationResult;
	private final ProcessedObjectsParser parser;
	private final PDFAFlavour flavour;
	private final boolean fixFlavour;

	private FixerConfigImpl(PDDocument document, ValidationInfo validationResult,
							ProcessedObjectsParser parser, PDFAFlavour flavour, boolean fixFlavour) {
		this.document = new PDFDocumentImpl(document);
		this.validationResult = validationResult;
		this.parser = parser;
		this.flavour = flavour;
		this.fixFlavour = fixFlavour;
	}

	@Override
	public Result getValidationResult() {
		return this.validationResult.getResult();
	}

	@Override
	public ValidationProfile getValidationProfile() {
		return this.validationResult.getProfile().getValidationProfile();
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
		return this.fixFlavour;
	}

	@Override
	public PDFAFlavour getPDFAFlavour() {
		return this.flavour;
	}

	public static FixerConfig getFixerConfig(InputStream toFix, ValidationInfo result) {
		return getFixerConfig(toFix, result, XMLProcessedObjectsParser.getInstance(), PDFAFlavour.PDFA_1_B, true);
	}

	public static FixerConfig getFixerConfig(InputStream toFix, ValidationInfo result, ProcessedObjectsParser parser) {
		return getFixerConfig(toFix, result, parser, PDFAFlavour.PDFA_1_B, true);
	}

	public static FixerConfig getFixerConfig(InputStream toFix, ValidationInfo result, PDFAFlavour flavour) {
		return getFixerConfig(toFix, result, XMLProcessedObjectsParser.getInstance(), flavour, true);
	}

	public static FixerConfig getFixerConfig(InputStream toFix, ValidationInfo result,
											 ProcessedObjectsParser parser, PDFAFlavour flavour, boolean fixFlavour) {
		if (toFix == null) {
			throw new IllegalArgumentException("Input stream of source document can not be null");
		}
		try {
			return getFixerConfig(PDDocument.load(toFix, false, true), result, parser, flavour, fixFlavour);
		} catch (IOException e) {
			throw new IllegalArgumentException("Can not load document from input stream", e);
		}
	}

	public static FixerConfig getFixerConfig(PDDocument toFix, ValidationInfo result) {
		return getFixerConfig(toFix, result, XMLProcessedObjectsParser.getInstance());
	}

	public static FixerConfig getFixerConfig(PDDocument toFix, ValidationInfo result, ProcessedObjectsParser parser) {
		return getFixerConfig(toFix, result, parser, PDFAFlavour.PDFA_1_B, true);
	}

	public static FixerConfig getFixerConfig(PDDocument toFix, ValidationInfo result, PDFAFlavour flavour) {
		return getFixerConfig(toFix, result, XMLProcessedObjectsParser.getInstance(), flavour, true);
	}

	public static FixerConfig getFixerConfig(PDDocument toFix, ValidationInfo result,
											 ProcessedObjectsParser parser, PDFAFlavour flavour, boolean fixFlavour) {
		if (toFix == null) {
			throw new IllegalArgumentException("Document for fix can not be null");
		}
		if (result == null) {
			throw new IllegalArgumentException("Validation result can not be null. Make validation before fixing.");
		}
		if (parser == null) {
			throw new IllegalArgumentException("Parser can not be null");
		}
		if (flavour == null) {
			throw new IllegalArgumentException("Flavour can not be null");
		}
		return new FixerConfigImpl(toFix, result, parser, flavour, fixFlavour);
	}

}
