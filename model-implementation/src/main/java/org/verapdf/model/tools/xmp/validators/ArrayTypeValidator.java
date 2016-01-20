package org.verapdf.model.tools.xmp.validators;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import com.adobe.xmp.options.PropertyOptions;
import org.verapdf.model.tools.xmp.ValidatorsContainer;
import org.verapdf.model.tools.xmp.XMPConstants;

/**
 * @author Maksim Bezrukov
 */
public class ArrayTypeValidator {

    private ArrayTypeEnum type;
    private ValidatorsContainer parentContainer;

    private ArrayTypeValidator(ArrayTypeEnum type, ValidatorsContainer parentContainer) {
        this.type = type;
        this.parentContainer = parentContainer;
    }

    public static ArrayTypeValidator fromValues(ArrayTypeEnum type, ValidatorsContainer parentContainer) {
        if (parentContainer == null) {
            throw new IllegalArgumentException("Argument parent container can not be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Argument type can not be null");
        }
        return new ArrayTypeValidator(type, parentContainer);
    }

    public boolean isCorresponding(VeraPDFXMPNode node, String childType) {
        if (node == null) {
            throw new IllegalArgumentException("Argument node can not be null");
        }
        if (childType == null) {
            throw new IllegalArgumentException("Argument child type can not be null");
        }
        PropertyOptions options = node.getOptions();
        boolean isValidArrayType;
        switch (type) {
            case ALT:
                isValidArrayType = options.isArrayAlternate();
                break;
            case SEQ:
                isValidArrayType = (options.isArrayOrdered() && !options.isArrayAlternate());
                break;
            case BAG:
                isValidArrayType = (options.isArray() && !(options.isArrayOrdered() || options.isArrayAlternate()));
                break;
            default:
                throw new IllegalStateException("Array type validator must be created with valid type");
        }

        if (!isValidArrayType) {
            return false;
        } else {
            for (VeraPDFXMPNode child : node.getChildren()) {
                if (!this.parentContainer.validate(child, childType)) {
                    return false;
                }
            }
            return true;
        }
    }

    public enum ArrayTypeEnum {

        BAG(XMPConstants.BAG),
        ALT(XMPConstants.ALT),
        SEQ(XMPConstants.SEQ);

        private String type;

        ArrayTypeEnum(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }

        public static ArrayTypeEnum fromString(String type) {
            if (type != null) {
                for (ArrayTypeEnum typeEnum : ArrayTypeEnum.values()) {
                    if (type.equalsIgnoreCase(typeEnum.type)) {
                        return typeEnum;
                    }
                }
            }
            return null;
        }
    }
}
