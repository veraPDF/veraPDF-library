package org.verapdf.model.tools.xmp.validators;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.adobe.xmp.impl.VeraPDFXMPNode;

/**
 * @author Maksim Bezrukov
 */
public class URLTypeValidator implements TypeValidator {

    private static final Logger LOGGER = Logger
            .getLogger(URLTypeValidator.class.getName());

    @SuppressWarnings("unused")
    @Override
    public boolean isCorresponding(VeraPDFXMPNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Argument node can not be null.");
        }
        try {
            if (!node.getOptions().isSimple()) {
                return false;
            }
            new URL(node.getValue());
            return true;
        } catch (MalformedURLException e) {
            LOGGER.log(Level.FINE,  "Node value not a valid URL: " + node.getValue(), e);
            return false;
        }
    }
}
