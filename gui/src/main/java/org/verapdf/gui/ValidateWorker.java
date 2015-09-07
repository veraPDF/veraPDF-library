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
	private File xmlReport = null;
	private File htmlReport = null;

	private long startTimeOfValidation;
	private long endTimeOfValidation;

	/**
	 * Creates new validate worker
	 *
	 * @param parent  parent component
	 * @param pdf     pdf file for validating
	 * @param profile validation profile for validating
	 */
	public ValidateWorker(CheckerPanel parent, File pdf, File profile) {
		this.parent = parent;
		this.pdf = pdf;
		this.profile = profile;
	}

	@Override
	protected ValidationInfo doInBackground() {
		ValidationInfo info = null;
		FeaturesCollection collection = null;

		startTimeOfValidation = System.currentTimeMillis();

		ModelLoader loader = new ModelLoader(this.pdf.getPath());

		try {
			org.verapdf.model.baselayer.Object root = loader.getRoot();
			info = runValidator(root);

			try {
				collection = PBFeatureParser.getFeaturesCollection(loader.getPDDocument());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this.parent,
						"Some error in creating features collection.",
						GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
				LOGGER.error("Exception in creating features collection: ", e);
			}

			try {
				loader.getPDDocument().close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this.parent, "Some error in closing document.",
						GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
				LOGGER.error("Exception in closing document: ", e);
			}

			endTimeOfValidation = System.currentTimeMillis();
			writeReports(info, collection);

		} catch (IOException e) {
			this.parent.errorInValidatingOccur(GUIConstants.ERROR_IN_PARSING, e);
		}

		return info;
	}

	private ValidationInfo runValidator(org.verapdf.model.baselayer.Object root) {
		try {
			// TODO : make checkbox
			return Validator.validate(root, this.profile, false, false);
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
				File dir = new File("./temp/");
				if (!dir.exists() && !dir.mkdir()) {
					throw new IOException("Can not create temporary directory.");
				}
				xmlReport = new File("./temp/tempXMLReport.xml");
				// TODO : make checkbox
				XMLReport.writeXMLReport(info, collection, xmlReport.getPath(),
						endTimeOfValidation - startTimeOfValidation, false);

				if (info != null) {
					try {
						htmlReport = new File("./temp/tempHTMLReport.html");
						HTMLReport.writeHTMLReport(htmlReport.getPath(), xmlReport,
								profile);

					} catch (IOException | TransformerException e) {
						JOptionPane.showMessageDialog(this.parent,
								GUIConstants.ERROR_IN_SAVING_HTML_REPORT,
								GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
						LOGGER.error("Exception saving the HTML report", e);
					}
				}

			} catch (DatatypeConfigurationException | ParserConfigurationException
					| IOException | TransformerException e) {
				JOptionPane.showMessageDialog(this.parent,
						GUIConstants.ERROR_IN_SAVING_XML_REPORT,
						GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
				LOGGER.error("Exception saving the XML report", e);
			}
		}
	}
}
