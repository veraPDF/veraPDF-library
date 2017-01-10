/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.metadata.fixer.entity;

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
