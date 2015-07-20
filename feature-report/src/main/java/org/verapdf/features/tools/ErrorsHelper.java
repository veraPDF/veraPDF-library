package org.verapdf.features.tools;

import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;

/**
 * Static class with constants for feature error ids and messages
 *
 * @author Maksim Bezrukov
 */
public final class ErrorsHelper {

    private ErrorsHelper() {
    }

    public static final String ERRORID = "errorId";
    public static final String ID = "id";

    public static final String METADATACONVERT_ID = "metaerr1";
    public static final String METADATACONVERT_MESSAGE = "Error while converting metadata stream with use of ISO-8859-1 encoding.";
    public static final String METADATAPARSER_ID = "metaerr2";
    public static final String METADATAPARSER_MESSAGE = "Error while parsing metadata into DOM.";

    public static final String PAGESCALLING_ID = "pageerr1";
    public static final String PAGESCALLING_MESSAGE = "Page dictionary must contain number value for key \"PZ\".";

    public static final String DATE_ID = "dateerr1";
    public static final String DATE_MESSAGE = "A serious configuration error while creating xml formatted date field.";

    public static final String OUTPUTINTENTSTYPE_ID = "outinterr1";
    public static final String OUTPUTINTENTSTYPE_MESSAGE = "In OutputIntent dictionary value for key \"S\" must be of type CosName.";

    public static final String OUTLINESCOLOR_ID = "outlineserr1";
    public static final String OUTLINESCOLOR_MESSAGE = "In Outputlines dictionary value for key \"C\" must be an array of three numbers.";

    public static final String LOWLVLINFODOCUMENTID_ID = "llierr1";
    public static final String LOWLVLINFODOCUMENTID_MESSAGE = "In Trailer dictionary value for key \"ID\" must be an array of two Strings.";

    public static final String PARSINGEMBEDDEDFILEERROR_ID = "embfierr1";
    public static final String PARSINGEMBEDDEDFILEERROR_MESSAGE = "Error while parsing embedded files.";

    public static final String XMLINVALIDCHARACTERS_ID = "xmlinvcharerr1";
    public static final String XMLINVALIDCHARACTERS_MESSAGE = "Element's data contains invalid for XML characters.";

    public static void addErrorIntoCollection(FeaturesCollection collection, String errorID, String errorMessage) {
        try {
            FeatureTreeNode error = FeatureTreeNode.newInstance("error", errorMessage, null);
            error.addAttribute(ErrorsHelper.ID, errorID);
            if (!collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ERROR).contains(error)) {
                collection.addNewFeatureTree(FeaturesObjectTypesEnum.ERROR, error);
            }
        } catch (FeaturesTreeNodeException ignore) {
            // This exception occurs when wrong node creates for feature tree.
            // The logic of the method guarantees this doesn't occur.
        }
    }
}
