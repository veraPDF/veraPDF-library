package org.verapdf.features.pb.objects;

import org.apache.log4j.Logger;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

/**
 * Feature object for icc profile
 *
 * @author Maksim Bezrukov
 */
public class PBICCProfileFeaturesObject implements IFeaturesObject {

    private static final Logger LOGGER = Logger
            .getLogger(PBICCProfileFeaturesObject.class);

    private static final String ID = "id";
    private static final int HEADER_SIZE = 128;
    private static final int FF_FLAG = 0xFF;
    private static final int F_FLAG = 0x0F;
    private static final int REQUIRED_LENGTH = 4;
    private static final int TAGINFO_LENGTH = 12;
    private static final int BITSINBYTE = 8;
    private static final int VERSION_BYTE = 8;
    private static final int SUBVERSION_BYTE = 9;
    private static final int CMMTYPE_BEGIN = 4;
    private static final int CMMTYPE_END = 8;
    private static final int DATACOLORSPACE_BEGIN = 16;
    private static final int DATACOLORSPACE_END = 20;
    private static final int RENDERINGINTENT_BEGIN = 64;
    private static final int RENDERINGINTENT_END = 68;
    private static final int PROFILEID_BEGIN = 84;
    private static final int PROFILEID_END = 100;
    private static final int DEVICEMODEL_BEGIN = 52;
    private static final int DEVICEMODEL_END = 56;
    private static final int DEVICEMANUFACTURER_BEGIN = 48;
    private static final int DEVICEMANUFACTURER_END = 52;
    private static final int CREATOR_BEGIN = 80;
    private static final int CREATOR_END = 84;
    private static final int CREATION_YEAR = 24;
    private static final int CREATION_MONTH = 26;
    private static final int CREATION_DAY = 28;
    private static final int CREATION_HOUR = 30;
    private static final int CREATION_MIN = 32;
    private static final int CREATION_SEC = 34;

    private InputStream profile;
    private String id;
    private Set<String> outInts;
    private Set<String> iccBaseds;

    /**
     * Constructs new icc profile feature object
     *
     * @param profile   input stream which represents the icc profile for feature report
     * @param id        - id of the profile
     * @param outInts   - set of ids of all parent output intents for this icc profile
     * @param iccBaseds - set of ids of all parent icc based color spaces for this icc profile
     */
    public PBICCProfileFeaturesObject(InputStream profile, String id, Set<String> outInts, Set<String> iccBaseds) {
        this.profile = profile;
        this.id = id;
        this.outInts = outInts;
        this.iccBaseds = iccBaseds;
    }

    /**
     * @return ICCPROFILE instance of the FeaturesObjectTypesEnum enumeration
     */
    @Override
    public FeaturesObjectTypesEnum getType() {
        return FeaturesObjectTypesEnum.ICCPROFILE;
    }

    /**
     * Reports all features from the object into the collection
     *
     * @param collection - collection for feature report
     * @return FeatureTreeNode class which represents a root node of the constructed collection tree
     * @throws FeaturesTreeNodeException - occurs when wrong features tree node constructs
     */
    @Override
    public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException {

        if (profile != null) {
            FeatureTreeNode root = FeatureTreeNode.newRootInstance("iccProfile");

            if (id != null) {
                root.addAttribute(ID, id);
            }

            addParents(root);

            parseProfileHeader(root, collection);

            collection.addNewFeatureTree(FeaturesObjectTypesEnum.ICCPROFILE, root);
            return root;
        }
        return null;
    }

    private void addParents(FeatureTreeNode root) throws FeaturesTreeNodeException {
        if ((outInts != null && !outInts.isEmpty()) || (iccBaseds != null && !iccBaseds.isEmpty())) {
            FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);

            if (outInts != null) {
                for (String outInt : outInts) {
                    if (outInt != null) {
                        FeatureTreeNode pageNode = FeatureTreeNode.newChildInstance("outputIntent", parents);
                        pageNode.addAttribute(ID, outInt);
                    }
                }
            }

            if (iccBaseds != null) {
                for (String iccBased : iccBaseds) {
                    if (iccBased != null) {
                        FeatureTreeNode pageNode = FeatureTreeNode.newChildInstance("iccBased", parents);
                        pageNode.addAttribute(ID, iccBased);
                    }
                }
            }
        }
    }

    private void parseProfileHeader(FeatureTreeNode root, FeaturesCollection collection) throws FeaturesTreeNodeException {
        try {
            byte[] profileBytes = inputStreamToByteArray(profile);

            if (profileBytes.length < HEADER_SIZE) {
                root.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.GETINGICCPROFILEHEADERSIZEERROR_ID);
                ErrorsHelper.addErrorIntoCollection(collection,
                        ErrorsHelper.GETINGICCPROFILEHEADERSIZEERROR_ID,
                        ErrorsHelper.GETINGICCPROFILEHEADERSIZEERROR_MESSAGE);
            } else {
                PBCreateNodeHelper.addNotEmptyNode("version", getVersion(profileBytes), root);
                PBCreateNodeHelper.addNotEmptyNode("cmmType", getStringFromHeader(profileBytes, CMMTYPE_BEGIN, CMMTYPE_END), root);
                PBCreateNodeHelper.addNotEmptyNode("dataColorSpace", getStringFromHeader(profileBytes, DATACOLORSPACE_BEGIN, DATACOLORSPACE_END), root);
                PBCreateNodeHelper.addNotEmptyNode("creator", getStringFromHeader(profileBytes, CREATOR_BEGIN, CREATOR_END), root);
                PBCreateNodeHelper.createDateNode("creationDate", root, getCreationDate(profileBytes), collection);
                PBCreateNodeHelper.addNotEmptyNode("defaultRenderingIntent", getStringFromHeader(profileBytes, RENDERINGINTENT_BEGIN, RENDERINGINTENT_END), root);
                PBCreateNodeHelper.addNotEmptyNode("copyright", getStringTag(profileBytes, "cprt"), root);
                PBCreateNodeHelper.addNotEmptyNode("description", getStringTag(profileBytes, "desc"), root);
                PBCreateNodeHelper.addNotEmptyNode("profileId", getStringFromHeader(profileBytes, PROFILEID_BEGIN, PROFILEID_END), root);
                PBCreateNodeHelper.addNotEmptyNode("deviceModel", getStringFromHeader(profileBytes, DEVICEMODEL_BEGIN, DEVICEMODEL_END), root);
                PBCreateNodeHelper.addNotEmptyNode("deviceManufacturer", getStringFromHeader(profileBytes, DEVICEMANUFACTURER_BEGIN, DEVICEMANUFACTURER_END), root);
            }

        } catch (IOException e) {
            LOGGER.debug("Reading byte array from InputStream error", e);
            root.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.GETINGICCPROFILEHEADERERROR_ID);
            ErrorsHelper.addErrorIntoCollection(collection,
                    ErrorsHelper.GETINGICCPROFILEHEADERERROR_ID,
                    ErrorsHelper.GETINGICCPROFILEHEADERERROR_MESSAGE);
        }
    }

    private static byte[] inputStreamToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int reads = is.read();
        while (reads != -1) {
            baos.write(reads);
            reads = is.read();
        }
        return baos.toByteArray();
    }

    private static String getVersion(byte[] header) {

        if (header[VERSION_BYTE] == 0 && header[SUBVERSION_BYTE] == 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(header[VERSION_BYTE] & FF_FLAG).append(".");
        builder.append((header[SUBVERSION_BYTE] & FF_FLAG) >>> REQUIRED_LENGTH).append(".");
        builder.append(header[SUBVERSION_BYTE] & F_FLAG);
        return builder.toString();
    }

    private static String getStringFromHeader(byte[] header, int begin, int end) {
        StringBuilder builder = new StringBuilder();
        boolean isEmpty = true;
        for (int i = begin; i < end; ++i) {
            if (header[i] != 0) {
                isEmpty = false;
            }
            builder.append((char) header[i]);
        }

        return isEmpty ? null : builder.toString();
    }

    private static Calendar getCreationDate(byte[] header) {

        int year = getCreationPart(header, CREATION_YEAR);
        int month = getCreationPart(header, CREATION_MONTH);
        int day = getCreationPart(header, CREATION_DAY);
        int hour = getCreationPart(header, CREATION_HOUR);
        int min = getCreationPart(header, CREATION_MIN);
        int sec = getCreationPart(header, CREATION_SEC);

        if (year != 0 || month != 0 || day != 0 || hour != 0 || min != 0 || sec != 0) {
            return new GregorianCalendar(year, month - 1, day, hour, min, sec);
        }

        return null;
    }

    private static int getCreationPart(byte[] header, int off) {
        int part = header[off] & FF_FLAG;
        part <<= BITSINBYTE;
        part += header[off + 1] & FF_FLAG;
        return part;
    }

    private static String getStringTag(byte[] profileBytes, String tagName) {
        if (profileBytes.length < HEADER_SIZE + REQUIRED_LENGTH) {
            return null;
        }

        int tagsNumberRemained = byteArrayToInt(Arrays.copyOfRange(profileBytes, HEADER_SIZE, HEADER_SIZE + REQUIRED_LENGTH));

        int curOffset = HEADER_SIZE + REQUIRED_LENGTH;

        while (tagsNumberRemained > 0 && curOffset + TAGINFO_LENGTH <= profileBytes.length) {
            String tag = new String(Arrays.copyOfRange(profileBytes, curOffset, curOffset + REQUIRED_LENGTH));
            if (tag.equals(tagName)) {
                curOffset += REQUIRED_LENGTH;
                int offset = byteArrayToInt(Arrays.copyOfRange(profileBytes, curOffset,
                        curOffset + REQUIRED_LENGTH));
                curOffset += REQUIRED_LENGTH;
                int length = byteArrayToInt(Arrays.copyOfRange(profileBytes, curOffset,
                        curOffset + REQUIRED_LENGTH));
                if (profileBytes.length < offset + length) {
                    return null;
                }

                return new String(Arrays.copyOfRange(profileBytes, offset + REQUIRED_LENGTH, offset + length)).trim();
            }
            curOffset += TAGINFO_LENGTH;
        }

        return null;
    }

    private static int byteArrayToInt(byte[] b) {
        int value = 0;
        for (int i = 0; i < REQUIRED_LENGTH; i++) {
            int shift = (REQUIRED_LENGTH - 1 - i) * BITSINBYTE;
            value += (b[i] & FF_FLAG) << shift;
        }
        return value;
    }
}
