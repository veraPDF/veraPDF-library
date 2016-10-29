package org.verapdf.processor.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.verapdf.features.FeatureExtractorConfig;
import org.verapdf.features.FeatureFactory;
import org.verapdf.metadata.fixer.FixerFactory;
import org.verapdf.metadata.fixer.MetadataFixerConfig;
import org.verapdf.metadata.fixer.utils.MetadataFixerConstants;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.validators.ValidatorConfig;
import org.verapdf.pdfa.validation.validators.ValidatorFactory;

/**
 * @author Maksim Bezrukov
 */

@XmlRootElement(name = "config")
public final class Config {
	public static final String DEFAULT_PROFILES_WIKI_PATH = "https://github.com/veraPDF/veraPDF-validation-profiles/wiki";
	public static final ProcessingType DEFAULT_PROCESSING_TYPE = ProcessingType.VALIDATION;
	public static final FormatOption DEFAULT_REPORT_TYPE = FormatOption.MRR;
	public static final Path DEFAULT_VALIDATION_PROFILE_PATH = FileSystems.getDefault().getPath("");
	public static final PDFAFlavour DEFAULT_FLAVOUR = PDFAFlavour.NO_FLAVOUR;
	public static final boolean DEFAULT_VERBOSE_CLI = false;
	public static final Path DEFAULT_POLICY_PROFILE = FileSystems.getDefault().getPath("");
	public static final Path DEFAULT_PLUGINS_CONFIG_PATH = FileSystems.getDefault().getPath("");
	public static final Path DEFAULT_FEATURES_CONFIG_PATH = FileSystems.getDefault().getPath("");
	public static final String DEFAULT_REPORT_FOLDER_PATH = "";
	public static final String DEFAULT_REPORT_FILE_PATH = "";
	public static final boolean DEFAULT_IS_OVERWRITE_REPORT_FILE = false;

	private final ValidatorConfig validatorConfig;
	private final FeatureExtractorConfig featureConfig;
	private final MetadataFixerConfig fixerConfig;

	private Path pluginsConfigPath;

	private int maxNumberOfDisplayedFailedChecks;
	private String profileWikiPath;
	private String reportFolderPath;
	private String reportFilePath;
	private boolean isOverwriteReportFile;
	private ProcessingType processingType;
	private FormatOption reportType;

	private Path validationProfilePath;

	private boolean verboseCli;

	private Path policyProfilePath;


	public Config() {
		this(ValidatorFactory.defaultConfig(), FeatureFactory.defaultConfig(), FixerFactory.defaultConfig());
	}

	private Config(final ValidatorConfig config, final FeatureExtractorConfig featureConfig,
			final MetadataFixerConfig fixerConfig) {
		super();
		this.validatorConfig = config;
		this.featureConfig = featureConfig;
		this.fixerConfig = fixerConfig;
		this.profileWikiPath = DEFAULT_PROFILES_WIKI_PATH;
		this.processingType = DEFAULT_PROCESSING_TYPE;
		this.reportType = DEFAULT_REPORT_TYPE;
		this.validationProfilePath = DEFAULT_VALIDATION_PROFILE_PATH;
		this.verboseCli = DEFAULT_VERBOSE_CLI;
		this.policyProfilePath = DEFAULT_POLICY_PROFILE;
		this.pluginsConfigPath = DEFAULT_PLUGINS_CONFIG_PATH;
		this.reportFolderPath = DEFAULT_REPORT_FOLDER_PATH;
		this.reportFilePath = DEFAULT_REPORT_FILE_PATH;
		this.isOverwriteReportFile = DEFAULT_IS_OVERWRITE_REPORT_FILE;
	}

	public boolean isFixMetadata() {
		return (this.fixerConfig == FixerFactory.defaultConfig());
	}
	/**
	 * @return selected number for maximum displayed fail checks for a rule. If
	 *         not selected returns -1
	 */
	@XmlElement
	public int getMaxNumberOfDisplayedFailedChecks() {
		return maxNumberOfDisplayedFailedChecks;
	}

	/**
	 * @return type of operation to be performed, e. g. validation & feature
	 *         describing
	 */
	@XmlElement
	@XmlJavaTypeAdapter(ProcessingTypeAdapter.class)
	public ProcessingType getProcessingType() {
		return processingType;
	}

	/**
	 * @return path to the profiles wiki
	 */
	@XmlElement
	public String getProfileWikiPath() {
		return profileWikiPath;
	}

	/**
	 * @return type of report to be generated
	 */
	@XmlElement
	@XmlJavaTypeAdapter(FormatOptionAdapter.class)
	public FormatOption getReportType() {
		return reportType;
	}

	@XmlElement
	@XmlJavaTypeAdapter(PathAdapter.class)
	private Path getValidationProfilePath() {
		return validationProfilePath.toString().equals("") ? null : validationProfilePath;
	}

	@XmlElement
	@XmlJavaTypeAdapter(PathAdapter.class)
	private Path getPolicyProfilePath() {
		return policyProfilePath.toString().equals("") ? null : policyProfilePath;
	}

	/**
	 * @return path to the policy profile
	 */
	public Path getPolicyProfile() {
		return policyProfilePath;
	}

	/**
	 * @return path to validation profile to be used. If returned value is null,
	 *         then validation flavour is processed
	 */
	public Path getValidationProfile() {
		return validationProfilePath;
	}

	/**
	 * @return true if cli text report will be verbose
	 */
	@XmlElement
	public boolean isVerboseCli() {
		return verboseCli;
	}

	@XmlElement
	@XmlJavaTypeAdapter(PathAdapter.class)
	private Path getPluginsConfigPath() {
		return pluginsConfigPath.toString().equals("") ? null : pluginsConfigPath;
	}

	/**
	 * @return path to the plugins config file
	 */
	public Path getPluginsConfigFilePath() {
		return pluginsConfigPath;
	}

	public ValidatorConfig getValidatorConfig() {
		return this.validatorConfig;
	}

	public FeatureExtractorConfig getFeatureConfig() {
		return this.featureConfig;
	}

	public MetadataFixerConfig getFixerConfig() {
		return this.fixerConfig;
	}
	/**
	 * @author: mancuska@digitaldocuments.org
	 * @return path to folder for reports
	 */
	@XmlElement
	public String getReportFolder() {
		return reportFolderPath;
	}

	/**
	 * @author: mancuska@digitaldocuments.org
	 * @return path for report file
	 */
	@XmlElement
	public String getReportFile() {
		return reportFilePath;
	}

	/**
	 * @author: mancuska@digitaldocuments.org
	 * @return: true if existing report file must be overwritten
	 */
	@XmlElement
	public boolean isOverwriteReportFile() {
		return isOverwriteReportFile;
	}

	/**
	 * Converts Config to XML,
	 *
	 * @see javax.xml.bind.JAXB for more details
	 */
	public static String toXml(final Config toConvert, Boolean prettyXml) throws JAXBException, IOException {
		String retVal = "";
		try (StringWriter writer = new StringWriter()) {
			toXml(toConvert, writer, prettyXml);
			retVal = writer.toString();
			return retVal;
		}
	}

	/**
	 * Converts XML file to Config,
	 *
	 * @see javax.xml.bind.JAXB for more details
	 */
	public static Config fromXml(final String toConvert) throws JAXBException {
		try (StringReader reader = new StringReader(toConvert)) {
			return fromXml(reader);
		}
	}

	/**
	 * Converts Config to XML,
	 *
	 * @see javax.xml.bind.JAXB for more details
	 */
	public static void toXml(final Config toConvert, final OutputStream stream, Boolean prettyXml)
			throws JAXBException {
		Marshaller varMarshaller = getMarshaller(prettyXml);
		varMarshaller.marshal(toConvert, stream);
	}

	/**
	 * Converts XML file to Config,
	 *
	 * @see javax.xml.bind.JAXB for more details
	 */
	public static Config fromXml(final InputStream toConvert) throws JAXBException {
		Unmarshaller stringUnmarshaller = getUnmarshaller();
		return (Config) stringUnmarshaller.unmarshal(toConvert);
	}

	static void toXml(final Config toConvert, final Writer writer, Boolean prettyXml) throws JAXBException {
		Marshaller varMarshaller = getMarshaller(prettyXml);
		varMarshaller.marshal(toConvert, writer);
	}

	static Config fromXml(final Reader toConvert) throws JAXBException {
		Unmarshaller stringUnmarshaller = getUnmarshaller();
		return (Config) stringUnmarshaller.unmarshal(toConvert);
	}

	private static Unmarshaller getUnmarshaller() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Config.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return unmarshaller;
	}

	private static Marshaller getMarshaller(Boolean setPretty) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Config.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, setPretty);
		return marshaller;
	}

	/**
	 * Checks is the parameter path a valid for saving fixed file
	 *
	 * @param path
	 *            path for check
	 * @return true if it is valid
	 */
	public static boolean isValidFolderPath(Path path) {
		if (path == null) {
			return false;
		}
		File f = path.toFile();
		return path.toString().isEmpty() || (f.isDirectory() && f.canWrite());
	}

	/**
	 * Checks is the parameter path a valid validation profile
	 *
	 * @param path
	 *            path for check
	 * @return true if it is valid
	 */
	public static boolean isValidProfilePath(Path path) {
		if (path == null) {
			return true; // If we chose some path and then decided to use
							// flavour
		}
		File f = path.toFile();
		return path.toString().isEmpty() || (f.isFile() && f.canRead());
	}

	private static class PathAdapter extends XmlAdapter<String, Path> {

		@Override
		public Path unmarshal(String v) throws Exception {
			Path path = Paths.get(new URI(v));
			return path.toAbsolutePath();
		}

		@Override
		public String marshal(Path v) throws Exception {
			return v.toAbsolutePath().toUri().toString();
		}
	}

	private static class ProcessingTypeAdapter extends XmlAdapter<String, ProcessingType> {

		private final Logger LOGGER = Logger.getLogger(ProcessingTypeAdapter.class.getName());

		@Override
		public ProcessingType unmarshal(String v) throws Exception {
			try {
				return ProcessingType.fromString(v);
			} catch (IllegalArgumentException e) {
				LOGGER.log(Level.WARNING,
						"Can't construct ProcessingType from string \"" + v + "\", setting ProcessingType to default",
						e);
			}
			return Config.DEFAULT_PROCESSING_TYPE;
		}

		@Override
		public String marshal(ProcessingType v) throws Exception {
			return v.toString();
		}
	}

	private static class FormatOptionAdapter extends XmlAdapter<String, FormatOption> {

		@Override
		public FormatOption unmarshal(String v) {
			return FormatOption.fromOption(v);
		}

		@Override
		public String marshal(FormatOption v) {
			return v.toString();
		}
	}

	private static class FlavourAdapter extends XmlAdapter<String, PDFAFlavour> {

		@Override
		public PDFAFlavour unmarshal(String v) {
			return PDFAFlavour.fromString(v);
		}

		@Override
		public String marshal(PDFAFlavour v) {
			return v.toString();
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.featureConfig == null) ? 0 : this.featureConfig.hashCode());
		result = prime * result + (this.isOverwriteReportFile ? 1231 : 1237);
		result = prime * result + this.maxNumberOfDisplayedFailedChecks;
		result = prime * result + ((this.pluginsConfigPath == null) ? 0 : this.pluginsConfigPath.hashCode());
		result = prime * result + ((this.policyProfilePath == null) ? 0 : this.policyProfilePath.hashCode());
		result = prime * result + ((this.processingType == null) ? 0 : this.processingType.hashCode());
		result = prime * result + ((this.profileWikiPath == null) ? 0 : this.profileWikiPath.hashCode());
		result = prime * result + ((this.reportFilePath == null) ? 0 : this.reportFilePath.hashCode());
		result = prime * result + ((this.reportFolderPath == null) ? 0 : this.reportFolderPath.hashCode());
		result = prime * result + ((this.reportType == null) ? 0 : this.reportType.hashCode());
		result = prime * result + ((this.validationProfilePath == null) ? 0 : this.validationProfilePath.hashCode());
		result = prime * result + ((this.validatorConfig == null) ? 0 : this.validatorConfig.hashCode());
		result = prime * result + (this.verboseCli ? 1231 : 1237);
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Config)) {
			return false;
		}
		Config other = (Config) obj;
		if (this.featureConfig == null) {
			if (other.featureConfig != null) {
				return false;
			}
		} else if (!this.featureConfig.equals(other.featureConfig)) {
			return false;
		}
		if (this.isOverwriteReportFile != other.isOverwriteReportFile) {
			return false;
		}
		if (this.maxNumberOfDisplayedFailedChecks != other.maxNumberOfDisplayedFailedChecks) {
			return false;
		}
		if (this.pluginsConfigPath == null) {
			if (other.pluginsConfigPath != null) {
				return false;
			}
		} else if (!this.pluginsConfigPath.equals(other.pluginsConfigPath)) {
			return false;
		}
		if (this.policyProfilePath == null) {
			if (other.policyProfilePath != null) {
				return false;
			}
		} else if (!this.policyProfilePath.equals(other.policyProfilePath)) {
			return false;
		}
		if (this.processingType != other.processingType) {
			return false;
		}
		if (this.profileWikiPath == null) {
			if (other.profileWikiPath != null) {
				return false;
			}
		} else if (!this.profileWikiPath.equals(other.profileWikiPath)) {
			return false;
		}
		if (this.reportFilePath == null) {
			if (other.reportFilePath != null) {
				return false;
			}
		} else if (!this.reportFilePath.equals(other.reportFilePath)) {
			return false;
		}
		if (this.reportFolderPath == null) {
			if (other.reportFolderPath != null) {
				return false;
			}
		} else if (!this.reportFolderPath.equals(other.reportFolderPath)) {
			return false;
		}
		if (this.reportType != other.reportType) {
			return false;
		}
		if (this.validationProfilePath == null) {
			if (other.validationProfilePath != null) {
				return false;
			}
		} else if (!this.validationProfilePath.equals(other.validationProfilePath)) {
			return false;
		}
		if (this.validatorConfig == null) {
			if (other.validatorConfig != null) {
				return false;
			}
		} else if (!this.validatorConfig.equals(other.validatorConfig)) {
			return false;
		}
		if (this.verboseCli != other.verboseCli) {
			return false;
		}
		return true;
	}

}
