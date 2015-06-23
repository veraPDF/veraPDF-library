package org.verapdf.report;

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
import java.util.List;


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

        if (info != null) {
            if (info.getProfile() != null) {
                Element profile = doc.createElement("profile");
                validationInfo.appendChild(profile);

                if (info.getProfile().getName() != null) {
                    Element profileName = doc.createElement("name");
                    profileName.appendChild(doc.createTextNode(info.getProfile().getName()));
                    profile.appendChild(profileName);
                }

                if (info.getProfile().getHash() != null) {
                    Element profileHash = doc.createElement("hash");
                    profileHash.appendChild(doc.createTextNode(info.getProfile().getHash()));
                    profile.appendChild(profileHash);
                }
            }

            if (info.getResult() != null) {
                Element result = doc.createElement("result");
                validationInfo.appendChild(result);

                Element compliant = doc.createElement("compliant");
                compliant.appendChild(doc.createTextNode(Boolean.toString(info.getResult().isCompliant())));
                result.appendChild(compliant);

                if (info.getResult().getStatement() != null) {
                    Element statement = doc.createElement("statement");
                    statement.appendChild(doc.createTextNode(info.getResult().getStatement()));
                    result.appendChild(statement);
                }

                if (info.getResult().getSummary() != null) {
                    Element summary = doc.createElement("summary");
                    summary.setAttribute("passedRules", Integer.toString(info.getResult().getSummary().getAttrPassedRules()));
                    summary.setAttribute("failedRules", Integer.toString(info.getResult().getSummary().getAttrFailedRules()));
                    summary.setAttribute("passedChecks", Integer.toString(info.getResult().getSummary().getAttrPassedChecks()));
                    summary.setAttribute("failedChecks", Integer.toString(info.getResult().getSummary().getAttrFailedChecks()));
                    summary.setAttribute("completedMetadataFixes", Integer.toString(info.getResult().getSummary().getAttrCompletedMetadataFixes()));
                    summary.setAttribute("failedMetadataFixes", Integer.toString(info.getResult().getSummary().getAttrFailedMetadataFixes()));
                    summary.setAttribute("warnings", Integer.toString(info.getResult().getSummary().getAttrWarnings()));
                    result.appendChild(summary);
                }

                if (info.getResult().getDetails() != null) {
                    Element details = doc.createElement("details");
                    result.appendChild(details);

                    if (info.getResult().getDetails().getRules() != null) {
                        Element rules = doc.createElement("rules");
                        details.appendChild(rules);

                        makeRules(info, doc, rules);
                    }

                    if (info.getResult().getDetails().getWarnings() != null) {
                        Element warnings = doc.createElement("warnings");
                        details.appendChild(warnings);

                        for (String war : info.getResult().getDetails().getWarnings()) {
                            if (war != null) {
                                Element warning = doc.createElement("warning");
                                warning.appendChild(doc.createTextNode(war));
                                warnings.appendChild(warning);
                            }
                        }
                    }
                }
            }
        }
        return validationInfo;
    }

    private static void makeRules(ValidationInfo info, Document doc, Element rules){
        for(Rule rul : info.getResult().getDetails().getRules()){

            if (rul != null) {
                Element rule = doc.createElement("rule");

                if (rul.getAttrID() != null) {
                    rule.setAttribute("id", rul.getAttrID());
                }
                if (rul.getAttrStatus() != null) {
                    rule.setAttribute("status", rul.getAttrStatus());
                }

                rule.setAttribute("checks", Integer.toString(rul.getAttrChecks()));

                if (rul.getChecks() != null) {
                    for (Check che : rul.getChecks()) {

                        if (che != null) {
                            Element check = doc.createElement("check");

                            if (che.getAttrStatus() != null) {
                                check.setAttribute("status", che.getAttrStatus());
                            }

                            if (che.getLocation() != null) {
                                Element location = doc.createElement("location");

                                if (che.getLocation().getAttrLevel() != null) {
                                    location.setAttribute("level", che.getLocation().getAttrLevel());
                                }

                                if (che.getLocation().getContext() != null) {
                                    Element context = doc.createElement("context");
                                    context.appendChild(doc.createTextNode(che.getLocation().getContext()));
                                    location.appendChild(context);
                                }

                                check.appendChild(location);
                            }

                            if (che.getError() != null) {
                                String errorName = che.isHasError() ? "error" : "warning";
                                Element error = doc.createElement(errorName);

                                if (che.getError().getMessage() != null) {
                                    Element message = doc.createElement("message");
//                                    message.appendChild(doc.createTextNode(che.getError().getMessage()));
                                    message.appendChild(doc.createTextNode(getFormattedMessage(che.getError().getMessage(), che.getError().getArgument())));
                                    error.appendChild(message);
                                }

//                                if (che.getError().getArgument() != null) {
//                                    for (String attr : che.getError().getArgument()) {
//                                        if (attr != null) {
//                                            Element argument = doc.createElement("argument");
//                                            argument.appendChild(doc.createTextNode(attr));
//                                            error.appendChild(argument);
//                                        }
//                                    }
//                                }

                                check.appendChild(error);
                            }

                            rule.appendChild(check);
                        }
                    }
                }

                rules.appendChild(rule);
            }
        }
    }

    private static String getFormattedMessage(String message, List<String> arguments){
        if (arguments == null || arguments.isEmpty()) {
            return message;
        } else {

            StringBuffer buffer = new StringBuffer(message);

            for (int i = 0; i < buffer.length(); ++i) {
                if (buffer.charAt(i) == '%') {
                    if (buffer.charAt(++i) != '0' && Character.isDigit(buffer.charAt(i)) && !Character.isDigit(buffer.charAt(i + 1))) {
                        int argumentNumber = Character.getNumericValue(buffer.charAt(i)) - 1;
                        String argumentValue = arguments.get(argumentNumber);
                        buffer.replace(i - 1, i + 1, argumentValue);
                        i += argumentValue.length() - 2;

                    }
                }
            }

            return buffer.toString();
        }
    }

}
