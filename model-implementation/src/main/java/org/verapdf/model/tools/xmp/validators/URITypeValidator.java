package org.verapdf.model.tools.xmp.validators;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.apache.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Maksim Bezrukov
 */
public class URITypeValidator implements TypeValidator {

    private static final Logger LOGGER = Logger
            .getLogger(URITypeValidator.class);

    @Override
    public boolean isCorresponding(VeraPDFXMPNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Argument node can not be null.");
        }
        try {
            if (!node.getOptions().isSimple()) {
                return false;
            }
            URI uri = new URI(node.getValue());
            return true;
        } catch (URISyntaxException e) {
            LOGGER.debug(e);
            return false;
        }
    }
}
