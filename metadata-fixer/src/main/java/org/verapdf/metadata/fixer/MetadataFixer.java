package org.verapdf.metadata.fixer;

import org.apache.log4j.Logger;
import org.verapdf.metadata.fixer.entity.FixReport;
import org.verapdf.metadata.fixer.entity.InfoDictionary;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.entity.ValidationStatus;
import org.verapdf.metadata.fixer.schemas.AdobePDF;
import org.verapdf.metadata.fixer.schemas.BasicSchema;
import org.verapdf.metadata.fixer.schemas.DublinCore;
import org.verapdf.metadata.fixer.schemas.XMPBasic;
import org.verapdf.metadata.fixer.utils.FileGenerator;
import org.verapdf.metadata.fixer.utils.FixerConfig;
import org.verapdf.metadata.fixer.utils.ProcessedObjectsInspector;
import org.verapdf.validation.profile.model.ValidationProfile;
import org.verapdf.validation.report.model.Result;
import org.verapdf.validation.report.model.Rule;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Evgeniy Muravitskiy
 */
public class MetadataFixer {

	private static final Logger LOGGER = Logger.getLogger(MetadataFixer.class);

	private static final Map<String, String> attributes = new HashMap<>(8);

	private static final String TITLE = "title";
	private static final String SUBJECT = "description";
	private static final String AUTHOR = "creator";
	private static final String PRODUCER = "Producer";
	private static final String KEYWORDS = "Keywords";
	private static final String CREATE_DATE = "CreateDate";
	private static final String CREATOR = "CreatorTool";
	private static final String MODIFY_DATE = "ModifyDate";

	private MetadataFixer() {
		// disable default constructor
	}

	/**
	 * Fix metadata and info dictionary for {@link org.verapdf.metadata.fixer.entity.PDFDocument} and
	 * save fixed file near source file. If fixer no changes apply then no save
	 * will be produced.
	 * <p>
	 * {@code input} is file near which will be save fixed version of document.
	 * Input file give provides path to the folder and the result file name
	 *
	 * @param input file near which will be save fixed version of document
	 * @return report of made corrections
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws SAXException
	 */
	public static FixReport fixDocument(File input, FixerConfig config) throws IOException, URISyntaxException,
			ParserConfigurationException, TransformerException, SAXException {
		File output = FileGenerator.createOutputFile(input);
		return fixDocument(getOutputStream(output), config);
	}

	/**
	 * Fix metadata and info dictionary for {@link org.verapdf.metadata.fixer.entity.PDFDocument} and
	 * save fixed file near source file. If fixer no changes apply then no save
	 * will be produced.
	 * <p>
	 * {@code input} is file near which will be save fixed version of document.
	 * Input file provides path to the folder and the result file name. Prefix
	 * provide additional part for the result file name.
	 *
	 * @param inputFile file near which will be save fixed version of document
	 * @param prefix    for the result file name
	 * @return report of made corrections
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws SAXException
	 */
	public static FixReport fixDocument(File inputFile, String prefix, FixerConfig config) throws IOException, URISyntaxException,
			TransformerException, ParserConfigurationException, SAXException {
		File output = FileGenerator.createOutputFile(inputFile, prefix);
		return fixDocument(getOutputStream(output), config);
	}

	public static FixReport fixDocument(File inputFile, String fileName, String prefix, FixerConfig config)
			throws IOException, URISyntaxException, TransformerException, ParserConfigurationException, SAXException {
		File output = FileGenerator.createOutputFile(inputFile, prefix);
		return fixDocument(getOutputStream(output), config);
	}

	private static BufferedOutputStream getOutputStream(File output) throws FileNotFoundException {
		return new BufferedOutputStream(new FileOutputStream(output));
	}

	/**
	 * Fix metadata and info dictionary for {@link org.verapdf.metadata.fixer.entity.PDFDocument} and
	 * save fixed file a certain path. If fixer no changes apply then no save
	 * will be produced.
	 *
	 * @param output stream to result file
	 * @param config
	 * @return report of made corrections
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static FixReport fixDocument(OutputStream output, FixerConfig config) throws IOException, URISyntaxException,
			TransformerException, ParserConfigurationException, SAXException {
		Result result = config.getValidationResult();
		if (result != null && result.isCompliant()) {
			FixReport report = new FixReport();
			report.addFix("Document is already valid. Nothing to change or add.");
			return report;
		}
		return fixAndSaveDocument(output, config);
	}

	private static FixReport fixAndSaveDocument(OutputStream output, FixerConfig config) throws IOException {
		FixReport report = new FixReport();;
		Metadata metadata = config.getMetadata();
		if (metadata != null) {
			report.setStatus(getValidationStatus(config));

			switch (report.getStatus()) {
				case INVALID_DOCUMENT:
					metadata.removePDFIdentificationSchema(report);
					fixMetadata(report, config);
					break;
				case INVALID_METADATA:
					fixMetadata(report, config);
					metadata.addPDFIdentificationSchema(report);
					break;
				case INVALID_STRUCTURE:
					metadata.removePDFIdentificationSchema(report);
					metadata.setNeedToBeUpdated(true);
					break;
			}

			config.getDocument().saveDocumentIncremental(report, output);

			return report;
		} else {
			report.setStatus(ValidationStatus.INVALID_METADATA);
			report.addFix("Problems with metadata obtain, nothing to save or change.");
			return report;
		}
	}

	private static ValidationStatus getValidationStatus(FixerConfig config) {
		Result result = config.getValidationResult();
		if (result != null) {
			List<Rule> rules = result.getDetails().getRules();
			ValidationProfile profile = config.getValidationProfile();
			if (profile != null) {
				try {
					return ProcessedObjectsInspector.validationStatus(rules, profile, config.getParser());
				} catch (IOException | URISyntaxException | ParserConfigurationException | SAXException e) {
					LOGGER.error("Problem with validation status obtain. Validation status set as Invalid Document.");
					LOGGER.error(e);
					return ValidationStatus.INVALID_DOCUMENT;
				}
			}
		}
		LOGGER.error("Problem with validation status obtain. Validation status set as Invalid Metadata.");
		return ValidationStatus.INVALID_METADATA;
	}

	private static FixReport getInvalidStatus(ValidationStatus status, String message) {
		FixReport report = new FixReport();
		report.setStatus(status);
		report.addFix(message);
		LOGGER.error(message);
		return report;
	}

	private static void fixMetadata(FixReport report, FixerConfig config) {
		fixDublinCoreSchema(report, config);
		fixAdobePDFSchema(report, config);
		fixBasicXMLSchema(report, config);
	}

	private static void fixDublinCoreSchema(FixReport report, FixerConfig config) {
		Metadata metadata = config.getMetadata();
		InfoDictionary info = config.getDocument().getInfoDictionary();
		DublinCore schema = metadata.getDublinCoreSchema(info);
		if (schema != null) {
			fixProperty(report, schema, info, schema.getTitle(), info.getTitle(), TITLE);
			fixProperty(report, schema, info, schema.getSubject(), info.getSubject(), SUBJECT);
			fixProperty(report, schema, info, schema.getAuthor(), info.getAuthor(), AUTHOR);
		}
	}

	private static void fixAdobePDFSchema(FixReport report, FixerConfig config) {
		Metadata metadata = config.getMetadata();
		InfoDictionary info = config.getDocument().getInfoDictionary();
		AdobePDF schema = metadata.getAdobePDFSchema(info);
		if (schema != null) {
			fixProperty(report, schema, info, schema.getProducer(), info.getProducer(), PRODUCER);
			fixProperty(report, schema, info, schema.getKeywords(), info.getKeywords(), KEYWORDS);
		}
	}

	private static void fixBasicXMLSchema(FixReport report, FixerConfig config) {
		Metadata metadata = config.getMetadata();
		InfoDictionary info = config.getDocument().getInfoDictionary();
		XMPBasic schema = metadata.getXMPBasicSchema(info);
		if (schema != null) {
			fixProperty(report, schema, info, schema.getCreator(), info.getCreator(), CREATOR);
			fixCalendarProperty(report, schema, info, schema.getCreationDate(), info.getCreationDate(), CREATE_DATE);
			fixCalendarProperty(report, schema, info, schema.getModificationDate(), info.getModificationDate(), MODIFY_DATE);
		}
	}

	private static void fixProperty(FixReport entity, BasicSchema schema, InfoDictionary info, String metaValue,
									String infoValue, String attribute) {
		String key = attributes.get(attribute);
		if (metaValue == null && infoValue != null) {
			doSaveAction(schema, attribute, infoValue);
			entity.addFix("Added '" + key + "' to metadata from info dictionary");
		} else if (metaValue != null && infoValue != null && !metaValue.equals(infoValue)) {
			doSaveAction(info, attribute, metaValue);
			entity.addFix("Added '" + attribute + "' to info dictionary from metadata");
		}
	}

	private static void fixCalendarProperty(FixReport entity, BasicSchema schema, InfoDictionary info, String metaValue,
									String infoValue, String attribute) {
		String key = attributes.get(attribute);
		if (metaValue == null && infoValue != null) {
			doSaveAction(schema, attribute, infoValue);
			entity.addFix("Added '" + key + "' to metadata from info dictionary");
		} else if (metaValue != null && infoValue != null &&
				(metaValue.compareTo(infoValue) != 0 || !isValidDateFormat(infoValue))) {
			doSaveAction(info, attribute, metaValue);
			entity.addFix("Added '" + attribute + "' to info dictionary from metadata");
		}
	}

	private static boolean isValidDateFormat(String value) {
		final String pdfDateFormatRegex = "(D:)?(\\d\\d){2,7}((([+-](\\d\\d[']))(\\d\\d['])?)?|[Z])";
		return value.matches(pdfDateFormatRegex);
	}

	private static void doSaveAction(BasicSchema schema, String attribute, String value) {
		switch (attribute) {
			case TITLE:
				((DublinCore) schema).setTitle(value);
				break;
			case SUBJECT:
				((DublinCore) schema).setSubject(value);
				break;
			case AUTHOR:
				((DublinCore) schema).setAuthor(value);
				break;
			case PRODUCER:
				((AdobePDF) schema).setProducer(value);
				break;
			case KEYWORDS:
				((AdobePDF) schema).setKeywords(value);
				break;
			case CREATOR:
				((XMPBasic) schema).setCreator(value);
				break;
			case CREATE_DATE:
				((XMPBasic) schema).setCreationDate(value);
				break;
			case MODIFY_DATE:
				((XMPBasic) schema).setModificationDate(value);
				break;
			default:
				return;
		}
		schema.setNeedToBeUpdated(true);
	}

	static {
		attributes.put(TITLE, "Title");
		attributes.put(SUBJECT, "Subject");
		attributes.put(AUTHOR, "Author");
		attributes.put(PRODUCER, PRODUCER);
		attributes.put(KEYWORDS, KEYWORDS);
		attributes.put(CREATOR, "Creator");
		attributes.put(CREATE_DATE, "CreationDate");
		attributes.put(MODIFY_DATE, "ModDate");
	}

}
