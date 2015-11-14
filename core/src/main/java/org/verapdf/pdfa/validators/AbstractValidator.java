/**
 * 
 */
package org.verapdf.pdfa.validators;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptableObject;
import org.verapdf.core.ValidationException;
import org.verapdf.model.baselayer.Object;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.ValidationModelParser;
import org.verapdf.pdfa.results.Location;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.Rule;
import org.verapdf.pdfa.validation.RuleId;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.pdfa.validation.Variable;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public abstract class AbstractValidator implements PDFAValidator {

    private static final int OPTIMIZATION_LEVEL = 9;
    private final ValidationProfile profile;
    private Context context;
    private ScriptableObject scope;

    private final Deque<Object> objectsStack = new ArrayDeque<>();
    private final Deque<String> objectsContext = new ArrayDeque<>();
    private final Deque<Set<String>> contextSet = new ArrayDeque<>();
    protected final Set<TestAssertion> results = new HashSet<>();

    private Map<RuleId, Script> ruleScripts = new HashMap<>();
    private Map<String, Script> variableScripts = new HashMap<>();

    private Set<String> idSet = new HashSet<>();

    protected String rootType;

    protected AbstractValidator(final ValidationProfile profile) {
        super();
        this.profile = profile;
    }

    protected ValidationResult validate(Object root) throws ValidationException {
        initialise();
        this.rootType = root.getObjectType();
        this.objectsStack.push(root);
        this.objectsContext.push("root");

        Set<String> rootIDContext = new HashSet<>();

        if (root.getID() != null) {
            rootIDContext.add(root.getID());
            this.idSet.add(root.getID());
        }

        this.contextSet.push(rootIDContext);

        while (!this.objectsStack.isEmpty()) {
            checkNext();
        }

        Context.exit();

        return ValidationResults.resultFromValues(this.profile.getPDFAFlavour(), this.results);
    }

    protected void initialise() {
        this.context = Context.enter();
        this.context.setOptimizationLevel(OPTIMIZATION_LEVEL);
        this.scope = this.context.initStandardObjects();
        this.objectsStack.clear();
        this.objectsContext.clear();
        this.contextSet.clear();
        this.results.clear();
        this.idSet.clear();
        initializeAllVariables();
    }

    private void initializeAllVariables() {
        for (Variable var : this.profile.getVariables()) {
            if (var == null)
                continue;

            java.lang.Object res;
            res = this.context.evaluateString(this.scope,
                    var.getDefaultValue(), null, 0, null);
            if (res instanceof NativeJavaObject) {
                res = ((NativeJavaObject) res).unwrap();
            }

            this.scope.put(var.getName(), this.scope, res);
        }
    }

    private boolean checkNext() throws ValidationException {

        Object checkObject = this.objectsStack.pop();
        String checkContext = this.objectsContext.pop();
        Set<String> checkIDContext = this.contextSet.pop();

        boolean res = checkAllRules(checkObject, checkContext);

        updateVariables(checkObject);

        addAllLinkedObjects(checkObject, checkContext, checkIDContext);

        return res;
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
        for (Variable var : this.profile
                .getVariablesByObject(objectType)) {

            if (var == null)
                continue;

            java.lang.Object variable = evalVariableResult(var, object);
            this.scope.put(var.getName(), this.scope, variable);
        }
    }

    private java.lang.Object evalVariableResult(Variable variable, Object object) {
        Script script;
        if (!this.variableScripts.containsKey(variable.getName())) {
            String source = getStringScript(object, variable.getValue());
            script = this.context.compileString(source, null, 0, null);
            this.variableScripts.put(variable.getName(), script);
        } else {
            script = this.variableScripts.get(variable.getName());
        }

        this.scope.put("obj", this.scope, object);

        java.lang.Object res = script.exec(this.context, this.scope);

        if (res instanceof NativeJavaObject) {
            res = ((NativeJavaObject) res).unwrap();
        }
        return res;
    }

    private void addAllLinkedObjects(Object checkObject, String checkContext,
                                     Set<String> checkIDContext) throws ValidationException {
        List<String> links = checkObject.getLinks();
        for (int j = links.size() - 1; j >= 0; --j) {
            String link = links.get(j);

            if (link == null) {
                throw new ValidationException(
                        "There is a null link name in an object. Context: "
                                + checkContext);
            }
            List<? extends Object> objects = checkObject.getLinkedObjects(link);
            if (objects == null) {
                throw new ValidationException(
                        "There is a null link in an object. Context: "
                                + checkContext);
            }

            for (int i = 0; i < objects.size(); ++i) {
                Object obj = objects.get(i);

                StringBuilder path = new StringBuilder(checkContext);
                path.append("/");
                path.append(link);
                path.append("[");
                path.append(i);
                path.append("]");

                if (obj == null) {
                    throw new ValidationException(
                            "There is a null link in an object. Context of the link: "
                                    + path);
                }

                if (checkRequired(obj, checkIDContext)) {
                    this.objectsStack.push(obj);

                    Set<String> newCheckIDContext = new HashSet<>(
                            checkIDContext);

                    if (obj.getID() != null) {
                        path.append("(");
                        path.append(obj.getID());
                        path.append(")");

                        newCheckIDContext.add(obj.getID());
                        this.idSet.add(obj.getID());
                    }

                    this.objectsContext.push(path.toString());
                    this.contextSet.push(newCheckIDContext);
                }
            }
        }
    }

    private boolean checkRequired(Object obj, Set<String> checkIDContext) {

        if (obj.getID() == null) {
            return true;
        } else if (obj.isContextDependent() == null
                || obj.isContextDependent().booleanValue()) {
            return !checkIDContext.contains(obj.getID());
        } else {
            return !this.idSet.contains(obj.getID());
        }
    }

    private boolean checkAllRules(Object checkObject, String checkContext) {
        boolean res = true;
        Set<Rule> roolsForObject = this.profile.getRulesByObject(
                        checkObject.getObjectType());
        for (Rule rule : roolsForObject) {
            res &= checkObjWithRule(checkObject, checkContext, rule);
        }

        for (String checkType : checkObject.getSuperTypes()) {
            roolsForObject = this.profile.getRulesByObject(checkType);
            if (roolsForObject != null) {
                for (Rule rule : roolsForObject) {
                    if (rule != null) {
                        res &= checkObjWithRule(checkObject, checkContext, rule);
                    }
                }
            }
        }

        return res;
    }

    private static String getScript(Object obj, Rule rule) {
        return getStringScript(obj, "(" + rule.getTest() + ")==true");
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

    private boolean checkObjWithRule(Object obj, String cntxtForRule, Rule rule) {
        this.scope.put("obj", this.scope, obj);
        Script scr;
        if (!this.ruleScripts.containsKey(rule.getRuleId())) {
            scr = this.context.compileString(getScript(obj, rule), null, 0,
                    null);
            this.ruleScripts.put(rule.getRuleId(), scr);
        } else {
            scr = this.ruleScripts.get(rule.getRuleId());
        }

        boolean testEvalResult = ((Boolean) scr.exec(this.context, this.scope))
                .booleanValue();
        
        this.processAssertionResult(testEvalResult, cntxtForRule, rule);

        return testEvalResult;
    }
    
    abstract protected void processAssertionResult(final boolean assertionResult, final String locationContext, final Rule rule);

    /* (non-Javadoc)
     * @see org.verapdf.pdfa.PDFAValidator#getProfile()
     */
    @Override
    public ValidationProfile getProfile() {
        return this.profile;
    }


    @Override
    public ValidationResult validate(ValidationModelParser toValidate) throws ValidationException, IOException {
        return this.validate(toValidate.getRoot());
    }
}
