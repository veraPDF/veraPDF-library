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
package org.verapdf.pdfa.validation.validators;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptableObject;
import org.verapdf.model.baselayer.Object;
import org.verapdf.pdfa.validation.profiles.ErrorArgument;
import org.verapdf.pdfa.validation.profiles.ErrorArgumentImpl;
import org.verapdf.pdfa.validation.profiles.Rule;
import org.verapdf.pdfa.validation.profiles.Variable;

import java.util.*;

public class JavaScriptEvaluator {
	private static final int OPTIMIZATION_LEVEL = 9;

	private static final ThreadLocal<Context> context = new ThreadLocal<>();

	private static final ThreadLocal<Map<String, Script>> ruleScripts = ThreadLocal.withInitial(HashMap::new);
	private static final ThreadLocal<Map<String, Script>> variableScripts = ThreadLocal.withInitial(HashMap::new);
	private static final ThreadLocal<Map<String, Script>> argumentScripts = ThreadLocal.withInitial(HashMap::new);

	public static ScriptableObject initialise() {
		final Context newContext = Context.enter();
		context.set(newContext);
		newContext.setOptimizationLevel(OPTIMIZATION_LEVEL);
		return newContext.initStandardObjects();
	}

	public static java.lang.Object evaluateString(String source, ScriptableObject scope) {
		return context.get().evaluateString(scope, source, null, 0, null);
	}

	public static java.lang.Object evalVariableResult(Variable variable, Object object, ScriptableObject scope) {
		Script script;
		if (!variableScripts.get().containsKey(variable.getName())) {
			String source = getStringScript(object, variable.getValue());
			script = JavaScriptEvaluator.compileString(source);

			variableScripts.get().put(variable.getName(), script);
		} else {
			script = variableScripts.get().get(variable.getName());
		}

		scope.put("obj", scope, object);

		java.lang.Object res = script.exec(context.get(), scope);

		if (res instanceof NativeJavaObject) {
			res = ((NativeJavaObject) res).unwrap();
		}
		return res;
	}

	private static Script compileString(String source) {
		return context.get().compileString(source, null, 0, null);
	}

	private static String getStringScript(Object obj, String test) {
		String resultTest = test;
		StringBuilder builder = new StringBuilder();
		String[] vars = test.split("\\W");
		for (String prop : obj.getProperties()) {
			resultTest = updateTest(builder, resultTest, vars, prop, "obj.get" + prop + "()");
		}
		for (String linkName : obj.getLinks()) {
			resultTest = updateTest(builder, resultTest, vars, linkName + "_size", "obj.getLinkedObjects(\"" + linkName + "\").size()");
		}
		builder.append("function test(){return ");
		builder.append(resultTest);
		builder.append(";}\ntest();");
		return builder.toString();
	}
	
	private static String updateTest(StringBuilder builder, String test, String[] vars, String varName, String methodCall) {
		int number = numberOfOccurrences(vars, varName);
		if (number > 1) {
			builder.append("var ");
			builder.append(varName);
			builder.append(" = ");
			builder.append(methodCall);
			builder.append(";\n");
		} else if (number == 1) {
			return test.replaceFirst("(?<!\\w)" + varName + "(?!\\w)", methodCall);
		}
		return test;
	}

	private static int numberOfOccurrences(String[] values, String prop) {
		int number = 0;
		for (String value : values) {
			if (value.equals(prop)) {
				number++;
			}
		}
		return number;
	}

	private static String getScript(Object obj, String test) {
		return getStringScript(obj, "(" + test + ")==true");
	}

	public static boolean getTestEvalResult(Object obj, Rule rule, ScriptableObject scope) {
		scope.put("obj", scope, obj);

		Script scr;
		String test = rule.getTest();
		if (!ruleScripts.get().containsKey(test)) {
			scr = compileString(getScript(obj, test));
			ruleScripts.get().put(test, scr);
		} else {
			scr = ruleScripts.get().get(test);
		}

		boolean testEvalResult = (Boolean) scr.exec(context.get(), scope);

		return testEvalResult;
	}

	private static String getErrorArgumentResult(String argument, Object obj, ScriptableObject scope) {
		Script scr;

		if (!argumentScripts.get().containsKey(argument)) {
			scr = JavaScriptEvaluator.compileString(getStringScript(obj, argument));
			argumentScripts.get().put(argument, scr);
		} else {
			scr = argumentScripts.get().get(argument);
		}
		java.lang.Object res = scr.exec(context.get(), scope);
		if (res instanceof NativeJavaObject) {
			res = ((NativeJavaObject) res).unwrap();
		}
		if (res instanceof Double && Math.abs((Double) res - Math.floor((Double) res)) < 1.0E-7){
			return Integer.toString(((Double) res).intValue());
		}
		if (res instanceof String) {
			String resultString = res.toString();
			if (resultString.isEmpty() || "null".equals(resultString)) {
				return "\"" + resultString + "\"";
			}
		}
		return res != null ? res.toString() : null;

	}

	public static List<ErrorArgument> getErrorArgumentsResult(Object obj, List<ErrorArgument> arguments,
																		   ScriptableObject scope) {
		List<ErrorArgument> result = new LinkedList<>();
		for (ErrorArgument argument : arguments) {
			result.add(ErrorArgumentImpl.fromValues(argument.getArgument(), argument.getName(),
					getErrorArgumentResult(argument.getArgument(), obj, scope)));
		}
		return result;
	}

	public static void exitContext() {
		Context.exit();
	}
}
