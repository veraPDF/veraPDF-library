package org.verapdf.metadata.fixer.impl.pb.model;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.xml.DomXmpParser;
import org.apache.xmpbox.xml.XmpParsingException;
import org.apache.xmpbox.xml.XmpSerializer;
import org.verapdf.metadata.fixer.MetadataFixerResult;
import org.verapdf.metadata.fixer.entity.InfoDictionary;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.entity.PDFDocument;

import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Evgeniy Muravitskiy
 */
public class PDFDocumentImpl implements PDFDocument {

	private static final Logger LOGGER = Logger.getLogger(PDFDocumentImpl.class);

	private final PDDocument document;
	private MetadataImpl metadata;
	private InfoDictionaryImpl info;

	public PDFDocumentImpl(PDDocument document) {
		if (document == null) {
			throw new IllegalArgumentException("Document representation can not be null");
		}
		this.document = document;
		this.metadata = parseMetadata();
	}

	@Override
	public Metadata getMetadata() {
		return this.metadata;
	}

	private MetadataImpl parseMetadata() {
		PDMetadata meta = this.document.getDocumentCatalog().getMetadata();
		if (meta == null) {
			// TODO : do we need to check count of predefined fields in info dictionary?
			COSStream stream = this.document.getDocument().createCOSStream();
			this.document.getDocumentCatalog().setMetadata(new PDMetadata(stream));
			XMPMetadata xmp = XMPMetadata.createXMPMetadata();
			return new MetadataImpl(xmp, stream);
		} else {
			return parseMetadata(meta);
		}
	}

	private MetadataImpl parseMetadata(PDMetadata meta) {
		try {
			DomXmpParser xmpParser = new DomXmpParser();
			XMPMetadata xmp = xmpParser.parse(meta.getStream().getUnfilteredStream());
			if (xmp != null) {
				return new MetadataImpl(xmp, meta.getStream());
			}
		} catch (IOException e) {
			LOGGER.error(
					"Problems with document parsing or structure. "
							+ e.getMessage(), e);
		} catch (XmpParsingException e) {
			LOGGER.error("Problems with XMP parsing. " + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public InfoDictionary getInfoDictionary() {
		if (this.info == null) {
			this.info = new InfoDictionaryImpl(this.document.getDocumentInformation());
		}
		return this.info;
	}

	@Override
	public boolean isNeedToBeUpdated() {
		PDMetadata meta = this.document.getDocumentCatalog().getMetadata();
		COSDictionary info = this.document.getDocumentInformation().getCOSObject();
		return meta != null && (meta.getStream().isNeedToBeUpdated() || info.isNeedToBeUpdated());
	}

	@Override
	public void saveDocumentIncremental(MetadataFixerResult result, OutputStream output) throws IOException {
		PDMetadata meta = this.document.getDocumentCatalog().getMetadata();
		if (meta != null) {
			checkFilters(meta);
			updateMetadataStatus(meta);
			if (isNeedToBeUpdated()) {
				this.document.saveIncremental(output);
				output.close();
				result.setStatus(MetadataFixerResult.RepairStatus.SUCCESSFUL);
			} else {
				result.setStatus(MetadataFixerResult.RepairStatus.NO_ACTION);
			}
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

	private void updateMetadataStatus(PDMetadata meta) throws IOException {
		if (meta.getStream().isNeedToBeUpdated()) {
			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				new XmpSerializer().serialize(this.metadata.getAbsorbedMetadata(), out, true);
				meta.importXMPMetadata(out.toByteArray());
			} catch (TransformerException e) {
				LOGGER.error("Problems with xmp serializing");
			}
		}
	}

}
