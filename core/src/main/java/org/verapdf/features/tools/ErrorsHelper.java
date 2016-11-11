package org.verapdf.features.tools;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;

/**
 * Static class with constants for feature error ids and messages
 *
 * @author Maksim Bezrukov
 */
public final class ErrorsHelper {
	private static final FeatureObjectType TYPE = FeatureObjectType.ERROR;
	private static final Logger LOGGER = Logger.getLogger(ErrorsHelper.class.getName());

	public static final String ERRORID = "errorId";
	public static final String ID = "id";

	private ErrorsHelper() {
		// Disable default public constructor
	}

	/**
	 * Adds an error to a {@link FeaturesCollection}
	 *
	 * @param collection
	 *            the {@link FeaturesCollection} to add the error to
	 * @param element
	 *            element which contains error
	 * @param errorMessageArg
	 *            the error message
	 * @return id of the generated error node as String
	 */
	public static String addErrorIntoCollection(FeatureExtractionResult collection, FeatureTreeNode element,
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
			for (FeatureTreeNode errNode : collection.getFeatureTreesForType(TYPE)) {
				if (errorMessage.equals(errNode.getValue())) {
					id = errNode.getAttributes().get(ID);
					break;
				}
			}
			if (id == null) {
				id = TYPE.getNodeName() + collection.getFeatureTreesForType(TYPE).size();
				FeatureTreeNode error = FeatureTreeNode.createRootNode(TYPE.getNodeName());
				error.setValue(errorMessage);
				error.setAttribute(ErrorsHelper.ID, id);
				collection.addNewFeatureTree(TYPE, error);
			}
			if (element != null) {
				String elementErrorID = id;
				if (element.getAttributes().get(ERRORID) != null) {
					elementErrorID = element.getAttributes().get(ERRORID) + ", " + elementErrorID;
				}
				element.setAttribute(ERRORID, elementErrorID);
			}
			return id;
		} catch (FeatureParsingException ignore) {
			// This exception occurs when wrong node creates for feature tree.
			// The logic of the method guarantees this doesn't occur.
			String message = "FeatureTreeNode root instance logic failure";
			LOGGER.log(Level.SEVERE, message, ignore);
			throw new IllegalStateException(message, ignore);
		}
	}
}
