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
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

    private static final byte[] utf8MetadataPrefix = {0x3C, 0x3F, 0x78, 0x70, 0x61, 0x63, 0x6B,
            0x65, 0x74, 0x20, 0x62, 0x65, 0x67, 0x69, 0x6E, 0x3D};
    private static final byte[] utf16BEMetadataPrefix = {0x3C, 0x00, 0x3F, 0x00, 0x78, 0x00, 0x70, 0x00, 0x61, 0x00,
            0x63, 0x00, 0x6B, 0x00, 0x65, 0x00, 0x74, 0x00, 0x20, 0x00, 0x62, 0x00, 0x65, 0x00, 0x67, 0x00, 0x69, 0x00, 0x6E, 0x00, 0x3D};
    private static final byte[] utf16LEMetadataPrefix = {0x3C, 0x00, 0x3F, 0x00, 0x78, 0x00, 0x70, 0x00, 0x61, 0x00,
            0x63, 0x00, 0x6B, 0x00, 0x65, 0x00, 0x74, 0x00, 0x20, 0x00, 0x62, 0x00, 0x65, 0x00, 0x67, 0x00, 0x69, 0x00, 0x6E, 0x00, 0x3D, 0x00};
    private static final byte[] utf32BEMetadataPrefix = {0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78,
            0x00, 0x00, 0x00, 0x70, 0x00, 0x00, 0x00, 0x61, 0x00, 0x00, 0x00, 0x63, 0x00, 0x00, 0x00, 0x6B,
            0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00, 0x74, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x62,
            0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00, 0x67, 0x00, 0x00, 0x00, 0x69, 0x00, 0x00, 0x00, 0x6E, 0x00, 0x00, 0x00, 0x3D};
    private static final byte[] utf32LEMetadataPrefix = {0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78,
            0x00, 0x00, 0x00, 0x70, 0x00, 0x00, 0x00, 0x61, 0x00, 0x00, 0x00, 0x63, 0x00, 0x00, 0x00, 0x6B,
            0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00, 0x74, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x62,
            0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00, 0x67, 0x00, 0x00, 0x00, 0x69, 0x00, 0x00, 0x00, 0x6E, 0x00, 0x00, 0x00, 0x3D, 0x00, 0x00, 0x00};
    private static final byte[] utf8SingleQuote = {0x27};
    private static final byte[] utf8DoubleQuote = {0x22};
    private static final byte[] utf16BESingleQuote = {0x00, 0x27};
    private static final byte[] utf16BEDoubleQuote = {0x00, 0x22};
    private static final byte[] utf16LESingleQuote = {0x27, 0x00};
    private static final byte[] utf16LEDoubleQuote = {0x22, 0x00};
    private static final byte[] utf32BESingleQuote = {0x00, 0x00, 0x00, 0x27};
    private static final byte[] utf32BEDoubleQuote = {0x00, 0x00, 0x00, 0x22};
    private static final byte[] utf32LESingleQuote = {0x27, 0x00, 0x00, 0x00};
    private static final byte[] utf32LEDoubleQuote = {0x22, 0x00, 0x00, 0x00};

    private static final byte[] utf8Begin = {-0x11, -0x45, -0x41};
    private static final byte[] utf16BEBegin = {-0x02, -0x01};
    private static final byte[] utf16LEBegin = {-0x01, -0x02};
    private static final byte[] utf32BEBegin = {0x00, 0x00, -0x02, -0x01};
    private static final byte[] utf32LEBegin = {-0x01, -0x02, 0x00, 0x00};

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
        GregorianCalendar greg = new GregorianCalendar();
        greg.setTime(calendar.getTime());
        greg.setTimeZone(calendar.getTimeZone());
        XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(greg);
        return xmlCalendar.toXMLFormat();
    }

    /**
     * Creates node with date value formatted in XML format from the given Calendar
     *
     * @param nodeName   - name of the created node
     * @param parent     - parent element for created node
     * @param date       - the given date as Calendar class
     * @param collection - collection for which this node creates
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
     * @param baseParam - COSBase object
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
     * @param name   - name of the node
     * @param box    - PDRectangle object represents the box
     * @param parent - parent element for the created node
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
     * @param name   - name of the node
     * @param value  - value of the node
     * @param parent - parent of the node
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
     * @param name       - name for the created node
     * @param color      - PDColor class represents device color space for creating node
     * @param parent     - parent node for the creating node
     * @param collection - features collection in which parent situated
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
     * @param set         - set of elements id
     * @param elementName - element names
     * @param setName     - name of the parent element for created elements. If null, all created elements will be attached to the {@code root}
     * @param root        - root element for the generated parent element for generated elements or direct paren for generated elements in case of {@code setName} equals to null
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
     * @param metadata   - PDMetadata class from which metadata will be taken
     * @param nodeName   - name for the created node
     * @param collection - collection for the created node
     * @return created node
     * @throws FeaturesTreeNodeException - occurs when wrong features tree node constructs
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
            String metadataString = getStringFromMeta(bStream);
            node.setValue(metadataString);
        } catch (IOException e) {
            LOGGER.debug("Error while converting stream to string", e);
            node.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.METADATACONVERT_ID);
            ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.METADATACONVERT_ID, ErrorsHelper.METADATACONVERT_MESSAGE);
        }

        return node;
    }

    private static String getEncodingWithBegin(byte[] bStream, int beginOffset) {
        if (beginOffset >= 0) {
            byte[] suffix = Arrays.copyOfRange(bStream, beginOffset, bStream.length);
            if (startsWith(suffix, utf32BEBegin)) {
                return "UTF-32BE";
            } else if (startsWith(suffix, utf32LEBegin)) {
                return "UTF-32LE";
            } else if (startsWith(suffix, utf8Begin)) {
                return "UTF-8";
            } else if (startsWith(suffix, utf16BEBegin)) {
                return "UTF-16BE";
            } else if (startsWith(suffix, utf16LEBegin)) {
                return "UTF-16LE";
            }
        }
        return "UTF-8";
    }

    private static String getStringFromMeta(byte[] stream) {
        int beginOffset = -1;
        int packageBeginOffset = 0;
        if (stream != null) {
            for (int i = 0; i < stream.length; ++i) {
                if (stream[i] == 0x3C) {
                    beginOffset = getBegOffsetFromCutted(Arrays.copyOfRange(stream, i, stream.length));
                    if (beginOffset >= 0) {
                        packageBeginOffset = i;
                        beginOffset += i;
                        break;
                    }
                }
            }
            return new String(Arrays.copyOfRange(stream, packageBeginOffset, stream.length), Charset.forName(getEncodingWithBegin(stream, beginOffset)));
        }

        return null;
    }

    private static int getBegOffsetFromCutted(byte[] bStream) {
        int beginOffset = -1;
        if ((bStream.length >= utf32LEMetadataPrefix.length + utf32LEDoubleQuote.length + utf32LEBegin.length) &&
                startsWith(bStream, utf32LEMetadataPrefix)) {
            byte[] quote = Arrays.copyOfRange(bStream, utf32LEMetadataPrefix.length, utf32LEMetadataPrefix.length + utf32LEDoubleQuote.length);
            if (startsWith(quote, utf32LESingleQuote) || startsWith(quote, utf32LEDoubleQuote)) {
                beginOffset = utf32LEMetadataPrefix.length + utf32LEDoubleQuote.length;
            }
        } else if ((bStream.length >= utf32BEMetadataPrefix.length + utf32BEDoubleQuote.length + utf32BEBegin.length) &&
                startsWith(bStream, utf32BEMetadataPrefix)) {
            byte[] quote = Arrays.copyOfRange(bStream, utf32BEMetadataPrefix.length, utf32BEMetadataPrefix.length + utf32BEDoubleQuote.length);
            if (startsWith(quote, utf32BESingleQuote) || startsWith(quote, utf32BEDoubleQuote)) {
                beginOffset = utf32BEMetadataPrefix.length + utf32BEDoubleQuote.length;
            }
        } else if ((bStream.length >= utf16LEMetadataPrefix.length + utf16LEDoubleQuote.length + utf16LEBegin.length) &&
                startsWith(bStream, utf16LEMetadataPrefix)) {
            byte[] quote = Arrays.copyOfRange(bStream, utf16LEMetadataPrefix.length, utf16LEMetadataPrefix.length + utf16LEDoubleQuote.length);
            if (startsWith(quote, utf16LESingleQuote) || startsWith(quote, utf16LEDoubleQuote)) {
                beginOffset = utf16LEMetadataPrefix.length + utf16LEDoubleQuote.length;
            }
        } else if ((bStream.length >= utf16BEMetadataPrefix.length + utf16BEDoubleQuote.length + utf16BEBegin.length) &&
                startsWith(bStream, utf16BEMetadataPrefix)) {
            byte[] quote = Arrays.copyOfRange(bStream, utf16BEMetadataPrefix.length, utf16BEMetadataPrefix.length + utf16BEDoubleQuote.length);
            if (startsWith(quote, utf16BESingleQuote) || startsWith(quote, utf16BEDoubleQuote)) {
                beginOffset = utf16BEMetadataPrefix.length + utf16BEDoubleQuote.length;
            }
        } else if ((bStream.length >= utf8MetadataPrefix.length + utf8DoubleQuote.length + utf8Begin.length) &&
                startsWith(bStream, utf8MetadataPrefix)) {
            byte[] quote = Arrays.copyOfRange(bStream, utf8MetadataPrefix.length, utf8MetadataPrefix.length + utf8DoubleQuote.length);
            if (startsWith(quote, utf8SingleQuote) || startsWith(quote, utf8DoubleQuote)) {
                beginOffset = utf8MetadataPrefix.length + utf8DoubleQuote.length;
            }
        }
        return beginOffset;
    }

    private static boolean startsWith(byte[] source, byte[] match) {
        if (match.length > source.length) {
            return false;
        }
        for (int i = 0; i < match.length; i++) {
            if (source[i] != match[i]) {
                return false;
            }
        }
        return true;
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
