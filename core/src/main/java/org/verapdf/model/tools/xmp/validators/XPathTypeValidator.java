package org.verapdf.model.tools.xmp.validators;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.apache.log4j.Logger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * @author Maksim Bezrukov
 */
public class XPathTypeValidator implements TypeValidator {

    private static final Logger LOGGER = Logger
            .getLogger(XPathTypeValidator.class);

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
            XPathExpression expr = xpath.compile(node.getValue());
            return true;
        } catch (XPathExpressionException e) {
            LOGGER.debug(e);
            return false;
        }
    }
}
