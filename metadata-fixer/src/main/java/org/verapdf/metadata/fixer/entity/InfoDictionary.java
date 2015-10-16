package org.verapdf.metadata.fixer.entity;

import org.verapdf.metadata.fixer.schemas.AdobePDF;
import org.verapdf.metadata.fixer.schemas.DublinCore;
import org.verapdf.metadata.fixer.schemas.XMPBasic;

/**
 * @author Evgeniy Muravitskiy
 */
public interface InfoDictionary extends AdobePDF, DublinCore, XMPBasic {

	boolean isNeedToBeUpdated();

}
