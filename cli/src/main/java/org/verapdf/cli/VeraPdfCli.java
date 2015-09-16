/**
 *
 */
package org.verapdf.cli;

import com.beust.jcommander.JCommander;
import org.verapdf.cli.commands.CommandVeraPDF;
import org.verapdf.config.Input;
import org.verapdf.config.VeraPdfTaskConfig;
import org.verapdf.report.XMLReport;
import org.verapdf.runner.ValidationRunner;
import org.verapdf.validation.report.model.ValidationInfo;

public class VeraPdfCli {

    private static final CommandVeraPDF commandVeraPDF;

    static {
        commandVeraPDF = new CommandVeraPDF();
    }

    private VeraPdfCli() {
        // disable default constructor
    }

    /**
     * Main CLI entry point, process the command line arguments
     *
     * @param args
     *            Java.lang.String array of command line args, to be processed
     *            using Apache commons CLI.
     */
    public static void main(String[] args) throws Exception {
        JCommander jCommander = new JCommander();
        jCommander.addCommand(commandVeraPDF);
        jCommander.parse(args);

        VeraPdfTaskConfig taskConfig = createConfigFromCliOptions(commandVeraPDF);
        long startTime = System.currentTimeMillis();
        ValidationInfo validationInfo = ValidationRunner
                .runValidation(taskConfig);
        long endTime = System.currentTimeMillis();
        if (validationInfo != null) {
            XMLReport.writeXMLReport(validationInfo, taskConfig.getOutput(),
                    endTime - startTime, taskConfig.isLogPassedChecks());
        } else {
            throw new IllegalStateException("Internal error during validation");
        }
    }

    /**
     * Creates instance of VeraPdfTaskConfig from parsed cli options
     * 
     * @param cmdVeraPDF
     *            options used by VeraPDF software
     * @return an immutable VeraPdfTaskConfig object populated from the parsed
     *         options
     */
    private static VeraPdfTaskConfig createConfigFromCliOptions(
            final CommandVeraPDF cmdVeraPDF) {
        VeraPdfTaskConfig.Builder configBuilder = new VeraPdfTaskConfig.Builder();
        configBuilder
                .input(new Input(cmdVeraPDF.getInputPath(), cmdVeraPDF
                        .isInputPathURL()))
				.profile(cmdVeraPDF.getProfile())
                .validate(cmdVeraPDF.isValidate())
                .output(cmdVeraPDF.getOutput())
				.maxFailedChecks(cmdVeraPDF.getMaxFailedChecks())
				.maxDisplayedFailedChecks(cmdVeraPDF.getMaxDisplayedFailedChecks())
				.logPassedChecks(cmdVeraPDF.isLogPassedChecks());
        return configBuilder.build();
    }

}