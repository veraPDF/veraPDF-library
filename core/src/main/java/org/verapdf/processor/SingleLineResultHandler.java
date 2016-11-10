package org.verapdf.processor;

import org.verapdf.core.VeraPDFException;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.profiles.RuleId;
import org.verapdf.processor.reports.BatchSummary;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * This result handler outputs validation summaries for documents in single line
 * and outputs only basic information.
 *
 * @author Sergey Shemyakov
 */
public class SingleLineResultHandler implements BatchProcessingHandler {

    private OutputStream outputStream;
    private boolean isVerbose;

    public SingleLineResultHandler(boolean isVerbose) {
        this(System.out, isVerbose);
    }

    public SingleLineResultHandler(OutputStream outputStream, boolean isVerbose) {
        this.outputStream = outputStream;
        this.isVerbose = isVerbose;
    }

    @Override
    public void handleBatchStart() throws VeraPDFException {
        // Do nothing here
    }

    @Override
    public void handleResult(ProcessorResult result) throws VeraPDFException {
        try {
            if (result == null) {
                throw new VeraPDFException("Trying to handle result that is null");
            }
            ValidationResult validationResult = result.getValidationResult();
            TaskResult taskValidation = result.getResultForTask(TaskType.VALIDATE);
            if (taskValidation == null) {
                String reportSummary = "FAIL " + result.getProcessedItem().getName() + "\n";
                reportSummary += "Validation wasn't started due to error in file.\n";
                outputStream.write(reportSummary.getBytes());
            } else if (taskValidation.isExecuted() && taskValidation.isSuccess()) {
                String reportSummary = (validationResult.isCompliant() ? "PASS " : "FAIL ")
                        + result.getProcessedItem().getName() + "\n";
                outputStream.write(reportSummary.getBytes());
                if (this.isVerbose) {
                    processFiledRules(validationResult);
                }
            } else if (taskValidation.getException() != null) {
                String reportSummary = "ERROR " + result.getProcessedItem().getName() +
                        " " + taskValidation.getException().toString() + "\n";
                outputStream.write(reportSummary.getBytes());
            } else {
                String reportSummary = "Validation wasn't started.\n";
                outputStream.write(reportSummary.getBytes());
            }
        } catch (IOException e) {
            throw new VeraPDFException("Exception is caught when writing to output stream", e);
        }
    }

    @Override
    public void handleBatchEnd(BatchSummary summary) throws VeraPDFException {
        // Do nothing here
    }

    @Override
    public void close() throws IOException {
        if (this.outputStream != System.out) {
            this.outputStream.close();
        }
    }

    private void processFiledRules(ValidationResult validationResult) throws IOException {
        Set<RuleId> ruleIds = new HashSet<>();
        for (TestAssertion assertion : validationResult.getTestAssertions()) {
            if (assertion.getStatus() == TestAssertion.Status.FAILED) {
                ruleIds.add(assertion.getRuleId());
            }
        }
        for (RuleId id : ruleIds) {
            String reportRuleSummary = id.getClause() + "-" +
                    id.getTestNumber() + "\n";
            outputStream.write(reportRuleSummary.getBytes());
        }
    }
}
