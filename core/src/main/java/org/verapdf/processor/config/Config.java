package org.verapdf.processor.config;

import org.apache.log4j.Logger;
import org.verapdf.metadata.fixer.utils.MetadataFixerConstants;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.*;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Maksim Bezrukov
 */

@XmlRootElement(name = "config")
public final class Config {

	private static final char[] FORBIDDEN_SYMBOLS_IN_FILE_NAME = new char[]{'\\', '/', ':', '*', '?', '\"', '<', '>', '|', '+', '\0', '%'};

	public static final int DEFAULT_MAX_NUMBER_OF_FAILED_CHECKS = -1;
	public static final boolean DEFAULT_SHOW_PASSED_RULES = false;
	public static final int DEFAULT_MAX_NUMBER_OF_DISPLAYED_FAILED_CHECKS = 100;
	public static final String DEFAULT_METADATA_FIXER_PREFIX = MetadataFixerConstants.DEFAULT_PREFIX;
	public static final Path DEFAULT_FIX_METADATA_PATH_FOLDER = FileSystems.getDefault().getPath("");
	public static final String DEFAULT_PROFILES_WIKI_PATH = "https://github.com/veraPDF/veraPDF-validation-profiles/wiki";
	public static final boolean DEFAULT_IS_FIX_METADATA = false;
	public static final ProcessingType DEFAULT_PROCESSING_TYPE = ProcessingType.VALIDATION;
	public static final FormatOption DEFAULT_REPORT_TYPE = FormatOption.MRR;
	public static final Path DEFAULT_VALIDATION_PROFILE_PATH = FileSystems.getDefault().getPath("");
	public static final PDFAFlavour DEFAULT_FLAVOUR = PDFAFlavour.AUTO;
	public static final boolean DEFAULT_VERBOSE_CLI = false;
	public static final Path DEFAULT_POLICY_PROFILE = FileSystems.getDefault().getPath("");
	public static final Path DEFAULT_PLUGINS_CONFIG_PATH = FileSystems.getDefault().getPath("");
	public static final Path DEFAULT_FEATURES_CONFIG_PATH = FileSystems.getDefault().getPath("");
	public static final String DEFAULT_REPORT_FOLDER_PATH = "";
	public static final String DEFAULT_REPORT_FILE_PATH = "";
	public static final boolean DEFAULT_IS_OVERWRITE_REPORT_FILE = false;
	
	private boolean showPassedRules;
	private int maxNumberOfFailedChecks;
	private int maxNumberOfDisplayedFailedChecks;
	private String metadataFixerPrefix;
	private Path fixMetadataPathFolder;
	private String profileWikiPath;
	private ProcessingType processingType;
	private boolean isFixMetadata;
	private FormatOption reportType;
	private Path validationProfilePath;
	private PDFAFlavour flavour;
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

		if (showPassedRules != config.showPassedRules) return false;
		if (maxNumberOfFailedChecks != config.maxNumberOfFailedChecks) return false;
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
		if (flavour != config.flavour) return false;
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
		int result = (showPassedRules ? 1 : 0);
		result = 31 * result + maxNumberOfFailedChecks;
		result = 31 * result + maxNumberOfDisplayedFailedChecks;
		result = 31 * result + (metadataFixerPrefix != null ? metadataFixerPrefix.hashCode() : 0);
		result = 31 * result + (fixMetadataPathFolder != null ? fixMetadataPathFolder.hashCode() : 0);
		result = 31 * result + (profileWikiPath != null ? profileWikiPath.hashCode() : 0);
		result = 31 * result + (processingType != null ? processingType.hashCode() : 0);
		result = 31 * result + (isFixMetadata ? 1 : 0);
		result = 31 * result + (reportType != null ? reportType.hashCode() : 0);
		result = 31 * result + (validationProfilePath != null ? validationProfilePath.hashCode() : 0);
		result = 31 * result + (flavour != null ? flavour.hashCode() : 0);
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
		this.showPassedRules = DEFAULT_SHOW_PASSED_RULES;
		this.maxNumberOfFailedChecks = DEFAULT_MAX_NUMBER_OF_FAILED_CHECKS;
		this.maxNumberOfDisplayedFailedChecks = DEFAULT_MAX_NUMBER_OF_DISPLAYED_FAILED_CHECKS;
		this.metadataFixerPrefix = DEFAULT_METADATA_FIXER_PREFIX;
		this.fixMetadataPathFolder = DEFAULT_FIX_METADATA_PATH_FOLDER;
		this.profileWikiPath = DEFAULT_PROFILES_WIKI_PATH;
		this.isFixMetadata = DEFAULT_IS_FIX_METADATA;
		this.processingType = DEFAULT_PROCESSING_TYPE;
		this.reportType = DEFAULT_REPORT_TYPE;
		this.validationProfilePath = DEFAULT_VALIDATION_PROFILE_PATH;
		this.flavour = DEFAULT_FLAVOUR;
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
	 * @return selected number for maximum fail checks for a rule. If not selected returns -1
	 */
	@XmlElement
	public int getMaxNumberOfFailedChecks() {
		return maxNumberOfFailedChecks;
	}

	/**
	 * @return true if desplay passed pules option selected
	 */
	@XmlElement
	public boolean isShowPassedRules() {
		return showPassedRules;
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
	 * @return validation flavour to be used
	 */
	@XmlElement
	@XmlJavaTypeAdapter(FlavourAdapter.class)
	public PDFAFlavour getFlavour() {
		return flavour;
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
	 * Changes settings parameters
	 *
	 * @param metadataFixerPrefix a prefix which will be added to the fixed file
	 * @throws IllegalArgumentException parameter can not be null
	 */
	public void setMetadataFixerPrefix(String metadataFixerPrefix) {
		if (metadataFixerPrefix == null) {
			throw new IllegalArgumentException("Prefix for metadata fixer can not be null");
		}
		for (char c : metadataFixerPrefix.toCharArray()) {
			if (!isValidFileNameCharacter(c)) {
				throw new IllegalArgumentException("Prefix for metadata fixer contains forbidden symbols");
			}
		}
		this.metadataFixerPrefix = metadataFixerPrefix;
	}

	/**
	 * Changes settings parameter
	 *
	 * @param showPassedRules true for show passed rules at the report
	 */
	public void setShowPassedRules(boolean showPassedRules) {
		this.showPassedRules = showPassedRules;
	}

	/**
	 * Changes settings parameter
	 *
	 * @param maxNumberOfFailedChecks a natural number that indicates maximum number of failed checks for rule or -1 for unlimited
	 * @throws IllegalArgumentException if parameter is not a natural number or -1
	 */
	public void setMaxNumberOfFailedChecks(int maxNumberOfFailedChecks) {
		if (maxNumberOfFailedChecks > 0 || maxNumberOfFailedChecks == -1) {
			this.maxNumberOfFailedChecks = maxNumberOfFailedChecks;
		} else {
			throw new IllegalArgumentException("Max number of failed checks for rule for setter method is not a natural or -1");
		}
	}

	/**
	 * Changes settings parameter
	 *
	 * @param maxNumberOfDisplayedFailedChecks a non negative integer number that indicates maximum number of displayed
	 *                                         failed checks for rule or -1 for infinite
	 * @throws IllegalArgumentException if parameter is less than -1
	 */
	public void setMaxNumberOfDisplayedFailedChecks(int maxNumberOfDisplayedFailedChecks) {
		if (maxNumberOfDisplayedFailedChecks >= -1) {
			this.maxNumberOfDisplayedFailedChecks = maxNumberOfDisplayedFailedChecks;
		} else {
			throw new IllegalArgumentException("Max number of displayed failed checks for rule for setter method is less than -1");
		}
	}

	/**
	 * Changes settings parameters
	 *
	 * @param fixMetadataPathFolder a path to the folder in which fixed files will be saved
	 * @throws IllegalArgumentException parameter should be an empty path or a path to an existing and write acceptable directory
	 */
	public void setFixMetadataPathFolder(Path fixMetadataPathFolder) {
		if (isValidFolderPath(fixMetadataPathFolder)) {
			this.fixMetadataPathFolder = fixMetadataPathFolder;
		} else {
			throw new IllegalArgumentException("Path should be an empty path or a path to an existing and write acceptable directory");
		}
	}

	/**
	 * Changes settings parameters
	 *
	 * @param profileWikiPath a link to wiki about validation rules
	 */
	public void setProfileWikiPath(String profileWikiPath) {
		this.profileWikiPath = profileWikiPath;
	}

	/**
	 * Changes settings parameters
	 *
	 * @param fixMetadata true if metadata fixes should be performed
	 */
	public void setFixMetadata(boolean fixMetadata) {
		isFixMetadata = fixMetadata;
	}

	/**
	 * Changes settings parameters
	 *
	 * @param processingType type of operation to be performed
	 */
	public void setProcessingType(ProcessingType processingType) {
		this.processingType = processingType;
	}

	/**
	 * Changes settings parameters
	 *
	 * @param reportType type of report to be generated
	 */
	public void setReportType(FormatOption reportType) {
		this.reportType = reportType;
	}

	/**
	 * Changes settings parameters
	 *
	 * @param validationProfilePath a path to the validation profile to be used in validation
	 * @throws IllegalArgumentException parameter should be an empty path or a path to an existing and write acceptable file
	 */
	public void setValidationProfilePath(Path validationProfilePath) {
		if (isValidProfilePath(validationProfilePath)) {
			this.validationProfilePath = validationProfilePath;
		} else {
			throw new IllegalArgumentException("Path should be path to an existing and read acceptable file");
		}
	}

	/**
	 * Changes settings parameters
	 *
	 * @param policyProfilePath a path to the policy profile to be used in policy checks
	 * @throws IllegalArgumentException parameter should be an empty path or a path to an existing and write acceptable file
	 */
	public void setPolicyProfilePath(Path policyProfilePath) {
		if (isValidProfilePath(policyProfilePath)) {
			this.policyProfilePath = policyProfilePath;
		} else {
			throw new IllegalArgumentException("Path should be path to an existing and read acceptable file");
		}
	}

	/**
	 * Changes settings parameters
	 *
	 * @param pluginsConfigPath a path to the plugins config file
	 */
	public void setPluginsConfigPath(Path pluginsConfigPath) {
		this.pluginsConfigPath = pluginsConfigPath;
	}

	/**
	 * Changes settings parameters
	 *
	 * @param featuresConfigPath a path to the features config file
	 */
	public void setFeaturesConfigPath(Path featuresConfigPath) {
		this.featuresConfigPath = featuresConfigPath;
	}

	/**
	 * Changes settings parameters
	 *
	 * @param flavour validation flavour to be used
	 */
	public void setFlavour(PDFAFlavour flavour) {
		this.flavour = flavour;
	}

	/**
	 * Changes settings parameters
	 *
	 * @param verboseCli true if cli text report should be verbose
	 */
	public void setVerboseCli(boolean verboseCli) {
		this.verboseCli = verboseCli;
	}

	/**
	 * Changes settings parameters
	 *
     * @author: mancuska@digitaldocuments.org
	 * @param reportFolder path to folder where results will be saved
	 */
	public void setReportFolderPath(String reportFolder) {
        if (reportFolder.isEmpty())
            this.reportFolderPath = reportFolder;
        else {
            File dir = new File(reportFolder);

            if (dir.exists()) {
                this.reportFolderPath = reportFolder;
            }
            else {
                try {
                    dir.mkdirs();
                    this.reportFolderPath = reportFolder;
                }
                catch (SecurityException ex) {
                    this.reportFolderPath = "";
                }
            }
        }
    }

	/**
	 * Changes settings parameters
	 *
     * @author: mancuska@digitaldocuments.org
	 * @param reportFile path to report file
	 */
	public void setReportFilePath(String reportFile) {
	    this.reportFilePath = reportFile;
	}
	
	/**
	 * Changes settings parameters
	 *
     * @author: mancuska@digitaldocuments.org
	 * @param overwriteReportFile report file overwriting flag
	 */
	public void setOverwriteReportFile(boolean overwriteReportFile) {
		this.isOverwriteReportFile = overwriteReportFile;
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

	/**
	 * Checks is the character valid for file name
	 *
	 * @param c character to be checked
	 * @return true if it is valid
	 */
	public static boolean isValidFileNameCharacter(char c) {
		for (char ch : FORBIDDEN_SYMBOLS_IN_FILE_NAME) {
			if (ch == c) {
				return false;
			}
		}
		return true;
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

		private final Logger LOGGER = Logger.getLogger(ProcessingTypeAdapter.class);

		@Override
		public ProcessingType unmarshal(String v) throws Exception {
			try {
				return ProcessingType.fromString(v);
			} catch (IllegalArgumentException e) {
				LOGGER.error("Can't construct ProcessingType from string \"" + v + "\", setting ProcessingType to default", e);
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
		public FormatOption unmarshal(String v) throws Exception {
			return FormatOption.fromOption(v);
		}

		@Override
		public String marshal(FormatOption v) throws Exception {
			return v.toString();
		}
	}

	private static class FlavourAdapter extends XmlAdapter<String, PDFAFlavour> {

		@Override
		public PDFAFlavour unmarshal(String v) throws Exception {
			return PDFAFlavour.fromString(v);
		}

		@Override
		public String marshal(PDFAFlavour v) throws Exception {
			return v.toString();
		}
	}
}
