package org.verapdf.gui;

import org.apache.log4j.Logger;
import org.verapdf.exceptions.validationlogic.MultiplyGlobalVariableNameException;
import org.verapdf.exceptions.validationlogic.NullLinkException;
import org.verapdf.exceptions.validationlogic.NullLinkNameException;
import org.verapdf.exceptions.validationlogic.NullLinkedObjectException;
import org.verapdf.exceptions.validationprofileparser.MissedHashTagException;
import org.verapdf.exceptions.validationprofileparser.WrongSignatureException;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.tools.FeaturesCollection;
import org.verapdf.gui.tools.GUIConstants;
import org.verapdf.gui.tools.SettingsManager;
import org.verapdf.metadata.fixer.MetadataFixer;
import org.verapdf.model.ModelLoader;
import org.verapdf.report.HTMLReport;
import org.verapdf.report.XMLReport;
import org.verapdf.validation.logic.Validator;
import org.verapdf.validation.report.model.ValidationInfo;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Validates PDF in a new threat.
 *
 * @author Maksim Bezrukov
 */
public class ValidateWorker extends SwingWorker<ValidationInfo, Integer> {

	private static final Logger LOGGER = Logger.getLogger(ValidateWorker.class);

	private File pdf;
	private File profile;
	private CheckerPanel parent;
	private SettingsManager settings;
	private File xmlReport = null;
	private File htmlReport = null;

	private long startTimeOfValidation;
	private long endTimeOfValidation;

	/**
	 * Creates new validate worker
	 *
	 * @param parent   parent component
	 * @param pdf      pdf file for validating
	 * @param profile  validation profile for validating
	 * @param settings settings for validation
	 */
	public ValidateWorker(CheckerPanel parent, File pdf, File profile, SettingsManager settings) {
		this.parent = parent;
		this.pdf = pdf;
		this.profile = profile;
		this.settings = settings;
	}

	@Override
	protected ValidationInfo doInBackground() {
		xmlReport = null;
		htmlReport = null;
		ValidationInfo info = null;
		FeaturesCollection collection = null;

		startTimeOfValidation = System.currentTimeMillis();

		try (ModelLoader loader = new ModelLoader(this.pdf.getPath())) {
			int flag = settings.getProcessingType();

			if ((flag & 1) == 1) {
				org.verapdf.model.baselayer.Object root = loader.getRoot();
				info = runValidator(root);

				// TODO : make field for incremental save
				if (false) {
					MetadataFixer fixer = new MetadataFixer(loader.getPDDocument(), info);
					fixer.fixDocument(new File("res" + this.pdf.getName()));
				}
			}

			if ((flag & (1 << 1)) == (1 << 1)) {
				try {
					File config = null;
					if (settings.getFeaturesPluginsConfigFilePath() != null) {
						config = settings.getFeaturesPluginsConfigFilePath().toFile();
					}
					collection = PBFeatureParser.getFeaturesCollection(loader.getPDDocument(), config);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this.parent,
							"Some error in creating features collection.",
							GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
					LOGGER.error("Exception in creating features collection: ", e);
				}
			}
			endTimeOfValidation = System.currentTimeMillis();
			writeReports(info, collection);
		} catch (IOException e) {
			this.parent.errorInValidatingOccur(GUIConstants.ERROR_IN_PARSING, e);
		} catch (TransformerException | URISyntaxException |
				ParserConfigurationException | SAXException e) {
			this.parent.errorInValidatingOccur(GUIConstants.ERROR_IN_INCREMETAL_SAVE, e);
		}

		return info;
	}

	private ValidationInfo runValidator(org.verapdf.model.baselayer.Object root) {
		try {
			return Validator.validate(root, this.profile, false, settings.isShowPassedRules(), settings.getMaxNumberOfFailedChecks(), settings.getMaxNumberOfDisplayedFailedChecks());
		} catch (IOException | NullLinkNameException | NullLinkException |
				NullLinkedObjectException | MissedHashTagException |
				WrongSignatureException | MultiplyGlobalVariableNameException |
				ParserConfigurationException | SAXException | XMLStreamException e) {

			this.parent.errorInValidatingOccur(GUIConstants.ERROR_IN_VALIDATING, e);
		}
		return null;
	}

	@Override
	protected void done() {
		this.parent.validationEnded(this.xmlReport, this.htmlReport);
	}

	private void writeReports(ValidationInfo info, FeaturesCollection collection) {
		if (info != null || collection != null) {
			try {
				xmlReport = File.createTempFile("veraPDF-tempXMLReport", ".xml");
				xmlReport.deleteOnExit();
				XMLReport.writeXMLReport(info, collection, xmlReport.getPath(),
						endTimeOfValidation - startTimeOfValidation, settings.isShowPassedRules());

				if (info != null) {
					try {
						htmlReport = File.createTempFile("veraPDF-tempHTMLReport", ".html");
						htmlReport.deleteOnExit();
						HTMLReport.writeHTMLReport(htmlReport.getPath(), xmlReport,
								profile);

					} catch (IOException | TransformerException e) {
						JOptionPane.showMessageDialog(this.parent,
								GUIConstants.ERROR_IN_SAVING_HTML_REPORT,
								GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
						LOGGER.error("Exception saving the HTML report", e);
						htmlReport = null;
					}
				}

			} catch (DatatypeConfigurationException | ParserConfigurationException
					| IOException | TransformerException e) {
				JOptionPane.showMessageDialog(this.parent,
						GUIConstants.ERROR_IN_SAVING_XML_REPORT,
						GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
				LOGGER.error("Exception saving the XML report", e);
				xmlReport = null;
			}
		}
	}
}
