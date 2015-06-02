package org.verapdf.validation.report;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.verapdf.validation.report.model.Check;
import org.verapdf.validation.report.model.Rule;
import org.w3c.dom.*;
import org.verapdf.validation.report.model.ValidationInfo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;


/**
 * Generating XML structure of file for validation report
 * Created by bezrukov on 5/8/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public final class XMLValidationReport {

    private XMLValidationReport(){

    }

    /**
     * Creates tree of xml tags for validation report
     * @param info - validation info model to be writed
     * @param doc - document used for writing xml in further
     * @return root element of the xml structure
     */
    public static Element makeXMLTree(ValidationInfo info, Document doc){

        Element validationInfo = doc.createElement("validationInfo");

        Element profile = doc.createElement("profile");
        validationInfo.appendChild(profile);

        Element profileName = doc.createElement("name");
        profileName.appendChild(doc.createTextNode(info.getProfile().getName()));
        profile.appendChild(profileName);

        Element profileHash = doc.createElement("hash");
        profileHash.appendChild(doc.createTextNode(info.getProfile().getHash()));
        profile.appendChild(profileHash);

        Element result = doc.createElement("result");
        validationInfo.appendChild(result);

        Element compliant = doc.createElement("compliant");
        compliant.appendChild(doc.createTextNode(Boolean.toString(info.getResult().isCompliant())));
        result.appendChild(compliant);

        Element statement = doc.createElement("statement");
        statement.appendChild(doc.createTextNode(info.getResult().getStatement()));
        result.appendChild(statement);

        Element summary = doc.createElement("summary");
        summary.setAttribute("passedRules", Integer.toString(info.getResult().getSummary().getAttrPassedRules()));
        summary.setAttribute("failedRules", Integer.toString(info.getResult().getSummary().getAttrFailedRules()));
        summary.setAttribute("passedChecks", Integer.toString(info.getResult().getSummary().getAttrPassedChecks()));
        summary.setAttribute("failedChecks", Integer.toString(info.getResult().getSummary().getAttrFailedChecks()));
        summary.setAttribute("completedMetadataFixes", Integer.toString(info.getResult().getSummary().getAttrCompletedMetadataFixes()));
        summary.setAttribute("failedMetadataFixes", Integer.toString(info.getResult().getSummary().getAttrFailedMetadataFixes()));
        summary.setAttribute("warnings", Integer.toString(info.getResult().getSummary().getAttrWarnings()));
        result.appendChild(summary);

        Element details = doc.createElement("details");
        result.appendChild(details);

        Element rules = doc.createElement("rules");
        details.appendChild(rules);

        makeRules(info, doc, rules);

        Element warnings = doc.createElement("warnings");
        details.appendChild(warnings);

        for(String war : info.getResult().getDetails().getWarnings()){
            Element warning = doc.createElement("warning");
            warning.appendChild(doc.createTextNode(war));
            warnings.appendChild(warning);
        }

        return validationInfo;
    }

    private static void makeRules(ValidationInfo info, Document doc, Element rules){
        for(Rule rul : info.getResult().getDetails().getRules()){
            Element rule = doc.createElement("rule");

            rule.setAttribute("id", rul.getAttrID());
            rule.setAttribute("status", rul.getAttrStatus());
            rule.setAttribute("checks", Integer.toString(rul.getAttrChecks()));

            for(Check che : rul.getChecks()){
                Element check = doc.createElement("check");

                check.setAttribute("status", che.getAttrStatus());

                Element location = doc.createElement("location");
                location.setAttribute("level", che.getLocation().getAttrLevel());
                Element context = doc.createElement("context");

                context.appendChild(doc.createTextNode(che.getLocation().getContext()));

                location.appendChild(context);
                check.appendChild(location);

                if (che.getError() != null){
                    String errorName = che.isHasError() ? "error" : "warning";
                    Element error = doc.createElement(errorName);

                    Element message = doc.createElement("message");
                    message.appendChild(doc.createTextNode(che.getError().getMessage()));
                    error.appendChild(message);

                    for(String attr : che.getError().getArgument()){
                        Element argument = doc.createElement("argument");
                        argument.appendChild(doc.createTextNode(attr));
                        error.appendChild(argument);
                    }

                    check.appendChild(error);
                }

                rule.appendChild(check);
            }

            rules.appendChild(rule);
        }
    }

    /**
     * Write the given validation info into xml formatted report.
     * @param info - validation info model to be writed
     * @param path - the path for output the resulting document. Path have to ends with file name with extension.
     * @throws ParserConfigurationException - if a DocumentBuilder cannot be created which satisfies the configuration requested.
     * @throws TransformerFactoryConfigurationError - thrown in case of service configuration error or if the implementation is not available or cannot be instantiated or when it is not possible to create a Transformer instance.
     * @throws TransformerException - if an unrecoverable error occurs during the course of the transformation or
     * @throws FileNotFoundException - if the file with path {@code path} exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public static void writeXMLValidationReport(ValidationInfo info, String path) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, FileNotFoundException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.newDocument();

        doc.appendChild(makeXMLTree(info, doc));

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        try (FileOutputStream fos = new FileOutputStream(path)) {
            transformer.transform(new DOMSource(doc), new StreamResult(fos));
        } catch (IOException excep) {
        	// TODO: Review this exception handling
			// Thrown on failure to close fos so do nothing
		}
    }

    /**
     * Write the given validation info into xml formatted report.
     * @param info - validation info model to be writed
     * @return {@code String} representation of the resulting xml report
     * @throws ParserConfigurationException - if a DocumentBuilder cannot be created which satisfies the configuration requested.
     * @throws TransformerFactoryConfigurationError - thrown in case of service configuration error or if the implementation is not available or cannot be instantiated or when it is not possible to create a Transformer instance.
     * @throws TransformerException - if an unrecoverable error occurs during the course of the transformation or
     * @throws FileNotFoundException - if the file with path {@code path} exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public static String getXMLValidationReportAsString(ValidationInfo info) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, FileNotFoundException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.newDocument();

        doc.appendChild(makeXMLTree(info, doc));

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.getBuffer().toString();
    }

}
