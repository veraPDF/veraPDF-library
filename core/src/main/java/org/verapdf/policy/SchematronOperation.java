package org.verapdf.policy;

import org.verapdf.features.objects.Feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Enum with operations on elements of Feature.FeatureType that can be used in
 * policy configuration file.
 *
 * @author Sergey Shemyakov
 */
public enum SchematronOperation {

    // Any type
    PRESENT(false, "is present"),
    NOT_PRESENT(false, "is not present"),

    //Numbers and strings
    IS_EQUAL(true, "equals to"),
    NOT_EQUAL(true, "not equals to"),

    // Boolean
    IS_TRUE(false, "is true"),
    IS_FALSE(false, "is false"),

    // Number
    IS_GREATER(true, "is greater than"),
    IS_GREATER_OR_EQUAL(true, "is greater or equal to"),
    IS_LESS(true, "is less than"),
    IS_LESS_OR_EQUAL(true, "is less or equal to"),

    // String
    STARTS_WITH(true, "starts with"),
    ENDS_WITH(true, "ends with"),
    CONTAINS(true, "contains substring");

    private static List<SchematronOperation> operationsOnBooleans = new ArrayList<>(4);
    private static List<SchematronOperation> operationsOnNumbers = new ArrayList<>(8);
    private static List<SchematronOperation> operationsOnStrings = new ArrayList<>(7);

    static {
        operationsOnBooleans.add(PRESENT);
        operationsOnBooleans.add(NOT_PRESENT);
        operationsOnBooleans.add(IS_TRUE);
        operationsOnBooleans.add(IS_FALSE);

        operationsOnNumbers.add(PRESENT);
        operationsOnNumbers.add(NOT_PRESENT);
        operationsOnNumbers.add(IS_EQUAL);
        operationsOnNumbers.add(NOT_EQUAL);
        operationsOnNumbers.add(IS_GREATER);
        operationsOnNumbers.add(IS_GREATER_OR_EQUAL);
        operationsOnNumbers.add(IS_LESS);
        operationsOnNumbers.add(IS_LESS_OR_EQUAL);

        operationsOnStrings.add(PRESENT);
        operationsOnStrings.add(NOT_PRESENT);
        operationsOnStrings.add(IS_EQUAL);
        operationsOnStrings.add(NOT_EQUAL);
        operationsOnStrings.add(STARTS_WITH);
        operationsOnStrings.add(ENDS_WITH);
        operationsOnStrings.add(CONTAINS);
    }

    private boolean hasArguments;
    private String description;

    SchematronOperation(boolean hasArguments, String description) {
        this.hasArguments = hasArguments;
        this.description = description;
    }

    public boolean hasArguments() {
        return hasArguments;
    }

    public String getDescription() {
        return description;
    }

    public static List<SchematronOperation> getOperationsForType(Feature.FeatureType type) {
        switch (type) {
            case BOOLEAN:
                return Collections.unmodifiableList(operationsOnBooleans);
            case NUMBER:
                return Collections.unmodifiableList(operationsOnNumbers);
            case STRING:
                return Collections.unmodifiableList(operationsOnStrings);
            default:
                throw new IllegalStateException("Unsupported FeatureType in getOperationsForType: " + type);
        }
    }

    public static class AssertionInformation {
        private String testAssertion;
        private String assertionDescription;

        private AssertionInformation(String testAssertion, String assertionDescription) {
            this.testAssertion = testAssertion;
            this.assertionDescription = assertionDescription;
        }

        public String getTestAssertion() {
            return testAssertion;
        }

        public String getAssertionDescription() {
            return assertionDescription;
        }
    }
}