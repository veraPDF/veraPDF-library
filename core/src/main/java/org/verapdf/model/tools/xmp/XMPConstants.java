package org.verapdf.model.tools.xmp;

import com.adobe.xmp.impl.XMPSchemaRegistryImpl;

/**
 * @author Maksim Bezrukov
 */
public class XMPConstants {

    // array types
    public static final String BAG = "bag";
    public static final String SEQ = "seq";
    public static final String ALT = "alt";

    // simple types
    public static final String LANG_ALT = "lang alt";
    public static final String BOOLEAN = "boolean";
    public static final String INTEGER = "integer";
    public static final String LOCALE = "locale";
    public static final String REAL = "real";
    public static final String MIME_TYPE = "mimetype";
    public static final String PROPER_NAME = "propername";
    public static final String TEXT = "text";
    public static final String AGENT_NAME = "agentname";
    public static final String RATIONAL = "rational";
    public static final String GPS_COORDINATE = "gpscoordinate";
    public static final String RENDITION_CLASS = "renditionclass";

    // exception check types
    public static final String DATE = "date";
    public static final String URI = "uri";
    public static final String URL = "url";
    public static final String XPATH = "xpath";

    // structured types for all parts of PDF/A
    public static final String DIMENSIONS = "dimensions";
    public static final String THUMBNAIL = "thumbnail";
    public static final String RESOURCE_EVENT = "resourceevent";
    public static final String RESOURCE_REF = "resourceref";
    public static final String VERSION = "version";
    public static final String JOB = "job";
    public static final String FLASH = "flash";
    public static final String OECF_SFR = "oecf/sfr";
    public static final String CFA_PATTERN = "cfapattern";
    public static final String DEVICE_SETTINGS = "devicesettings";

    // structured types for PDF/A-2 and PDF/A-3
    public static final String COLORANT = "colorant";
    public static final String FONT = "font";
    public static final String BEAT_SPLICE_STRETCH = "beatsplicestretch";
    public static final String MARKER = "marker";
    public static final String MEDIA = "media";
    public static final String PROJECT_LINK = "projectlink";
    public static final String RESAMPLE_STRETCH = "resamplestretch";
    public static final String TIME = "time";
    public static final String TIMECODE = "timecode";
    public static final String TIME_SCALE_STRETCH = "timescalestretch";

    // structured types structures
    public static final String[] COLORANT_WITHOUT_RESTRICTED_FIELD_STRUCTURE = {XMPSchemaRegistryImpl.TYPE_GRAPHICS,
            "swatchName", TEXT
    };

    public static final String[] COLORANT_RESTRICTED_FIELD_STRUCTURE = {
            "mode", TEXT, "^(CMYK|RGB|LAB)$", // closed Choice (CMYK, RGB, LAB)
            "type", TEXT, "^(PROCESS|SPOT)$", // closed Choice (PROCESS, SPOT)
            "cyan", REAL, "^[+]?(\\d{1,2}(\\.\\d*)?|\\d{0,2}\\.\\d+|100(\\.0*)?)$", // Real. Range: 0-100
            "magenta", REAL, "^[+]?(\\d{1,2}(\\.\\d*)?|\\d{0,2}\\.\\d+|100(\\.0*)?)$", // Real. Range: 0-100
            "yellow", REAL, "^[+]?(\\d{1,2}(\\.\\d*)?|\\d{0,2}\\.\\d+|100(\\.0*)?)$", // Real. Range: 0-100
            "black", REAL, "^[+]?(\\d{1,2}(\\.\\d*)?|\\d{0,2}\\.\\d+|100(\\.0*)?)$", // Real. Range: 0-100
            "red", INTEGER, "^[+]?([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])$", // Integer. Range: 0-255
            "green", INTEGER, "^[+]?([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])$", // Integer. Range: 0-255
            "blue", INTEGER, "^[+]?([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])$", // Integer. Range: 0-255
            "L", REAL, "^[+]?(\\d{1,2}(\\.\\d*)?|\\d{0,2}\\.\\d+|100(\\.0*)?)$", // Real. Range: 0-100
            "A", INTEGER, "^([+-]?[0]?[0-9]{1,2}|[+-]?1[01][0-9]|[+-]?12[0-7]|-128)$", // Integer. Range: -128-127
            "B", INTEGER, "^([+-]?[0]?[0-9]{1,2}|[+-]?1[01][0-9]|[+-]?12[0-7]|-128)$" // Integer. Range: -128-127
    };

    public static final String[] DIMENSIONS_STRUCTURE = {XMPSchemaRegistryImpl.TYPE_DIMENSIONS,
            "w", REAL,
            "h", REAL,
            "unit", TEXT // open Choice
    };

    public static final String[] FONT_STRUCTURE = {XMPSchemaRegistryImpl.TYPE_FONT,
            "fontName", TEXT,
            "fontFamily", TEXT,
            "fontFace", TEXT,
            "fontType", TEXT, // open Choice
            "versionString", TEXT,
            "composite", BOOLEAN,
            "fontFileName", TEXT,
            "childFontFiles", SEQ + " " + TEXT
    };

    public static final String[] THUMBNAIL_WITHOUT_RESTRICTED_FIELD_STRUCTURE = {XMPSchemaRegistryImpl.TYPE_IMAGE,
            "height", INTEGER,
            "width", INTEGER,
            "image", TEXT
    };

    public static final String[] THUMBNAIL_RESTRICTED_FIELD_STRUCTURE = {
            "format", TEXT, "^JPEG$" // closed Choice (JPEG)
    };

    public static final String[] RESOURCE_EVENT_STRUCTURE = {XMPSchemaRegistryImpl.TYPE_RESOURCEEVENT,
            "action", TEXT, // open Choice
            "instanceID", URI,
            "parameters", TEXT,
            "softwareAgent", AGENT_NAME,
            "when", DATE
    };

    public static final String[] RESOURCE_REF_STRUCTURE = {XMPSchemaRegistryImpl.TYPE_RESOURCEREF,
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

    public static final String[] VERSION_STRUCTURE = {XMPSchemaRegistryImpl.TYPE_ST_VERSION,
            "comments", TEXT,
            "event", RESOURCE_EVENT,
            "modifyDate", DATE,
            "modifier", PROPER_NAME,
            "version", TEXT
    };

    public static final String[] JOB_STRUCTURE = {XMPSchemaRegistryImpl.TYPE_ST_JOB,
            "name", TEXT,
            "id", TEXT,
            "url", URL
    };

    public static final String[] BEAT_SPLICE_STRETCH_STRUCTURE = {XMPSchemaRegistryImpl.NS_DM,
            "useFileBeatsMarker", BOOLEAN,
            "riseInDecibel", REAL,
            "riseInTimeDuration", TIME
    };

    public static final String[] MARKER_WITHOUT_RESTRICTED_FIELD_STRUCTURE = {XMPSchemaRegistryImpl.NS_DM,
            "startTime", TIME,
            "duration", TIME,
            "comment", TEXT,
            "name", TEXT,
            "location", URI,
            "target", TEXT
    };

    public static final String[] MARKER_RESTRICTED_FIELD_STRUCTURE = {
            "type", TEXT, "^(Chapter|Cue|Beat|Track|Index)$" // closed Choice of Text (Chapter, Cue, Beat, Track, Index)
    };

    public static final String[] MEDIA_STRUCTURE = {XMPSchemaRegistryImpl.NS_DM,
            "path", URI,
            "track", TEXT,
            "startTime", TIME,
            "duration", TIME,
            "managed", BOOLEAN,
            "webStatement", URI
    };

    public static final String[] PROJECT_LINK_WITHOUT_RESTRICTED_FIELD_STRUCTURE = {XMPSchemaRegistryImpl.NS_DM,
            "path", URI
    };

    public static final String[] PROJECT_LINK_RESTRICTED_FIELD_STRUCTURE = {
            "type", TEXT, "^(movie|still|audio|custom)$" // closed Choice of Text (movie, still, audio, custom)
    };

    public static final String[] RESAMPLE_STRETCH_WITHOUT_RESTRICTED_FIELD_STRUCTURE = {XMPSchemaRegistryImpl.NS_DM
    };

    public static final String[] RESAMPLE_STRETCH_RESTRICTED_FIELD_STRUCTURE = {
            "quality", TEXT, "^(High|Medium|Low)$" // closed Choice of Text (High, Medium, Low)
    };

    public static final String[] TIME_STRUCTURE = {XMPSchemaRegistryImpl.NS_DM,
            "value", INTEGER,
            "scale", RATIONAL
    };

    public static final String[] TIMECODE_WITHOUT_RESTRICTED_FIELD_STRUCTURE = {XMPSchemaRegistryImpl.NS_DM
    };

    public static final String[] TIMECODE_RESTRICTED_FIELD_STRUCTURE = {
            "timeValue", TEXT, "^\\d{2}((:\\d{2}){3}|(;\\d{2}){3})$", // A time value in the specified format: hh:mm:ss:ff or hh;mm;ss;ff
            "timeFormat", TEXT, "^(24|25|2997Drop|2997NonDrop|30|50|5994Drop|5994NonDrop|60|23976)(Timecode)$"
            // closed Choice of Text (24Timecode, 25Timecode, 2997DropTimecode, 2997NonDropTimecode, 30Timecode,
            // 50Timecode, 5994DropTimecode, 5994NonDropTimecode, 60Timecode, 23976Timecode)
    };

    public static final String[] TIME_SCALE_STRETCH_WITHOUT_RESTRICTED_FIELD_STRUCTURE = {XMPSchemaRegistryImpl.NS_DM,
            "frameSize", REAL,
            "frameOverlappingPercentage", REAL
    };

    public static final String[] TIME_SCALE_STRETCH_RESTRICTED_FIELD_STRUCTURE = {
            "quality", TEXT, "^(High|Medium|Low)$" // closed Choice of Text (High, Medium, Low)
    };

    public static final String[] FLASH_WITHOUT_RESTRICTED_FIELD_STRUCTURE = {XMPSchemaRegistryImpl.NS_EXIF,
            "Fired", BOOLEAN,
            "Function", BOOLEAN,
            "RedEyeMode", BOOLEAN
    };

    public static final String[] FLASH_RESTRICTED_FIELD_STRUCTURE = {
            "Return", TEXT, "^[023]$", // closed Choice (0, 2, 3)
            "Mode", TEXT, "^[0-3]$" // closed Choice (0, 1, 2, 3)
    };

    public static final String[] OECF_SFR_STRUCTURE = {XMPSchemaRegistryImpl.NS_EXIF,
            "Columns", INTEGER,
            "Rows", INTEGER,
            "Names", SEQ + " " + TEXT,
            "Values", SEQ + " " + RATIONAL
    };

    public static final String[] CFA_PATTERN_STRUCTURE = {XMPSchemaRegistryImpl.NS_EXIF,
            "Columns", INTEGER,
            "Rows", INTEGER,
            "Values", SEQ + " " + INTEGER
    };

    public static final String[] DEVICE_SETTINGS_STRUCTURE = {XMPSchemaRegistryImpl.NS_EXIF,
            "Columns", INTEGER,
            "Rows", INTEGER,
            "Settings", SEQ + " " + TEXT
    };

    // Properties common for PDF/A-1 and PDF/A-2, PDF/A-3
    public static final String[] PDFA_IDENTIFICATION_COMMON = {XMPSchemaRegistryImpl.NS_PDFA_ID,
            "part", INTEGER,
            "amd", TEXT
    };

    public static final String[] DUBLIN_CORE_COMMON = {XMPSchemaRegistryImpl.NS_DC,
            "contributor", BAG + " " + PROPER_NAME,
            "coverage", TEXT,
            "creator", SEQ + " " + PROPER_NAME,
            "date", SEQ + " " + DATE,
            "description", LANG_ALT,
            "format", MIME_TYPE,
            "identifier", TEXT,
            "language", BAG + " " + LOCALE,
            "publisher", BAG + " " + PROPER_NAME,
            "relation", BAG + " " + TEXT,
            "rights", LANG_ALT,
            "source", TEXT,
            "subject", BAG + " " + TEXT,
            "title", LANG_ALT,
            "type", BAG + " " + TEXT // bag open Choice
    };

    public static final String[] XMP_BASIC_COMMON = {XMPSchemaRegistryImpl.NS_XMP,
            "Advisory", BAG + " " + TEXT,
            "BaseURL", URL,
            "CreateDate", DATE,
            "CreatorTool", AGENT_NAME,
            "Identifier", BAG + " " + TEXT,
            "MetadataDate", DATE,
            "ModifyDate", DATE,
            "Nickname", TEXT,
            "Thumbnails", ALT + " " + THUMBNAIL
    };

    public static final String[] XMP_RIGHTS_COMMON = {XMPSchemaRegistryImpl.NS_XMP_RIGHTS,
            "Certificate", URL,
            "Marked", BOOLEAN,
            "Owner", BAG + " " + PROPER_NAME,
            "UsageTerms", LANG_ALT,
            "WebStatement", URL
    };

    public static final String[] XMP_MEDIA_MANAGEMENT_COMMON = {XMPSchemaRegistryImpl.NS_XMP_MM,
            "DerivedFrom", RESOURCE_REF,
            "DocumentID", URI,
            "History", SEQ + " " + RESOURCE_EVENT,
            "InstanceID", URI,
            "ManagedFrom", RESOURCE_REF,
            "Manager", AGENT_NAME,
            "ManageTo", URI,
            "ManageUI", URI,
            "ManagerVariant", TEXT,
            "RenditionClass", RENDITION_CLASS,
            "RenditionParams", TEXT,
            "VersionID", TEXT,
            "Versions", SEQ + " " + VERSION,
            "LastURL", URL,
            "RenditionOf", RESOURCE_REF,
            "SaveID", INTEGER
    };

    public static final String[] XMP_BASIC_JOB_COMMON = {XMPSchemaRegistryImpl.NS_XMP_BJ,
            "JobRef", BAG + " " + JOB
    };

    public static final String[] XMP_PAGED_TEXT_COMMON = {XMPSchemaRegistryImpl.TYPE_PAGEDFILE,
            "MaxPageSize", DIMENSIONS,
            "NPages", INTEGER
    };

    public static final String[] ADOBE_PDF_COMMON = {XMPSchemaRegistryImpl.NS_PDF,
            "Keywords", TEXT,
            "PDFVersion", TEXT,
            "Producer", AGENT_NAME
    };

    public static final String[] PHOTOSHOP_COMMON = {XMPSchemaRegistryImpl.NS_PHOTOSHOP,
            "AuthorsPosition", TEXT,
            "CaptionWriter", PROPER_NAME,
            "Category", TEXT,
            "City", TEXT,
            "Country", TEXT,
            "Credit", TEXT,
            "DateCreated", DATE,
            "Headline", TEXT,
            "Instructions", TEXT,
            "Source", TEXT,
            "State", TEXT,
            "TransmissionReference", TEXT,
            "Urgency", INTEGER
    };

    public static final String[] TIFF_WITHOUT_RESTRICTED_FIELD_COMMON = {XMPSchemaRegistryImpl.NS_TIFF,
            "ImageWidth", INTEGER,
            "ImageLength", INTEGER,
            "BitsPerSample", SEQ + " " + INTEGER,
            "SamplesPerPixel", INTEGER,
            "XResolution", RATIONAL,
            "YResolution", RATIONAL,
            "TransferFunction", SEQ + " " + INTEGER,
            "WhitePoint", SEQ + " " + RATIONAL,
            "PrimaryChromaticities", SEQ + " " + RATIONAL,
            "YCbCrCoefficients", SEQ + " " + RATIONAL,
            "ReferenceBlackWhite", SEQ + " " + RATIONAL,
            "DateTime", DATE,
            "ImageDescription", LANG_ALT,
            "Make", PROPER_NAME,
            "Model", PROPER_NAME,
            "Software", AGENT_NAME,
            "Artist", PROPER_NAME,
            "Copyright", LANG_ALT
    };

    public static final String[] TIFF_RESTRICTED_FIELD_COMMON = {XMPSchemaRegistryImpl.NS_TIFF,
            "Compression", INTEGER, "^[16]$", // closed Choice of Integer (1, 6)
            "PhotometricInterpretation", INTEGER, "^[26]$", // closed Choice of Integer (2, 6)
            "Orientation", INTEGER, "^[1-8]$", // closed Choice of Integer (1, 2, 3, 4, 5, 6, 7, 8)
            "PlanarConfiguration", INTEGER, "^[12]$", // closed Choice of Integer (1, 2)
            "YCbCrPositioning", INTEGER, "^[12]$", // closed Choice of Integer (1, 2)
            "ResolutionUnit", INTEGER, "^[23]$" // closed Choice of Integer (2, 3)
    };

    public static final String[][] TIFF_YCBCRSUBSAMPLING_SEQ_CHOICE_COMMON = {
            {"2","1"}, {"2","2"} // closed Choice of Seq Integer ([2,1], [2,2])
    };

    public static final String[] EXIF_WITHOUT_RESTRICTED_FIELD_COMMON = {XMPSchemaRegistryImpl.NS_EXIF,
            "CompressedBitsPerPixel", RATIONAL,
            "PixelXDimension", INTEGER,
            "PixelYDimension", INTEGER,
            "UserComment", LANG_ALT,
            "RelatedSoundFile", TEXT,
            "DateTimeOriginal", DATE,
            "DateTimeDigitized", DATE,
            "ExposureTime", RATIONAL,
            "FNumber", RATIONAL,
            "SpectralSensitivity", TEXT,
            "ISOSpeedRatings", SEQ + " " + INTEGER,
            "OECF", OECF_SFR,
            "ShutterSpeedValue", RATIONAL,
            "ApertureValue", RATIONAL,
            "BrightnessValue", RATIONAL,
            "ExposureBiasValue", RATIONAL,
            "MaxApertureValue", RATIONAL,
            "SubjectDistance", RATIONAL,
            "Flash", FLASH,
            "FocalLength", RATIONAL,
            "SubjectArea", SEQ + " " + INTEGER,
            "FlashEnergy", RATIONAL,
            "SpatialFrequencyResponse", OECF_SFR,
            "FocalPlaneXResolution", RATIONAL,
            "FocalPlaneYResolution", RATIONAL,
            "SubjectLocation", SEQ + " " + INTEGER,
            "ExposureIndex", RATIONAL,
            "CFAPattern", CFA_PATTERN,
            "DigitalZoomRatio", RATIONAL,
            "FocalLengthIn35mmFilm", INTEGER,
            "DeviceSettingDescription", DEVICE_SETTINGS,
            "ImageUniqueID", TEXT,
            "GPSVersionID", TEXT,
            "GPSLatitude", GPS_COORDINATE,
            "GPSLongitude", GPS_COORDINATE,
            "GPSAltitude", RATIONAL,
            "GPSTimeStamp", DATE,
            "GPSSatellites", TEXT,
            "GPSDOP", RATIONAL,
            "GPSSpeed", RATIONAL,
            "GPSTrack", RATIONAL,
            "GPSImgDirection", RATIONAL,
            "GPSMapDatum", TEXT,
            "GPSDestLatitude", GPS_COORDINATE,
            "GPSDestLongitude", GPS_COORDINATE,
            "GPSDestBearing", RATIONAL,
            "GPSDestDistance", RATIONAL,
            "GPSProcessingMethod", TEXT,
            "GPSAreaInformation", TEXT
    };

    public static final String[] EXIF_RESTRICTED_FIELD_COMMON = {XMPSchemaRegistryImpl.NS_EXIF,
            "ExposureProgram", INTEGER, "^[0-8]$", // closed Choice of Integer (0, 1, 2, 3, 4, 5, 6, 7, 8)
            "MeteringMode", INTEGER, "^([0-6]|255)$", // closed Choice of Integer (0, 1, 2, 3, 4, 5, 6, 255)
            "FocalPlaneResolutionUnit", INTEGER, "^[23]$", // closed Choice of Integer (2, 3)
            "SensingMethod", INTEGER, "^[1-8]$", // closed Choice of Integer (1, 2, 3, 4, 5, 6, 7, 8)
            "FileSource", INTEGER, "^3$", // closed Choice of Integer (3)
            "SceneType", INTEGER, "^1$", // closed Choice of Integer (1)
            "CustomRendered", INTEGER, "^[01]$", // closed Choice of Integer (0, 1)
            "ExposureMode", INTEGER, "^[0-2]$", // closed Choice of Integer (0, 1, 2)
            "WhiteBalance", INTEGER, "^[01]$",  // closed Choice of Integer (0, 1)
            "SceneCaptureType", INTEGER, "^[0-3]$", // closed Choice of Integer (0, 1, 2, 3)
            "GainControl", INTEGER, "^[0-4]$", // closed Choice of Integer (0, 1, 2, 3, 4)
            "Contrast", INTEGER, "^[0-2]$", // closed Choice of Integer (0, 1, 2)
            "Saturation", INTEGER, "^[0-2]$", // closed Choice of Integer (0, 1, 2)
            "Sharpness", INTEGER, "^[0-2]$", // closed Choice of Integer (0, 1, 2)
            "SubjectDistanceRange", INTEGER, "^[0-3]$", // closed Choice of Integer (0, 1, 2, 3)
            "GPSAltitudeRef", INTEGER, "^[01]$", // closed Choice of Integer (0, 1)
            "GPSStatus", TEXT, "^[AV]$", // closed Choice of Text (A, V)
            "GPSSpeedRef", TEXT, "^[KMN]$", // closed Choice of Text (K, M, N)
            "GPSTrackRef", TEXT, "^[TM]$", // closed Choice of Text (T, M)
            "GPSImgDirectionRef", TEXT, "^[TM]$", // closed Choice of Text (T, M)
            "GPSDestBearingRef", TEXT, "^[TM]$", // closed Choice of Text (T, M)
            "GPSDestDistanceRef", TEXT, "^[KMN]$", // closed Choice of Text (K, M, N)
            "GPSDifferential", INTEGER, "^[01]$" // closed Choice of Integer (0, 1)
    };

    public static final String[][] EXIF_COMPONENTS_CONFIGURATION_CLOSED_SEQ_CHOICE_COMMON = {
            {"4", "5", "6", "0"}, {"1", "2", "3", "0"}
            // closed Choice of Seq Integer ([4,5,6,0], [1,2,3,0])
    };

    // Properties differ in for PDF/A-1 and PDF/A-2, PDF/A-3
    public static final String[] PDFA_IDENTIFICATION_RESTRICTED_FIELD_DIFFER_1 = {XMPSchemaRegistryImpl.NS_PDFA_ID,
            "conformance", TEXT, "^[AB]$" //closed Choice (A, B)
    };

    public static final String[] PDFA_IDENTIFICATION_RESTRICTED_FIELD_DIFFER_2_3 = {XMPSchemaRegistryImpl.NS_PDFA_ID,
            "conformance", TEXT, "^[AUB]$" //closed Choice (A, U, B)
    };

    public static final String[] PHOTOSHOP_DIFFER_1 = {XMPSchemaRegistryImpl.NS_PHOTOSHOP,
            "SupplementalCategories", TEXT
    };

    public static final String[] PHOTOSHOP_DIFFER_2_3 = {XMPSchemaRegistryImpl.NS_PHOTOSHOP,
            "SupplementalCategories", BAG + " " + TEXT
    };

    public static final String[] EXIF_WITHOUT_RESTRICTED_FIELD_DIFFER_1 = {XMPSchemaRegistryImpl.NS_EXIF,
            "MakerNote", TEXT
    };

    public static final String[] EXIF_RESTRICTED_FIELD_DIFFER_1 = {XMPSchemaRegistryImpl.NS_EXIF,
            "ExifVersion", TEXT, "^0210$",  // closed Choice of Text (0210)
            "FlashpixVersion", TEXT, "^0100$",  // closed Choice of Text (0100)
            "ColorSpace", INTEGER, "^1|-32786$", // closed Choice of Integer (1, -32786)
            "LightSource", INTEGER, "^[0-3]|1[7-9]|2[0-2]|255$", // closed Choice of Integer (0, 1, 2, 3, 17, 18, 19, 20, 21, 22, 255)
            "GPSMeasureMode", INTEGER, "^[23]$" // closed Choice of Integer (2, 3)
    };

    public static final String[] EXIF_WITHOUT_RESTRICTED_FIELD_DIFFER_2_3 = {XMPSchemaRegistryImpl.NS_EXIF,
            "ExifVersion", TEXT,  // closed Choice of Text (EXIF tag 36864, 0x9000. Exif version number.)
            "FlashpixVersion", TEXT,  // closed Choice of Text (EXIF tag 40960, 0xA000. Version of FlashPix.)
            "GPSMeasureMode", TEXT
    };

    public static final String[] EXIF_RESTRICTED_FIELD_DIFFER_2_3 = {XMPSchemaRegistryImpl.NS_EXIF,
            "ColorSpace", INTEGER, "^1|65535$", // closed Choice of Integer (1, 65535)
            "LightSource", INTEGER, "^[0-4]|9|1[0-5]|1[7-9]|2[0-4]|255$"
            // closed Choice of Integer (0, 1, 2, 3, 4, 9, 10, 11, 12, 13, 14, 15, 17, 18, 19, 20, 21, 22, 23, 24, 255)
    };

    // Properties specified for PDF/A-2 and PDF/A-3
    public static final String[] PDFA_IDENTIFICATION_SPECIFIED_2_3 = {XMPSchemaRegistryImpl.NS_PDFA_ID,
            "corr", TEXT
    };

    public static final String[] XMP_BASIC_SPECIFIED_2_3 = {XMPSchemaRegistryImpl.NS_XMP,
            "Label", TEXT,
            "Rating", INTEGER   // closed Choice of Integer (A number that indicates a document’s status relative to
            // Integer other documents, used to organize documents in a file browser.
            // Values are user-defined within an application- defined range.)
    };

    public static final String[] XMP_PAGED_TEXT_SPECIFIED_2_3 = {XMPSchemaRegistryImpl.TYPE_PAGEDFILE,
            "Fonts", BAG + " " + FONT,
            "Colorants", SEQ + " " + COLORANT,
            "PlateNames", SEQ + " " + TEXT
    };

    public static final String[] XMP_DYNAMIC_MEDIA_WITHOUT_RESTRICTED_FIELD_SPECIFIED_2_3 = {XMPSchemaRegistryImpl.NS_DM,
            "projectRef", PROJECT_LINK,
            "videoFrameRate", TEXT, // open Choice of Text
            "videoFrameSize", DIMENSIONS,
            "videoPixelAspectRatio", RATIONAL,
            "videoAlphaUnityIsTransparent", BOOLEAN,
            "videoCompressor", TEXT,
            "audioSampleRate", INTEGER,
            "audioCompressor", TEXT,
            "speakerPlacement", TEXT,
            "fileDataRate", RATIONAL,
            "tapeName", TEXT,
            "altTapeName", TEXT,
            "startTimecode", TIMECODE,
            "altTimecode", TIMECODE,
            "duration", TIME,
            "scene", TEXT,
            "shotName", TEXT,
            "shotDate", DATE,
            "shotLocation", TEXT,
            "logComment", TEXT,
            "markers", SEQ + " " + MARKER,
            "contributedMedia", BAG + " " + MEDIA,
            "absPeakAudioFilePath", URI,
            "relativePeakAudioFilePath", URI,
            "videoModDate", DATE,
            "audioModDate", DATE,
            "metadataModDate", DATE,
            "artist", TEXT,
            "album", TEXT,
            "trackNumber", INTEGER,
            "genre", TEXT,
            "copyright", TEXT,
            "releaseDate", DATE,
            "composer", TEXT,
            "engineer", TEXT,
            "tempo", REAL,
            "instrument", TEXT,
            "introTime", TIME,
            "outCue", TIME,
            "relativeTimestamp", TIME,
            "loop", BOOLEAN,
            "numberOfBeats", REAL,
            "timeScaleParams", TIME_SCALE_STRETCH,
            "resampleParams", RESAMPLE_STRETCH,
            "beatSpliceParams", BEAT_SPLICE_STRETCH
    };

    public static final String[] XMP_DYNAMIC_MEDIA_RESTRICTED_FIELD_SPECIFIED_2_3 = {XMPSchemaRegistryImpl.NS_DM,
            "videoPixelDepth", TEXT, "^(8Int|16Int|32Int|32Float)$", // closed Choice of Text (8Int, 16Int, 32Int, 32Float)
            "videoColorSpace", TEXT, "^(sRGB|CCIR-601|CCIR-709)$", // closed Choice of Text (sRGB, CCIR-601, CCIR-709)
            "videoAlphaMode", TEXT, "^(straight|pre-multiplied)$", // closed Choice of Text (straight, pre-multiplied)
            "videoFieldOrder", TEXT, "^(Upper|Lower|Progressive)$", // closed Choice of Text (Upper, Lower, Progressive)
            "pullDown", TEXT, "^(WSSW|SSWWW|SWWWS|WWWSS|WWSSW)(_24p)?$", // closed Choice of Text (WSSWW, SSWWW, SWWWS, WWWSS, WWSSW, WSSWW_24p, SSWWW_24p, SWWWS_24p, WWWSS_24p, WWSSW_24p)
            "audioSampleType", TEXT, "^(8Int|16Int|32Int|32Float)$", // closed Choice of Text (8Int, 16Int, 32Int, 32Float)
            "audioChannelType", TEXT, "^(Mono|Stereo|5\\.1|7\\.1)$", // closed Choice of Text (Mono, Stereo, 5.1, 7.1)
            "key", TEXT, "^([ACDFG]#?|[BE])$", // closed Choice of Text (C, C#, D, D#, E, F, F#, G, G#, A, A#, B)
            "stretchMode", TEXT, "^(Fixed length|Time-Scale|Resample|Beat Splice|Hybrid)$",
            // closed Choice of Text (Fixed length, Time-Scale, Resample, Beat Splice, Hybrid)
            "timeSignature", TEXT, "^([2-57]/4|[69]/8|12/8|other)$", //
            // closed Choice of Text (2/4, 3/4, 4/4, 5/4, 7/4, 6/8, 9/8, 12/8, other)
            "scaleType", TEXT, "^(Major|Minor|Both|Neither)$" // closed Choice of Text (Major, Minor, Both, Neither)
    };

    public static final String[] CAMERA_RAW_WITHOUT_RESTRICTED_FIELD_SPECIFIED_2_3 = {XMPSchemaRegistryImpl.NS_CAMERARAW,
            "AutoBrightness", BOOLEAN,
            "AutoContrast", BOOLEAN,
            "AutoExposure", BOOLEAN,
            "AutoShadows", BOOLEAN,
            "BlueHue", INTEGER,
            "BlueSaturation", INTEGER,
            "Brightness", INTEGER,
            "CameraProfile", TEXT,
            "ChromaticAberrationB", INTEGER,
            "ChromaticAberrationR", INTEGER,
            "ColorNoiseReduction", INTEGER,
            "Contrast", INTEGER,
            "CropTop", REAL,
            "CropLeft", REAL,
            "CropBottom", REAL,
            "CropRight", REAL,
            "CropAngle", REAL,
            "CropWidth", REAL,
            "CropHeight", REAL,
            "CropUnits", INTEGER,
            "Exposure", REAL,
            "GreenHue", INTEGER,
            "GreenSaturation", INTEGER,
            "HasCrop", BOOLEAN,
            "HasSettings", BOOLEAN,
            "LuminanceSmoothing", INTEGER,
            "RawFileName", TEXT,
            "RedHue", INTEGER,
            "RedSaturation", INTEGER,
            "Saturation", INTEGER,
            "Shadows", INTEGER,
            "ShadowTint", INTEGER,
            "Sharpness", INTEGER,
            "Temperature", INTEGER,
            "Tint", INTEGER,
            "ToneCurveName", TEXT, // Choice Text
            "Version", TEXT,
            "VignetteAmount", INTEGER,
            "VignetteMidpoint", INTEGER
    };

    public static final String[] CAMERA_RAW_RESTRICTED_FIELD_SPECIFIED_2_3 = {XMPSchemaRegistryImpl.NS_CAMERARAW,
            "WhiteBalance", TEXT, "^(As Shot|Auto|Daylight|Cloudy|Shade|Tungsten|Fluorescent|Flash|Custom)$"
            // closed Choice of Text (As Shot, Auto, Daylight, Cloudy, Shade, Tungsten, Fluorescent, Flash, Custom)
    };

    public static final String[] CAMERA_RAW_SEQ_OF_POINTS_SPECIFIED_2_3 = {XMPSchemaRegistryImpl.NS_CAMERARAW,
            "ToneCurve", TEXT, "^\\([+-]?\\d+, [+-]?\\d+\\)$" // Seq of points (Integer, Integer)
    };

    public static final String[] AUX_SPECIFIED_2_3 = {XMPSchemaRegistryImpl.NS_EXIF_AUX,
            "Lens", TEXT,
            "SerialNumber", TEXT
    };
}