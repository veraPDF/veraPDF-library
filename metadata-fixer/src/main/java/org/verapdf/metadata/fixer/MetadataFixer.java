package org.verapdf.metadata.fixer;

import org.apache.log4j.Logger;
import org.verapdf.metadata.fixer.entity.InfoDictionary;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.entity.ValidationStatus;
import org.verapdf.metadata.fixer.schemas.AdobePDF;
import org.verapdf.metadata.fixer.schemas.BasicSchema;
import org.verapdf.metadata.fixer.schemas.DublinCore;
import org.verapdf.metadata.fixer.schemas.XMPBasic;
import org.verapdf.metadata.fixer.utils.DateConverter;
import org.verapdf.metadata.fixer.utils.FileGenerator;
import org.verapdf.metadata.fixer.utils.FixerConfig;
import org.verapdf.metadata.fixer.utils.ProcessedObjectsInspector;
import org.verapdf.metadata.fixer.utils.flavour.Part;
import org.verapdf.validation.profile.model.ValidationProfile;
import org.verapdf.validation.report.model.Result;
import org.verapdf.validation.report.model.Rule;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Calendar;
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
	 * <p/>
	 * {@code input} is file near which will be save fixed version of document.
	 * Input file give provides path to the folder and the result file name
	 *
	 * @param input  file near which will be save fixed version of document
	 * @param config configuration for metadata fixer
	 * @return report of made corrections
	 */
	public static MetadataFixerResult fixMetadata(File input, FixerConfig config) throws FileNotFoundException {
		File output = FileGenerator.createOutputFile(input);
		return fixMetadata(getOutputStream(output), config);
	}

	/**
	 * Fix metadata and info dictionary for {@link org.verapdf.metadata.fixer.entity.PDFDocument} and
	 * save fixed file near source file. If fixer no changes apply then no save
	 * will be produced.
	 * <p/>
	 * {@code input} is file near which will be save fixed version of document.
	 * Input file provides path to the folder and the result file name. Prefix
	 * provide additional part for the result file name.
	 *
	 * @param inputFile file near which will be save fixed version of document
	 * @param prefix    for the result file name
	 * @param config    configuration for metadata fixer
	 * @return report of made corrections
	 */
	public static MetadataFixerResult fixMetadata(File inputFile, String prefix, FixerConfig config)
			throws FileNotFoundException {
		File output = FileGenerator.createOutputFile(inputFile, prefix);
		return fixMetadata(getOutputStream(output), config);
	}

	/**
	 * Fix metadata and info dictionary for {@link org.verapdf.metadata.fixer.entity.PDFDocument} and
	 * save fixed file near source file. If fixer no changes apply then no save
	 * will be produced.
	 * <p/>
	 * {@code input} is file near which will be save fixed version of document.
	 * Input file give provides path to the folder and the result file name
	 *
	 * @param inputFile file near which will be save fixed version of document
	 * @param fileName  expected name of result file
	 * @param prefix    prefix for name of result file
	 * @param config    configuration for metadata fixer
	 * @return report of made corrections
	 */
	public static MetadataFixerResult fixMetadata(File inputFile, String fileName, String prefix, FixerConfig config)
			throws FileNotFoundException {
		File output = FileGenerator.createOutputFile(inputFile, fileName, prefix);
		return fixMetadata(getOutputStream(output), config);
	}

	private static OutputStream getOutputStream(File output) throws FileNotFoundException {
		return new BufferedOutputStream(new FileOutputStream(output));
	}

	/**
	 * Fix metadata and info dictionary for {@link org.verapdf.metadata.fixer.entity.PDFDocument} and
	 * save fixed file a certain path. If fixer no changes apply then no save
	 * will be produced.
	 *
	 * @param output stream to result file
	 * @param config configuration for metadata fixer
	 * @return report of made corrections
	 */
	public static MetadataFixerResult fixMetadata(OutputStream output, FixerConfig config) {
		Result result = config.getValidationResult();
		return result != null && result.isCompliant() ? new MetadataFixerResult() :
				fixAndSaveDocument(output, config);
	}

	private static MetadataFixerResult fixAndSaveDocument(OutputStream output, FixerConfig config) {
		Metadata metadata = config.getMetadata();
		if (metadata != null) {
			MetadataFixerResult result = new MetadataFixerResult();
			ValidationStatus status = getValidationStatus(config);
			metadata.unfilterMetadataStream(result);

			switch (status) {
				case INVALID_DOCUMENT:
					executeInvalidDocumentCase(config, metadata, result);
					break;
				case INVALID_METADATA:
					executeInvalidMetadataCase(config, metadata, result);
					break;
				case INVALID_STRUCTURE:
					executeInvalidStructureCase(config, metadata, result);
					break;
			}

			config.getDocument().saveDocumentIncremental(result, output);

			return result;
		} else {
			MetadataFixerResult.RepairStatus res = MetadataFixerResult.RepairStatus.NOT_REPAIRABLE;
			return new MetadataFixerResult(res);
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

	private static void executeInvalidDocumentCase(FixerConfig config, Metadata metadata, MetadataFixerResult result) {
		if (config.isFixIdentification()) {
			metadata.removePDFIdentificationSchema(result);
		}
		fixMetadata(result, config);
	}

	private static void executeInvalidMetadataCase(FixerConfig config, Metadata metadata, MetadataFixerResult result) {
		if (config.isFixIdentification()) {
			metadata.addPDFIdentificationSchema(result, config.getPDFAFlavour());
		}
		fixMetadata(result, config);
	}

	private static void executeInvalidStructureCase(FixerConfig config, Metadata metadata, MetadataFixerResult result) {
		if (config.isFixIdentification()) {
			metadata.removePDFIdentificationSchema(result);
		}
	}

	private static void fixMetadata(MetadataFixerResult result, FixerConfig config) {
		if (config.getPDFAFlavour().getPart() == Part.ISO_19005_1) {
			fixDublinCoreSchema(result, config);
			fixAdobePDFSchema(result, config);
			fixBasicXMLSchema(result, config);
		}
	}

	private static void fixDublinCoreSchema(MetadataFixerResult result, FixerConfig config) {
		Metadata metadata = config.getMetadata();
		InfoDictionary info = config.getDocument().getInfoDictionary();
		DublinCore schema = metadata.getDublinCoreSchema(info);
		if (schema != null) {
			fixProperty(result, schema, info, schema.getTitle(), info.getTitle(), TITLE);
			fixProperty(result, schema, info, schema.getSubject(), info.getSubject(), SUBJECT);
			fixProperty(result, schema, info, schema.getAuthor(), info.getAuthor(), AUTHOR);
		}
	}

	private static void fixAdobePDFSchema(MetadataFixerResult result, FixerConfig config) {
		Metadata metadata = config.getMetadata();
		InfoDictionary info = config.getDocument().getInfoDictionary();
		AdobePDF schema = metadata.getAdobePDFSchema(info);
		if (schema != null) {
			fixProperty(result, schema, info, schema.getProducer(), info.getProducer(), PRODUCER);
			fixProperty(result, schema, info, schema.getKeywords(), info.getKeywords(), KEYWORDS);
		}
	}

	private static void fixBasicXMLSchema(MetadataFixerResult result, FixerConfig config) {
		Metadata metadata = config.getMetadata();
		InfoDictionary info = config.getDocument().getInfoDictionary();
		XMPBasic schema = metadata.getXMPBasicSchema(info);
		if (schema != null) {
			fixProperty(result, schema, info, schema.getCreator(), info.getCreator(), CREATOR);
			fixCalendarProperty(result, schema, info, schema.getCreationDate(), info.getCreationDate(), CREATE_DATE);
			fixCalendarProperty(result, schema, info, schema.getModificationDate(), info.getModificationDate(), MODIFY_DATE);
			updateModificationDate(info, schema, config, result);
		}
	}

	private static void fixProperty(MetadataFixerResult result, BasicSchema schema, InfoDictionary info, String metaValue,
									String infoValue, String attribute) {
		String key = attributes.get(attribute);
		if (metaValue == null && infoValue != null) {
			doSaveAction(schema, attribute, infoValue);
			result.addAppliedFix("Added '" + key + "' to metadata from info dictionary");
		} else if (metaValue != null && infoValue != null && !metaValue.equals(infoValue)) {
			doSaveAction(info, attribute, metaValue);
			result.addAppliedFix("Added '" + attribute + "' to info dictionary from metadata");
		}
	}

	private static void fixCalendarProperty(MetadataFixerResult result, BasicSchema schema, InfoDictionary info, String metaValue,
											String infoValue, String attribute) {
		String key = attributes.get(attribute);
		if (metaValue == null && infoValue != null) {
			doSaveAction(schema, attribute, infoValue);
			result.addAppliedFix("Added '" + key + "' to metadata from info dictionary");
		} else if (metaValue != null && infoValue != null &&
				(metaValue.compareTo(infoValue) != 0 || !isValidDateFormat(infoValue))) {
			doSaveAction(info, attribute, metaValue);
			result.addAppliedFix("Added '" + attribute + "' to info dictionary from metadata");
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

	private static void updateModificationDate(InfoDictionary info, XMPBasic schema,
											   FixerConfig config, MetadataFixerResult result) {
		if (config.getDocument().isNeedToBeUpdated()) {
			Calendar time = Calendar.getInstance();
			if (schema.getModificationDate() != null) {
				doSaveAction(schema, MODIFY_DATE, DateConverter.toUTCString(time));
				result.addAppliedFix("Set new modification date to metadata");
			}
			if (info.getModificationDate() != null) {
				doSaveAction(info, MODIFY_DATE, DateConverter.toPDFFormat(time));
				result.addAppliedFix("Set new modification date to info dictionary");
			}
		}
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
