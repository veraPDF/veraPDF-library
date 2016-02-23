/**
 * 
 */
package org.verapdf.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.verapdf.cli.commands.FormatOption;
import org.verapdf.cli.commands.VeraCliArgParser;
import org.verapdf.core.ProfileException;
import org.verapdf.core.ValidationException;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.tools.FeaturesCollection;
import org.verapdf.model.ModelParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.pdfa.validators.Validators;
import org.verapdf.report.FeaturesReport;
import org.verapdf.report.HTMLReport;
import org.verapdf.report.ItemDetails;
import org.verapdf.report.MachineReadableReport;
import org.verapdf.validation.profile.parser.LegacyProfileConverter;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
final class VeraPdfCliProcessor {
    final FormatOption format;
    final boolean extractFeatures;
    final boolean logPassed;
    final boolean recurse;
    final PDFAValidator validator;

    private VeraPdfCliProcessor() throws FileNotFoundException, IOException {
        this(new VeraCliArgParser());
    }

    private VeraPdfCliProcessor(final VeraCliArgParser args)
            throws FileNotFoundException, IOException {
        this.format = args.getFormat();
        this.extractFeatures = args.extractFeatures();
        this.logPassed = args.logPassed();
        this.recurse = args.isRecurse();
        ValidationProfile profile = profileFromArgs(args);
        this.validator = (profile == Profiles.defaultProfile()) ? null
                : Validators.createValidator(profile, logPassed(args));

    }

    private static boolean logPassed(final VeraCliArgParser args) {
        return (args.getFormat() != FormatOption.XML) || args.logPassed();
    }

    void processPaths(final List<String> pdfPaths) {
        for (String pdfPath : pdfPaths) {
            File file = new File(pdfPath);
            if (file.isDirectory()) {
                processDir(file);
            } else {
                processFile(file);
            }
        }
    }

    static VeraPdfCliProcessor createProcessorFromArgs(
            final VeraCliArgParser args) throws FileNotFoundException,
            IOException {
        return new VeraPdfCliProcessor(args);
    }

    private void processDir(final File dir) {
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                int extIndex = file.getName().lastIndexOf(".");
                String ext = file.getName().substring(extIndex + 1);
                if ("pdf".equalsIgnoreCase(ext)) {
                    processFile(file);
                }
            } else if (file.isDirectory()) {
                if (this.recurse) {
                    processDir(file);
                }
            }
        }
    }

    private void processFile(final File pdfFile) {
        if (checkFileCanBeProcessed(pdfFile)) {
            try (InputStream toProcess = new FileInputStream(pdfFile)) {
                processStream(ItemDetails.fromFile(pdfFile), toProcess);
            } catch (IOException e) {
                System.err.println("Exception raised while processing "
                        + pdfFile.getAbsolutePath());
                e.printStackTrace();
            }
        }
    }

    private void processStream(final ItemDetails item,
            final InputStream toProcess) {
        ValidationResult validationResult = null;
        FeaturesCollection featuresCollection = null;
        long start = System.currentTimeMillis();
        try (ModelParser toValidate = new ModelParser(toProcess)) {
            if (this.validator != null) {
                validationResult = this.validator.validate(toValidate);
            }
            if (this.extractFeatures) {
                featuresCollection = PBFeatureParser
                        .getFeaturesCollection(toValidate.getPDDocument());
            }
        } catch (IOException e) {
            System.err.println("Failed to parse pdf file " + item.getName());
            // TODO : do we need stacktrace in cli application?
            // e.printStackTrace();
        } catch (ValidationException e) {
            System.err.println("Exception raised while validating "
                    + item.getName());
            e.printStackTrace();
        }
        if (this.format == FormatOption.XML)
            outputXmlResults(item, validationResult, featuresCollection);
        else if (this.format == FormatOption.BRIEF) {
            System.out.println(item.getName() + ":"
                    + validationResult.isCompliant());
        } else {
            MachineReadableReport report = MachineReadableReport.fromValues(
                    item.getName(),
                    this.validator == null ? Profiles.defaultProfile()
                            : this.validator.getProfile(), validationResult,
                    this.logPassed, null, featuresCollection,
                    System.currentTimeMillis() - start);
            outputMrr(report, this.format == FormatOption.HTML);
        }
    }

    private static void outputXmlResults(final ItemDetails item,
            final ValidationResult validationResult,
            final FeaturesCollection featuresCollection) {
        try {
            ItemDetails.toXml(item, System.out, Boolean.TRUE);
            if (validationResult != null)
                ValidationResults.toXml(validationResult, System.out,
                        Boolean.TRUE);
            if (featuresCollection != null) {
                FeaturesReport featuresReport = FeaturesReport
                        .fromValues(featuresCollection);
                FeaturesReport.toXml(featuresReport, System.out, Boolean.TRUE);
            }
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void outputMrr(final MachineReadableReport report,
            final boolean toHtml) {
        try {
            if (toHtml) {
                outputMrrAsHtml(report);
            } else {
                MachineReadableReport.toXml(report, System.out, Boolean.TRUE);
            }
        } catch (JAXBException | IOException | TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void outputMrrAsHtml(final MachineReadableReport report)
            throws IOException, JAXBException, TransformerException {
        File tmp = File.createTempFile("verpdf", "xml");
        try (OutputStream os = new FileOutputStream(tmp)) {
            MachineReadableReport.toXml(report, os, Boolean.FALSE);
        }
        try (InputStream is = new FileInputStream(tmp)) {
            HTMLReport.writeHTMLReport(is, System.out);
        }
    }

    private static boolean checkFileCanBeProcessed(final File file) {
        if (!file.isFile()) {
            System.err.println("Path " + file.getAbsolutePath()
                    + " is not an existing file.");
            return false;
        } else if (!file.canRead()) {
            System.err.println("Path " + file.getAbsolutePath()
                    + " is not a readable file.");
            return false;
        }
        return true;
    }

    private static ValidationProfile profileFromArgs(final VeraCliArgParser args)
            throws FileNotFoundException, IOException {
        if (args.getProfileFile() == null) {
            return (args.getFlavour() == PDFAFlavour.NO_FLAVOUR) ? Profiles
                    .defaultProfile() : Profiles.getVeraProfileDirectory()
                    .getValidationProfileByFlavour(args.getFlavour());
        }
        ValidationProfile profile = profileFromFile(args.getProfileFile());
        if (profile.equals(Profiles.defaultProfile())) {
            profile = profileFromLegacyFile(args.getProfileFile());
        }

        return profile;
    }

    private static ValidationProfile profileFromFile(final File profileFile)
            throws IOException {
        ValidationProfile profile = Profiles.defaultProfile();
        try (InputStream is = new FileInputStream(profileFile)) {
            profile = Profiles.profileFromXml(is);
            if ("sha-1 hash code".equals(profile.getHexSha1Digest())) {
                return Profiles.defaultProfile();
            }
            return profile;
        } catch (JAXBException e) {
            e.printStackTrace();
            return Profiles.defaultProfile();
        }
    }

    private static ValidationProfile profileFromLegacyFile(
            final File profileFile) throws FileNotFoundException, IOException {
        ValidationProfile profile = Profiles.defaultProfile();
        try (InputStream is = new FileInputStream(profileFile)) {
            profile = LegacyProfileConverter.fromLegacyStream(is);
            return profile;
        } catch (ProfileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Profiles.defaultProfile();
        }
    }
}
