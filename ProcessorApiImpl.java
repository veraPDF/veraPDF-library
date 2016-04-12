package org.verapdf.processor;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.verapdf.core.ValidationException;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.tools.FeaturesCollection;
import org.verapdf.metadata.fixer.impl.MetadataFixerImpl;
import org.verapdf.metadata.fixer.impl.pb.FixerConfigImpl;
import org.verapdf.metadata.fixer.utils.FileGenerator;
import org.verapdf.metadata.fixer.utils.FixerConfig;
import org.verapdf.model.ModelParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.pdfa.validators.Validators;
import org.verapdf.processor.config.Config;
import org.verapdf.processor.config.FormatOption;
import org.verapdf.report.ItemDetails;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Sergey Shemyakov on 4/11/16.
 */
public class ProcessorApiImpl implements ProcessorApi {

	private static final Logger LOGGER = Logger.getLogger(ProcessorApiImpl.class);

	private Config config;		// TODO: decide what fields (if any) we really need
	private ValidationProfile validationProfile;
	private PDFAValidator validator;
	private InputStream pdfFileStream;
	private ItemDetails fileDetails;
	private FileOutputStream report;
	private ValidationResult validationResult = null;
	private MetadataFixerResult fixerResult = null;
	private FeaturesCollection featuresCollection = null;


	@Override
	public void validate(InputStream pdfFileStream, ItemDetails fileDetails, Config config, FileOutputStream report) {
		this.config = config;
		this.validationProfile = profileFromConfig(config);
		this.validator = (this.validationProfile == Profiles.defaultProfile())
				? null : Validators.createValidator(this.validationProfile,
				logPassed(config), config.getMaxNumberOfFailedChecks());
		this.pdfFileStream = pdfFileStream;
		this.fileDetails = fileDetails;
		this.report = report;

		long startTimeOfValidation = System.currentTimeMillis();
		try (ModelParser toValidate = new ModelParser(pdfFileStream,
				this.validationProfile.getPDFAFlavour())) {
			if(config.getProcessingType().isValidating()) {
				if (validator != null) {
					this.validationResult = validator.validate(toValidate);
					if (config.isFixMetadata()) {
						this.fixerResult = fixMetadata(validationResult, toValidate,
								this.fileDetails.getName());
					}
				}
			}
			if (config.getProcessingType().isFeatures()) {
				this.featuresCollection = PBFeatureParser
						.getFeaturesCollection(toValidate.getPDDocument());
			}
		} catch (InvalidPasswordException e) {
			LOGGER.error("Error: " + fileDetails.getName() + " is an encrypted PDF file.", e);
			return;
		} catch (IOException e) {
			LOGGER.error("Error: " + fileDetails.getName() + " is not a PDF format file.");
			return;
		} catch (ValidationException e) {
			LOGGER.error("Exception raised while validating "
					+ fileDetails.getName());
			//e.printStackTrace();
		}
		long endTimeOfValidation = System.currentTimeMillis();
	}

	private static boolean logPassed(final Config config) {
		return (config.getReportType() != FormatOption.XML)
				|| config.isShowPassedRules();
	}

	private static ValidationProfile profileFromConfig(final Config config) {
		if (config.getValidationProfilePath() == null) {
			return (config.getFlavour() == PDFAFlavour.NO_FLAVOUR) ? Profiles
					.defaultProfile() : Profiles.getVeraProfileDirectory()
					.getValidationProfileByFlavour(config.getFlavour());
		}
		ValidationProfile profile = profileFromFile(
				config.getValidationProfilePath().toFile());
		return profile;
	}

	private static ValidationProfile profileFromFile(final File profileFile) {
		ValidationProfile profile = Profiles.defaultProfile();
		try (InputStream is = new FileInputStream(profileFile)) {
			profile = Profiles.profileFromXml(is);
			if ("sha-1 hash code".equals(profile.getHexSha1Digest())) {
				return Profiles.defaultProfile();
			}
			return profile;
		} catch (JAXBException e) {
			LOGGER.error("Error in parsing profile XML", e);
			return Profiles.defaultProfile();
		} catch (IOException e) {
			LOGGER.error("Error in reading profile from disc", e);
			return Profiles.defaultProfile();
		}
	}

	private MetadataFixerResult fixMetadata(ValidationResult info,
											ModelParser parser, String fileName) throws IOException {
		FixerConfig fixerConfig = FixerConfigImpl.getFixerConfig(
				parser.getPDDocument(), info);
		Path path = this.config.getFixMetadataPathFolder();
		File tempFile = File.createTempFile("fixedTempFile", ".pdf");
		tempFile.deleteOnExit();
		try (OutputStream tempOutput = new BufferedOutputStream(
				new FileOutputStream(tempFile))) {
			MetadataFixerResult fixerResult = MetadataFixerImpl.fixMetadata(
					tempOutput, fixerConfig);
			MetadataFixerResult.RepairStatus repairStatus = fixerResult
					.getRepairStatus();
			if (repairStatus == MetadataFixerResult.RepairStatus.SUCCESS || repairStatus == MetadataFixerResult.RepairStatus.ID_REMOVED) {
				File resFile;
				boolean flag = true;
				while (flag) {
					if (!path.toString().trim().isEmpty()) {
						resFile = FileGenerator.createOutputFile(this.config.getFixMetadataPathFolder().toFile(),
								fileName, this.config.getMetadataFixerPrefix());
					} else {
						resFile = FileGenerator.createOutputFile(new File(fileName),
								this.config.getMetadataFixerPrefix());
					}

					try {
						Files.copy(tempFile.toPath(), resFile.toPath());
						flag = false;
					} catch (FileAlreadyExistsException e) {
						LOGGER.warn("Exception in copying temp file, file already exists", e);	//TODO: warn?
					}
				}
			}
			return fixerResult;
		}
	}
}
