package org.verapdf.model.tools.xmp.validators;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.tools.xmp.ValidatorsContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Maksim Bezrukov
 */
public class StructuredTypeWithRestrictedFieldsValidator implements TypeValidator {

    private String childNamespaceURI;
    private Map<String, String> childrenTypes;
    private Map<String, Pattern> childrenChoiceTypes;
    private ValidatorsContainer parentContainer;

    public StructuredTypeWithRestrictedFieldsValidator(String childNamespaceURI, Map<String, String> childrenTypes, Map<String, Pattern> childrenChoiceTypes, ValidatorsContainer parentContainer) {
        this.childNamespaceURI = childNamespaceURI;
        this.childrenTypes = childrenTypes;
        this.childrenChoiceTypes = childrenChoiceTypes;
        this.parentContainer = parentContainer;
    }

    public static StructuredTypeWithRestrictedFieldsValidator fromValues(String childNamespaceURI, Map<String, String> childrenTypes, Map<String, Pattern> childrenClosedTypes, ValidatorsContainer parentContainer) {
        if (childNamespaceURI == null) {
            throw new IllegalArgumentException("Argument child namespace URI can not be null");
        }
        if (childrenTypes == null) {
            throw new IllegalArgumentException("Argument children types can not be null");
        }
        if (childrenClosedTypes == null) {
            throw new IllegalArgumentException("Argument children closed types can not be null");
        }
        if (parentContainer == null) {
            throw new IllegalArgumentException("Argument parent container can not be null");
        }

        return new StructuredTypeWithRestrictedFieldsValidator(childNamespaceURI, new HashMap<>(childrenTypes), new HashMap<>(childrenClosedTypes), parentContainer);
    }

    public boolean isCorresponding(VeraPDFXMPNode node) {
        if (!node.getOptions().isStruct()) {
            return false;
        }
        for (VeraPDFXMPNode child : node.getChildren()) {
            if (!childNamespaceURI.equals(child.getNamespaceURI())) {
                return false;
            }

            if (childrenChoiceTypes.containsKey(child.getName())) {
                Pattern p = childrenChoiceTypes.get(child.getName());
                if (!p.matcher(child.getValue()).matches()) {
                    return false;
                }
            } else if (childrenTypes.containsKey(child.getName())) {
                if (!parentContainer.validate(child, childrenTypes.get(child.getName()))) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }
}
