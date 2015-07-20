package org.verapdf.features.pb.tools;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Helps in creating similar nodes in different features objects
 *
 * @author Maksim Bezrukov
 */
public final class PBCreateNodeHelper {

    private static final String LLX = "llx";
    private static final String LLY = "lly";
    private static final String URX = "urx";
    private static final String URY = "ury";

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
            modificationDate = FeatureTreeNode.newInstance(nodeName, parent);
            try {
                modificationDate.setValue(getXMLFormat(date));
            } catch (DatatypeConfigurationException e) {
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
            return str.isHex() ? str.toHexString() : str.getString();
        } else {
            return null;
        }
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
            boxNode = FeatureTreeNode.newInstance(name, parent);
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
            return FeatureTreeNode.newInstance(name, value, parent);
        } else {
            return null;
        }
    }
}
