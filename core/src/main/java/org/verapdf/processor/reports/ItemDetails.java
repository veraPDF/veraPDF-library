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
/**
 * 
 */
package org.verapdf.processor.reports;

import java.io.File;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Encapsulates the details of a PDF document, just the name and the size in
 * bytes.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@XmlRootElement(name = "item")
public class ItemDetails {
	static final ItemDetails DEFAULT = new ItemDetails();
	@XmlElement
	private final String name;
	@XmlAttribute
	private final long size;

	private ItemDetails() {
		this("unknown");
	}

	private ItemDetails(final String name) {
		this(name, -1);
	}

	private ItemDetails(final String name, final long size) {
		this.name = name;
		this.size = size;
	}

	/**
	 * @return the item name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the size of the item in bytes
	 */
	public long getSize() {
		return this.size;
	}

	/**
	 * @param file
	 *            the {@link File} to extract details from
	 * @return a new {@code ItemDetails} instance initialised from the passed
	 *         file.
	 */
	public static ItemDetails fromFile(final File file) {
		return fromValues(file.getAbsolutePath(), file.length());
	}

	/**
	 * @param name
	 *            a name to identify the item
	 * @return a new {@code ItemDetails} instance with the given name and -1
	 *         (unknown) as the size in bytes;
	 */
	public static ItemDetails fromValues(final String name) {
		return fromValues(name, -1);
	}

	/**
	 * @param name
	 *            a name to identify the item
	 * @param size
	 *            the size of the item in bytes.
	 * @return a new {@code ItemDetails} instance initialised from the passed
	 *         parameters
	 */
	public static ItemDetails fromValues(final String name, final long size) {
		if (name == null)
			throw new NullPointerException("Parameter name can not be null.");
		return new ItemDetails(name, size);
	}

	/**
	 * @return the default ItemDetails instance
	 */
	public static ItemDetails defaultInstance() {
		return DEFAULT;
	}
}
