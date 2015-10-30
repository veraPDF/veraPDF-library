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
import org.verapdf.metadata.fixer.MetadataFixerResultImpl;
import org.verapdf.metadata.fixer.entity.InfoDictionary;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.impl.pb.schemas.AdobePDFSchemaImpl;
import org.verapdf.metadata.fixer.impl.pb.schemas.DublinCoreSchemaImpl;
import org.verapdf.metadata.fixer.impl.pb.schemas.XMPBasicSchemaImpl;
import org.verapdf.metadata.fixer.schemas.AdobePDF;
import org.verapdf.metadata.fixer.schemas.DublinCore;
import org.verapdf.metadata.fixer.schemas.XMPBasic;
import org.verapdf.metadata.fixer.utils.flavour.PDFAFlavour;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Evgeniy Muravitskiy
 */
public class MetadataImpl implements Metadata {

	private static final Logger LOGGER = Logger.getLogger(MetadataImpl.class);

	private final XMPMetadata metadata;
	private final COSStream stream;

	public MetadataImpl(XMPMetadata metadata, COSStream stream) {
		if (metadata == null) {
			throw new IllegalArgumentException("Metadata package can not be null");
		}
		if (stream == null) {
			throw new IllegalArgumentException("Metadata stream can not be null");
		}
		this.metadata = metadata;
		this.stream = stream;
	}

	@Override
	public void checkMetadataStream(MetadataFixerResultImpl report) {
		COSBase filters = this.stream.getFilters();
		if (filters instanceof COSName ||
				(filters instanceof COSArray && ((COSArray) filters).size() != 0)) {
			try {
				this.stream.setFilters(null);
				this.stream.setNeedToBeUpdated(true);
				report.addAppliedFix("Metadata stream unfiltered");
			} catch (IOException e) {
				LOGGER.warn("Problems with unfilter stream.");
				LOGGER.warn(e);
			}
		}
		this.setRequiredDictionaryValue(COSName.METADATA, COSName.TYPE, report);
		this.setRequiredDictionaryValue(COSName.getPDFName("XML"), COSName.SUBTYPE, report);
	}

	private void setRequiredDictionaryValue(COSName value, COSName key, MetadataFixerResultImpl report) {
		if (!value.equals(this.stream.getDictionaryObject(key))) {
			this.stream.setItem(key, value);
			this.stream.setNeedToBeUpdated(true);
			report.addAppliedFix(value.getName() + " value of " + key.getName() + " key is set " +
					"to metadata dictionary.");
		}
	}

	@Override
	public void removePDFIdentificationSchema(MetadataFixerResultImpl result) {
		PDFAIdentificationSchema schema = this.metadata.getPDFIdentificationSchema();
		if (schema != null) {
			this.metadata.removeSchema(schema);
			this.setNeedToBeUpdated(true);
			result.addAppliedFix("Identification schema removed.");
		}
	}

	@Override
	public void addPDFIdentificationSchema(MetadataFixerResultImpl report, PDFAFlavour flavour) {
		PDFAIdentificationSchema schema = this.metadata.getPDFIdentificationSchema();
		int part = flavour.getPart().getPartNumber();
		String conformance = flavour.getLevel().getCode();

		if (schema != null) {
			if (schema.getPart() == part && conformance.equals(schema.getConformance())) {
				return;
			} else {
				this.metadata.removeSchema(schema);
			}
		}

		schema = this.metadata.createAndAddPFAIdentificationSchema();

		try {
			schema.setPart(part);
			schema.setConformance(conformance);
			this.setNeedToBeUpdated(true);
			report.addAppliedFix("Identification schema added.");
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

	public void updateMetadataStream() throws Exception {
		if (this.stream.isNeedToBeUpdated()) {
			OutputStream out = this.stream.createUnfilteredStream();
			new XmpSerializer().serialize(this.metadata, out, true);
		}
	}

	private boolean dublinCoreInfoPresent(InfoDictionary info) {
		return info != null && (info.getTitle() != null || info.getSubject() != null ||
				info.getAuthor() != null);
	}

	private boolean adobePDFInfoPresent(InfoDictionary info) {
		return info != null && (info.getProducer() != null || info.getKeywords() != null);
	}

	private boolean xmpBasicInfoPresent(InfoDictionary info) {
		return info != null && (info.getCreator() != null || info.getCreationDate() != null ||
				info.getModificationDate() != null);
	}

}
