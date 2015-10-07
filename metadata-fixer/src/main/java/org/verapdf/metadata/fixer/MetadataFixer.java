package org.verapdf.metadata.fixer;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.util.DateConverter;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.XmpConstants;
import org.apache.xmpbox.schema.AdobePDFSchema;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.XMPBasicSchema;
import org.apache.xmpbox.schema.XMPSchema;
import org.apache.xmpbox.type.*;
import org.apache.xmpbox.xml.DomXmpParser;
import org.apache.xmpbox.xml.XmpParsingException;
import org.apache.xmpbox.xml.XmpSerializer;
import org.verapdf.metadata.fixer.entity.FixReport;
import org.verapdf.metadata.fixer.entity.ValidationStatus;
import org.verapdf.metadata.fixer.utils.FileGenerator;
import org.verapdf.metadata.fixer.utils.ProcessedObjectsInspector;
import org.verapdf.metadata.fixer.utils.parser.ProcessedObjectsParser;
import org.verapdf.metadata.fixer.utils.parser.XMLProcessedObjectsParser;
import org.verapdf.validation.profile.model.ValidationProfile;
import org.verapdf.validation.report.model.Rule;
import org.verapdf.validation.report.model.ValidationInfo;
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
	private static final String DESCRIPTION = "description";
	private static final String CREATOR = "creator";
	private static final String PRODUCER = "Producer";
	private static final String KEYWORDS = "Keywords";
	private static final String CREATE_DATE = "CreateDate";
	private static final String CREATOR_TOOL = "CreatorTool";
	private static final String MODIFY_DATE = "ModifyDate";

	private final PDDocument document;
	private final XMPMetadata metadata;
	private final ValidationInfo validationResult;

	private ProcessedObjectsParser parser;

	public MetadataFixer(PDDocument document,
						 ValidationInfo validationResult) {
		this.document = document;
		this.metadata = this.getMetadata();
		this.validationResult = validationResult;
	}

	private XMPMetadata getMetadata() {
		PDMetadata metadata = this.document.getDocumentCatalog().getMetadata();
		if (metadata == null) {
			if (this.document.getDocumentInformation().getCOSObject().size() > 0) {
				return XMPMetadata.createXMPMetadata();
			}
		} else {
			try {
				DomXmpParser xmpParser = new DomXmpParser();
				return xmpParser.parse(metadata.getStream().getUnfilteredStream());
			} catch (IOException e) {
				LOGGER.error(
						"Problems with document parsing or structure. "
								+ e.getMessage(), e);
			} catch (XmpParsingException e) {
				LOGGER.error("Problems with XMP parsing. " + e.getMessage(), e);
			}

		}
		return null;
	}

	/**
	 * Initialize parser of processed objects. By default using
	 * {@link org.verapdf.metadata.fixer.utils.parser.XMLProcessedObjectsParser}
	 *
	 * @param parser parser of processed objects
	 */
	public void setParser(ProcessedObjectsParser parser) {
		this.parser = parser;
	}

	/**
	 * Fix metadata and info dictionary for {@link MetadataFixer#document} and
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
	public FixReport fixDocument(File input) throws IOException, URISyntaxException,
			ParserConfigurationException, TransformerException, SAXException {
		File output = FileGenerator.createOutputFile(input);
		return fixDocument(getOutputStream(output));
	}

	/**
	 * Fix metadata and info dictionary for {@link MetadataFixer#document} and
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
	public FixReport fixDocument(File inputFile, String prefix) throws IOException, URISyntaxException,
			TransformerException, ParserConfigurationException, SAXException {
		File output = FileGenerator.createOutputFile(inputFile, prefix);
		return fixDocument(getOutputStream(output));
	}

	/**
	 * Fix metadata and info dictionary for {@link MetadataFixer#document} and
	 * save fixed file a certain path. If fixer no changes apply then no save
	 * will be produced.
	 *
	 * @param folderPath a certain path for store result file
	 * @param fileName   the result file name
	 * @param prefix     for the result file name
	 * @return report of made corrections
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws SAXException
	 */
	public FixReport fixDocument(String folderPath, String fileName, String prefix) throws IOException, URISyntaxException,
			TransformerException, ParserConfigurationException, SAXException {
		File output = FileGenerator.createOutputFile(folderPath, fileName, prefix);
		return fixDocument(getOutputStream(output));
	}

	private BufferedOutputStream getOutputStream(File output) throws FileNotFoundException {
		return new BufferedOutputStream(new FileOutputStream(output));
	}

	/**
	 * Fix metadata and info dictionary for {@link MetadataFixer#document} and
	 * save fixed file a certain path. If fixer no changes apply then no save
	 * will be produced.
	 *
	 * @param output stream to result file
	 * @return report of made corrections
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public FixReport fixDocument(OutputStream output) throws IOException, URISyntaxException,
			TransformerException, ParserConfigurationException, SAXException {
		FixReport report;
		if (this.validationResult.getResult().isCompliant()) {
			report = new FixReport();
			return report;
		} else if (this.metadata != null) {

			report = getFixReport();

			switch (report.getStatus()) {
				case INVALID_DOCUMENT:
					this.metadata.removeSchema(this.metadata.getPDFIdentificationSchema());
					this.document.getDocumentCatalog().getMetadata().getStream().setNeedToBeUpdated(true);
					this.fixMetadata(report);
					break;
				case INVALID_METADATA:
					this.fixMetadata(report);
					addPDFASchema(report);
					break;
				case INVALID_STRUCTURE:
					this.metadata.removeSchema(this.metadata.getPDFIdentificationSchema());
					this.document.getDocumentCatalog().getMetadata().getStream().setNeedToBeUpdated(true);
					break;
			}

			saveDocumentIncremental(output);

			return report;
		} else {
			report = new FixReport();
			report.setStatus(ValidationStatus.INVALID_METADATA);
			report.addFix("Problems with metadata obtain, nothing to save or change.");
			return report;
		}
	}

	private FixReport getFixReport() throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
		FixReport report;
		List<Rule> rules = this.validationResult.getResult().getDetails().getRules();
		ValidationProfile profile = this.validationResult.getProfile().getValidationProfile();
		if (parser == null) {
			parser = XMLProcessedObjectsParser.getInstance();
		}
		report = ProcessedObjectsInspector.validationStatus(rules, profile, parser);
		return report;
	}

	private void fixMetadata(FixReport entity) {
		this.fixDublinCoreSchema(entity);
		this.fixAdobePDFSchema(entity);
		this.fixBasicXMLSchema(entity);
	}

	private void fixDublinCoreSchema(FixReport entity) {
		DublinCoreSchema schema = this.metadata.getDublinCoreSchema();
		if (schema == null && dublinCoreInfoPresent()) {
			schema = this.metadata.createAndAddDublinCoreSchema();
		}
		if (schema != null) {
			List<String> creators = schema.getCreators();
			String metaValue = creators != null && !creators.isEmpty() ? creators.get(0) : null;
			PDDocumentInformation info = this.document.getDocumentInformation();

			fixProperty(entity, schema, schema.getTitle(), info.getTitle(), TITLE);
			fixProperty(entity, schema, schema.getDescription(), info.getSubject(), DESCRIPTION);
			fixProperty(entity, schema, metaValue, info.getAuthor(), CREATOR);
		}
	}

	private void fixAdobePDFSchema(FixReport entity) {
		AdobePDFSchema schema = this.metadata.getAdobePDFSchema();
		if (schema == null && adobePDFInfoPresent()) {
			schema = this.metadata.createAndAddAdobePDFSchema();
		}

		if (schema != null) {
			PDDocumentInformation info = this.document.getDocumentInformation();

			fixProperty(entity, schema, schema.getProducer(), info.getProducer(), PRODUCER);
			fixProperty(entity, schema, schema.getKeywords(), info.getKeywords(), KEYWORDS);
		}
	}

	private void fixBasicXMLSchema(FixReport entity) {
		XMPBasicSchema schema = this.metadata.getXMPBasicSchema();
		if (schema == null && xmpBasicInfoPresent()) {
			schema = this.metadata.createAndAddXMPBasicSchema();
		}

		if (schema != null) {
			PDDocumentInformation info = this.document.getDocumentInformation();

			fixProperty(entity, schema, schema.getCreatorTool(), info.getCreator(), CREATOR_TOOL);
			fixCalendarProperty(entity, schema, schema.getCreateDate(), info.getCreationDate(), CREATE_DATE);
			fixCalendarProperty(entity, schema, schema.getModifyDate(), info.getModificationDate(), MODIFY_DATE);
		}
	}

	private void fixProperty(FixReport entity, XMPSchema schema, String metaValue,
							 String infoValue, String attribute) {
		String key = attributes.get(attribute);
		if (metaValue == null && infoValue != null) {
			doSaveAction(schema, attribute, infoValue);
			entity.addFix("Add " + key + " to metadata from info dictionary");
		} else if (metaValue != null && infoValue != null && !metaValue.equals(infoValue)) {
			PDDocumentInformation info = this.document.getDocumentInformation();
			info.getCOSObject().setString(key, metaValue);
			info.getCOSObject().setNeedToBeUpdated(true);
			entity.addFix("Add " + attribute + " to info dictionary from metadata");
		}
	}

	private void fixCalendarProperty(FixReport entity, XMPBasicSchema schema, Calendar metaValue,
									 Calendar infoValue, String attribute) {
		String key = attributes.get(attribute);
		if (metaValue == null && infoValue != null) {
			TypeMapping tm = schema.getMetadata().getTypeMapping();
			DateType tt = (DateType) tm.instanciateSimpleField(schema.getClass(),
					null, schema.getPrefix(), attribute, infoValue);
			schema.setCreateDateProperty(tt);
			entity.addFix("Add " + key + " to metadata from info dictionary");
		} else if (metaValue != null && infoValue != null &&
				(metaValue.compareTo(infoValue) != 0 || !isValidDateFormat(key))) {
			PDDocumentInformation info = this.document.getDocumentInformation();
			info.getCOSObject().setString(key, DateConverter.toString(metaValue));
			info.getCOSObject().setNeedToBeUpdated(true);
			entity.addFix("Add " + attribute + " to info dictionary from metadata");
		}
	}

	private void addPDFASchema(FixReport entity) {
		if (this.metadata.getPDFIdentificationSchema() == null) {
			this.metadata.createAndAddPFAIdentificationSchema();
			this.document.getDocumentCatalog().getMetadata()
					.getStream().setNeedToBeUpdated(true);
			entity.addFix("PDF/A Identification schema added");
		}
	}

	private boolean isValidDateFormat(String key) {
		final String pdfDateFormatRegex = "(D:)?(\\d\\d){2,7}((([+-](\\d\\d[']))(\\d\\d['])?)?|[Z])";
		String date = this.document.getDocumentInformation().getCOSObject().getString(key);
		return date.matches(pdfDateFormatRegex);
	}

	private void doSaveAction(XMPSchema schema, String attribute, String value) {
		switch (attribute) {
			case TITLE:
				((DublinCoreSchema) schema).setTitle(value);
				break;
			case DESCRIPTION:
				((DublinCoreSchema) schema).setDescription(value);
				break;
			case CREATOR:
				this.fixCreator(schema, value);
				break;
			case PRODUCER:
				((AdobePDFSchema) schema).setProducer(value);
				break;
			case KEYWORDS:
				((AdobePDFSchema) schema).setKeywords(value);
				break;
			case CREATOR_TOOL:
				((XMPBasicSchema) schema).setCreatorTool(value);
				break;
			default:
				return;
		}
		this.document.getDocumentCatalog().getMetadata().getStream().setNeedToBeUpdated(true);
	}

	private void fixCreator(XMPSchema schema, String value) {
		ArrayProperty seq = (ArrayProperty) schema.getAbstractProperty(CREATOR);
		if (seq != null) {
			schema.removeProperty(seq);
		}
		TextType li = schema.createTextType(XmpConstants.LIST_NAME, value);
		ArrayProperty newSeq = schema.createArrayProperty(CREATOR, Cardinality.Seq);
		newSeq.getContainer().addProperty(li);
		schema.addProperty(newSeq);
	}

	private boolean dublinCoreInfoPresent() {
		PDDocumentInformation info = this.document.getDocumentInformation();
		return info.getTitle() != null || info.getSubject() != null || info.getAuthor() != null;
	}

	private boolean adobePDFInfoPresent() {
		PDDocumentInformation info = this.document.getDocumentInformation();
		return info.getProducer() != null && info.getKeywords() != null;
	}

	private boolean xmpBasicInfoPresent() {
		PDDocumentInformation info = this.document.getDocumentInformation();
		return info.getCreator() != null || info.getCreationDate() != null ||
				info.getModificationDate() != null;
	}

	private void saveDocumentIncremental(OutputStream output) throws IOException, TransformerException {
		PDMetadata metadata = this.document.getDocumentCatalog().getMetadata();
		checkFilters(metadata);
		if (metadata.getStream().isNeedToBeUpdated()) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			new XmpSerializer().serialize(this.metadata, out, true);
			metadata.importXMPMetadata(out.toByteArray());
		}
		COSDictionary infoDict = this.document.getDocumentInformation().getCOSObject();
		if (metadata.getStream().isNeedToBeUpdated() || infoDict.isNeedToBeUpdated()) {
			this.document.saveIncremental(output);
			// TODO : are we need to close stream after save?
		}
	}

	private void checkFilters(PDMetadata metadata) {
		COSStream stream = metadata.getStream();
		COSBase filters = stream.getFilters();
		if (filters instanceof COSName ||
				(filters instanceof COSArray && ((COSArray) filters).size() != 0)) {
			stream.setItem(COSName.FILTER, null);
			stream.setNeedToBeUpdated(true);
		}
	}

	static {
		attributes.put(TITLE, "Title");
		attributes.put(DESCRIPTION, "Subject");
		attributes.put(CREATOR, "Author");
		attributes.put(PRODUCER, PRODUCER);
		attributes.put(KEYWORDS, KEYWORDS);
		attributes.put(CREATOR_TOOL, "Creator");
		attributes.put(CREATE_DATE, "CreationDate");
		attributes.put(MODIFY_DATE, "ModDate");
	}

}
