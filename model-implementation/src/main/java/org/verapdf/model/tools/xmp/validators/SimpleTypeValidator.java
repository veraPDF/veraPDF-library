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

    public static SimpleTypeValidator fromValue(String type) {
        SimpleTypeEnum typeEnum = SimpleTypeEnum.fromString(type);
        if (typeEnum == null) {
            throw new IllegalArgumentException("Argument type must conform to one of defined simple types");
        }
        String regexp = typeEnum.getRegexp();
        return new SimpleTypeValidator(Pattern.compile(regexp));
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
        CHOICE(XMPConstants.CHOICE, "^.*$"),
        INTEGER(XMPConstants.INTEGER, "^[+-]?\\d+$"),
        LOCALE(XMPConstants.LOCALE, "^([a-zA-Z]{1,8})((-[a-zA-Z]{1,8})*)$"),
        REAL(XMPConstants.REAL, "^[+-]?\\d+\\.?\\d*|[+-]?\\d*\\.?\\d+$"),
        MIME_TYPE(XMPConstants.MIME_TYPE, "^[-\\w+\\.]+/[-\\w+\\.]+$"),
        PROPER_NAME(XMPConstants.PROPER_NAME, "^.*$"),
        TEXT(XMPConstants.TEXT, "^.*$"),
        AGENT_NAME(XMPConstants.AGENT_NAME, "^.*$"),
        RATIONAL(XMPConstants.RATIONAL, "^\\d+/\\d+$"),
        GPS_COORDINATE(XMPConstants.GPS_COORDINATE, "^\\d{2},\\d{2}[,\\.]\\d{2}[NSEW]$"),
        RENDITION_CLASS(XMPConstants.RENDITION_CLASS, "^.*$");

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
