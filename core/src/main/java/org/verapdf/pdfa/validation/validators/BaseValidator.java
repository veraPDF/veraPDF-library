/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
/**
 *
 */
package org.verapdf.pdfa.validation.validators;

import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.ScriptableObject;
import org.verapdf.component.ComponentDetails;
import org.verapdf.component.Components;
import org.verapdf.core.ModelParsingException;
import org.verapdf.core.ValidationException;
import org.verapdf.core.utils.ValidationProgress;
import org.verapdf.model.baselayer.Object;
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.Location;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.profiles.*;
import org.verapdf.processor.reports.enums.JobEndStatus;

import java.net.URI;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public class BaseValidator implements PDFAValidator {
	private static final Logger LOGGER = Logger.getLogger(BaseValidator.class.getCanonicalName());
	public static final int DEFAULT_MAX_NUMBER_OF_DISPLAYED_FAILED_CHECKS = 100;
	private static final int MAX_CHECKS_NUMBER = 10_000;
	private static final URI componentId = URI.create("http://pdfa.verapdf.org/validators#default");
	private static final String componentName = "veraPDF Validator";
	private static final ComponentDetails componentDetails = Components.libraryDetails(componentId, componentName);
	List<FlavourValidator> validators = new LinkedList<>();
	private ScriptableObject scope;

	private final Deque<Object> objectsStack = new ArrayDeque<>();
	private final Deque<String> objectsContext = new ArrayDeque<>();
	protected volatile boolean abortProcessing = false;
	protected final boolean logPassedChecks;
	protected final int maxNumberOfDisplayedFailedChecks;
	private boolean showErrorMessages = false;
	protected final ValidationProgress validationProgress;
	protected volatile JobEndStatus jobEndStatus = JobEndStatus.NORMAL;

	private final Set<String> idSet = new HashSet<>();

	protected String rootType;

	protected BaseValidator(final ValidationProfile profile) {
		this(profile, false);
	}

	protected BaseValidator(final List<ValidationProfile> profiles) {
		this(profiles, DEFAULT_MAX_NUMBER_OF_DISPLAYED_FAILED_CHECKS, false, false, false);
	}

	protected BaseValidator(final ValidationProfile profile, final boolean logPassedChecks) {
		this(profile, DEFAULT_MAX_NUMBER_OF_DISPLAYED_FAILED_CHECKS, logPassedChecks, false, false);
	}

	protected BaseValidator(final ValidationProfile profile, final int maxNumberOfDisplayedFailedChecks,
							final boolean logPassedChecks, final boolean showErrorMessages, boolean showProgress) {
		this(Collections.singletonList(profile), maxNumberOfDisplayedFailedChecks, logPassedChecks, showErrorMessages, showProgress);
	}

	protected BaseValidator(final List<ValidationProfile> profiles, final int maxNumberOfDisplayedFailedChecks,
							final boolean logPassedChecks, final boolean showErrorMessages, boolean showProgress) {
		super();
		createCompatibleValidators(profiles);
		this.maxNumberOfDisplayedFailedChecks = maxNumberOfDisplayedFailedChecks;
		this.logPassedChecks = logPassedChecks;
		this.showErrorMessages = showErrorMessages;
		this.validationProgress = new ValidationProgress(showProgress);
	}

	private void createCompatibleValidators(List<ValidationProfile> profiles) {
		if (profiles.isEmpty()) {
			return;
		}
		PDFAFlavour flavour = profiles.get(0).getPDFAFlavour();
		PDFAFlavour.PDFSpecification pdfSpecification = flavour.getPart().getPdfSpecification();
		for (ValidationProfile profile : profiles) {
			PDFAFlavour currentFlavour = profile.getPDFAFlavour();
			PDFAFlavour.PDFSpecification currentPDFSpecification = currentFlavour.getPart().getPdfSpecification();
			if (pdfSpecification == currentPDFSpecification) {
				validators.add(new FlavourValidator(profile));
			} else {
				LOGGER.log(Level.WARNING,String.format("PDF version %s of detected flavour %s is incompatible with the PDF version %s of other detected flavour %s. The validation of flavour %s is skipped",
						currentPDFSpecification, currentFlavour, pdfSpecification, flavour, currentFlavour));
			}
		}
	}
	

	@Override
	public ValidationProfile getProfile() {
		return this.validators.isEmpty() ? null : this.validators.get(0).getProfile();
	}
	
	private List<PDFAFlavour> getFlavours() {
		List<PDFAFlavour> flavours = new LinkedList<>();
		for (FlavourValidator validator : validators) {
			flavours.add(validator.getProfile().getPDFAFlavour());
		}
		return flavours;
	}

	@Override
	public ValidationResult validate(PDFAParser toValidate) throws ValidationException {
		if (validators.isEmpty()) {
			return null;
		}
		validators = Collections.singletonList(validators.get(0));
		List<ValidationResult> validationResults = validateAll(toValidate);
		return validationResults.get(0);
	}

	@Override
	public List<ValidationResult> validateAll(PDFAParser toValidate) throws ValidationException {
		toValidate.setFlavours(getFlavours());
		try {
			return this.validate(toValidate.getRoot());
		} catch (RuntimeException e) {
			throw new ValidationException("Caught unexpected runtime exception during validation", e);
		} catch (ModelParsingException excep) {
			throw new ValidationException("Parsing problem trying to validate.", excep);
		}
	}

	@Override
	public ComponentDetails getDetails() {
		return componentDetails;
	}

	@Override
	public String getValidationProgressString() {
		return validationProgress.getCurrentValidationJobProgressWithCommas();
	}

	@Override
	public void cancelValidation(JobEndStatus endStatus) {
		this.jobEndStatus = endStatus;
		this.abortProcessing = true;
	}

	protected List<ValidationResult> validate(Object root) throws ValidationException {
		initialise();
		this.validationProgress.updateVariables();
		this.rootType = root.getObjectType();
		this.objectsStack.push(root);
		this.objectsContext.push("root");

		if (root.getID() != null) {
			this.idSet.add(root.getID());
		}

		while (!this.objectsStack.isEmpty() && !this.abortProcessing) {
			checkNext();
			this.validationProgress.incrementNumberOfProcessedObjects();
			this.validationProgress.updateNumberOfObjectsToBeProcessed(objectsStack.size());
		}

		for (FlavourValidator validator : validators) {
			for (Map.Entry<Rule, List<ObjectWithContext>> entry : validator.getDeferredRules().entrySet()) {
				for (ObjectWithContext objectWithContext : entry.getValue()) {
					checkObjWithRule(validator, objectWithContext.getObject(), objectWithContext.getContext(), entry.getKey());
				}
			}			
		}

		this.validationProgress.showProgressAfterValidation();

		JavaScriptEvaluator.exitContext();

		List<ValidationResult> results = new LinkedList<>();
		for (FlavourValidator validator : validators) {
			results.add(ValidationResults.resultFromValues(validator.getProfile(), validator.results, validator.getFailedChecks(), validator.isCompliant,
					validator.testCounter, this.jobEndStatus));
		}
		return results;
	}

	protected void initialise() {
		this.scope = JavaScriptEvaluator.initialise();
		this.objectsStack.clear();
		this.objectsContext.clear();
		this.idSet.clear();
		for (FlavourValidator validator : validators) {
			validator.getFailedChecks().clear();
			validator.getDeferredRules().clear();
			validator.results.clear();
			validator.testCounter = 0;
			validator.isCompliant = true;
		}
		initializeAllVariables();
	}

	private void initializeAllVariables() {
		for (FlavourValidator flavourValidator : validators) {
			for (Variable var : flavourValidator.getProfile().getVariables()) {
				if (var == null) {
					continue;
				}

				java.lang.Object res = JavaScriptEvaluator.evaluateString(var.getDefaultValue(), this.scope);

				if (res instanceof NativeJavaObject) {
					res = ((NativeJavaObject) res).unwrap();
				}
				this.scope.put(var.getName(), this.scope, res);
			}			
		}
	}

	private void checkNext() throws ValidationException {
		Object checkObject = this.objectsStack.pop();
		String checkContext = this.objectsContext.pop();

		checkAllRules(checkObject, checkContext);

		updateVariables(checkObject);

		addAllLinkedObjects(checkObject, checkContext);
	}

	private void updateVariables(Object object) {
		if (object != null) {
			updateVariableForObjectWithType(object, object.getObjectType());

			for (String parentName : object.getSuperTypes()) {
				updateVariableForObjectWithType(object, parentName);
			}
		}
	}

	private void updateVariableForObjectWithType(Object object, String objectType) {
		for (FlavourValidator flavourValidator : validators) {
			for (Variable var : flavourValidator.getProfile().getVariablesByObject(objectType)) {
				if (var == null) {
					continue;
				}
				java.lang.Object variable = JavaScriptEvaluator.evalVariableResult(var, object, this.scope);

				this.scope.put(var.getName(), this.scope, variable);
			}
		}
	}

	private void addAllLinkedObjects(Object checkObject, String checkContext)
			throws ValidationException {
		List<String> links = checkObject.getLinks();
		for (int j = links.size() - 1; j >= 0; --j) {
			String link = links.get(j);

			if (link == null) {
				throw new ValidationException("There is a null link name in an object. Context: " + checkContext);
			}
			List<? extends Object> objects = checkObject.getLinkedObjects(link);
			if (objects == null) {
				throw new ValidationException("There is a null link in an object. Context: " + checkContext);
			}

			for (int i = objects.size() - 1; i >= 0; --i) {
				Object obj = objects.get(i);

				StringBuilder path = new StringBuilder(checkContext);
				path.append("/");
				path.append(link);
				path.append("[");
				path.append(i);
				path.append("]");

				if (obj == null) {
					throw new ValidationException("There is a null link in an object. Context of the link: " + path);
				}

				if (checkRequired(obj)) {
					this.objectsStack.push(obj);

					if (obj.getID() != null) {
						path.append("(");
						path.append(obj.getID());
						path.append(")");

						this.idSet.add(obj.getID());
					}

					if (obj.getExtraContext() != null) {
						path.append("{");
						path.append(obj.getExtraContext());
						path.append("}");
					}

					this.objectsContext.push(path.toString());
				}
			}
		}
	}

	private boolean checkRequired(Object obj) {
		return obj.getID() == null || !this.idSet.contains(obj.getID());
	}

	private boolean checkAllRules(Object checkObject, String checkContext) {
		boolean res = true;
		for (FlavourValidator flavourValidator : validators) {
			Set<Rule> roolsForObject = flavourValidator.getProfile().getRulesByObject(checkObject.getObjectType());
			for (Rule rule : roolsForObject) {
				res &= firstProcessObjectWithRule(flavourValidator, checkObject, checkContext, rule);
			}
			for (String checkType : checkObject.getSuperTypes()) {
				roolsForObject = flavourValidator.getProfile().getRulesByObject(checkType);
				if (roolsForObject != null) {
					for (Rule rule : roolsForObject) {
						if (rule != null) {
							res &= firstProcessObjectWithRule(flavourValidator, checkObject, checkContext, rule);
						}
					}
				}
			}
		}
		return res;
	}

	public boolean firstProcessObjectWithRule(FlavourValidator flavourValidator, Object checkObject, String checkContext, Rule rule) {
		Boolean deferred = rule.getDeferred();
		if (deferred != null && deferred) {
			List<BaseValidator.ObjectWithContext> list = flavourValidator.getDeferredRules().computeIfAbsent(rule, k -> new ArrayList<>());
			list.add(new BaseValidator.ObjectWithContext(checkObject, checkContext));
			return true;
		}
		return checkObjWithRule(flavourValidator, checkObject, checkContext, rule);
	}

	private boolean checkObjWithRule(FlavourValidator flavourValidator, Object obj, String contextForRule, Rule rule) {
		boolean testEvalResult = JavaScriptEvaluator.getTestEvalResult(obj, rule, this.scope);

		this.processAssertionResult(flavourValidator, testEvalResult, contextForRule, rule, obj);

		this.validationProgress.updateNumberOfFailedChecks(flavourValidator.getFailedChecks().size());
		this.validationProgress.incrementNumberOfChecks();

		return testEvalResult;
	}

	protected void processAssertionResult(FlavourValidator flavourValidator, final boolean assertionResult, final String locationContext,
										  final Rule rule, final Object obj) {
		if (!this.abortProcessing) {
			flavourValidator.testCounter++;
			if (flavourValidator.isCompliant) {
				flavourValidator.isCompliant = assertionResult;
			}
			if (!assertionResult) {
				int failedChecksNumberOfRule = flavourValidator.getFailedChecks().getOrDefault(rule.getRuleId(), 0);
				flavourValidator.getFailedChecks().put(rule.getRuleId(), ++failedChecksNumberOfRule);
				if ((failedChecksNumberOfRule <= maxNumberOfDisplayedFailedChecks || maxNumberOfDisplayedFailedChecks == -1) &&
						(flavourValidator.results.size() <= MAX_CHECKS_NUMBER || failedChecksNumberOfRule <= 1)) {
					Location location = ValidationResults.locationFromValues(this.rootType, locationContext);
					List<ErrorArgument> errorArguments = showErrorMessages ?
							JavaScriptEvaluator.getErrorArgumentsResult(obj, rule.getError().getArguments(),
									this.scope) : Collections.emptyList();
					String errorMessage = showErrorMessages ? createErrorMessage(rule.getError().getMessage(), errorArguments) : null;
					TestAssertion assertion = ValidationResults.assertionFromValues(flavourValidator.testCounter, rule.getRuleId(),
							Status.FAILED, rule.getDescription(), location, obj.getContext(), errorMessage, errorArguments);
					flavourValidator.results.add(assertion);
				}
			} else if (this.logPassedChecks && flavourValidator.results.size() <= MAX_CHECKS_NUMBER) {
				Location location = ValidationResults.locationFromValues(this.rootType, locationContext);
				TestAssertion assertion = ValidationResults.assertionFromValues(flavourValidator.testCounter, rule.getRuleId(),
						Status.PASSED, rule.getDescription(), location, obj.getContext(), null, Collections.emptyList());
				flavourValidator.results.add(assertion);
			}
		}
	}

	private String createErrorMessage(String errorMessage, List<ErrorArgument> arguments) {
		String result = errorMessage;
		for (int i = arguments.size(); i > 0 ; --i) {
			ErrorArgument argument = arguments.get(i - 1);
			String value = argument.getArgumentValue() != null ? argument.getArgumentValue() : "null";
			result = result.replace("%" + argument.getName() + "%", value);
			result = result.replace("%" + i, value);
		}
		return result;
	}

	@Override
	public void close() {
		/**
		 * Empty
		 */
	}

	public static class ObjectWithContext {
		private final Object object;
		private final String context;

		public ObjectWithContext(Object object, String context) {
			this.object = object;
			this.context = context;
		}

		public Object getObject() {
			return this.object;
		}

		public String getContext() {
			return this.context;
		}
	}
}
