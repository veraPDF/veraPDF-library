package org.verapdf.model.tools.xmp.validators;

import com.adobe.xmp.XMPDateTimeFactory;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.apache.log4j.Logger;

/**
 * @author Maksim Bezrukov
 */
public class DateTypeValidator implements TypeValidator {

    private static final Logger LOGGER = Logger
            .getLogger(DateTypeValidator.class);

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
            LOGGER.debug(e);
            return false;
        }
    }
}
