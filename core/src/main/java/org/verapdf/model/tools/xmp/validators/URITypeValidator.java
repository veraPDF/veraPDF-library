package org.verapdf.model.tools.xmp.validators;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.adobe.xmp.impl.VeraPDFXMPNode;

/**
 * @author Maksim Bezrukov
 */
public class URITypeValidator implements TypeValidator {

    private static final Logger LOGGER = Logger
            .getLogger(URITypeValidator.class.getName());

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
            new URI(node.getValue());
            return true;
        } catch (URISyntaxException e) {
            LOGGER.log(Level.FINE, "Node value not a URI: " + node.getValue(), e);
            return false;
        }
    }
}
