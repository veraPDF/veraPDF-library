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

	private ErrorsHelper() {
		// Disable default public constructor
	}

	/**
	 * Adds an error to a {@link FeaturesCollection}
	 *
	 * @param collection      the {@link FeaturesCollection} to add the error to
	 * @param element         element which contains error
	 * @param errorMessageArg the error message
	 * @return id of the generated error node as String
	 */
	public static String addErrorIntoCollection(FeaturesCollection collection,
												FeatureTreeNode element,
												String errorMessageArg) {
		if (collection == null) {
			throw new IllegalArgumentException("Collection can not be null");
		}
		String errorMessage = errorMessageArg;
		if (errorMessage == null) {
			errorMessage = "Exception with null message.";
		}
		try {
			String id = null;
			for (FeatureTreeNode errNode : collection.getFeatureTreesForType(
					FeaturesObjectTypesEnum.ERROR)) {
				if (errorMessage.equals(errNode.getValue())) {
					id = errNode.getAttributes().get(ID);
					break;
				}
			}
			if (id == null) {
				id = "error" + collection.getFeatureTreesForType(
						FeaturesObjectTypesEnum.ERROR).size();
				FeatureTreeNode error = FeatureTreeNode.newRootInstanceWIthValue(
						"error", errorMessage);
				error.addAttribute(ErrorsHelper.ID, id);
				collection.addNewFeatureTree(FeaturesObjectTypesEnum.ERROR,
						error);
			}
			if (element != null) {
				String elementErrorID = id;
				if (element.getAttributes().get(ERRORID) != null) {
					elementErrorID = element.getAttributes().get(ERRORID) + ", " + elementErrorID;
				}
				element.addAttribute(ERRORID, elementErrorID);
			}
			return id;
		} catch (FeaturesTreeNodeException ignore) {
			// This exception occurs when wrong node creates for feature tree.
			// The logic of the method guarantees this doesn't occur.
			String message = "FeatureTreeNode root instance logic failure";
			LOGGER.fatal(message, ignore);
			throw new IllegalStateException(message, ignore);
		}
	}
}
