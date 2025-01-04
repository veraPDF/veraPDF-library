/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.metadata.fixer.schemas;

/**
 * Current interface represent XMP Basic schema
 *
 * @author Evgeniy Muravitskiy
 */
public interface XMPBasic extends BasicSchema {

	/**
	 * Return Creator entry. For information dictionary
	 * represented by {@code Creator} entry, in metadata -
	 * {@code CreatorTool} entry
	 *
	 * @return Creator entry
	 */
	String getCreator();

	/**
	 * Set Creator entry. For information dictionary
	 * represented by {@code Creator} entry, in metadata -
	 * {@code CreatorTool} entry
	 *
	 * @param creatorTool new CreatorTool value
	 */
	void setCreator(String creatorTool);

	/**
	 * Return Creation Date entry. For information dictionary
	 * represented by {@code CreationDate} entry, in metadata -
	 * {@code CreationDate} entry
	 *
	 * @return Creation Date entry
	 */
	String getCreationDate();

	/**
	 * Set Creation Date entry. For information dictionary
	 * represented by {@code CreationDate} entry, in metadata -
	 * {@code CreationDate} entry
	 *
	 * @param creationDate new Creation Date value
	 */
	void setCreationDate(String creationDate);

	/**
	 * Return Modification Date entry. For information dictionary
	 * represented by {@code ModDate} entry, in metadata -
	 * {@code ModifyDate} entry
	 *
	 * @return Modification Date entry
	 */
	String getModificationDate();

	/**
	 * Set Modification Date entry. For information dictionary
	 * represented by {@code ModDate} entry, in metadata -
	 * {@code ModifyDate} entry
	 *
	 * @param modificationDate new Modification Date value
	 */
	void setModificationDate(String modificationDate);
}
