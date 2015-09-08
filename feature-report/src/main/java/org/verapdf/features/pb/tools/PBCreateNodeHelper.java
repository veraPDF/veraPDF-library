package org.verapdf.features.pb.tools;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
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
	 * @throws FeaturesTreeNodeException
	 */
	public static FeatureTreeNode createDateNode(String nodeName, FeatureTreeNode parent, Calendar date, FeaturesCollection collection) throws FeaturesTreeNodeException {
		FeatureTreeNode modificationDate = null;

		if (date != null) {
			modificationDate = FeatureTreeNode.newChildInstance(nodeName, parent);
			try {
				modificationDate.setValue(getXMLFormat(date));
			} catch (DatatypeConfigurationException e) {
				LOGGER.debug("DatatypeFactory implementation not available or can't be instantiated", e);
				modificationDate.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.DATE_ID);
				ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.DATE_ID, ErrorsHelper.DATE_MESSAGE);
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
			return str.isHex().booleanValue() ? str.toHexString() : str.getString();
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
	 * @throws FeaturesTreeNodeException
	 */
	public static FeatureTreeNode addBoxFeature(String name, PDRectangle box, FeatureTreeNode parent) throws FeaturesTreeNodeException {
		FeatureTreeNode boxNode = null;

		if (box != null) {
			boxNode = FeatureTreeNode.newChildInstance(name, parent);
			boxNode.addAttribute(LLX, String.valueOf(box.getLowerLeftX()));
			boxNode.addAttribute(LLY, String.valueOf(box.getLowerLeftY()));
			boxNode.addAttribute(URX, String.valueOf(box.getUpperRightX()));
			boxNode.addAttribute(URY, String.valueOf(box.getUpperRightY()));
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
	 * @throws FeaturesTreeNodeException
	 */
	public static FeatureTreeNode addNotEmptyNode(String name, String value, FeatureTreeNode parent) throws FeaturesTreeNodeException {
		if (name != null && value != null) {
			return FeatureTreeNode.newChildInstanceWithValue(name, value, parent);
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
	 * @throws FeaturesTreeNodeException
	 */
	public static FeatureTreeNode addDeviceColorSpaceNode(String name, PDColor color, FeatureTreeNode parent, FeaturesCollection collection) throws FeaturesTreeNodeException {
		if (name != null && color != null) {
			FeatureTreeNode colorNode = FeatureTreeNode.newChildInstance(name, parent);

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
					colorNode.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.COLOR_ID);
					ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.COLOR_ID, ErrorsHelper.COLOR_MESSAGE);
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
	 * @throws FeaturesTreeNodeException
	 */
	public static void parseIDSet(Set<String> set, String elementName, String setName, FeatureTreeNode root) throws FeaturesTreeNodeException {
		if (set != null && !set.isEmpty()) {
			FeatureTreeNode setNode;
			if (setName == null) {
				setNode = root;
			} else {
				setNode = FeatureTreeNode.newChildInstance(setName, root);
			}
			for (String entry : set) {
				if (entry != null) {
					FeatureTreeNode entryNode = FeatureTreeNode.newChildInstance(elementName, setNode);
					entryNode.addAttribute("id", entry);
				}
			}
		}
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
	 * @throws FeaturesTreeNodeException occurs when wrong features tree node constructs
	 */
	public static FeatureTreeNode parseMetadata(PDMetadata metadata, String nodeName, FeatureTreeNode parent, FeaturesCollection collection) throws FeaturesTreeNodeException {
		if (metadata == null) {
			return null;
		}

		FeatureTreeNode node;
		if (parent == null) {
			node = FeatureTreeNode.newRootInstance(nodeName);
		} else {
			node = FeatureTreeNode.newChildInstance(nodeName, parent);
		}
		try {
			byte[] bStream = metadata.getByteArray();
			node.setValue(bStream);
		} catch (IOException e) {
			LOGGER.debug("Error while converting stream to string", e);
			node.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.METADATACONVERT_ID);
			ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.METADATACONVERT_ID, ErrorsHelper.METADATACONVERT_MESSAGE);
		}

		return node;
	}

	private static void createGray(float[] components, FeatureTreeNode parent) throws FeaturesTreeNodeException {
		FeatureTreeNode.newChildInstanceWithValue("gray", String.valueOf(components[GRAY_COMPONENT_NUMBER]), parent);
	}

	private static void createRGB(float[] components, FeatureTreeNode parent) throws FeaturesTreeNodeException {
		FeatureTreeNode.newChildInstanceWithValue("red", String.valueOf(components[RED_COMPONENT_NUMBER]), parent);
		FeatureTreeNode.newChildInstanceWithValue("green", String.valueOf(components[GREEN_COMPONENT_NUMBER]), parent);
		FeatureTreeNode.newChildInstanceWithValue("blue", String.valueOf(components[BLUE_COMPONENT_NUMBER]), parent);
	}

	private static void createCMYK(float[] components, FeatureTreeNode parent) throws FeaturesTreeNodeException {
		FeatureTreeNode.newChildInstanceWithValue("cyan", String.valueOf(components[CYAN_COMPONENT_NUMBER]), parent);
		FeatureTreeNode.newChildInstanceWithValue("magenta", String.valueOf(components[MAGENTA_COMPONENT_NUMBER]), parent);
		FeatureTreeNode.newChildInstanceWithValue("yellow", String.valueOf(components[YELLOW_COMPONENT_NUMBER]), parent);
		FeatureTreeNode.newChildInstanceWithValue("black", String.valueOf(components[BLACK_COMPONENT_NUMBER]), parent);
	}
}
