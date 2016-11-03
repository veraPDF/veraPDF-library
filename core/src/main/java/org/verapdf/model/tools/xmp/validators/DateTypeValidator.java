package org.verapdf.model.tools.xmp.validators;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.adobe.xmp.XMPDateTimeFactory;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFXMPNode;

/**
 * @author Maksim Bezrukov
 */
public class DateTypeValidator implements TypeValidator {

    private static final Logger LOGGER = Logger
            .getLogger(DateTypeValidator.class.getName());

    @Override
    public boolean isCorresponding(VeraPDFXMPNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Argument node can not be null.");
        }
        try {
            if (!node.getOptions().isSimple()) {
                return false;
            }
            XMPDateTimeFactory.createFromISO8601(node.getValue());
            return true;
        } catch (XMPException e) {
            LOGGER.log(Level.WARNING, "Node:" + node.getName() + " with value:" + node.getValue() + " is not a valid ISO8601 date value");
            LOGGER.log(Level.FINE, "XMP Exception is", e);
            return false;
        }
    }
}
