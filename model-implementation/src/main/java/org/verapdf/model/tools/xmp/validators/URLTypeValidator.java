package org.verapdf.model.tools.xmp.validators;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Maksim Bezrukov
 */
public class URLTypeValidator implements TypeValidator {

    private static final Logger LOGGER = Logger
            .getLogger(URLTypeValidator.class);

    @Override
    public boolean isCorresponding(VeraPDFXMPNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Argument node can not be null.");
        }
        try {
            if (!node.getOptions().isSimple()) {
                return false;
            }
            URL url = new URL(node.getValue());
            return true;
        } catch (MalformedURLException e) {
            LOGGER.debug(e);
            return false;
        }
    }
}
