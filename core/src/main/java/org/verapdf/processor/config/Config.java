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

import org.verapdf.metadata.fixer.utils.MetadataFixerConstants;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.validators.ValidatorConfig;
import org.verapdf.pdfa.validation.validators.ValidatorFactory;

/**
 * @author Maksim Bezrukov
 */

@XmlRootElement(name = "config")
public final class Config {

//	private static final char[] FORBIDDEN_SYMBOLS_IN_FILE_NAME = new char[]{'\\', '/', ':', '*', '?', '\"', '<', '>', '|', '+', '\0', '%'};

	public static final String DEFAULT_METADATA_FIXER_PREFIX = MetadataFixerConstants.DEFAULT_PREFIX;
	public static final Path DEFAULT_FIX_METADATA_PATH_FOLDER = FileSystems.getDefault().getPath("");
	public static final String DEFAULT_PROFILES_WIKI_PATH = "https://github.com/veraPDF/veraPDF-validation-profiles/wiki";
	public static final boolean DEFAULT_IS_FIX_METADATA = false;
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
	private int maxNumberOfDisplayedFailedChecks;
	private String metadataFixerPrefix;
	private Path fixMetadataPathFolder;
	private String profileWikiPath;
	private ProcessingType processingType;
	private boolean isFixMetadata;
	private FormatOption reportType;
	private Path validationProfilePath;
	private boolean verboseCli;
	private Path policyProfilePath;
	private Path pluginsConfigPath;
	private Path featuresConfigPath;
	private String reportFolderPath;
	private String reportFilePath;
	private boolean isOverwriteReportFile;
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Config)) return false;

		Config config = (Config) o;

		if (maxNumberOfDisplayedFailedChecks != config.maxNumberOfDisplayedFailedChecks) return false;
		if (isFixMetadata != config.isFixMetadata) return false;
		if (verboseCli != config.verboseCli) return false;
		if (isOverwriteReportFile != config.isOverwriteReportFile) return false;
		if (metadataFixerPrefix != null ? !metadataFixerPrefix.equals(config.metadataFixerPrefix) : config.metadataFixerPrefix != null)
			return false;
		if (fixMetadataPathFolder != null ? !fixMetadataPathFolder.equals(config.fixMetadataPathFolder) : config.fixMetadataPathFolder != null)
			return false;
		if (profileWikiPath != null ? !profileWikiPath.equals(config.profileWikiPath) : config.profileWikiPath != null)
			return false;
		if (processingType != config.processingType) return false;
		if (reportType != config.reportType) return false;
		if (validationProfilePath != null ? !validationProfilePath.equals(config.validationProfilePath) : config.validationProfilePath != null)
			return false;
		if (policyProfilePath != null ? !policyProfilePath.equals(config.policyProfilePath) : config.policyProfilePath != null)
			return false;
		if (pluginsConfigPath != null ? !pluginsConfigPath.equals(config.pluginsConfigPath) : config.pluginsConfigPath != null)
			return false;
		if (featuresConfigPath != null ? !featuresConfigPath.equals(config.featuresConfigPath) : config.featuresConfigPath != null)
			return false;
		if (reportFolderPath != null ? !reportFolderPath.equals(config.reportFolderPath) : config.reportFolderPath != null)
			return false;
		return reportFilePath != null ? reportFilePath.equals(config.reportFilePath) : config.reportFilePath == null;
		
	}

	@Override
	public int hashCode() {
		int result = 31 * maxNumberOfDisplayedFailedChecks;
		result = 31 * result + (metadataFixerPrefix != null ? metadataFixerPrefix.hashCode() : 0);
		result = 31 * result + (fixMetadataPathFolder != null ? fixMetadataPathFolder.hashCode() : 0);
		result = 31 * result + (profileWikiPath != null ? profileWikiPath.hashCode() : 0);
		result = 31 * result + (processingType != null ? processingType.hashCode() : 0);
		result = 31 * result + (isFixMetadata ? 1 : 0);
		result = 31 * result + (reportType != null ? reportType.hashCode() : 0);
		result = 31 * result + (validationProfilePath != null ? validationProfilePath.hashCode() : 0);
		result = 31 * result + (verboseCli ? 1 : 0);
		result = 31 * result + (policyProfilePath != null ? policyProfilePath.hashCode() : 0);
		result = 31 * result + (pluginsConfigPath != null ? pluginsConfigPath.hashCode() : 0);
		result = 31 * result + (featuresConfigPath != null ? featuresConfigPath.hashCode() : 0);
		result = 31 * result + (reportFolderPath != null ? reportFolderPath.hashCode() : 0);
		result = 31 * result + (reportFilePath != null ? reportFilePath.hashCode() : 0);
		result = 31 * result + (isOverwriteReportFile ? 1 : 0);
		return result;
	}

	public Config() {
		this(ValidatorFactory.defaultValidatorConfig());
	}
	
	private Config(final ValidatorConfig config) {
		super();
		this.validatorConfig = config;
		this.metadataFixerPrefix = DEFAULT_METADATA_FIXER_PREFIX;
		this.fixMetadataPathFolder = DEFAULT_FIX_METADATA_PATH_FOLDER;
		this.profileWikiPath = DEFAULT_PROFILES_WIKI_PATH;
		this.isFixMetadata = DEFAULT_IS_FIX_METADATA;
		this.processingType = DEFAULT_PROCESSING_TYPE;
		this.reportType = DEFAULT_REPORT_TYPE;
		this.validationProfilePath = DEFAULT_VALIDATION_PROFILE_PATH;
		this.verboseCli = DEFAULT_VERBOSE_CLI;
		this.policyProfilePath = DEFAULT_POLICY_PROFILE;
		this.pluginsConfigPath = DEFAULT_PLUGINS_CONFIG_PATH;
		this.featuresConfigPath = DEFAULT_FEATURES_CONFIG_PATH;
		this.reportFolderPath = DEFAULT_REPORT_FOLDER_PATH;
		this.reportFilePath = DEFAULT_REPORT_FILE_PATH;
		this.isOverwriteReportFile = DEFAULT_IS_OVERWRITE_REPORT_FILE;
	}

	/**
	 * @return selected number for maximum displayed fail checks for a rule. If not selected returns -1
	 */
	@XmlElement
	public int getMaxNumberOfDisplayedFailedChecks() {
		return maxNumberOfDisplayedFailedChecks;
	}

	/**
	 * @return String representation of prefix for fixed files
	 */
	@XmlElement
	public String getMetadataFixerPrefix() {
		return metadataFixerPrefix;
	}

	@XmlElement
	@XmlJavaTypeAdapter(PathAdapter.class)
	private Path getFixMetadataPathFolder() {
		return fixMetadataPathFolder.toString().equals("") ?
				null : fixMetadataPathFolder;
	}

	/**
	 * @return path to the folder in which fixed file will be placed
	 */
	public Path getFixMetadataFolder() {
		return fixMetadataPathFolder;
	}

	/**
	 * @return type of operation to be performed, e. g. validation & feature describing
	 */
	@XmlElement
	@XmlJavaTypeAdapter(ProcessingTypeAdapter.class)
	public ProcessingType getProcessingType() {
		return processingType;
	}

	/**
	 * @return true if metadata fixes are to be performed
	 */
	@XmlElement
	public boolean isFixMetadata() {
		return isFixMetadata;
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
		return validationProfilePath.toString().equals("") ?
				null : validationProfilePath;
	}
	@XmlElement
	@XmlJavaTypeAdapter(PathAdapter.class)
	private Path getPolicyProfilePath() {
		return policyProfilePath.toString().equals("") ?
				null : policyProfilePath;
	}

	/**
	 * @return path to the policy profile
	 */
	public Path getPolicyProfile() {
		return policyProfilePath;
	}

	/**
	 * @return path to validation profile to be used. If returned value
	 * is null, then validation flavour is processed
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
		return pluginsConfigPath.toString().equals("") ?
				null : pluginsConfigPath;
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

	@XmlElement
	@XmlJavaTypeAdapter(PathAdapter.class)
	private Path getFeaturesConfigPath() {
		return featuresConfigPath.toString().equals("") ?
				null : featuresConfigPath;
	}

	/**
	 * @return path to the features config file
	 */
	public Path getFeaturesConfigFilePath() {
		return featuresConfigPath;
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
	public static String toXml(final Config toConvert, Boolean prettyXml)
			throws JAXBException, IOException {
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
	public static Config fromXml(final String toConvert)
			throws JAXBException {
		try (StringReader reader = new StringReader(toConvert)) {
			return fromXml(reader);
		}
	}

	/**
	 * Converts Config to XML,
	 *
	 * @see javax.xml.bind.JAXB for more details
	 */
	public static void toXml(final Config toConvert,
							 final OutputStream stream, Boolean prettyXml) throws JAXBException {
		Marshaller varMarshaller = getMarshaller(prettyXml);
		varMarshaller.marshal(toConvert, stream);
	}

	/**
	 * Converts XML file to Config,
	 *
	 * @see javax.xml.bind.JAXB for more details
	 */
	public static Config fromXml(final InputStream toConvert)
			throws JAXBException {
		Unmarshaller stringUnmarshaller = getUnmarshaller();
		return (Config) stringUnmarshaller.unmarshal(toConvert);
	}

	static void toXml(final Config toConvert, final Writer writer,
					  Boolean prettyXml) throws JAXBException {
		Marshaller varMarshaller = getMarshaller(prettyXml);
		varMarshaller.marshal(toConvert, writer);
	}

	static Config fromXml(final Reader toConvert)
			throws JAXBException {
		Unmarshaller stringUnmarshaller = getUnmarshaller();
		return (Config) stringUnmarshaller.unmarshal(toConvert);
	}

	private static Unmarshaller getUnmarshaller() throws JAXBException {
		JAXBContext context = JAXBContext
				.newInstance(Config.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return unmarshaller;
	}

	private static Marshaller getMarshaller(Boolean setPretty)
			throws JAXBException {
		JAXBContext context = JAXBContext
				.newInstance(Config.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, setPretty);
		return marshaller;
	}

	/**
	 * Checks is the parameter path a valid for saving fixed file
	 *
	 * @param path path for check
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
	 * @param path path for check
	 * @return true if it is valid
	 */
	public static boolean isValidProfilePath(Path path) {
		if (path == null) {
			return true;    // If we chose some path and then decided to use flavour
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
				LOGGER.log(Level.WARNING, "Can't construct ProcessingType from string \"" + v + "\", setting ProcessingType to default", e);
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
}
