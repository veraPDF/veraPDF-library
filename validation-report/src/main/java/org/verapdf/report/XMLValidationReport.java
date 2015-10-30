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
	 * @param info              validation info model to be writed
	 * @param doc               document used for writing xml in further
	 * @param isLogPassedChecks is log passed checks for result report
	 * @return root element of the xml structure
	 */
	public static Element makeXMLTree(ValidationInfo info, Document doc, boolean isLogPassedChecks) {

		Element validationInfo = doc.createElement("validationInfo");

		if (info != null) {

			makeProfileInformation(info.getProfile(), doc, validationInfo);

			if (info.getResult() != null) {
				Element result = doc.createElement("result");
				validationInfo.appendChild(result);

				Element compliant = doc.createElement("compliant");
				compliant.appendChild(doc.createTextNode(Boolean.toString(info
						.getResult().isCompliant())));
				result.appendChild(compliant);

				if (info.getResult().getStatement() != null) {
					Element statement = doc.createElement("statement");
					statement.appendChild(doc.createTextNode(info.getResult()
							.getStatement()));
					result.appendChild(statement);
				}

				if (info.getResult().getSummary() != null) {
					Element summaryElement = doc.createElement("summary");
					Summary summary = info.getResult().getSummary();
					summaryElement.setAttribute(
							"passedRules",
							Integer.toString(summary.getAttrPassedRules()));
					summaryElement.setAttribute(
							"failedRules",
							Integer.toString(summary.getAttrFailedRules()));
					summaryElement.setAttribute(
							"passedChecks",
							Integer.toString(summary.getAttrPassedChecks()));
					summaryElement.setAttribute(
							"failedChecks",
							Integer.toString(summary.getAttrFailedChecks()));

					if (!summary.getAttrMetadataFixerResult().isEmpty()) {
						summaryElement.setAttribute(
								"metadataFixesStatus",
								summary.getAttrMetadataFixerResult()
						);
						if (summary.getAttrCompletedMetadataFixes() >= 0) {
							summaryElement.setAttribute(
									"completedMetadataFixes",
									Integer.toString(summary.getAttrCompletedMetadataFixes()));
						}
					}

					summaryElement.setAttribute(
							"warnings",
							Integer.toString(summary.getAttrWarnings()));
					result.appendChild(summaryElement);
				}

				makeDetails(info.getResult().getDetails(), doc, result, isLogPassedChecks);
			}
		}
		return validationInfo;
	}

	private static void makeProfileInformation(Profile profile, Document doc,
											   Element validationInfo) {
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

	private static void makeDetails(Details details, Document doc,
									Element result, boolean isLogPassedChecks) {
		if (details != null) {
			Element detailsElement = doc.createElement("details");
			result.appendChild(detailsElement);

			if (details.getRules() != null) {
				Element rules = doc.createElement("rules");
				detailsElement.appendChild(rules);

				makeRules(details.getRules(), doc, rules, isLogPassedChecks);
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

	private static void makeRules(List<Rule> rulesList, Document doc,
								  Element rules, boolean isLogPassedChecks) {
		for (Rule rul : rulesList) {

			if (rul != null) {
				if (isLogPassedChecks || rul.getFailedChecksCount() > 0) {
					Element rule = doc.createElement("rule");

					if (rul.getID() != null) {
						rule.setAttribute("id", rul.getID());
					}
					rule.setAttribute("status", rul.getStatus().toString());

					rule.setAttribute("passedChecks",
							Integer.toString(rul.getPassedChecksCount()));
					rule.setAttribute("failedChecks",
							Integer.toString(rul.getFailedChecksCount()));

					if (rul.getChecks() != null) {
						for (Check che : rul.getChecks()) {
							makeChecks(che, doc, rule);
						}
					}

					rules.appendChild(rule);
				}
			}
		}
	}

	private static void makeChecks(Check chck, Document doc, Element rule) {
		if (chck == null) {
			return;
		}

		Element check = doc.createElement("check");

		check.setAttribute("status", chck.getStatus().toString());

		if (chck.getLocation() != null) {
			Element location = doc.createElement("location");

			if (chck.getLocation().getAttrLevel() != null) {
				location.setAttribute("level", chck.getLocation()
						.getAttrLevel());
			}

			if (chck.getLocation().getContext() != null) {
				Element context = doc.createElement("context");
				context.appendChild(doc.createTextNode(chck.getLocation()
						.getContext()));
				location.appendChild(context);
			}

			check.appendChild(location);
		}

		makeCheckError(chck, doc, check);

		rule.appendChild(check);
	}

	private static void makeCheckError(Check check, Document doc,
									   Element checkElement) {
		if (check.getError() != null) {
			Element error = doc.createElement("error");

			if (check.getError().getMessage() != null) {
				Element message = doc.createElement("message");
				message.appendChild(doc.createTextNode(getFormattedMessage(
						check.getError().getMessage(), check.getError()
								.getArguments())));
				error.appendChild(message);
			}

			checkElement.appendChild(error);
		}
	}

	private static String getFormattedMessage(String message,
											  List<String> arguments) {
		if (arguments == null || arguments.isEmpty()) {
			return message;
		}
		StringBuilder buffer = new StringBuilder(message);

		for (int i = 0; i < buffer.length() - 1; ++i) {
			if (buffer.charAt(i) == '%'
					&& buffer.charAt(++i) != '0'
					&& Character.isDigit(buffer.charAt(i))
					&& (i + 1 == buffer.length() || !Character.isDigit(buffer.charAt(i + 1)))) {
				int argumentNumber = Character
						.getNumericValue(buffer.charAt(i)) - 1;
				String argumentValue = arguments.get(argumentNumber);
				buffer.replace(i - 1, i + 1, argumentValue);
				i += argumentValue.length() - 2;
			}
		}

		return buffer.toString();
	}

}
