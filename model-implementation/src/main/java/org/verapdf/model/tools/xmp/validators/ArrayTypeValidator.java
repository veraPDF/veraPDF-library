package org.verapdf.model.tools.xmp.validators;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import com.adobe.xmp.options.PropertyOptions;
import org.verapdf.model.tools.xmp.XMPConstants;

/**
 * @author Maksim Bezrukov
 */
public class ArrayTypeValidator implements TypeValidator {

    private ArrayTypeEnum type;
    private TypeValidator childValidator;

    private ArrayTypeValidator(ArrayTypeEnum type, TypeValidator childValidator) {
        this.type = type;
        this.childValidator = childValidator;
    }

    public static ArrayTypeValidator fromValues(String type, TypeValidator childValidator) {
        ArrayTypeEnum typeEnum = ArrayTypeEnum.fromString(type);
        if (typeEnum == null) {
            throw new IllegalArgumentException("Argument type must conform to one of defined array types");
        }
        if (typeEnum == ArrayTypeEnum.LANG_ALT && childValidator != null) {
            throw new IllegalArgumentException("For Lang Alt validator argument child Validator must be null.");
        }
        if (typeEnum != ArrayTypeEnum.LANG_ALT && childValidator == null) {
            throw new IllegalArgumentException("For not Lang Alt validator argument child Validator must not null.");
        }
        return new ArrayTypeValidator(typeEnum, childValidator);
    }

    @Override
    public boolean isCorresponding(VeraPDFXMPNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Argument node can not be null");
        }
        PropertyOptions options = node.getOptions();
        boolean isValidArrayType;
        switch (type) {
            case ALT:
                isValidArrayType = options.isArrayAlternate();
                break;
            case SEQ:
                isValidArrayType = options.isArrayOrdered();
                break;
            case BAG:
                isValidArrayType = (options.isArray() && !(options.isArrayOrdered() || options.isArrayAlternate()));
                break;
            case LANG_ALT:
                return options.isArrayAltText();
            default:
                throw new IllegalStateException("Array type validator must be created with valid type");
        }

        if (!isValidArrayType) {
            return false;
        } else {
            for (VeraPDFXMPNode child : node.getChildren()) {
                if (!this.childValidator.isCorresponding(child)) {
                    return false;
                }
            }
            return true;
        }
    }

    public enum ArrayTypeEnum {

        BAG(XMPConstants.BAG),
        ALT(XMPConstants.ALT),
        SEQ(XMPConstants.SEQ),
        LANG_ALT(XMPConstants.LANG_ALT);

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
