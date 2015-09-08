package org.verapdf.features.pb.objects;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
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
import java.io.UnsupportedEncodingException;
import java.util.*;

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
	private static final int FIRST_RECORD_STRING_LENGTH_IN_TEXTDESCRIPTIONTYPE_BEGIN = 8;
	private static final int FIRST_RECORD_STRING_LENGTH_IN_TEXTDESCRIPTIONTYPE_END = 12;
	private static final int NUMBER_OF_RECORDS_IN_MULTILOCALIZEDUNICODETYPE_BEGIN = 8;
	private static final int NUMBER_OF_RECORDS_IN_MULTILOCALIZEDUNICODETYPE_END = 12;

	private COSStream profile;
	private String id;
	private Set<String> outInts;
	private Set<String> iccBaseds;

	/**
	 * Constructs new icc profile feature object
	 *
	 * @param profile   COSStream which represents the icc profile for feature report
	 * @param id        id of the profile
	 * @param outInts   set of ids of all parent output intents for this icc profile
	 * @param iccBaseds set of ids of all parent icc based color spaces for this icc profile
	 */
	public PBICCProfileFeaturesObject(COSStream profile, String id, Set<String> outInts, Set<String> iccBaseds) {
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
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the constructed collection tree
	 * @throws FeaturesTreeNodeException occurs when wrong features tree node constructs
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

			COSBase cosBase = profile.getDictionaryObject(COSName.METADATA);
			if (cosBase instanceof COSStream) {
				PDMetadata meta = new PDMetadata((COSStream) cosBase);
				PBCreateNodeHelper.parseMetadata(meta, "metadata", root, collection);
			}

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
			byte[] profileBytes = inputStreamToByteArray(profile.getUnfilteredStream());

			if (profileBytes.length < HEADER_SIZE) {
				root.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.GETINGICCPROFILEHEADERSIZEERROR_ID);
				ErrorsHelper.addErrorIntoCollection(collection,
						ErrorsHelper.GETINGICCPROFILEHEADERSIZEERROR_ID,
						ErrorsHelper.GETINGICCPROFILEHEADERSIZEERROR_MESSAGE);
			} else {
				PBCreateNodeHelper.addNotEmptyNode("version", getVersion(profileBytes), root);
				PBCreateNodeHelper.addNotEmptyNode("cmmType", getString(profileBytes, CMMTYPE_BEGIN, CMMTYPE_END), root);
				PBCreateNodeHelper.addNotEmptyNode("dataColorSpace", getString(profileBytes, DATACOLORSPACE_BEGIN, DATACOLORSPACE_END), root);
				PBCreateNodeHelper.addNotEmptyNode("creator", getString(profileBytes, CREATOR_BEGIN, CREATOR_END), root);
				PBCreateNodeHelper.createDateNode("creationDate", root, getCreationDate(profileBytes), collection);
				String intent = getIntent(getString(profileBytes, RENDERINGINTENT_BEGIN, RENDERINGINTENT_END));
				PBCreateNodeHelper.addNotEmptyNode("defaultRenderingIntent", intent, root);
				PBCreateNodeHelper.addNotEmptyNode("copyright", getStringTag(profileBytes, "cprt"), root);
				PBCreateNodeHelper.addNotEmptyNode("description", getStringTag(profileBytes, "desc"), root);
				PBCreateNodeHelper.addNotEmptyNode("profileId", getString(profileBytes, PROFILEID_BEGIN, PROFILEID_END), root);
				PBCreateNodeHelper.addNotEmptyNode("deviceModel", getString(profileBytes, DEVICEMODEL_BEGIN, DEVICEMODEL_END), root);
				PBCreateNodeHelper.addNotEmptyNode("deviceManufacturer", getString(profileBytes, DEVICEMANUFACTURER_BEGIN, DEVICEMANUFACTURER_END), root);
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

	private static String getIntent(String str) {
		if (str == null) {
			return "Perceptual";
		}
		switch (str) {
			case "\u0000\u0000\u0000\u0001":
				return "Media-Relative Colorimetric";
			case "\u0000\u0000\u0000\u0002":
				return "Saturation";
			case "\u0000\u0000\u0000\u0003":
				return "ICC-Absolute Colorimetric";
			default:
				return str;
		}
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

	private static String getString(byte[] header, int begin, int end) {
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
			GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
			cal.set(year, month - 1, day, hour, min, sec);
			cal.set(Calendar.MILLISECOND, 0);
			return cal;
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

				String type = new String(Arrays.copyOfRange(profileBytes, offset, offset + REQUIRED_LENGTH));
				if ("mluc".equals(type)) {

					int number = byteArrayToInt(Arrays.copyOfRange(profileBytes, offset + NUMBER_OF_RECORDS_IN_MULTILOCALIZEDUNICODETYPE_BEGIN,
							offset + NUMBER_OF_RECORDS_IN_MULTILOCALIZEDUNICODETYPE_END));
					int recOffset = offset + NUMBER_OF_RECORDS_IN_MULTILOCALIZEDUNICODETYPE_END + REQUIRED_LENGTH;
					for (int i = 0; i < number; ++i) {
						String local = getString(profileBytes, recOffset, recOffset + REQUIRED_LENGTH);
						if ("enUS".equals(local)) {
							length = byteArrayToInt(Arrays.copyOfRange(profileBytes, recOffset + REQUIRED_LENGTH,
									recOffset + REQUIRED_LENGTH + REQUIRED_LENGTH));
							offset += byteArrayToInt(Arrays.copyOfRange(profileBytes, recOffset + REQUIRED_LENGTH * 2,
									recOffset + REQUIRED_LENGTH * 2 + REQUIRED_LENGTH));
							try {
								return new String(Arrays.copyOfRange(profileBytes, offset, offset + length), "UTF-16BE").trim();
							} catch (UnsupportedEncodingException e) {
								// UTF-16BE Character set exists, so do nothing
								/**
								 * cfw Conversely if it doesn't exist (which is the only way this
								 * block is hit) something is badly amiss, I'd suggest wrapping and
								 * throwing a runtime IllegalStateException if this is ever hit.
								 * e.g. throw new
								 * IllegalStateException("No UTF-16BE encoding detected", e);
								 */
								throw new IllegalStateException(
										"Required UTF-16BE encoding not found", e);
							}
						}
					}
				} else if ("desc".equals(type)) {
					length = byteArrayToInt(Arrays.copyOfRange(profileBytes, offset + FIRST_RECORD_STRING_LENGTH_IN_TEXTDESCRIPTIONTYPE_BEGIN,
							offset + FIRST_RECORD_STRING_LENGTH_IN_TEXTDESCRIPTIONTYPE_END));
					offset += FIRST_RECORD_STRING_LENGTH_IN_TEXTDESCRIPTIONTYPE_END;
				} else {
					offset += REQUIRED_LENGTH;
					length -= REQUIRED_LENGTH;
				}

				return new String(Arrays.copyOfRange(profileBytes, offset, offset + length)).trim();
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
