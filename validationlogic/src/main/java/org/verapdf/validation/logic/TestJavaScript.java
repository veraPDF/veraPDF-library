package org.verapdf.validation.logic;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

/**
 * Created by bezrukov on 6/11/15.
 */
public class TestJavaScript {

    public static void main(String[] args) {
        Context cx = Context.enter();
        ScriptableObject scope = cx.initStandardObjects();

        scope.put("obj", scope, new LolClass());

        Object res;

        res = cx.evaluateString(scope, "var some = obj.getLol();\nfunction test(){return \"hey\";}\ntest();", null, 0, null);

        System.out.println(res.toString());
    }

}
