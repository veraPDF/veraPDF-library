package org.verapdf.model.tools.xmp;

import com.adobe.xmp.impl.XMPSchemaRegistryImpl;

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

    // exception check types
    public static final String DATE = "Date";
    public static final String URI = "URI";
    public static final String URL = "URL";
    public static final String XPATH = "XPath";
    public static final String CHOICE = "Choice";

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
    public static final String[] COLORANT_WITHOUT_RESTRICTED_FIELD_STRUCTURE = {XMPSchemaRegistryImpl.TYPE_GRAPHICS,
            "swatchName", TEXT
    };

    public static final String[] COLORANT_RESTRICTED_FIELD_STRUCTURE = {
            "mode", "^(CMYK|RGB|LAB)$", // closed Choice (CMYK, RGB, LAB)
            "type", "^(PROCESS|SPOT)$", // closed Choice (PROCESS, SPOT)
            "cyan", "^[+]?(\\d{1,2}(\\.\\d*)?|\\d{0,2}\\.\\d+|100(\\.0*)?)$", // Real. Range: 0-100
            "magenta", "^[+]?(\\d{1,2}(\\.\\d*)?|\\d{0,2}\\.\\d+|100(\\.0*)?)$", // Real. Range: 0-100
            "yellow", "^[+]?(\\d{1,2}(\\.\\d*)?|\\d{0,2}\\.\\d+|100(\\.0*)?)$", // Real. Range: 0-100
            "black", "^[+]?(\\d{1,2}(\\.\\d*)?|\\d{0,2}\\.\\d+|100(\\.0*)?)$", // Real. Range: 0-100
            "red", "^[+]?([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])$", // Integer. Range: 0-255
            "green", "^[+]?([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])$", // Integer. Range: 0-255
            "blue", "^[+]?([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])$", // Integer. Range: 0-255
            "L", "^[+]?(\\d{1,2}(\\.\\d*)?|\\d{0,2}\\.\\d+|100(\\.0*)?)$", // Real. Range: 0-100
            "A", "^([+-]?[0]?[0-9]{1,2}|[+-]?1[01][0-9]|[+-]?12[0-7]|-128)$", // Integer. Range: -128-127
            "B", "^([+-]?[0]?[0-9]{1,2}|[+-]?1[01][0-9]|[+-]?12[0-7]|-128)$" // Integer. Range: -128-127
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
            "format", "^JPEG$" // closed Choice (JPEG)
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
            "type", "^(Chapter|Cue|Beat|Track|Index)$" // closed Choice of Text (Chapter, Cue, Beat, Track, Index)
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
            "type", "^(movie|still|audio|custom)$" // closed Choice of Text (movie, still, audio, custom)
    };

    public static final String[] RESAMPLE_STRETCH_WITHOUT_RESTRICTED_FIELD_STRUCTURE = {XMPSchemaRegistryImpl.NS_DM
    };

    public static final String[] RESAMPLE_STRETCH_RESTRICTED_FIELD_STRUCTURE = {
            "quality", "^(High|Medium|Low)$" // closed Choice of Text (High, Medium, Low)
    };

    public static final String[] TIME_STRUCTURE = {XMPSchemaRegistryImpl.NS_DM,
            "value", INTEGER,
            "scale", RATIONAL
    };

    public static final String[] TIMECODE_WITHOUT_RESTRICTED_FIELD_STRUCTURE = {XMPSchemaRegistryImpl.NS_DM
    };

    public static final String[] TIMECODE_RESTRICTED_FIELD_STRUCTURE = {
            "timeValue", "^\\d{2}((:\\d{2}){3}|(;\\d{2}){3})$", // A time value in the specified format: hh:mm:ss:ff or hh;mm;ss;ff
            "timeFormat", "^(24|25|2997Drop|2997NonDrop|30|50|5994Drop|5994NonDrop|60|23976)(Timecode)$"
            // closed Choice of Text (24Timecode, 25Timecode, 2997DropTimecode, 2997NonDropTimecode, 30Timecode,
            // 50Timecode, 5994DropTimecode, 5994NonDropTimecode, 60Timecode, 23976Timecode)
    };

    public static final String[] TIME_SCALE_STRETCH_WITHOUT_RESTRICTED_FIELD_STRUCTURE = {XMPSchemaRegistryImpl.NS_DM,
            "frameSize", REAL,
            "frameOverlappingPercentage", REAL
    };

    public static final String[] TIME_SCALE_STRETCH_RESTRICTED_FIELD_STRUCTURE = {
            "quality", "^(High|Medium|Low)$" // closed Choice of Text (High, Medium, Low)
    };

    public static final String[] FLASH_WITHOUT_RESTRICTED_FIELD_STRUCTURE = {XMPSchemaRegistryImpl.NS_EXIF,
            "Fired", BOOLEAN,
            "Function", BOOLEAN,
            "RedEyeMode", BOOLEAN
    };

    public static final String[] FLASH_RESTRICTED_FIELD_STRUCTURE = {
            "Return", "^[023]$", // closed Choice (0, 2, 3)
            "Mode", "^[0-3]$" // closed Choice (0, 1, 2, 3)
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
            "Advisory", BAG + " " + XPATH,
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
            "Compression", "^[16]$", // closed Choice of Integer (1, 6)
            "PhotometricInterpretation", "^[26]$", // closed Choice of Integer (2, 6)
            "Orientation", "^[1-8]$", // closed Choice of Integer (1, 2, 3, 4, 5, 6, 7, 8)
            "PlanarConfiguration", "^[12]$", // closed Choice of Integer (1, 2)
            "YCbCrPositioning", "^[12]$", // closed Choice of Integer (1, 2)
            "ResolutionUnit", "^[23]$" // closed Choice of Integer (2, 3)
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
            "ExposureProgram", "^[0-8]$", // closed Choice of Integer (0, 1, 2, 3, 4, 5, 6, 7, 8)
            "MeteringMode", "^([0-6]|255)$", // closed Choice of Integer (0, 1, 2, 3, 4, 5, 6, 255)
            "FocalPlaneResolutionUnit", "^[23]$", // closed Choice of Integer (2, 3)
            "SensingMethod", "^[1-8]$", // closed Choice of Integer (1, 2, 3, 4, 5, 6, 7, 8)
            "FileSource", "^3$", // closed Choice of Integer (3)
            "SceneType", "^1$", // closed Choice of Integer (1)
            "CustomRendered", "^[01]$", // closed Choice of Integer (0, 1)
            "ExposureMode", "^[0-2]$", // closed Choice of Integer (0, 1, 2)
            "WhiteBalance", "^[01]$",  // closed Choice of Integer (0, 1)
            "SceneCaptureType", "^[0-3]$", // closed Choice of Integer (0, 1, 2, 3)
            "GainControl", "^[0-4]$", // closed Choice of Integer (0, 1, 2, 3, 4)
            "Contrast", "^[0-2]$", // closed Choice of Integer (0, 1, 2)
            "Saturation", "^[0-2]$", // closed Choice of Integer (0, 1, 2)
            "Sharpness", "^[0-2]$", // closed Choice of Integer (0, 1, 2)
            "SubjectDistanceRange", "^[0-3]$", // closed Choice of Integer (0, 1, 2, 3)
            "GPSAltitudeRef", "^[01]$", // closed Choice of Integer (0, 1)
            "GPSStatus", "^[AV]$", // closed Choice of Text (A, V)
            "GPSSpeedRef", "^[KMN]$", // closed Choice of Text (K, M, N)
            "GPSTrackRef", "^[TM]$", // closed Choice of Text (T, M)
            "GPSImgDirectionRef", "^[TM]$", // closed Choice of Text (T, M)
            "GPSDestBearingRef", "^[TM]$", // closed Choice of Text (T, M)
            "GPSDestDistanceRef", "^[KMN]$", // closed Choice of Text (K, M, N)
            "GPSDifferential", "^[01]$" // closed Choice of Integer (0, 1)
    };

    public static final String[][] EXIF_COMPONENTS_CONFIGURATION_CLOSED_SEQ_CHOICE_COMMON = {
            {"4", "5", "6", "0"}, {"1", "2", "3", "0"}
            // closed Choice of Seq Integer ([4,5,6,0], [1,2,3,0])
    };

    // Properties differ in for PDF/A-1 and PDF/A-2, PDF/A-3
    public static final String[] PDFA_IDENTIFICATION_RESTRICTED_FIELD_DIFFER_1 = {XMPSchemaRegistryImpl.NS_PDFA_ID,
            "conformance", "^[AB]$" //closed Choice (A, B)
    };

    public static final String[] PDFA_IDENTIFICATION_RESTRICTED_FIELD_DIFFER_2_3 = {XMPSchemaRegistryImpl.NS_PDFA_ID,
            "conformance", "^[AUB]$" //closed Choice (A, U, B)
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
            "ExifVersion", "^0210$",  // closed Choice of Text (0210)
            "FlashpixVersion", "^0100$",  // closed Choice of Text (0100)
            "ColorSpace", "^1|-32786$", // closed Choice of Integer (1, -32786)
            "LightSource", "^[0-3]|1[7-9]|2[0-2]|255$", // closed Choice of Integer (0, 1, 2, 3, 17, 18, 19, 20, 21, 22, 255)
            "GPSMeasureMode", "^[23]$" // closed Choice of Integer (2, 3)
    };

    public static final String[] EXIF_WITHOUT_RESTRICTED_FIELD_DIFFER_2_3 = {XMPSchemaRegistryImpl.NS_EXIF,
            "ExifVersion", TEXT,  // closed Choice of Text (EXIF tag 36864, 0x9000. Exif version number.)
            "FlashpixVersion", TEXT,  // closed Choice of Text (EXIF tag 40960, 0xA000. Version of FlashPix.)
            "GPSMeasureMode", TEXT
    };

    public static final String[] EXIF_RESTRICTED_FIELD_DIFFER_2_3 = {XMPSchemaRegistryImpl.NS_EXIF,
            "ColorSpace", "^1|65535$", // closed Choice of Integer (1, 65535)
            "LightSource", "^[0-4]|9|1[0-5]|1[7-9]|2[0-4]|255$"
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
            "videoPixelDepth", "^(8Int|16Int|32Int|32Float)$", // closed Choice of Text (8Int, 16Int, 32Int, 32Float)
            "videoColorSpace", "^(sRGB|CCIR-601|CCIR-709)$", // closed Choice of Text (sRGB, CCIR-601, CCIR-709)
            "videoAlphaMode", "^(straight|pre-multiplied)$", // closed Choice of Text (straight, pre-multiplied)
            "videoFieldOrder", "^(Upper|Lower|Progressive)$", // closed Choice of Text (Upper, Lower, Progressive)
            "pullDown", "^(WSSW|SSWWW|SWWWS|WWWSS|WWSSW)(_24p)?$", // closed Choice of Text (WSSWW, SSWWW, SWWWS, WWWSS, WWSSW, WSSWW_24p, SSWWW_24p, SWWWS_24p, WWWSS_24p, WWSSW_24p)
            "audioSampleType", "^(8Int|16Int|32Int|32Float)$", // closed Choice of Text (8Int, 16Int, 32Int, 32Float)
            "audioChannelType", "^(Mono|Stereo|5\\.1|7\\.1)$", // closed Choice of Text (Mono, Stereo, 5.1, 7.1)
            "key", "^([ACDFG]#?|[BE])$", // closed Choice of Text (C, C#, D, D#, E, F, F#, G, G#, A, A#, B)
            "stretchMode", "^(Fixed length|Time-Scale|Resample|Beat Splice|Hybrid)$",
            // closed Choice of Text (Fixed length, Time-Scale, Resample, Beat Splice, Hybrid)
            "timeSignature", "^([2-57]/4|[69]/8|12/8|other)$", //
            // closed Choice of Text (2/4, 3/4, 4/4, 5/4, 7/4, 6/8, 9/8, 12/8, other)
            "scaleType", "^(Major|Minor|Both|Neither)$" // closed Choice of Text (Major, Minor, Both, Neither)
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
            "WhiteBalance", "^(As Shot|Auto|Daylight|Cloudy|Shade|Tungsten|Fluorescent|Flash|Custom)$"
            // closed Choice of Text (As Shot, Auto, Daylight, Cloudy, Shade, Tungsten, Fluorescent, Flash, Custom)
    };

    public static final String[] CAMERA_RAW_SEQ_OF_POINTS_SPECIFIED_2_3 = {XMPSchemaRegistryImpl.NS_CAMERARAW,
            "ToneCurve", "^\\([+-]?\\d+, [+-]?\\d+\\)$" // Seq of points (Integer, Integer)
    };

    public static final String[] AUX_SPECIFIED_2_3 = {XMPSchemaRegistryImpl.NS_EXIF_AUX,
            "Lens", TEXT,
            "SerialNumber", TEXT
    };
}