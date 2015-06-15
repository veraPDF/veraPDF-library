package org.verapdf.model.tools;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.util.DateConverter;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.AdobePDFSchema;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.XMPBasicSchema;
import org.apache.xmpbox.xml.DomXmpParser;
import org.apache.xmpbox.xml.XmpParsingException;

import java.io.IOException;
import java.util.*;

/**
 * @author Evgeniy Muravitskiy
 */
public class XMPChecker {

    private final static Logger logger = Logger.getLogger(XMPChecker.class);

    private final static List<String> URLS = new ArrayList<>(4);

    private static final String TITLE = "Title";
    private static final String SUBJECT = "Subject";
    private static final String AUTHOR = "Author";
    private static final String KEYWORDS = "Keywords";
    private static final String PRODUCER = "Producer";
    private static final String CREATOR = "Creator";
    private static final String CREATION_DATE = "CreationDate";
    private static final String MODIFICATION_DATE = "ModDate";

    static {
        URLS.add("http://purl.org/dc/elements/1.1/");
        URLS.add("http://ns.adobe.com/xap/1.0/");
        URLS.add("http://ns.adobe.com/pdf/1.3/");
        URLS.add("http://www.aiim.org/pdfa/ns/id/");
    }

    public static Boolean doesInfoMatchXMP(COSDocument document) {
        COSDictionary info = getInformationDictionary(document);
        if (info == null) {
            return Boolean.TRUE;
        }

        try {
            COSStream meta = getMetadataDictionary(document);
            if (meta != null) {
                DomXmpParser xmpParser = new DomXmpParser();
                XMPMetadata metadata = xmpParser.parse(meta.getUnfilteredStream());

                Map<String, Object> properties = new HashMap<>(8);

                getTitleAuthorSubject(metadata, properties);

                getProducerKeywords(metadata, properties);

                getCreatorAndDates(metadata, properties);

                return checkMatch(info, properties);
            }
        } catch (IOException e) {
            logger.error("Problems with document parsing or structure. " + e.getMessage());
        } catch (XmpParsingException e) {
            logger.error("Problems with XMP parsing. " + e.getMessage());
        }

        return Boolean.FALSE;
    }

    private static COSStream getMetadataDictionary(COSDocument document) throws IOException {
        final COSDictionary catalog = (COSDictionary) document.getCatalog().getObject();
        final COSObject metaObj = (COSObject) catalog.getItem(COSName.METADATA);
        if (metaObj != null && metaObj.getObject() instanceof COSStream) {
            return (COSStream) metaObj.getObject();
        } else {
            return null;
        }
    }

    private static COSDictionary getInformationDictionary(COSDocument document) {
        final COSObject info = (COSObject) document.getTrailer().getItem(COSName.INFO);
        if (info != null) {
            if (info.getObject() instanceof COSDictionary) {
                return (COSDictionary) info.getObject();
            } else if (info.getObject() instanceof COSObject) {
                return getInformationDictionary((COSObject) info.getObject());
            }
        }
        return null;
    }

    private static COSDictionary getInformationDictionary(COSObject object) {
        if (object.getObject() instanceof COSDictionary) {
            return (COSDictionary) object.getObject();
        } else if (object.getObject() instanceof COSObject) {
            return getInformationDictionary((COSObject) object.getObject());
        } else {
            return null;
        }
    }

    private static void getTitleAuthorSubject(XMPMetadata metadata, Map<String, Object> properties) {
        DublinCoreSchema dc = metadata.getDublinCoreSchema();
        if (dc != null && URLS.contains(dc.getNamespace())) {
            final List<String> buffer = dc.getCreators();
            putProperty(properties, TITLE, dc.getTitle());
            putProperty(properties, SUBJECT, dc.getDescription());
            putProperty(properties, AUTHOR, buffer.toArray(new String[buffer.size()]));
        }
    }

    private static void getProducerKeywords(XMPMetadata metadata, Map<String, Object> properties) {
        AdobePDFSchema pdf = metadata.getAdobePDFSchema();
        if (pdf != null && URLS.contains(pdf.getNamespace())) {
            putProperty(properties, KEYWORDS, pdf.getKeywords());
            putProperty(properties, PRODUCER, pdf.getProducer());
        }
    }

    private static void getCreatorAndDates(XMPMetadata metadata, Map<String, Object> properties) {
        XMPBasicSchema basic = metadata.getXMPBasicSchema();
        if (basic != null && URLS.contains(basic.getNamespace())) {
            putProperty(properties, CREATOR, basic.getCreatorTool());
            putProperty(properties, CREATION_DATE, basic.getCreateDate());
            putProperty(properties, MODIFICATION_DATE, basic.getModifyDate());
        }
    }

    private static void putProperty(Map<String, Object> properties, String key, String... values) {
        if (values != null) {
            StringBuilder builder = new StringBuilder();
            for (String value : values) {
                if (value != null) {
                    builder.append(value).append(" ");
                }
            }
            if (builder.length() > 1) {
                //need to discard last space
                properties.put(key, builder.substring(0, builder.length() - 1));
            }
        }
    }

    private static void putProperty(Map<String, Object> properties, String key, Calendar date) {
        if (date != null) {
            properties.put(key, date);
        }
    }

    private static Boolean checkMatch(COSDictionary info, Map<String, Object> properties) {
        if (!checkProperty(info, properties, TITLE)) return Boolean.FALSE;
        if (!checkProperty(info, properties, SUBJECT)) return Boolean.FALSE;
        if (!checkProperty(info, properties, AUTHOR)) return Boolean.FALSE;
        if (!checkProperty(info, properties, KEYWORDS)) return Boolean.FALSE;
        if (!checkProperty(info, properties, PRODUCER)) return Boolean.FALSE;
        if (!checkProperty(info, properties, CREATOR)) return Boolean.FALSE;
        if (!checkProperty(info, properties, CREATION_DATE)) return Boolean.FALSE;
        if (!checkProperty(info, properties, MODIFICATION_DATE)) return Boolean.FALSE;

        return Boolean.TRUE;
    }

    private static Boolean checkProperty(COSDictionary info, Map<String, Object> properties, String checksRule) {
        final COSBase item = info.getItem(checksRule);
        if (item != null) {
            if (item instanceof COSString) {
                return checkCOSStringProperty((COSString) item, properties, checksRule);
            } else if (item instanceof COSObject) {
                return deepPropertyCheck((COSObject) item, properties, checksRule);
            }
        } else return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private static Boolean deepPropertyCheck(COSObject item, Map<String, Object> properties, String checksRule) {
        final COSBase external = item.getObject();
        if (external == null || external instanceof COSNull) {
            return Boolean.TRUE;
        } else if (external instanceof COSString) {
            return checkCOSStringProperty((COSString) external, properties, checksRule);
        } else if (external instanceof COSObject) {
            return deepPropertyCheck((COSObject) external, properties, checksRule);
        } else {
            return Boolean.FALSE;
        }
    }

    private static Boolean checkCOSStringProperty(COSString string, Map<String, Object> properties, String checksRule) {
        final Object value = properties.get(checksRule);
        if (value != null) {
            if (value instanceof String) {
                return Boolean.valueOf(value.equals(string.getASCII()));
            } else if (value instanceof Calendar) {
                //DateConverter can parse as pdf date format as simple date format
                if (string.getASCII().matches("\\s*(D:)?(\\d\\d){2,7}([+-](\\d\\d['`]){0,2})")) {
                    final Calendar valueDate = DateConverter.toCalendar(string);
                    return Boolean.valueOf(valueDate != null && valueDate.compareTo((Calendar) value) == 0);
                }
            }
        }
        return Boolean.FALSE;
    }
}
