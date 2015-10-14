package org.verapdf.metadata.fixer.entity;

import org.verapdf.metadata.fixer.schemas.AdobePDF;
import org.verapdf.metadata.fixer.schemas.DublinCore;
import org.verapdf.metadata.fixer.schemas.XMPBasic;

/**
 * @author Evgeniy Muravitskiy
 */
public interface Metadata {

	void removePDFIdentificationSchema(FixReport report);

	void addPDFIdentificationSchema(FixReport report);

	DublinCore getDublinCoreSchema(InfoDictionary info);

	AdobePDF getAdobePDFSchema(InfoDictionary info);

	XMPBasic getXMPBasicSchema(InfoDictionary info);

	boolean isNeedToBeUpdated();

	void setNeedToBeUpdated(boolean needToBeUpdated);
}
