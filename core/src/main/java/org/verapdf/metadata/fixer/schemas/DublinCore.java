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
 * Current interface represent Dublin Core schema
 *
 * @author Evgeniy Muravitskiy
 */
public interface DublinCore extends BasicSchema {

	/**
	 * Return Title entry. For information dictionary
	 * represented by {@code Title} entry, in metadata -
	 * {@code title} entry
	 *
	 * @return Title entry
	 */
	String getTitle();

	/**
	 * Set Title entry. For information dictionary
	 * represented by {@code Title} entry, in metadata -
	 * {@code title} entry
	 *
	 * @param title new Title value
	 */
	void setTitle(String title);

	/**
	 * Return Subject entry. For information dictionary
	 * represented by {@code Subject} entry, in metadata -
	 * {@code description} entry
	 *
	 * @return Subject entry
	 */
	String getSubject();

	/**
	 * Set Subject entry. For information dictionary
	 * represented by {@code Subject} entry, in metadata -
	 * {@code description} entry
	 *
	 * @param description new Subject value
	 */
	void setSubject(String description);

	/**
	 * Return Author entry. For information dictionary
	 * represented by {@code Author} entry, in metadata -
	 * {@code creator} entry
	 *
	 * @return Author entry
	 */
	String getAuthor();

	int getAuthorSize();

	/**
	 * Set Author entry. For information dictionary
	 * represented by {@code Author} entry, in metadata -
	 * {@code creator} entry
	 *
	 * @param creator new Author value
	 */
	void setAuthor(String creator);

}
