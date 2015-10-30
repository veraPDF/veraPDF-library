package org.verapdf.metadata.fixer;

import org.apache.log4j.Logger;
import org.verapdf.metadata.fixer.entity.InfoDictionary;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.entity.PDFDocument;
import org.verapdf.metadata.fixer.entity.ValidationStatus;
import org.verapdf.metadata.fixer.schemas.AdobePDF;
import org.verapdf.metadata.fixer.schemas.BasicSchema;
import org.verapdf.metadata.fixer.schemas.DublinCore;
import org.verapdf.metadata.fixer.schemas.XMPBasic;
import org.verapdf.metadata.fixer.utils.DateConverter;
import org.verapdf.metadata.fixer.utils.FileGenerator;
import org.verapdf.metadata.fixer.utils.FixerConfig;
import org.verapdf.metadata.fixer.utils.ProcessedObjectsInspector;
import org.verapdf.metadata.fixer.utils.flavour.PDFAFlavour;
import org.verapdf.validation.profile.model.ValidationProfile;
import org.verapdf.validation.report.model.Result;
import org.verapdf.validation.report.model.Rule;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

import static org.verapdf.metadata.fixer.utils.MetadataFixerConstants.*;

/**
 * @author Evgeniy Muravitskiy
 */
public class MetadataFixer {

	private static final Logger LOGGER = Logger.getLogger(MetadataFixer.class);

	private static final Map<String, String> attributes = new HashMap<>(8);

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
	public static MetadataFixerResultImpl fixMetadata(File input, FixerConfig config) throws FileNotFoundException {
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
	public static MetadataFixerResultImpl fixMetadata(File inputFile, String prefix, FixerConfig config)
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
	public static MetadataFixerResultImpl fixMetadata(File inputFile, String fileName, String prefix, FixerConfig config)
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
	public static MetadataFixerResultImpl fixMetadata(OutputStream output, FixerConfig config) {
		Result result = config.getValidationResult();
		return result != null && result.isCompliant() ? new MetadataFixerResultImpl() :
				fixAndSaveDocument(output, config);
	}

	private static MetadataFixerResultImpl fixAndSaveDocument(OutputStream output, FixerConfig config) {
		Metadata metadata = config.getMetadata();
		if (metadata != null) {
			MetadataFixerResultImpl result = new MetadataFixerResultImpl();
			ValidationStatus status = getValidationStatus(config);

			metadata.checkMetadataStream(result);

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

			updateModificationDate(config, result);

			config.getDocument().saveDocumentIncremental(result, output);

			return result;
		} else {
			MetadataFixerResultImpl result = new MetadataFixerResultImpl();
			MetadataFixerResultImpl.RepairStatus status = MetadataFixerResultImpl.RepairStatus.FIX_ERROR;
			result.setRepairStatus(status);
			result.addAppliedFix("Problems with metadata obtain. No possibility to fix metadata.");
			return result;
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

	private static void executeInvalidDocumentCase(FixerConfig config, Metadata metadata, MetadataFixerResultImpl result) {
		if (config.isFixIdentification()) {
			metadata.removePDFIdentificationSchema(result);
		}

		fixMetadata(result, config);

		if (result.getAppliedFixes().size() > 0) {
			result.setRepairStatus(MetadataFixerResultImpl.RepairStatus.WONT_FIX);
		}
	}

	private static void executeInvalidMetadataCase(FixerConfig config, Metadata metadata, MetadataFixerResultImpl result) {
		if (config.isFixIdentification()) {
			metadata.addPDFIdentificationSchema(result, config.getPDFAFlavour());
		}
		fixMetadata(result, config);
	}

	private static void executeInvalidStructureCase(FixerConfig config, Metadata metadata, MetadataFixerResultImpl result) {
		if (config.isFixIdentification()) {
			metadata.removePDFIdentificationSchema(result);
		}
		if (result.getAppliedFixes().size() > 0) {
			result.setRepairStatus(MetadataFixerResultImpl.RepairStatus.WONT_FIX);
		}
	}

	private static void fixMetadata(MetadataFixerResultImpl result, FixerConfig config) {
		if (config.getPDFAFlavour().getPart() == PDFAFlavour.Part.ISO_19005_1) {
			fixDublinCoreSchema(result, config);
			fixAdobePDFSchema(result, config);
			fixBasicXMLSchema(result, config);
		}
	}

	private static void fixDublinCoreSchema(MetadataFixerResultImpl result, FixerConfig config) {
		Metadata metadata = config.getMetadata();
		InfoDictionary info = config.getDocument().getInfoDictionary();
		DublinCore schema = metadata.getDublinCoreSchema(info);
		if (schema != null && info != null) {
			fixProperty(result, schema, info, schema.getTitle(), info.getTitle(), METADATA_TITLE);
			fixProperty(result, schema, info, schema.getSubject(), info.getSubject(), METADATA_SUBJECT);
			fixProperty(result, schema, info, schema.getAuthor(), info.getAuthor(), METADATA_AUTHOR);
		}
	}

	private static void fixAdobePDFSchema(MetadataFixerResultImpl result, FixerConfig config) {
		Metadata metadata = config.getMetadata();
		InfoDictionary info = config.getDocument().getInfoDictionary();
		AdobePDF schema = metadata.getAdobePDFSchema(info);
		if (schema != null && info != null) {
			fixProperty(result, schema, info, schema.getProducer(), info.getProducer(), PRODUCER);
			fixProperty(result, schema, info, schema.getKeywords(), info.getKeywords(), KEYWORDS);
		}
	}

	private static void fixBasicXMLSchema(MetadataFixerResultImpl result, FixerConfig config) {
		Metadata metadata = config.getMetadata();
		InfoDictionary info = config.getDocument().getInfoDictionary();
		XMPBasic schema = metadata.getXMPBasicSchema(info);
		if (schema != null && info != null) {
			fixProperty(result, schema, info, schema.getCreator(), info.getCreator(), METADATA_CREATOR);
			fixCalendarProperty(result, schema, info,
					schema.getCreationDate(), info.getCreationDate(), METADATA_CREATION_DATE);
			fixCalendarProperty(result, schema, info,
					schema.getModificationDate(), info.getModificationDate(), METADATA_MODIFICATION_DATE);
		}
	}

	private static void fixProperty(MetadataFixerResultImpl result, BasicSchema schema, InfoDictionary info, String metaValue,
									String infoValue, String attribute) {
		if (infoValue != null) {
			String key = attributes.get(attribute);
			if (metaValue == null) {
				doSaveAction(schema, attribute, infoValue);
				result.addAppliedFix("Added '" + key + "' to metadata from info dictionary");
			} else if (!metaValue.equals(infoValue)) {
				doSaveAction(info, attribute, metaValue);
				result.addAppliedFix("Added '" + attribute + "' to info dictionary from metadata");
			}
		}
	}

	private static void fixCalendarProperty(MetadataFixerResultImpl result, BasicSchema schema, InfoDictionary info, String metaValue,
											String infoValue, String attribute) {
		if (infoValue != null) {
			String key = attributes.get(attribute);
			String utcInfoValue = DateConverter.toUTCString(infoValue);
			if (metaValue == null) {
				doSaveAction(schema, attribute, infoValue);
				result.addAppliedFix("Added '" + key + "' to metadata from info dictionary");
			} else if (!metaValue.equals(utcInfoValue) || !infoValue.matches(PDF_DATE_FORMAT_REGEX)) {
				doSaveAction(info, attribute, metaValue);
				result.addAppliedFix("Added '" + attribute + "' to info dictionary from metadata");
			}
		}
	}

	private static void doSaveAction(BasicSchema schema, String attribute, String value) {
		switch (attribute) {
			case METADATA_TITLE:
				((DublinCore) schema).setTitle(value);
				break;
			case METADATA_SUBJECT:
				((DublinCore) schema).setSubject(value);
				break;
			case METADATA_AUTHOR:
				((DublinCore) schema).setAuthor(value);
				break;
			case PRODUCER:
				((AdobePDF) schema).setProducer(value);
				break;
			case KEYWORDS:
				((AdobePDF) schema).setKeywords(value);
				break;
			case METADATA_CREATOR:
				((XMPBasic) schema).setCreator(value);
				break;
			case METADATA_CREATION_DATE:
				((XMPBasic) schema).setCreationDate(value);
				break;
			case METADATA_MODIFICATION_DATE:
				((XMPBasic) schema).setModificationDate(value);
				break;
			default:
				return;
		}
		schema.setNeedToBeUpdated(true);
	}

	private static void updateModificationDate(FixerConfig config, MetadataFixerResultImpl result) {
		PDFDocument document = config.getDocument();
		InfoDictionary info = document.getInfoDictionary();
		XMPBasic schema = document.getMetadata().getXMPBasicSchema(info);

		if (document.isNeedToBeUpdated() && schema != null) {
			Calendar time = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			if (schema.getModificationDate() != null) {
				doSaveAction(schema, METADATA_MODIFICATION_DATE, DateConverter.toUTCString(time));
				result.addAppliedFix("Set new modification date to metadata");
			}
			if (info != null && info.getModificationDate() != null) {
				doSaveAction(info, METADATA_MODIFICATION_DATE, DateConverter.toPDFFormat(time));
				result.addAppliedFix("Set new modification date to info dictionary");
			}
		}
	}

	static {
		attributes.put(METADATA_TITLE, INFO_TITLE);
		attributes.put(METADATA_SUBJECT, INFO_SUBJECT);
		attributes.put(METADATA_AUTHOR, INFO_AUTHOR);
		attributes.put(PRODUCER, PRODUCER);
		attributes.put(KEYWORDS, KEYWORDS);
		attributes.put(METADATA_CREATOR, INFO_CREATOR);
		attributes.put(METADATA_CREATION_DATE, INFO_CREATION_DATE);
		attributes.put(METADATA_MODIFICATION_DATE, INFO_MODIFICATION_DATE);
	}

}
