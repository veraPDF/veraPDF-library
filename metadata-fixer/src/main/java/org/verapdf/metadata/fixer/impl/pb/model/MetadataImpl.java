package org.verapdf.metadata.fixer.impl.pb.model;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.AdobePDFSchema;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.schema.XMPBasicSchema;
import org.apache.xmpbox.type.BadFieldValueException;
import org.apache.xmpbox.xml.XmpSerializer;
import org.verapdf.metadata.fixer.entity.InfoDictionary;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.impl.MetadataFixerResultImpl;
import org.verapdf.metadata.fixer.impl.pb.schemas.AdobePDFSchemaImpl;
import org.verapdf.metadata.fixer.impl.pb.schemas.DublinCoreSchemaImpl;
import org.verapdf.metadata.fixer.impl.pb.schemas.XMPBasicSchemaImpl;
import org.verapdf.metadata.fixer.schemas.AdobePDF;
import org.verapdf.metadata.fixer.schemas.DublinCore;
import org.verapdf.metadata.fixer.schemas.XMPBasic;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import javax.xml.transform.TransformerException;

/**
 * @author Evgeniy Muravitskiy
 */
public class MetadataImpl implements Metadata {

    private static final Logger LOGGER = Logger.getLogger(MetadataImpl.class);

    private final XMPMetadata metadata;
    private final COSStream stream;

    /**
     * @param metadata
     * @param stream
     */
    public MetadataImpl(XMPMetadata metadata, COSStream stream) {
        if (metadata == null) {
            throw new IllegalArgumentException(
                    "Metadata package can not be null");
        }
        if (stream == null) {
            throw new IllegalArgumentException(
                    "Metadata stream can not be null");
        }
        this.metadata = metadata;
        this.stream = stream;
    }

    @Override
    public void checkMetadataStream(
            MetadataFixerResultImpl.Builder resultBuilder) {
        COSBase filters = this.stream.getFilters();
        if (filters instanceof COSName
                || (filters instanceof COSArray && ((COSArray) filters).size() != 0)) {
            try {
                this.stream.setFilters(null);
                this.stream.setNeedToBeUpdated(true);
                resultBuilder.addFix("Metadata stream unfiltered");
            } catch (IOException e) {
                LOGGER.warn("Problems with unfilter stream.");
                LOGGER.warn(e);
            }
        }
        this.setRequiredDictionaryValue(COSName.METADATA, COSName.TYPE,
                resultBuilder);
        this.setRequiredDictionaryValue(COSName.getPDFName("XML"),
                COSName.SUBTYPE, resultBuilder);
    }

    private void setRequiredDictionaryValue(COSName value, COSName key,
            MetadataFixerResultImpl.Builder resultBuilder) {
        if (!value.equals(this.stream.getDictionaryObject(key))) {
            this.stream.setItem(key, value);
            this.stream.setNeedToBeUpdated(true);
            resultBuilder.addFix(value.getName() + " value of " + key.getName()
                    + " key is set " + "to metadata dictionary.");
        }
    }

    @Override
    public void removePDFIdentificationSchema(
            MetadataFixerResultImpl.Builder resultBuilder, PDFAFlavour flavour) {
        PDFAIdentificationSchema schema = this.metadata
                .getPDFIdentificationSchema();
        if (schema != null && schemaContainSameFlavour(schema, flavour)) {
            this.metadata.removeSchema(schema);
            this.setNeedToBeUpdated(true);
            resultBuilder.addFix("Identification schema removed.");
        }
    }

    private static boolean schemaContainSameFlavour(
            PDFAIdentificationSchema schema, PDFAFlavour flavour) {
        if (!schema.getPart().equals(
                Integer.valueOf(flavour.getPart().getPartNumber()))) {
            return false;
        }
        String schemaConf = schema.getConformance();
        schemaConf = schemaConf != null ? schemaConf.toUpperCase(Locale.US)
                : null;
        String flavourConf = flavour.getLevel().getCode();
        return schemaConf != null && schemaConf.equals(flavourConf);
    }

    @Override
    public void addPDFIdentificationSchema(
            MetadataFixerResultImpl.Builder resultBuilder, PDFAFlavour flavour) {
        PDFAIdentificationSchema schema = this.metadata
                .getPDFIdentificationSchema();
        int part = flavour.getPart().getPartNumber();
        String conformance = flavour.getLevel().getCode();

        if (schema != null) {
            if (schema.getPart().intValue() == part
                    && conformance.equals(schema.getConformance())) {
                return;
            }
            this.metadata.removeSchema(schema);
        }

        schema = this.metadata.createAndAddPFAIdentificationSchema();

        try {
            schema.setPart(Integer.valueOf(part));
            schema.setConformance(conformance);
            this.setNeedToBeUpdated(true);
            resultBuilder.addFix("Identification schema added.");
        } catch (BadFieldValueException e) {
            LOGGER.error(e);
        }
    }

    @Override
    public DublinCore getDublinCoreSchema(InfoDictionary info) {
        DublinCoreSchema schema = this.metadata.getDublinCoreSchema();
        if (schema == null && dublinCoreInfoPresent(info)) {
            schema = this.metadata.createAndAddDublinCoreSchema();
        }
        return schema != null ? new DublinCoreSchemaImpl(schema, this) : null;
    }

    @Override
    public AdobePDF getAdobePDFSchema(InfoDictionary info) {
        AdobePDFSchema schema = this.metadata.getAdobePDFSchema();
        if (schema == null && adobePDFInfoPresent(info)) {
            schema = this.metadata.createAndAddAdobePDFSchema();
        }
        return schema != null ? new AdobePDFSchemaImpl(schema, this) : null;
    }

    @Override
    public XMPBasic getXMPBasicSchema(InfoDictionary info) {
        XMPBasicSchema schema = this.metadata.getXMPBasicSchema();
        if (schema == null && xmpBasicInfoPresent(info)) {
            schema = this.metadata.createAndAddXMPBasicSchema();
        }
        return schema != null ? new XMPBasicSchemaImpl(schema, this) : null;
    }

    @Override
    public boolean isNeedToBeUpdated() {
        return this.stream.isNeedToBeUpdated();
    }

    @Override
    public void setNeedToBeUpdated(boolean needToBeUpdated) {
        this.stream.setNeedToBeUpdated(true);
    }

    @Override
    public void updateMetadataStream() throws IOException, TransformerException {
        if (!this.stream.isNeedToBeUpdated()) {
            return;
        }
        try (OutputStream out = this.stream.createUnfilteredStream();) {
            new XmpSerializer().serialize(this.metadata, out, true);
        }
    }

    private static boolean dublinCoreInfoPresent(InfoDictionary info) {
        return info != null
                && (info.getTitle() != null || info.getSubject() != null || info
                        .getAuthor() != null);
    }

    private static boolean adobePDFInfoPresent(InfoDictionary info) {
        return info != null
                && (info.getProducer() != null || info.getKeywords() != null);
    }

    private static boolean xmpBasicInfoPresent(InfoDictionary info) {
        return info != null
                && (info.getCreator() != null || info.getCreationDate() != null || info
                        .getModificationDate() != null);
    }

}
