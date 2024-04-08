/**
 * This file is part of feature-reporting, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 * <p>
 * feature-reporting is free software: you can redistribute it and/or modify
 * it under the terms of either:
 * <p>
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with feature-reporting as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 * <p>
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * feature-reporting as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.features.tools;

import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.objects.FeaturesObject;

import jakarta.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helps in creating similar nodes in different features objects
 *
 * @author Maksim Bezrukov
 */
public final class CreateNodeHelper {

	private static final Logger LOGGER = Logger.getLogger(CreateNodeHelper.class.getCanonicalName());

	public static final String ID = "id";
	private static final String LLX = "llx";
	private static final String LLY = "lly";
	private static final String URX = "urx";
	private static final String URY = "ury";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";

	private static final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

	static {
		numberFormat.setGroupingUsed(false);
	}

	private CreateNodeHelper() {
	}

	private static String getXMLFormat(Calendar calendar) throws DatatypeConfigurationException {
		GregorianCalendar greg = new GregorianCalendar(Locale.US);
		greg.setTime(calendar.getTime());
		greg.setTimeZone(calendar.getTimeZone());
		XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(greg);
		return xmlCalendar.toXMLFormat();
	}

	/**
	 * Creates node with date value formatted in XML format from the given
	 * Calendar
	 *
	 * @param nodeName name of the created node
	 * @param parent   parent element for created node
	 * @param date     the given date as Calendar class
	 * @return created node
	 * @throws FeatureParsingException
	 */
	public static FeatureTreeNode createDateNode(String nodeName, FeatureTreeNode parent, Calendar date,
												 FeaturesObject object) throws FeatureParsingException {
		FeatureTreeNode modificationDate = null;

		if (date != null) {
			modificationDate = parent.addChild(nodeName);
			try {
				modificationDate.setValue(getXMLFormat(date));
			} catch (DatatypeConfigurationException e) {
				LOGGER.log(Level.FINE, "DatatypeFactory implementation not available or can't be instantiated", e);
				object.registerNewError(e.getMessage());
			}
		}

		return modificationDate;
	}

	/**
	 * Creates feature node for boxes
	 *
	 * @param name   name of the node
	 * @param box    PDRectangle object represents the box
	 * @param parent parent element for the created node
	 * @return created node
	 * @throws FeatureParsingException
	 */
	public static FeatureTreeNode addBoxFeature(String name, double[] box, FeatureTreeNode parent)
			throws FeatureParsingException {
		FeatureTreeNode boxNode = null;

		if (box != null && box.length >= 4) {
			boxNode = parent.addChild(name);
			boxNode.setAttribute(LLX, formatDouble(box[0], 3));
			boxNode.setAttribute(LLY, formatDouble(box[1], 3));
			boxNode.setAttribute(URX, formatDouble(box[2], 3));
			boxNode.setAttribute(URY, formatDouble(box[3], 3));
		}

		return boxNode;
	}

	public static void addWidthHeightFeatures(double[] box, FeatureTreeNode parent)
			throws FeatureParsingException {
		if (box != null && box.length >= 4) {
			parent.addChild(WIDTH).setValue(formatDouble(box[2] - box[0], 3));
			parent.addChild(HEIGHT).setValue(formatDouble(box[3] - box[1],3));
		}
	}

	/**
	 * Creates new node with given name and value if both of this parametrs are
	 * not null
	 *
	 * @param name   name of the node
	 * @param value  value of the node
	 * @param parent parent of the node
	 * @return generated node
	 * @throws FeatureParsingException
	 */
	public static FeatureTreeNode addNotEmptyNode(String name, String value, FeatureTreeNode parent)
			throws FeatureParsingException {
		if (name != null && value != null) {
			FeatureTreeNode node = parent.addChild(name);
			node.setValue(value);
			return node;
		}
		return null;
	}

	/**
	 * Creates new node for device color space
	 *
	 * @param name   name for the created node
	 * @param color  PDColor class represents device color space for creating node
	 * @param parent parent node for the creating node
	 * @param object features object which calls this method
	 * @return created node
	 * @throws FeatureParsingException
	 */
	public static FeatureTreeNode addDeviceColorSpaceNode(String name, double[] color, FeatureTreeNode parent,
														  FeaturesObject object) throws FeatureParsingException {
		if (name == null || color == null) {
			return null;
		}
		FeatureTreeNode colorNode = parent.addChild(name);
		boolean typeDefined = false;

		for (ColorComponent component : ColorComponent.values()) {
			if (component.getSize() == color.length) {
				typeDefined = true;
				colorNode.setAttributes(component.createAttributesMap(color));
			}
		}

		if (!typeDefined) {
			object.registerNewError("Can not define color type");
		}

		return colorNode;
	}

	/**
	 * Creates elements with name {@code elementName} and attribute id with
	 * values from {@code set} and attach them to the {@code root} element in
	 * case, when {@code setName} is null and to the element with {@code root}
	 * parent and name {@code elementName} in other case
	 *
	 * @param set         set of elements id
	 * @param elementName element names
	 * @param setName     name of the parent element for created elements. If null, all
	 *                    created elements will be attached to the {@code root}
	 * @param root        root element for the generated parent element for generated
	 *                    elements or direct paren for generated elements in case of
	 *                    {@code setName} equals to null
	 * @throws FeatureParsingException
	 */
	public static void parseIDSet(Set<String> set, String elementName, String setName, FeatureTreeNode root)
			throws FeatureParsingException {
		if (set != null && !set.isEmpty()) {
			FeatureTreeNode setNode;
			if (setName == null) {
				setNode = root;
			} else {
				setNode = root.addChild(setName);
			}
			for (String entry : set) {
				if (entry != null) {
					FeatureTreeNode entryNode = setNode.addChild(elementName);
					entryNode.setAttribute(ID, entry);
				}
			}
		}
	}

	public static FeatureTreeNode parseMetadata(InputStream metadata, String nodeName, FeatureTreeNode parent,
												FeaturesObject object) throws FeatureParsingException {
		if (metadata == null) {
			return null;
		}
		FeatureTreeNode node = parent.addMetadataChild(nodeName);
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			while (true) {
				int r = metadata.read(buffer);
				if (r == -1) break;
				out.write(buffer, 0, r);
			}
			byte[] bStream = out.toByteArray();
			String hexString = DatatypeConverter.printHexBinary(bStream);
			node.setValue(hexString);
		} catch (IOException e) {
			LOGGER.log(Level.FINE, "Error while obtaining unfiltered metadata stream", e);
			object.registerNewError(e.getMessage());
		}

		return node;
	}

	/**
	 * Generates byte array with contents of a stream
	 *
	 * @param is input stream for converting
	 * @return byte array with contents of a stream
	 * @throws IOException If the first byte cannot be read for any reason other than
	 *                     end of file, or if the input stream has been closed, or if
	 *                     some other I/O error occurs.
	 */
	public static byte[] inputStreamToByteArray(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] bytes = new byte[1024];
		int length;
		while ((length = is.read(bytes)) != -1) {
			baos.write(bytes, 0, length);
		}
		return baos.toByteArray();
	}

	public static void parseMatrix(double[] array, FeatureTreeNode parent) throws FeatureParsingException {
		if (array != null) {
			for (int i = 0; i < array.length; ++i) {
				FeatureTreeNode element = parent.addChild("element");
				element.setAttribute("index", String.valueOf(i + 1));
				element.setAttribute("value", formatDouble(array[i], 3));
			}
		}
	}

	public static String formatDouble(double value, int fractionDigits) {
		numberFormat.setMinimumFractionDigits(fractionDigits);
		numberFormat.setMaximumFractionDigits(fractionDigits);
		return numberFormat.format(value);
	}
}
