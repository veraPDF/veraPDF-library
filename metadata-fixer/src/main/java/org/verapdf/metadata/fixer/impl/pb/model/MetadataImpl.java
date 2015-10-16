package org.verapdf.metadata.fixer.impl.pb.model;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSStream;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.AdobePDFSchema;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.schema.XMPBasicSchema;
import org.apache.xmpbox.type.BadFieldValueException;
import org.verapdf.metadata.fixer.MetadataFixerResult;
import org.verapdf.metadata.fixer.entity.InfoDictionary;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.impl.pb.schemas.AdobePDFSchemaImpl;
import org.verapdf.metadata.fixer.impl.pb.schemas.DublinCoreSchemaImpl;
import org.verapdf.metadata.fixer.impl.pb.schemas.XMPBasicSchemaImpl;
import org.verapdf.metadata.fixer.schemas.AdobePDF;
import org.verapdf.metadata.fixer.schemas.DublinCore;
import org.verapdf.metadata.fixer.schemas.XMPBasic;
import org.verapdf.metadata.fixer.utils.PDFAFlavour;

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
	public void removePDFIdentificationSchema(MetadataFixerResult result) {
		PDFAIdentificationSchema schema = this.metadata.getPDFIdentificationSchema();
		if (schema != null) {
			this.metadata.removeSchema(schema);
			this.setNeedToBeUpdated(true);
			result.addAppliedFix("Identification schema removed.");
		}
	}

	@Override
	public void addPDFIdentificationSchema(MetadataFixerResult report, PDFAFlavour flavour) {
		PDFAIdentificationSchema schema = this.metadata.getPDFIdentificationSchema();
		if (schema == null) {
			schema = this.metadata.createAndAddPFAIdentificationSchema();
		}
		try {
			schema.setPart(flavour.getPart().getPartNumber());
			schema.setConformance(flavour.getLevel().getCode());
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
		return new DublinCoreSchemaImpl(schema, this);
	}

	@Override
	public AdobePDF getAdobePDFSchema(InfoDictionary info) {
		AdobePDFSchema schema = this.metadata.getAdobePDFSchema();
		if (schema == null && adobePDFInfoPresent(info)) {
			schema = this.metadata.createAndAddAdobePDFSchema();
		}
		return new AdobePDFSchemaImpl(schema, this);
	}

	@Override
	public XMPBasic getXMPBasicSchema(InfoDictionary info) {
		XMPBasicSchema schema = this.metadata.getXMPBasicSchema();
		if (schema == null && xmpBasicInfoPresent(info)) {
			schema = this.metadata.createAndAddXMPBasicSchema();
		}
		return new XMPBasicSchemaImpl(schema, this);
	}

	@Override
	public boolean isNeedToBeUpdated() {
		return this.stream.isNeedToBeUpdated();
	}

	@Override
	public void setNeedToBeUpdated(boolean needToBeUpdated) {
		this.stream.setNeedToBeUpdated(true);
	}

	private boolean dublinCoreInfoPresent(InfoDictionary info) {
		return info.getTitle() != null || info.getSubject() != null || info.getAuthor() != null;
	}

	private boolean adobePDFInfoPresent(InfoDictionary info) {
		return info.getProducer() != null && info.getKeywords() != null;
	}

	private boolean xmpBasicInfoPresent(InfoDictionary info) {
		return info.getCreator() != null || info.getCreationDate() != null ||
				info.getModificationDate() != null;
	}

	public XMPMetadata getAbsorbedMetadata() {
		return this.metadata;
	}

}
