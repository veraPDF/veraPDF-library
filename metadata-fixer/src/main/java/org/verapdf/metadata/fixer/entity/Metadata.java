package org.verapdf.metadata.fixer.entity;

import org.verapdf.metadata.fixer.MetadataFixerResult;
import org.verapdf.metadata.fixer.schemas.AdobePDF;
import org.verapdf.metadata.fixer.schemas.DublinCore;
import org.verapdf.metadata.fixer.schemas.XMPBasic;
import org.verapdf.metadata.fixer.utils.PDFAFlavour;

/**
 * @author Evgeniy Muravitskiy
 */
public interface Metadata {

	void removePDFIdentificationSchema(MetadataFixerResult result);

	void addPDFIdentificationSchema(MetadataFixerResult report, PDFAFlavour flavour);

	DublinCore getDublinCoreSchema(InfoDictionary info);

	AdobePDF getAdobePDFSchema(InfoDictionary info);

	XMPBasic getXMPBasicSchema(InfoDictionary info);

	boolean isNeedToBeUpdated();

	void setNeedToBeUpdated(boolean needToBeUpdated);
}
