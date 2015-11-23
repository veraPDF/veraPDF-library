package org.verapdf.features.pb.tools;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Set;

/**
 * Helps in creating similar nodes in different features objects
 *
 * @author Maksim Bezrukov
 */
public final class PBCreateNodeHelper {

	private static final Logger LOGGER = Logger
			.getLogger(PBCreateNodeHelper.class);

	private static final String LLX = "llx";
	private static final String LLY = "lly";
	private static final String URX = "urx";
	private static final String URY = "ury";

	private static final int GRAY_COLOR_COMPONENTS_NUMBER = 1;
	private static final int RGB_COLOR_COMPONENTS_NUMBER = 3;
	private static final int CMYK_COLOR_COMPONENTS_NUMBER = 4;

	private static final int GRAY_COMPONENT_NUMBER = 0;
	private static final int RED_COMPONENT_NUMBER = 0;
	private static final int GREEN_COMPONENT_NUMBER = 1;
	private static final int BLUE_COMPONENT_NUMBER = 2;
	private static final int CYAN_COMPONENT_NUMBER = 0;
	private static final int MAGENTA_COMPONENT_NUMBER = 1;
	private static final int YELLOW_COMPONENT_NUMBER = 2;
	private static final int BLACK_COMPONENT_NUMBER = 3;

	private PBCreateNodeHelper() {
	}

	private static String getXMLFormat(Calendar calendar) throws DatatypeConfigurationException {
		GregorianCalendar greg = new GregorianCalendar(Locale.US);
		greg.setTime(calendar.getTime());
		greg.setTimeZone(calendar.getTimeZone());
		XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(greg);
		return xmlCalendar.toXMLFormat();
	}

	/**
	 * Creates node with date value formatted in XML format from the given Calendar
	 *
	 * @param nodeName   name of the created node
	 * @param parent     parent element for created node
	 * @param date       the given date as Calendar class
	 * @param collection collection for which this node creates
	 * @return created node
	 * @throws FeatureParsingException
	 */
	public static FeatureTreeNode createDateNode(String nodeName, FeatureTreeNode parent, Calendar date, FeaturesCollection collection) throws FeatureParsingException {
		FeatureTreeNode modificationDate = null;

		if (date != null) {
			modificationDate = FeatureTreeNode.createChildNode(nodeName, parent);
			try {
				modificationDate.setValue(getXMLFormat(date));
			} catch (DatatypeConfigurationException e) {
				LOGGER.debug("DatatypeFactory implementation not available or can't be instantiated", e);
				ErrorsHelper.addErrorIntoCollection(collection,
						modificationDate,
						e.getMessage());
			}
		}

		return modificationDate;
	}

	/**
	 * Gets String value from COSBase class
	 *
	 * @param baseParam COSBase object
	 * @return String value of a COSString object if the direct object that will get from the given COSBase is COSString and null in all other cases
	 */
	public static String getStringFromBase(COSBase baseParam) {

		COSBase base = baseParam;

		while (base instanceof COSObject) {
			base = ((COSObject) base).getObject();
		}

		if (base instanceof COSString) {
			COSString str = (COSString) base;
			return str.isHex() ? str.toHexString() : str.getString();
		}
		return null;
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
	public static FeatureTreeNode addBoxFeature(String name, PDRectangle box, FeatureTreeNode parent) throws FeatureParsingException {
		FeatureTreeNode boxNode = null;

		if (box != null) {
			boxNode = FeatureTreeNode.createChildNode(name, parent);
			boxNode.setAttribute(LLX, String.valueOf(box.getLowerLeftX()));
			boxNode.setAttribute(LLY, String.valueOf(box.getLowerLeftY()));
			boxNode.setAttribute(URX, String.valueOf(box.getUpperRightX()));
			boxNode.setAttribute(URY, String.valueOf(box.getUpperRightY()));
		}

		return boxNode;
	}

	/**
	 * Creates new node with given name and value if both of this parametrs are not null
	 *
	 * @param name   name of the node
	 * @param value  value of the node
	 * @param parent parent of the node
	 * @return generated node
	 * @throws FeatureParsingException
	 */
	public static FeatureTreeNode addNotEmptyNode(String name, String value, FeatureTreeNode parent) throws FeatureParsingException {
		if (name != null && value != null) {
			FeatureTreeNode node = FeatureTreeNode.createChildNode(name, parent);
			node.setValue(value);
			return node;
		}
		return null;
	}

	/**
	 * Creates new node for device color space
	 *
	 * @param name       name for the created node
	 * @param color      PDColor class represents device color space for creating node
	 * @param parent     parent node for the creating node
	 * @param collection features collection in which parent situated
	 * @return created node
	 * @throws FeatureParsingException
	 */
	public static FeatureTreeNode addDeviceColorSpaceNode(String name, PDColor color, FeatureTreeNode parent, FeaturesCollection collection) throws FeatureParsingException {
		if (name != null && color != null) {
			FeatureTreeNode colorNode = FeatureTreeNode.createChildNode(name, parent);

			float[] numbers = color.getComponents();

			switch (numbers.length) {
				case GRAY_COLOR_COMPONENTS_NUMBER:
					createGray(color.getComponents(), colorNode);
					break;
				case RGB_COLOR_COMPONENTS_NUMBER:
					createRGB(color.getComponents(), colorNode);
					break;
				case CMYK_COLOR_COMPONENTS_NUMBER:
					createCMYK(color.getComponents(), colorNode);
					break;
				default:
					ErrorsHelper.addErrorIntoCollection(collection,
							colorNode,
							"Can not define color type");
			}

			return colorNode;
		}
		return null;
	}

	/**
	 * Creates elements with name {@code elementName} and attribute id with values from {@code set} and attach them
	 * to the {@code root} element in case, when {@code setName} is null and to the element with {@code root} parent and
	 * name {@code elementName} in other case
	 *
	 * @param set         set of elements id
	 * @param elementName element names
	 * @param setName     name of the parent element for created elements. If null, all created elements will be attached to the {@code root}
	 * @param root        root element for the generated parent element for generated elements or direct paren for generated elements in case of {@code setName} equals to null
	 * @throws FeatureParsingException
	 */
	public static void parseIDSet(Set<String> set, String elementName, String setName, FeatureTreeNode root) throws FeatureParsingException {
		if (set != null && !set.isEmpty()) {
			FeatureTreeNode setNode;
			if (setName == null) {
				setNode = root;
			} else {
				setNode = FeatureTreeNode.createChildNode(setName, root);
			}
			for (String entry : set) {
				if (entry != null) {
					FeatureTreeNode entryNode = FeatureTreeNode.createChildNode(elementName, setNode);
					entryNode.setAttribute("id", entry);
				}
			}
		}
	}

	/**
	 * Generates byte array with contents of a stream
	 *
	 * @param is input stream for converting
	 * @return byte array with contents of a stream
	 * @throws IOException If the first byte cannot be read for any reason
	 *                     other than end of file, or if the input stream has been closed, or if
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

	/**
	 * Creates FeatureTreeNode with name {@code nodeName}, parent {@code parent}, and content which is a stream r
	 * epresentation of the {@code metadata} content. If there is an exception during getting metadata, then it
	 * will create node with errorID and error for this situation.
	 *
	 * @param metadata   PDMetadata class from which metadata will be taken
	 * @param nodeName   name for the created node
	 * @param collection collection for the created node
	 * @return created node
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	public static FeatureTreeNode parseMetadata(PDMetadata metadata, String nodeName, FeatureTreeNode parent, FeaturesCollection collection) throws FeatureParsingException {
		if (metadata == null) {
			return null;
		}
		FeatureTreeNode node;
		node = FeatureTreeNode.createChildMetadataNode(nodeName, parent);
		try {
			byte[] bStream = metadata.getByteArray();
			if (bStream != null) {
				String hexString = DatatypeConverter.printHexBinary(bStream);
				node.setValue(hexString);
			}
		} catch (IOException e) {
			LOGGER.debug("Error while obtaining unfiltered metadata stream", e);
			ErrorsHelper.addErrorIntoCollection(collection,
					node,
					e.getMessage());
		}

		return node;
	}

	private static void createGray(float[] components, FeatureTreeNode parent) {
		parent.setAttribute("gray", String.valueOf(components[GRAY_COMPONENT_NUMBER]));
	}

	private static void createRGB(float[] components, FeatureTreeNode parent) {
		parent.setAttribute("red", String.valueOf(components[RED_COMPONENT_NUMBER]));
		parent.setAttribute("green", String.valueOf(components[GREEN_COMPONENT_NUMBER]));
		parent.setAttribute("blue", String.valueOf(components[BLUE_COMPONENT_NUMBER]));
	}

	private static void createCMYK(float[] components, FeatureTreeNode parent) {
		parent.setAttribute("cyan", String.valueOf(components[CYAN_COMPONENT_NUMBER]));
		parent.setAttribute("magenta", String.valueOf(components[MAGENTA_COMPONENT_NUMBER]));
		parent.setAttribute("yellow", String.valueOf(components[YELLOW_COMPONENT_NUMBER]));
		parent.setAttribute("black", String.valueOf(components[BLACK_COMPONENT_NUMBER]));
	}
}
