package org.verapdf.metadata.fixer.impl;

import org.apache.log4j.Logger;
import org.verapdf.metadata.fixer.entity.InfoDictionary;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.entity.PDFDocument;
import org.verapdf.metadata.fixer.schemas.AdobePDF;
import org.verapdf.metadata.fixer.schemas.BasicSchema;
import org.verapdf.metadata.fixer.schemas.DublinCore;
import org.verapdf.metadata.fixer.schemas.XMPBasic;
import org.verapdf.metadata.fixer.utils.DateConverter;
import org.verapdf.metadata.fixer.utils.FixerConfig;
import org.verapdf.metadata.fixer.utils.ProcessedObjectsInspector;
import org.verapdf.metadata.fixer.utils.ValidationStatus;
import org.verapdf.pdfa.MetadataFixer;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.MetadataFixerResultImpl;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.ProfileDirectory;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static org.verapdf.metadata.fixer.utils.MetadataFixerConstants.*;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class MetadataFixerImpl implements MetadataFixer {
    private static final ProfileDirectory PROFILES = Profiles
            .getVeraProfileDirectory();

    private static final Logger LOGGER = Logger.getLogger(MetadataFixerImpl.class);

    private static final Map<String, String> attributes = new HashMap<>(8);

    protected MetadataFixerImpl() {
        // enabled only for nested classes
    }

    /**
     * Fix metadata and info dictionary for
     * {@link org.verapdf.metadata.fixer.entity.PDFDocument} and save fixed file
     * a certain path. If fixer no changes apply then no save will be produced.
     *
     * @param output stream to result file
     * @param config configuration for metadata fixer
     * @return report of made corrections
     */
    public static MetadataFixerResult fixMetadata(OutputStream output, FixerConfig config) {
        ValidationResult result = config.getValidationResult();
        return result != null && result.isCompliant() ? new MetadataFixerResultImpl.Builder().build()
                : fixAndSaveDocument(output, config);
    }

    private static MetadataFixerResult fixAndSaveDocument(
            OutputStream output, FixerConfig config) {
        try {
            Metadata metadata = config.getDocument().getMetadata();
            if (metadata != null) {
                MetadataFixerResultImpl.Builder resultBuilder = new MetadataFixerResultImpl.Builder();
                ValidationStatus status = getValidationStatus(config);

                switch (status) {
                    case INVALID_METADATA:
                        executeInvalidMetadataCase(config, metadata, resultBuilder);
                        break;
                    case INVALID_DOCUMENT:
                    case INVALID_STRUCTURE: {
                        resultBuilder.status(MetadataFixerResult.RepairStatus.WONT_FIX);
                        if (config.isFixIdentification()) {
                            metadata.removePDFIdentificationSchema(resultBuilder,
                                    config.getValidationResult().getPDFAFlavour());
                        }
                        break;
                    }
                    default:
                        break;
                }

                updateModificationDate(config, resultBuilder);

                MetadataFixerResult partialResult = config.getDocument().saveDocumentIncremental(resultBuilder.getStatus(), output);
                resultBuilder.status(partialResult.getRepairStatus());
                for (String fix : partialResult.getAppliedFixes()) {
                    resultBuilder.addFix(fix);
                }
                return resultBuilder.build();
            }

            return getErrorResult("Problems with metadata obtain. No possibility to fix metadata.");
        } catch (Exception e) {
            return getErrorResult("Error while fixing metadata: " + e.getMessage());
        }
    }

    private static MetadataFixerResult getErrorResult(String message) {
        MetadataFixerResultImpl.Builder resultBuilder = new MetadataFixerResultImpl.Builder();
        resultBuilder.status(MetadataFixerResultImpl.RepairStatus.FIX_ERROR).addFix(message);
        return resultBuilder.build();
    }

    private static ValidationStatus getValidationStatus(FixerConfig config) {
        ValidationResult result = config.getValidationResult();
        ValidationProfile profile = PROFILES
                .getValidationProfileByFlavour(config.getValidationResult().getPDFAFlavour());
        if (profile != null) {
            try {
                return ProcessedObjectsInspector
                        .validationStatus(result.getTestAssertions(), profile,
                                config.getParser());
            } catch (IOException | URISyntaxException
                    | ParserConfigurationException | SAXException e) {
                LOGGER.error("Problem with validation status obtain. Validation status set as Invalid Document.");
                LOGGER.error(e);
                return ValidationStatus.INVALID_DOCUMENT;
            }
        }
        LOGGER.error("Problem with validation status obtain. Validation status set as Invalid Metadata.");
        return ValidationStatus.INVALID_METADATA;
    }

    private static void executeInvalidMetadataCase(FixerConfig config,
                                                   Metadata metadata, MetadataFixerResultImpl.Builder resultBuilder) {
        if (config.getValidationResult().getPDFAFlavour().getPart() == PDFAFlavour.Specification.ISO_19005_1) {
            config.getDocument().removeFiltersForAllMetadataObjects(resultBuilder);
        }
        fixMetadata(resultBuilder, config);
        if (config.isFixIdentification()) {
            metadata.addPDFIdentificationSchema(resultBuilder,
                    config.getValidationResult().getPDFAFlavour());
        }

        if (metadata.isNeedToBeUpdated()) {
            metadata.checkMetadataStream(resultBuilder, config.getValidationResult().getPDFAFlavour());
        }
    }

    private static void fixMetadata(MetadataFixerResultImpl.Builder resultBuilder,
                                    FixerConfig config) {
        PDFAFlavour pdfaFlavour = config.getValidationResult().getPDFAFlavour();
        if (pdfaFlavour.getPart() == PDFAFlavour.Specification.ISO_19005_1) {
            fixDublinCoreSchema(resultBuilder, config);
            fixAdobePDFSchema(resultBuilder, config);
            fixBasicXMLSchema(resultBuilder, config);
        }
    }

    private static void fixDublinCoreSchema(MetadataFixerResultImpl.Builder resultBuilder,
                                            FixerConfig config) {
        Metadata metadata = config.getDocument().getMetadata();
        InfoDictionary info = config.getDocument().getInfoDictionary();
        DublinCore schema = metadata.getDublinCoreSchema(info);
        if (schema != null && info != null) {
            fixProperty(resultBuilder, schema, info, schema.getTitle(),
                    info.getTitle(), METADATA_TITLE);
            fixProperty(resultBuilder, schema, info, schema.getSubject(),
                    info.getSubject(), METADATA_SUBJECT);
            fixProperty(resultBuilder, schema, info, schema.getAuthor(),
                    info.getAuthor(), METADATA_AUTHOR);
        }
    }

    private static void fixAdobePDFSchema(MetadataFixerResultImpl.Builder resultBuilder,
                                          FixerConfig config) {
        Metadata metadata = config.getDocument().getMetadata();
        InfoDictionary info = config.getDocument().getInfoDictionary();
        AdobePDF schema = metadata.getAdobePDFSchema(info);
        if (schema != null && info != null) {
            fixProperty(resultBuilder, schema, info, schema.getProducer(),
                    info.getProducer(), PRODUCER);
            fixProperty(resultBuilder, schema, info, schema.getKeywords(),
                    info.getKeywords(), KEYWORDS);
        }
    }

    private static void fixBasicXMLSchema(MetadataFixerResultImpl.Builder resultBuilder,
                                          FixerConfig config) {
        Metadata metadata = config.getDocument().getMetadata();
        InfoDictionary info = config.getDocument().getInfoDictionary();
        XMPBasic schema = metadata.getXMPBasicSchema(info);
        if (schema != null && info != null) {
            fixProperty(resultBuilder, schema, info, schema.getCreator(),
                    info.getCreator(), METADATA_CREATOR);
            fixCalendarProperty(resultBuilder, schema, info, schema.getCreationDate(),
                    info.getCreationDate(), METADATA_CREATION_DATE);
            fixCalendarProperty(resultBuilder, schema, info,
                    schema.getModificationDate(), info.getModificationDate(),
                    METADATA_MODIFICATION_DATE);
        }
    }

    private static void fixProperty(MetadataFixerResultImpl.Builder resultBuilder,
                                    BasicSchema schema, InfoDictionary info, String metaValue,
                                    String infoValue, String attribute) {
        if (infoValue != null) {
            String key = attributes.get(attribute);
            if (metaValue == null) {
                doSaveAction(schema, attribute, infoValue);
                resultBuilder.addFix("Added '" + key
                        + "' to metadata from info dictionary");
            } else if (!metaValue.equals(infoValue)) {
                doSaveAction(info, attribute, metaValue);
                resultBuilder.addFix("Added '" + attribute
                        + "' to info dictionary from metadata");
            }
        }
    }

    private static void fixCalendarProperty(MetadataFixerResultImpl.Builder resultBuilder,
                                            BasicSchema schema, InfoDictionary info, String metaValue,
                                            String infoValue, String attribute) {
        if (infoValue != null) {
            String key = attributes.get(attribute);
            String utcInfoValue = DateConverter.toUTCString(infoValue);
            if (metaValue == null) {
                doSaveAction(schema, attribute, infoValue);
                resultBuilder.addFix("Added '" + key
                        + "' to metadata from info dictionary");
            } else if (!metaValue.equals(utcInfoValue)
                    || !infoValue.matches(PDF_DATE_FORMAT_REGEX)) {
                doSaveAction(info, attribute, metaValue);
                resultBuilder.addFix("Added '" + attribute
                        + "' to info dictionary from metadata");
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

    private static void updateModificationDate(FixerConfig config,
                                               MetadataFixerResultImpl.Builder resultBuilder) {
        PDFDocument document = config.getDocument();
        InfoDictionary info = document.getInfoDictionary();
        XMPBasic schema = document.getMetadata().getXMPBasicSchema(info);

        if (document.isNeedToBeUpdated() && schema != null) {
            Calendar time = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            if (schema.getModificationDate() != null) {
                doSaveAction(schema, METADATA_MODIFICATION_DATE,
                        DateConverter.toUTCString(time));
                resultBuilder.addFix("Set new modification date to metadata");
            }
            if (info != null && info.getModificationDate() != null) {
                doSaveAction(info, METADATA_MODIFICATION_DATE,
                        DateConverter.toPDFFormat(time));
                resultBuilder.addFix("Set new modification date to info dictionary");
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
