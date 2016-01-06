package org.verapdf.model.tools.xmp;

/**
 * @author Maksim Bezrukov
 */
public class XMPConstants {

    // array types
    public static final String BAG = "Bag";
    public static final String SEQ = "Seq";
    public static final String ALT = "Alt";

    // simple types
    public static final String LANG_ALT = "Lang Alt";
    public static final String BOOLEAN = "Boolean";
    public static final String INTEGER = "Integer";
    public static final String LOCALE = "Locale";
    public static final String REAL = "Real";
    public static final String MIME_TYPE = "MIMEType";
    public static final String PROPER_NAME = "ProperName";
    public static final String TEXT = "Text";
    public static final String AGENT_NAME = "AgentName";
    public static final String RATIONAL = "Rational";
    public static final String GPS_COORDINATE = "GPSCoordinate";
    public static final String RENDITION_CLASS = "RenditionClass";
    public static final String CHOICE = "Choice";

    // exception check types
    public static final String DATE = "Date";
    public static final String URI = "URI";
    public static final String URL = "URL";
    public static final String XPATH = "XPath";

    // structured types for all parts of PDF/A
    public static final String DIMENSIONS = "Dimensions";
    public static final String THUMBNAIL = "Thumbnail";
    public static final String RESOURCE_EVENT = "ResourceEvent";
    public static final String RESOURCE_REF = "ResourceRef";
    public static final String VERSION = "Version";
    public static final String JOB = "Job";
    public static final String FLASH = "Flash";
    public static final String OECF_SFR = "OECF/SFR";
    public static final String CFA_PATTERN = "CFAPattern";
    public static final String DEVICE_SETTINGS = "DeviceSettings";

    // structured types for PDF/A-2 and PDF/A-3
    public static final String COLORANT = "Colorant";
    public static final String FONT = "Font";
    public static final String BEAT_SPLICE_STRETCH = "beatSpliceStretch";
    public static final String MARKER = "Marker";
    public static final String MEDIA = "Media";
    public static final String PROJECT_LINK = "ProjectLink";
    public static final String RESAMPLE_STRETCH = "resampleStretch";
    public static final String TIME = "Time";
    public static final String TIMECODE = "Timecode";
    public static final String TIME_SCALE_STRETCH = "timeScaleStretch";

    // structured types structures
    public static final String[] COLORANT_STRUCTURE = {"http://ns.adobe.com/xap/1.0/g/",
            "swatchName", TEXT,
            "mode", CHOICE,
            "type", CHOICE,
            "cyan", REAL,
            "magenta", REAL,
            "yellow", REAL,
            "black", REAL,
            "red", INTEGER,
            "green", INTEGER,
            "blue", INTEGER,
            "L", REAL,
            "A", INTEGER,
            "B", INTEGER
    };

    public static final String[] DIMENSIONS_STRUCTURE = {"http://ns.adobe.com/xap/1.0/sType/Dimensions#",
            "w", REAL,
            "h", REAL,
            "unit", CHOICE
    };

    public static final String[] FONT_STRUCTURE = {"http:ns.adobe.com/xap/1.0/sType/Font#",
            "fontName", TEXT,
            "fontFamily", TEXT,
            "fontFace", TEXT,
            "fontType", CHOICE,
            "versionString", TEXT,
            "composite", BOOLEAN,
            "fontFileName", TEXT,
            "childFontFiles", SEQ + " " + TEXT
    };

    public static final String[] THUMBNAIL_STRUCTURE = {"http://ns.adobe.com/xap/1.0/g/img/",
            "height", INTEGER,
            "width", INTEGER,
            "format", CHOICE,
            "image", TEXT
    };

    public static final String[] RESOURCE_EVENT_STRUCTURE = {"http://ns.adobe.com/xap/1.0/sType/ResourceEvent#",
            "action", CHOICE,
            "instanceID", URI,
            "parameters", TEXT,
            "softwareAgent", AGENT_NAME,
            "when", DATE
    };

    public static final String[] RESOURCE_REF_STRUCTURE = {"http://ns.adobe.com/xap/1.0/sType/ResourceRef#",
            "instanceID", URI,
            "documentID", URI,
            "versionID", TEXT,
            "renditionClass", RENDITION_CLASS,
            "renditionParams", TEXT,
            "manager", AGENT_NAME,
            "managerVariant", TEXT,
            "manageTo", URI,
            "manageUI", URI
    };

    public static final String[] VERSION_STRUCTURE = {"http://ns.adobe.com/xap/1.0/sType/Version#",
            "comments", TEXT,
            "event", RESOURCE_EVENT,
            "modifyDate", DATE,
            "modifier", PROPER_NAME,
            "version", TEXT
    };

    public static final String[] JOB_STRUCTURE = {"http://ns.adobe.com/xap/1.0/sType/Job#",
            "name", TEXT,
            "id", TEXT,
            "url", URL
    };

    public static final String[] BEAT_SPLICE_STRETCH_STRUCTURE = {"http://ns.adobe.com/xmp/1.0/DynamicMedia/",
            "useFileBeatsMarker", BOOLEAN,
            "riseInDecibel", REAL,
            "riseInTimeDuration", TIME
    };

    public static final String[] MARKER_STRUCTURE = {"http://ns.adobe.com/xmp/1.0/DynamicMedia/",
            "startTime", TIME,
            "duration", TIME,
            "comment", TEXT,
            "name", TEXT,
            "location", URI,
            "target", TEXT,
            "type", CHOICE
    };

    public static final String[] MEDIA_STRUCTURE = {"http://ns.adobe.com/xmp/1.0/DynamicMedia/",
            "path", URI,
            "track", TEXT,
            "startTime", TIME,
            "duration", TIME,
            "managed", BOOLEAN,
            "webStatement", URI
    };

    public static final String[] PROJECT_LINK_STRUCTURE = {"http://ns.adobe.com/xmp/1.0/DynamicMedia/",
            "type", CHOICE,
            "path", URI
    };

    public static final String[] RESAMPLE_STRETCH_STRUCTURE = {"http://ns.adobe.com/xmp/1.0/DynamicMedia/",
            "quality", CHOICE
    };

    public static final String[] TIME_STRUCTURE = {"http://ns.adobe.com/xmp/1.0/DynamicMedia/",
            "value", INTEGER,
            "scale", RATIONAL
    };

    public static final String[] TIMECODE_STRUCTURE = {"http://ns.adobe.com/xmp/1.0/DynamicMedia/",
            "timeValue", TEXT,
            "timeFormat", CHOICE
    };

    public static final String[] TIME_SCALE_STRETCH_STRUCTURE = {"http://ns.adobe.com/xmp/1.0/DynamicMedia/",
            "quality", CHOICE,
            "frameSize", REAL,
            "frameOverlappingPercentage", REAL
    };

    public static final String[] FLASH_STRUCTURE = {"http://ns.adobe.com/exif/1.0/",
            "Fired", BOOLEAN,
            "Return", CHOICE,
            "Mode", CHOICE,
            "Function", BOOLEAN,
            "RedEyeMode", BOOLEAN
    };

    public static final String[] OECF_SFR_STRUCTURE = {"http://ns.adobe.com/exif/1.0/",
            "Columns", INTEGER,
            "Rows", INTEGER,
            "Names", SEQ + " " + TEXT,
            "Values", SEQ + " " + RATIONAL
    };

    public static final String[] CFA_PATTERN_STRUCTURE = {"http://ns.adobe.com/exif/1.0/",
            "Columns", INTEGER,
            "Rows", INTEGER,
            "Values", SEQ + " " + INTEGER
    };

    public static final String[] DEVICE_SETTINGS_STRUCTURE = {"http://ns.adobe.com/exif/1.0/",
            "Columns", INTEGER,
            "Rows", INTEGER,
            "Settings", SEQ + " " + TEXT
    };
}
