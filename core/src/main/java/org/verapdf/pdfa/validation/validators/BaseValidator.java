/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 * <p>
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 * <p>
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 * <p>
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
import org.verapdf.model.baselayer.Object;
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.results.Location;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.profiles.Rule;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;
import org.verapdf.pdfa.validation.profiles.Variable;

import java.net.URI;
import java.util.*;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
class BaseValidator implements PDFAValidator {
	private static final URI componentId = URI.create("http://pdfa.verapdf.org/validators#default");
	private static final String componentName = "veraPDF PDF/A Validator";
	private static final ComponentDetails componentDetails = Components.libraryDetails(componentId, componentName);
	private final ValidationProfile profile;
	private ScriptableObject scope;

	private final Deque<Object> objectsStack = new ArrayDeque<>();
	private final Deque<String> objectsContext = new ArrayDeque<>();
	private final Map<Rule, List<ObjectWithContext>> deferredRules = new HashMap<>();
	protected final List<TestAssertion> results = new ArrayList<>();
	protected int testCounter = 0;
	protected boolean abortProcessing = false;
	protected final boolean logPassedTests;
	protected boolean isCompliant = true;

	private Set<String> idSet = new HashSet<>();

	protected String rootType;

	protected BaseValidator(final ValidationProfile profile) {
		this(profile, false);
	}

	protected BaseValidator(final ValidationProfile profile, final boolean logPassedTests) {
		super();
		this.profile = profile;
		this.logPassedTests = logPassedTests;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.verapdf.pdfa.PDFAValidator#getProfile()
	 */
	@Override
	public ValidationProfile getProfile() {
		return this.profile;
	}

	@Override
	public ValidationResult validate(PDFAParser toValidate) throws ValidationException {
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

	protected ValidationResult validate(Object root) throws ValidationException {
		initialise();
		this.rootType = root.getObjectType();
		this.objectsStack.push(root);
		this.objectsContext.push("root");

		if (root.getID() != null) {
			this.idSet.add(root.getID());
		}

		while (!this.objectsStack.isEmpty() && !this.abortProcessing) {
			checkNext();
		}

		for (Map.Entry<Rule, List<ObjectWithContext>> entry : this.deferredRules.entrySet()) {
			for (ObjectWithContext objectWithContext : entry.getValue()) {
				checkObjWithRule(objectWithContext.getObject(), objectWithContext.getContext(), entry.getKey());
			}
		}

		JavaScriptEvaluator.exitContext();

		return ValidationResults.resultFromValues(this.profile, this.results, this.isCompliant, this.testCounter);
	}

	protected void initialise() {
		this.scope = JavaScriptEvaluator.initialise();
		this.objectsStack.clear();
		this.objectsContext.clear();
		this.deferredRules.clear();
		this.results.clear();
		this.idSet.clear();
		this.testCounter = 0;
		this.isCompliant = true;
		initializeAllVariables();
	}

	private void initializeAllVariables() {
		for (Variable var : this.profile.getVariables()) {
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
		for (Variable var : this.profile.getVariablesByObject(objectType)) {
			if (var == null) {
				continue;
			}
			java.lang.Object variable = JavaScriptEvaluator.evalVariableResult(var, object, this.scope);

			this.scope.put(var.getName(), this.scope, variable);
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
		Set<Rule> roolsForObject = this.profile.getRulesByObject(checkObject.getObjectType());
		for (Rule rule : roolsForObject) {
			res &= firstProcessObjectWithRule(checkObject, checkContext, rule);
		}

		for (String checkType : checkObject.getSuperTypes()) {
			roolsForObject = this.profile.getRulesByObject(checkType);
			if (roolsForObject != null) {
				for (Rule rule : roolsForObject) {
					if (rule != null) {
						res &= firstProcessObjectWithRule(checkObject, checkContext, rule);
					}
				}
			}
		}
		return res;
	}

	private boolean firstProcessObjectWithRule(Object checkObject, String checkContext, Rule rule) {
		Boolean deferred = rule.getDeferred();
		if (deferred != null && deferred.booleanValue()) {
			List<ObjectWithContext> list = this.deferredRules.get(rule);
			if (list == null) {
				list = new ArrayList<>();
				this.deferredRules.put(rule, list);
			}
			list.add(new ObjectWithContext(checkObject, checkContext));
			return true;
		}
		return checkObjWithRule(checkObject, checkContext, rule);
	}

	private boolean checkObjWithRule(Object obj, String cntxtForRule, Rule rule) {
		boolean testEvalResult = JavaScriptEvaluator.getTestEvalResult(obj, rule, this.scope);

		this.processAssertionResult(testEvalResult, cntxtForRule, rule, obj);

		return testEvalResult;
	}

	protected void processAssertionResult(final boolean assertionResult, final String locationContext,
										  final Rule rule, final Object obj) {
		if (!this.abortProcessing) {
			this.testCounter++;
            //TODO rebuild structure of project for secure saving assertions without overflow memory exception with big files.
			if (this.isCompliant) {
                this.isCompliant = assertionResult;
            }
			if (!assertionResult || this.logPassedTests) {
				Location location;
				if (this.results.size() > 10_000) {
					location = ValidationResults.locationFromValues(this.rootType, null);
				} else {
					location = ValidationResults.locationFromValues(this.rootType, locationContext);
				}

				List<String> errorArguments = JavaScriptEvaluator.getErrorArgumentsResult(obj, rule.getError().getArguments(), this.scope);
				String errorMessage = createErrorMessage(rule.getError().getMessage(), errorArguments);
				TestAssertion assertion = ValidationResults.assertionFromValues(this.testCounter, rule.getRuleId(),
						assertionResult ? Status.PASSED : Status.FAILED, rule.getDescription(), location, null, errorMessage, errorArguments);
				this.results.add(assertion);
            }
		}
	}

	private String createErrorMessage(String errorMessage, List<String> arguments) {
		for (int i = arguments.size(); i > 0 ; --i) {
			errorMessage = errorMessage.replace("%" + i, arguments.get(i - 1));
		}
		return errorMessage;
	}

	@Override
	public void close() {
		/**
		 * Empty
		 */
	}

	private static class ObjectWithContext {
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
