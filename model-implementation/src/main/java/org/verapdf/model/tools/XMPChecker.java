package org.verapdf.model.tools;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.util.DateConverter;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class matches document information dictionary and xmp metadata by
 * comparing eight predefined fields:
 * <ol>
 * <li>Title (-> Title)</li>
 * <li>Author (-> Creators)</li>
 * <li>Producer (-> Producer)</li>
 * <li>Creator (-> CreatorTool)</li>
 * <li>Keywords (-> Keywords)</li>
 * <li>Subject (-> Description)</li>
 * <li>Creation Date (-> Create Date)</li>
 * <li>Mod Date (-> Modify Date)</li>
 * </ol>
 * Property shouldn't be defined in xmp metadata if not present in document
 * information dictionary.
 *
 * @author Evgeniy Muravitskiy
 */
public final class XMPChecker {

    private static final Logger LOGGER = Logger.getLogger(XMPChecker.class);

    private static final String TITLE = "Title";
    private static final String SUBJECT = "Subject";
    private static final String AUTHOR = "Author";
    private static final String KEYWORDS = "Keywords";
    private static final String PRODUCER = "Producer";
    private static final String CREATOR = "Creator";
    private static final String CREATION_DATE = "CreationDate";
    private static final String MODIFICATION_DATE = "ModDate";
    private static final int MAX_REQUIRED_RECORDS = 8;

    private XMPChecker() {
        // disable default constructor
    }

    /**
     * Matches properties of document information dictionary and xmp metadata.
     *
     * @param document which will be tested
     * @return true if fields of xmp matches with fields of info dictionary
     */
    public static Boolean doesInfoMatchXMP(COSDocument document) {
        COSDictionary info = getInformationDictionary(document);
        if (info == null) {
            return Boolean.TRUE;
        }

        try {
            COSStream meta = getMetadataDictionary(document);
            if (meta != null) {
                VeraPDFMeta metadata = VeraPDFMeta.parse(meta.getUnfilteredStream());

                Map<String, Object> properties = new HashMap<>(
                        MAX_REQUIRED_RECORDS);

                getTitleAuthorSubject(metadata, properties);

                getProducerKeywords(metadata, properties);

                getCreatorAndDates(metadata, properties);

                return checkMatch(info, properties);
            }
        } catch (IOException e) {
            LOGGER.debug(
                    "Problems with document parsing or structure. "
                            + e.getMessage(), e);
        } catch (XMPException e) {
            LOGGER.debug("Problems with XMP parsing. " + e.getMessage(), e);
        }

        return Boolean.FALSE;
    }

    private static COSStream getMetadataDictionary(COSDocument document)
            throws IOException {
        final COSDictionary catalog = (COSDictionary) document.getCatalog()
                .getObject();
        final COSObject metaObj = (COSObject) catalog.getItem(COSName.METADATA);
        if (metaObj != null && metaObj.getObject() instanceof COSStream) {
            return (COSStream) metaObj.getObject();
        }
        return null;
    }

    private static COSDictionary getInformationDictionary(COSDocument document) {
        final COSObject info = (COSObject) document.getTrailer().getItem(
                COSName.INFO);
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

    private static void getTitleAuthorSubject(VeraPDFMeta metadata,
                                              Map<String, Object> properties) throws XMPException {

        putProperty(properties, TITLE, metadata.getTitle());
        putProperty(properties, SUBJECT, metadata.getDescription());
        final List<String> buffer = metadata.getCreator();
        if (buffer != null) {
            putProperty(properties, AUTHOR, buffer);
        }
    }

    private static void getProducerKeywords(VeraPDFMeta metadata,
                                            Map<String, Object> properties) throws XMPException {
        putProperty(properties, KEYWORDS, metadata.getKeywords());
        putProperty(properties, PRODUCER, metadata.getProducer());
    }

    private static void getCreatorAndDates(VeraPDFMeta metadata,
                                           Map<String, Object> properties) throws XMPException {
        putProperty(properties, CREATOR, metadata.getCreatorTool());
        putProperty(properties, CREATION_DATE, metadata.getCreateDate());
        putProperty(properties, MODIFICATION_DATE, metadata.getModifyDate());
    }

    private static void putProperty(Map<String, Object> properties, String key,
                                    Object value) {
        if (value != null) {
            properties.put(key, value);
        }
    }

    private static Boolean checkMatch(COSDictionary info,
                                      Map<String, Object> properties) {
        boolean isDublinCoreMatch = checkProperty(info, properties, TITLE)
                && checkProperty(info, properties, SUBJECT)
                && checkProperty(info, properties, AUTHOR);
        if (!isDublinCoreMatch) {
            return Boolean.FALSE;
        }
        boolean isAdobePDFMatch = checkProperty(info, properties, KEYWORDS)
                && checkProperty(info, properties, PRODUCER);
        if (!isAdobePDFMatch) {
            return Boolean.FALSE;
        }
        boolean isXMPBasicMatch = checkProperty(info, properties, CREATOR)
                && checkProperty(info, properties, CREATION_DATE)
                && checkProperty(info, properties, MODIFICATION_DATE);
        return Boolean.valueOf(isXMPBasicMatch);
    }

    private static Boolean checkProperty(COSDictionary info,
                                         Map<String, Object> properties, String checksRule) {
        final COSBase item = info.getItem(checksRule);
        if (item == null || item instanceof COSNull) {
            return Boolean.TRUE;
        } else {
            if (item instanceof COSString) {
                return checkCOSStringProperty((COSString) item, properties,
                        checksRule);
            } else if (item instanceof COSObject) {
                return deepPropertyCheck((COSObject) item, properties,
                        checksRule);
            }
        }
        return Boolean.FALSE;
    }

    private static Boolean deepPropertyCheck(COSObject item,
                                             Map<String, Object> properties, String checksRule) {
        final COSBase external = item.getObject();
        if (external == null || external instanceof COSNull) {
            return Boolean.TRUE;
        } else if (external instanceof COSString) {
            return checkCOSStringProperty((COSString) external, properties,
                    checksRule);
        } else if (external instanceof COSObject) {
            return deepPropertyCheck((COSObject) external, properties,
                    checksRule);
        } else {
            return Boolean.FALSE;
        }
    }

    private static Boolean checkCOSStringProperty(COSString string,
                                                  Map<String, Object> properties, String checksRule) {
        final Object value = properties.get(checksRule);
        if (value != null) {
            if (value instanceof String) {
                return Boolean.valueOf(value.equals(string.getString()));
            } else if (value instanceof List) {
                List list = (List) value;
                return Boolean.valueOf(list.size() == 1 && list.get(0).equals(string.getString()));
            } else if (value instanceof Calendar) {
                // DateConverter can parse as pdf date format as simple date
                // format
                final String regex = "(D:)?(\\d\\d){2,7}((([+-](\\d\\d[']))(\\d\\d['])?)?|[Z])";
                String ascii = string.getASCII();
                if (ascii.matches(regex)) {
                    final Calendar valueDate = getCalendar(ascii);
                    return Boolean.valueOf(valueDate != null
                            && valueDate.compareTo((Calendar) value) == 0);
                } else {
                    LOGGER.warn("Date format in info dictionary is not complies pdf date format");
                }
            }
        }
        return Boolean.FALSE;
    }

    private static Calendar getCalendar(String date) {
        Matcher matcher = Pattern
                .compile("((([+-](\\d\\d[']))(\\d\\d['])?)|[Z])").matcher(date);
        return DateConverter.toCalendar(date, matcher.find());
    }
}
