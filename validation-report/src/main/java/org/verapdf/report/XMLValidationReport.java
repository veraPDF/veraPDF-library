package org.verapdf.report;

import org.verapdf.validation.report.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;


/**
 * Generating XML structure of file for validation report
 *
 * @author Maksim Bezrukov
 */
public final class XMLValidationReport {

    private XMLValidationReport() {

    }

    /**
     * Creates tree of xml tags for validation report
     *
     * @param info - validation info model to be writed
     * @param doc  - document used for writing xml in further
     * @return root element of the xml structure
     */
    public static Element makeXMLTree(ValidationInfo info, Document doc) {

        Element validationInfo = doc.createElement("validationInfo");

        if (info != null) {

            makeProfileInformation(info.getProfile(), doc, validationInfo);

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

                makeDetails(info.getResult().getDetails(), doc, result);
            }
        }
        return validationInfo;
    }

    private static void makeProfileInformation(Profile profile, Document doc, Element validationInfo) {
        if (profile != null) {
            Element profileElement = doc.createElement("profile");
            validationInfo.appendChild(profileElement);

            if (profile.getName() != null) {
                Element profileName = doc.createElement("name");
                profileName.appendChild(doc.createTextNode(profile.getName()));
                profileElement.appendChild(profileName);
            }

            if (profile.getHash() != null) {
                Element profileHash = doc.createElement("hash");
                profileHash.appendChild(doc.createTextNode(profile.getHash()));
                profileElement.appendChild(profileHash);
            }
        }
    }

    private static void makeDetails(Details details, Document doc, Element result) {
        if (details != null) {
            Element detailsElement = doc.createElement("details");
            result.appendChild(detailsElement);

            if (details.getRules() != null) {
                Element rules = doc.createElement("rules");
                detailsElement.appendChild(rules);

                makeRules(details.getRules(), doc, rules);
            }

            if (details.getWarnings() != null) {
                Element warnings = doc.createElement("warnings");
                detailsElement.appendChild(warnings);

                for (String war : details.getWarnings()) {
                    if (war != null) {
                        Element warning = doc.createElement("warning");
                        warning.appendChild(doc.createTextNode(war));
                        warnings.appendChild(warning);
                    }
                }
            }
        }
    }

    private static void makeRules(List<Rule> rulesList, Document doc, Element rules) {
        for (Rule rul : rulesList) {

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
                        makeChecks(che, doc, rule);
                    }
                }

                rules.appendChild(rule);
            }
        }
    }

    private static void makeChecks(Check che, Document doc, Element rule) {
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

            makeCheckError(che, doc, check);

            rule.appendChild(check);
        }
    }

    private static void makeCheckError(Check check, Document doc, Element checkElement) {
        if (check.getError() != null) {
            String errorName = check.isHasError() ? "error" : "warning";
            Element error = doc.createElement(errorName);

            if (check.getError().getMessage() != null) {
                Element message = doc.createElement("message");
                message.appendChild(doc.createTextNode(getFormattedMessage(check.getError().getMessage(), check.getError().getArguments())));
                error.appendChild(message);
            }

            checkElement.appendChild(error);
        }
    }

    private static String getFormattedMessage(String message, List<String> arguments) {
        if (arguments == null || arguments.isEmpty()) {
            return message;
        }
        StringBuilder buffer = new StringBuilder(message);

        for (int i = 0; i < buffer.length(); ++i) {
            if (buffer.charAt(i++) == '%' && buffer.charAt(i) != '0' && Character.isDigit(buffer.charAt(i)) && !Character.isDigit(buffer.charAt(i + 1))) {
                int argumentNumber = Character.getNumericValue(buffer.charAt(i)) - 1;
                String argumentValue = arguments.get(argumentNumber);
                buffer.replace(i - 1, i + 1, argumentValue);
                i += argumentValue.length() - 2;
            }
        }

        return buffer.toString();
    }

}
