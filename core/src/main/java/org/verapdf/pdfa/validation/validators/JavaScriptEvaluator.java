package org.verapdf.pdfa.validation.validators;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptableObject;
import org.verapdf.model.baselayer.Object;
import org.verapdf.pdfa.validation.profiles.ErrorArgument;
import org.verapdf.pdfa.validation.profiles.Rule;
import org.verapdf.pdfa.validation.profiles.Variable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaScriptEvaluator {
	private static final int OPTIMIZATION_LEVEL = 9;

	private static Context context;

	private static Map<String, Script> ruleScripts = new HashMap<>();
	private static Map<String, Script> variableScripts = new HashMap<>();
	private static Map<String, Script> argumentScripts = new HashMap<>();

	public static synchronized ScriptableObject initialise() {
		context = Context.enter();
		context.setOptimizationLevel(OPTIMIZATION_LEVEL);
		return context.initStandardObjects();
	}

	public static synchronized java.lang.Object evaluateString(String source, ScriptableObject scope) {
		return context.evaluateString(scope, source, null, 0, null);
	}

	public static synchronized java.lang.Object evalVariableResult(Variable variable, Object object, ScriptableObject scope) {
		Script script;
		if (!variableScripts.containsKey(variable.getName())) {
			String source = getStringScript(object, variable.getValue());
			script = JavaScriptEvaluator.compileString(source);

			variableScripts.put(variable.getName(), script);
		} else {
			script = variableScripts.get(variable.getName());
		}

		scope.put("obj", scope, object);

		java.lang.Object res = script.exec(context, scope);

		if (res instanceof NativeJavaObject) {
			res = ((NativeJavaObject) res).unwrap();
		}
		return res;
	}

	private static Script compileString(String source) {
		return context.compileString(source, null, 0, null);
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

	public static synchronized boolean getTestEvalResult(Object obj, Rule rule, ScriptableObject scope) {
		scope.put("obj", scope, obj);

		Script scr;
		String test = rule.getTest();
		if (!ruleScripts.containsKey(test)) {
			scr = compileString(getScript(obj, test));
			ruleScripts.put(test, scr);
		} else {
			scr = ruleScripts.get(test);
		}

		boolean testEvalResult = ((Boolean) scr.exec(context, scope)).booleanValue();

		return testEvalResult;
	}

	private static synchronized String getErrorArgumentResult(String argument, Object obj, ScriptableObject scope) {
		Script scr;

		if (!argumentScripts.containsKey(argument)) {
			scr = JavaScriptEvaluator.compileString(getStringScript(obj, argument));
			argumentScripts.put(argument, scr);
		} else {
			scr = argumentScripts.get(argument);
		}
		java.lang.Object res = scr.exec(context, scope);
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

	public static synchronized void setErrorArgumentsResult(Object obj, List<ErrorArgument> arguments, ScriptableObject scope) {
		for (ErrorArgument argument : arguments) {
			argument.setArgumentValue(getErrorArgumentResult(argument.getArgument(), obj, scope));
		}
	}

	public static void exitContext() {
		Context.exit();
	}
}
