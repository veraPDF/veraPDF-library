package org.verapdf.model.tools.xmp.validators;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.adobe.xmp.impl.VeraPDFXMPNode;

/**
 * @author Maksim Bezrukov
 */
public class XPathTypeValidator implements TypeValidator {

    private static final Logger LOGGER = Logger
            .getLogger(XPathTypeValidator.class.getName());

    @Override
    public boolean isCorresponding(VeraPDFXMPNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Argument node can not be null.");
        }
        try {
            if (!node.getOptions().isSimple()) {
                return false;
            }
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            xpath.compile(node.getValue());
            return true;
        } catch (XPathExpressionException e) {
            LOGGER.log(Level.FINE, "Node value: " + node.getValue() + " is not a valid XPath", e);
            return false;
        }
    }
}
