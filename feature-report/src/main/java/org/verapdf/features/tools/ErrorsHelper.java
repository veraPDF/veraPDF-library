package org.verapdf.features.tools;

import org.apache.log4j.Logger;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;

/**
 * Static class with constants for feature error ids and messages
 *
 * @author Maksim Bezrukov
 */
public final class ErrorsHelper {

	private static final Logger LOGGER = Logger.getLogger(ErrorsHelper.class);

	public static final String ERRORID = "errorID";
	public static final String ID = "id";

	public static final String METADATACONVERT_ID = "metaerr1";
	public static final String METADATACONVERT_MESSAGE = "Error while converting metadata stream with use of ISO-8859-1 encoding.";
	public static final String METADATAPARSER_ID = "metaerr2";
	public static final String METADATAPARSER_MESSAGE = "Error while parsing metadata into DOM.";

	public static final String PAGESCALLING_ID = "pageerr1";
	public static final String PAGESCALLING_MESSAGE = "Page dictionary must contain number value for key \"PZ\".";

	public static final String DATE_ID = "dateerr1";
	public static final String DATE_MESSAGE = "A serious configuration error while creating xml formatted date field.";

	public static final String BYTETOSTRING_ID = "bytestrerr1";
	public static final String BYTETOSTRING_MESSAGE = "Error in converting byte array to string.";

	public static final String OUTPUTINTENTSTYPE_ID = "outinterr1";
	public static final String OUTPUTINTENTSTYPE_MESSAGE = "In OutputIntent dictionary value for key \"S\" must be of type CosName.";

	public static final String OUTLINESCOLOR_ID = "outlineserr1";
	public static final String OUTLINESCOLOR_MESSAGE = "In Outputlines dictionary value for key \"C\" must be an array of three numbers.";

	public static final String LOWLVLINFODOCUMENTID_ID = "llierr1";
	public static final String LOWLVLINFODOCUMENTID_MESSAGE = "In Trailer dictionary value for key \"ID\" must be an array of two Strings.";

	public static final String PARSINGEMBEDDEDFILEERROR_ID = "embfierr1";
	public static final String PARSINGEMBEDDEDFILEERROR_MESSAGE = "Error while parsing embedded files.";

	public static final String GETINGICCPROFILEHEADERERROR_ID = "iccprofileerr2";
	public static final String GETINGICCPROFILEHEADERERROR_MESSAGE = "Error while getting icc profile bytes.";
	public static final String GETINGICCPROFILEHEADERSIZEERROR_ID = "iccprofileerr3";
	public static final String GETINGICCPROFILEHEADERSIZEERROR_MESSAGE = "ICC Profile has less than 128 bytes.";

	public static final String GETINGFONTERROR_ID = "fonterr1";
	public static final String GETINGFONTERROR_MESSAGE = "Error while getting font.";

	public static final String GETINGPATTERNERROR_ID = "ptrnerr1";
	public static final String GETINGPATTERNERROR_MESSAGE = "Error while getting pattern.";

	public static final String GETINGCOLORSPACEERROR_ID = "clrsperr1";
	public static final String GETINGCOLORSPACEERROR_MESSAGE = "Error while getting colorSpace.";

	public static final String GETINGSHADINGERROR_ID = "shaderr1";
	public static final String GETINGSHADINGERROR_MESSAGE = "Error while getting shading.";

	public static final String GETINGXOBJECTERROR_ID = "xobjerr1";
	public static final String GETINGXOBJECTERROR_MESSAGE = "Error while getting xobject.";

	public static final String COLOR_ID = "colorerr1";
	public static final String COLOR_MESSAGE = "Devise color space contains wrong number of components.";

	public static final String ANNOTATIONPARSER_ID = "annotparsrerr1";
	public static final String ANNOTATIONPARSER_MESSAGE = "Unknown annotation type.";

	private ErrorsHelper() {
		// Disable default public constructor
	}

	/**
	 * Adds an error to a {@link FeaturesCollection}
	 *
	 * @param collection   the {@link FeaturesCollection} to add the error to
	 * @param errorID      the unique ID of the error
	 * @param errorMessage the error message
	 */
	public static void addErrorIntoCollection(FeaturesCollection collection,
											  String errorID, String errorMessage) {
		try {
			FeatureTreeNode error = FeatureTreeNode.newRootInstanceWIthValue(
					"error", errorMessage);
			error.addAttribute(ErrorsHelper.ID, errorID);
			if (!collection.getFeatureTreesForType(
					FeaturesObjectTypesEnum.ERROR).contains(error)) {
				collection.addNewFeatureTree(FeaturesObjectTypesEnum.ERROR,
						error);
			}
		} catch (FeaturesTreeNodeException ignore) {
			// This exception occurs when wrong node creates for feature tree.
			// The logic of the method guarantees this doesn't occur.
			String message = "FeatureTreeNode root instance logic failure";
			LOGGER.fatal(message, ignore);
			throw new IllegalStateException(message, ignore);
		}
	}
}
