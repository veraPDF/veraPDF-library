package org.verapdf.model.tools.xmp.validators;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.tools.xmp.XMPConstants;

import java.util.regex.Pattern;

/**
 * @author Maksim Bezrukov
 */
public class SimpleTypeValidator implements TypeValidator {

    private Pattern pattern;

    private SimpleTypeValidator(Pattern pattern) {
        this.pattern = pattern;
    }

    public static SimpleTypeValidator fromValue(SimpleTypeEnum type) {
        if (type == null) {
            throw new IllegalArgumentException("Argument type can not be null");
        }
        String regexp = type.getRegexp();
        return new SimpleTypeValidator(Pattern.compile(regexp));
    }

    public static SimpleTypeValidator fromValue(Pattern pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Argument type can not be null");
        }
        return new SimpleTypeValidator(pattern);
    }

    @Override
    public boolean isCorresponding(VeraPDFXMPNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Argument node can not be null");
        }
        return node.getOptions().isSimple() && (pattern == null || pattern.matcher(node.getValue()).matches());
    }

    public enum SimpleTypeEnum {

        BOOLEAN(XMPConstants.BOOLEAN, "^True$|^False$"),
        INTEGER(XMPConstants.INTEGER, "^[+-]?\\d+$"),
        REAL(XMPConstants.REAL, "^[+-]?\\d+\\.?\\d*|[+-]?\\d*\\.?\\d+$"),
        MIME_TYPE(XMPConstants.MIME_TYPE, "^[-\\w+\\.]+/[-\\w+\\.]+$"),
        PROPER_NAME(XMPConstants.PROPER_NAME, "(?s)(^.*$)"),
        TEXT(XMPConstants.TEXT, "(?s)(^.*$)"),
        AGENT_NAME(XMPConstants.AGENT_NAME, "(?s)(^.*$)"),
        RATIONAL(XMPConstants.RATIONAL, "(?s)(^.*$)"),
        RENDITION_CLASS(XMPConstants.RENDITION_CLASS, "(?s)(^.*$)");

        private String type;
        private String regexp;

        SimpleTypeEnum(String type, String regexp) {
            this.type = type;
            this.regexp = regexp;
        }

        public String getType() {
            return this.type;
        }

        public String getRegexp() {
            return this.regexp;
        }

        public static SimpleTypeEnum fromString(String type) {
            if (type != null) {
                for (SimpleTypeEnum typeEnum : SimpleTypeEnum.values()) {
                    if (type.equalsIgnoreCase(typeEnum.type)) {
                        return typeEnum;
                    }
                }
            }
            return null;
        }
    }
}
