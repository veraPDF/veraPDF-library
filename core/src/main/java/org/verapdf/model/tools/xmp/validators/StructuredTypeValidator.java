package org.verapdf.model.tools.xmp.validators;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.tools.xmp.ValidatorsContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Maksim Bezrukov
 */
public class StructuredTypeValidator implements TypeValidator {

    private String childNamespaceURI;
    private Map<String, String> childrenTypes;
    private ValidatorsContainer parentContainer;

    private StructuredTypeValidator(String childNamespaceURI, Map<String, String> childrenTypes, ValidatorsContainer parentContainer) {
        this.childNamespaceURI = childNamespaceURI;
        this.childrenTypes = childrenTypes;
        this.parentContainer = parentContainer;
    }

    public static StructuredTypeValidator fromValues(String childNamespaceURI, Map<String, String> childrenTypes, ValidatorsContainer parentContainer) {
        if (childNamespaceURI == null) {
            throw new IllegalArgumentException("Argument child namespace URI can not be null");
        }
        if (childrenTypes == null || childrenTypes.isEmpty()) {
            throw new IllegalArgumentException("Argument children types can not be null or empty");
        }
        if (parentContainer == null) {
            throw new IllegalArgumentException("Argument parent container can not be null");
        }

        return new StructuredTypeValidator(childNamespaceURI, new HashMap<>(childrenTypes), parentContainer);
    }

    @Override
    public boolean isCorresponding(VeraPDFXMPNode node) {
        if (!node.getOptions().isStruct()) {
            return false;
        }
        for (VeraPDFXMPNode child : node.getChildren()) {
            if (!(childrenTypes.containsKey(child.getName()) &&
                    childNamespaceURI.equals(child.getNamespaceURI()) &&
                    parentContainer.validate(child, childrenTypes.get(child.getName())))) {
                return false;
            }
        }
        return true;
    }
}
