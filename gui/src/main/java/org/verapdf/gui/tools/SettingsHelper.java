package org.verapdf.gui.tools;

import java.util.Properties;

/**
 * Helps to get settings values. If any error accured in getting, it returns default values.
 *
 * @author Maksim Bezrukov
 */
public final class SettingsHelper {

	private SettingsHelper() {
	}

	/**
	 * @param settings properties object for obtaining settings
	 * @return integer that indicates selected processing type
	 */
	public static int getProcessingType(Properties settings) {
		if (settings == null) {
			return Integer.parseInt(GUIConstants.DEFAULT_PROPERTIES.getProperty(GUIConstants.PROPERTY_PROCESSING_TYPE));
		}
		int type;
		try {
			type = Integer.parseInt(settings.getProperty(GUIConstants.PROPERTY_PROCESSING_TYPE));
			if (!(type >= 1 && type <= 3)) {
				type = Integer.parseInt(GUIConstants.DEFAULT_PROPERTIES.getProperty(GUIConstants.PROPERTY_PROCESSING_TYPE));
			}
		} catch (NumberFormatException e) {
			type = Integer.parseInt(GUIConstants.DEFAULT_PROPERTIES.getProperty(GUIConstants.PROPERTY_PROCESSING_TYPE));
		}
		return type;
	}

	/**
	 * @param settings properties object for obtaining settings
	 * @return selected number for maximum displayed fail checks for a rule. If not selected returns -1
	 */
	public static int getNumbOfFailDisp(Properties settings) {
		if (settings == null) {
			return Integer.parseInt(GUIConstants.DEFAULT_PROPERTIES.getProperty(GUIConstants.PROPERTY_MAX_NUMBER_DISPLAYED_FAILED_CHECKS));
		}
		int numbOfFailDisp;
		try {
			numbOfFailDisp = Integer.parseInt(settings.getProperty(GUIConstants.PROPERTY_MAX_NUMBER_DISPLAYED_FAILED_CHECKS));
			if (numbOfFailDisp <= 0) {
				numbOfFailDisp = Integer.parseInt(GUIConstants.DEFAULT_PROPERTIES.getProperty(GUIConstants.PROPERTY_MAX_NUMBER_DISPLAYED_FAILED_CHECKS));
			}
		} catch (NumberFormatException e) {
			numbOfFailDisp = Integer.parseInt(GUIConstants.DEFAULT_PROPERTIES.getProperty(GUIConstants.PROPERTY_MAX_NUMBER_DISPLAYED_FAILED_CHECKS));
		}
		return numbOfFailDisp;
	}

	/**
	 * @param settings properties object for obtaining settings
	 * @return selected number for maximum fail checks for a rule. If not selected returns -1
	 */
	public static int getNumbOfFail(Properties settings) {
		if (settings == null) {
			return Integer.parseInt(GUIConstants.DEFAULT_PROPERTIES.getProperty(GUIConstants.PROPERTY_MAX_NUMBER_FAILED_CHECKS));
		}
		int numbOfFail;
		try {
			numbOfFail = Integer.parseInt(settings.getProperty(GUIConstants.PROPERTY_MAX_NUMBER_FAILED_CHECKS));
			if (numbOfFail <= 0) {
				numbOfFail = Integer.parseInt(GUIConstants.DEFAULT_PROPERTIES.getProperty(GUIConstants.PROPERTY_MAX_NUMBER_FAILED_CHECKS));
			}
		} catch (NumberFormatException e) {
			numbOfFail = Integer.parseInt(GUIConstants.DEFAULT_PROPERTIES.getProperty(GUIConstants.PROPERTY_MAX_NUMBER_FAILED_CHECKS));
		}
		return numbOfFail;
	}

	/**
	 * @param settings properties object for obtaining settings
	 * @return true if desplay passed pules option selected
	 */
	public static boolean isDispPassedRules(Properties settings) {
		if (settings == null) {
			return Boolean.getBoolean(GUIConstants.DEFAULT_PROPERTIES.getProperty(GUIConstants.PROPERTY_HIDE_PASSED_RULES).toLowerCase());
		}
		boolean dispPassedRulesBool;
		String dispProp = settings.getProperty(GUIConstants.PROPERTY_HIDE_PASSED_RULES);
		if ("true".equals(dispProp.toLowerCase())) {
			dispPassedRulesBool = true;
		} else if ("false".equals(dispProp.toLowerCase())) {
			dispPassedRulesBool = false;
		} else {
			dispPassedRulesBool = Boolean.getBoolean(GUIConstants.DEFAULT_PROPERTIES.getProperty(GUIConstants.PROPERTY_HIDE_PASSED_RULES).toLowerCase());
		}
		return dispPassedRulesBool;
	}
}
