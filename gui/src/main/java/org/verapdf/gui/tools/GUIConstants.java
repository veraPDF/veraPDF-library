package org.verapdf.gui.tools;

import java.awt.*;

/**
 * @author Maksim Bezrukov
 */
public final class GUIConstants {

	public static final String LOGO_NAME = "veraPDF-logo.jpg";
	public static final Color LOGO_BACKGROUND = Color.WHITE;
	public static final String PARTNERS_NAME = "partners.png";
	public static final Color PARTNERS_BACKGROUND = Color.WHITE;
	public static final String LOGO_LINK_TEXT = "Visit veraPDF.org";
	public static final String LOGO_LINK_URL = "http://www.verapdf.org";
	public static final String ERROR = "Error";
	public static final String XML_LOGO_NAME = "xml-logo.png";
	public static final String HTML_LOGO_NAME = "html-logo.png";
	public static final String CHOOSE_PDF_BUTTON_TEXT = "Choose PDF";
	public static final String PDF_NOT_CHOSEN_TEXT = "PDF file not chosen";
	public static final String FIX_METADATA_FOLDER_CHOOSE_BUTTON = "Choose";
	public static final String CHOOSE_PROFILE_BUTTON_TEXT = "Choose Profile";
	public static final String VALIDATION_PROFILE_NOT_CHOSEN = "Validation profile not chosen";
	public static final String VALIDATE_BUTTON_TEXT = "Execute";
	public static final String VALIDATION_OK = "PDF file is compliant with Validation Profile requirements";
	public static final String VALIDATION_FALSE = "PDF file is not compliant with Validation Profile requirements";
	public static final String SAVE_REPORT_BUTTON_TEXT = "Save XML";
	public static final String VIEW_REPORT_BUTTON_TEXT = "View XML";
	public static final String SAVE_HTML_REPORT_BUTTON_TEXT = "Save HTML";
	public static final String VIEW_HTML_REPORT_BUTTON_TEXT = "View HTML";
	public static final String REPORT = "Report";
	public static final String ERROR_IN_SAVING_HTML_REPORT = "Some error in saving the HTML report.";
	public static final String ERROR_IN_SAVING_XML_REPORT = "Some error in saving the XML report.";
	public static final String LABEL_TEXT = "     Please specify input PDF, Validation Profile and press \"" + VALIDATE_BUTTON_TEXT + "\"";
	public static final String CONSORTIUM_TEXT = "© 2015 veraPDF Consortium";
	public static final String PROPERTIES_NAME = "config.properties";
	public static final String TITLE = "PDF/A Conformance Checker";
	public static final String ERROR_IN_PARSING = "Some error in parsing pdf.";
	public static final String ERROR_IN_VALIDATING = "Some error in validating.";
	public static final String ERROR_IN_INCREMETAL_SAVE = "Some error in saving changes";
	public static final String PDF = "pdf";
	public static final String XML = "xml";
	public static final String HTML = "html";
	public static final String DOT = ".";
	public static final String PROCESSING_TYPE = " Report type: ";
	public static final String VALIDATING_AND_FEATURES = "Validation & Features";
	public static final String VALIDATING = "Validation";
	public static final String FEATURES = "Features";
	public static final String DISPLAY_PASSED_RULES = "Include passed rules:";
	public static final String MAX_NUMBER_FAILED_DISPLAYED_CHECKS = "Display failed checks for rule: ";
	public static final String MAX_NUMBER_FAILED_CHECKS = "Stop validating after failed checks:";
	public static final String FEATURES_GENERATED_CORRECT = "Features report generating finished";
	public static final String MAX_FAILED_CHECKS_SETTING_TIP = "1 to 999999 or empty for unlimited";
	public static final String MAX_FAILED_CHECKS_DISP_SETTING_TIP = "0 to 999999 or empty for unlimited";
	public static final String FIX_METADATA_LABEL_TEXT = "Fix metadata";
	public static final String SELECTED_PATH_FOR_FIXER_LABEL_TEXT = "Save fixed files into the folder:";
	public static final String FIX_METADATA_PREFIX_LABEL_TEXT = "Save fixed files with prefix:";
	public static final String SELECTED_PATH_FOR_FIXER_TOOLTIP = "The folder to save the fixed file to. Leave empty to save it near the original file.";

	public static final int EMPTYBORDER_INSETS = 5;
	public static final int FRAME_COORD_X = 100;
	public static final int FRAME_COORD_Y = 100;
	public static final int FRAME_WIDTH = 600;
	public static final int FRAME_HEIGHT = 350;

	public static final int BORDER_WIDTH = 4;
	public static final int LOGOPANEL_BORDER_WIDTH = 10;
	public static final int XMLLOGO_BORDER_WIDTH = 4;
	public static final int HTMLLOGO_BORDER_WIDTH = 4;
	public static final int ABOUTDIALOG_COORD_X = 150;
	public static final int ABOUTDIALOG_COORD_Y = 150;
	public static final int SETTINGSDIALOG_COORD_X = 150;
	public static final int SETTINGSDIALOG_COORD_Y = 150;
	public static final int PREFERRED_WIDTH = 450;
	public static final int PREFERRED_SIZE_WIDTH = 450;
	public static final int PREFERRED_SIZE_HEIGHT = 200;
	public static final int LOGOPANEL_PREFERRED_SIZE_WIDTH = 450;
	public static final int LOGOPANEL_PREFERRED_SIZE_HEIGHT = 200;
	public static final int VALIDATION_SUCCESS_COLOR_RGB_GREEN = 180;
	public static final int VALIDATION_FAILED_COLOR_RGB_RED = 180;

	public static final int CHOSENPDF_LABEL_CONSTRAINT_GRIDX = 0;
	public static final int CHOSENPDF_LABEL_CONSTRAINT_GRIDY = 0;
	public static final int CHOSENPDF_LABEL_CONSTRAINT_WEIGHTX = 3;
	public static final int CHOSENPDF_LABEL_CONSTRAINT_WEIGHTY = 1;
	public static final int CHOSENPDF_LABEL_CONSTRAINT_GRIDWIDTH = 3;
	public static final int CHOSENPDF_LABEL_CONSTRAINT_GRIDHEIGHT = 1;

	public static final int CHOOSEPDF_BUTTON_CONSTRAINT_GRIDX = 3;
	public static final int CHOOSEPDF_BUTTON_CONSTRAINT_GRIDY = 0;
	public static final int CHOOSEPDF_BUTTON_CONSTRAINT_WEIGHTX = 0;
	public static final int CHOOSEPDF_BUTTON_CONSTRAINT_WEIGHTY = 1;
	public static final int CHOOSEPDF_BUTTON_CONSTRAINT_GRIDWIDTH = 1;
	public static final int CHOOSEPDF_BUTTON_CONSTRAINT_GRIDHEIGHT = 1;

	public static final int CHOSENPROFILE_LABEL_CONSTRAINT_GRIDX = 0;
	public static final int CHOSENPROFILE_LABEL_CONSTRAINT_GRIDY = 1;
	public static final int CHOSENPROFILE_LABEL_CONSTRAINT_WEIGHTX = 3;
	public static final int CHOSENPROFILE_LABEL_CONSTRAINT_WEIGHTY = 1;
	public static final int CHOSENPROFILE_LABEL_CONSTRAINT_GRIDWIDTH = 3;
	public static final int CHOSENPROFILE_LABEL_CONSTRAINT_GRIDHEIGHT = 1;

	public static final int CHOOSEPROFILE_BUTTON_CONSTRAINT_GRIDX = 3;
	public static final int CHOOSEPROFILE_BUTTON_CONSTRAINT_GRIDY = 1;
	public static final int CHOOSEPROFILE_BUTTON_CONSTRAINT_WEIGHTX = 0;
	public static final int CHOOSEPROFILE_BUTTON_CONSTRAINT_WEIGHTY = 1;
	public static final int CHOOSEPROFILE_BUTTON_CONSTRAINT_GRIDWIDTH = 1;
	public static final int CHOOSEPROFILE_BUTTON_CONSTRAINT_GRIDHEIGHT = 1;

	public static final int RESULT_LABEL_CONSTRAINT_GRIDX = 0;
	public static final int RESULT_LABEL_CONSTRAINT_GRIDY = 3;
	public static final int RESULT_LABEL_CONSTRAINT_WEIGHTX = 3;
	public static final int RESULT_LABEL_CONSTRAINT_WEIGHTY = 1;
	public static final int RESULT_LABEL_CONSTRAINT_GRIDWIDTH = 3;
	public static final int RESULT_LABEL_CONSTRAINT_GRIDHEIGHT = 1;

	public static final int PROGRESSBAR_CONSTRAINT_GRIDX = 0;
	public static final int PROGRESSBAR_CONSTRAINT_GRIDY = 3;
	public static final int PROGRESSBAR_CONSTRAINT_WEIGHTX = 3;
	public static final int PROGRESSBAR_CONSTRAINT_WEIGHTY = 1;
	public static final int PROGRESSBAR_CONSTRAINT_GRIDWIDTH = 3;
	public static final int PROGRESSBAR_CONSTRAINT_GRIDHEIGHT = 1;

	public static final int VALIDATE_BUTTON_CONSTRAINT_GRIDX = 3;
	public static final int VALIDATE_BUTTON_CONSTRAINT_GRIDY = 3;
	public static final int VALIDATE_BUTTON_CONSTRAINT_WEIGHTX = 0;
	public static final int VALIDATE_BUTTON_CONSTRAINT_WEIGHTY = 1;
	public static final int VALIDATE_BUTTON_CONSTRAINT_GRIDWIDTH = 1;
	public static final int VALIDATE_BUTTON_CONSTRAINT_GRIDHEIGHT = 1;

	public static final int REPORT_PANEL_CONSTRAINT_GRIDX = 0;
	public static final int REPORT_PANEL_CONSTRAINT_GRIDY = 4;
	public static final int REPORT_PANEL_CONSTRAINT_WEIGHTX = 4;
	public static final int REPORT_PANEL_CONSTRAINT_WEIGHTY = 3;
	public static final int REPORT_PANEL_CONSTRAINT_GRIDWIDTH = 4;
	public static final int REPORT_PANEL_CONSTRAINT_GRIDHEIGHT = 1;

	public static final int REPORT_PANEL_LINES_NUMBER = 2;
	public static final int REPORT_PANEL_COLUMNS_NUMBER = 3;

	public static final double SCALE = 0.5;
	public static final double CONSORTIUM_FONT_SCALE = 1.3;

	public static final Color BEFORE_VALIDATION_COLOR = Color.BLACK;
	public static final Color VALIDATION_SUCCESS_COLOR = new Color(0,
			VALIDATION_SUCCESS_COLOR_RGB_GREEN, 0);
	public static final Color VALIDATION_FAILED_COLOR = new Color(
			VALIDATION_FAILED_COLOR_RGB_RED, 0, 0);

	private GUIConstants() {
		// Disable default constructor
	}
}
