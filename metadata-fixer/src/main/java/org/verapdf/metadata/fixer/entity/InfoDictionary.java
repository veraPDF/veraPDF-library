package org.verapdf.metadata.fixer.entity;

import org.verapdf.metadata.fixer.MetadataFixerImpl;
import org.verapdf.metadata.fixer.schemas.AdobePDF;
import org.verapdf.metadata.fixer.schemas.DublinCore;
import org.verapdf.metadata.fixer.schemas.XMPBasic;

/**
 * Current interface provide necessary behavior of pdf document
 * information dictionary. {@link MetadataFixerImpl}
 * check next entries:
 * <ul>
 *     <li>Title</li>
 *     <li>Author</li>
 *     <li>Subject</li>
 *     <li>Producer</li>
 *     <li>Keywords</li>
 *     <li>Creator</li>
 *     <li>CreationDate</li>
 *     <li>ModDate</li>
 * </ul>
 * Current properties described by corresponding schemas (such as AdobePDF,
 * DublinCore and XMPBasic).
 * <p>
 *     Values of {@code CreationDate} and {@code ModDate} must be the same
 *     as directly in the document
 * </p>
 *
 * @author Evgeniy Muravitskiy
 */
public interface InfoDictionary extends AdobePDF, DublinCore, XMPBasic {

	boolean isNeedToBeUpdated();

}
