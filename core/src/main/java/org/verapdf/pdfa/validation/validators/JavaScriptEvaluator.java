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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

	private static String getStringScript(Object obj, String arg) {
		return getScriptPrefix(obj, arg) + arg + getScriptSuffix();
	}

	private static String getScriptPrefix(Object obj, String test) {
		StringBuilder builder = new StringBuilder();
		String[] vars = test.split("\\W");

		for (String prop : obj.getProperties()) {
			if (contains(vars, prop)) {
				builder.append("var ");
				builder.append(prop);
				builder.append(" = obj.get");
				builder.append(prop);
				builder.append("();\n");
			}
		}

		for (String linkName : obj.getLinks()) {
			if (contains(vars, linkName + "_size")) {
				builder.append("var ");
				builder.append(linkName);
				builder.append("_size = obj.getLinkedObjects(\"");
				builder.append(linkName);
				builder.append("\").size();\n");
			}
		}
		builder.append("function test(){return ");
		return builder.toString();
	}

	private static boolean contains(String[] values, String prop) {
		for (String value : values) {
			if (value.equals(prop)) {
				return true;
			}
		}
		return false;
	}

	private static String getScriptSuffix() {
		return ";}\ntest();";
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
