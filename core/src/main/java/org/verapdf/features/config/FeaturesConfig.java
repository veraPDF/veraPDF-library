package org.verapdf.features.config;

import org.verapdf.features.FeaturesObjectTypesEnum;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.*;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement(name = "featuresConfig")
public class FeaturesConfig {

	@XmlElement
	private final Boolean informationDict;
	@XmlElement
	private final Boolean metadata;
	@XmlElement
	private final Boolean documentSecurity;
	@XmlElement
	private final Boolean signatures;
	@XmlElement
	private final Boolean lowLevelInfo;
	@XmlElement
	private final Boolean embeddedFiles;
	@XmlElement
	private final Boolean iccProfiles;
	@XmlElement
	private final Boolean outputIntents;
	@XmlElement
	private final Boolean outlines;
	@XmlElement
	private final Boolean annotations;
	@XmlElement
	private final Boolean pages;
	@XmlElement
	private final Boolean graphicsStates;
	@XmlElement
	private final Boolean colorSpaces;
	@XmlElement
	private final Boolean patterns;
	@XmlElement
	private final Boolean shadings;
	@XmlElement
	private final Boolean xobjects;
	@XmlElement
	private final Boolean fonts;
	@XmlElement
	private final Boolean propertiesDicts;

	private FeaturesConfig(Boolean informationDict, Boolean metadata,
						   Boolean documentSecurity, Boolean signatures,
						   Boolean lowLevelInfo, Boolean embeddedFiles,
						   Boolean iccProfiles, Boolean outputIntents,
						   Boolean outlines, Boolean annotations,
						   Boolean pages, Boolean graphicsStates,
						   Boolean colorSpaces, Boolean patterns,
						   Boolean shadings, Boolean xobjects,
						   Boolean fonts, Boolean propertiesDicts) {
		this.informationDict = informationDict;
		this.metadata = metadata;
		this.documentSecurity = documentSecurity;
		this.signatures = signatures;
		this.lowLevelInfo = lowLevelInfo;
		this.embeddedFiles = embeddedFiles;
		this.iccProfiles = iccProfiles;
		this.outputIntents = outputIntents;
		this.outlines = outlines;
		this.annotations = annotations;
		this.pages = pages;
		this.graphicsStates = graphicsStates;
		this.colorSpaces = colorSpaces;
		this.patterns = patterns;
		this.shadings = shadings;
		this.xobjects = xobjects;
		this.fonts = fonts;
		this.propertiesDicts = propertiesDicts;
	}

	private FeaturesConfig() {
		this(false, false, false, false, false, false, false, false, false,
				false, false, false, false, false, false, false, false, false);
	}

	public boolean isFeaturesEnabledForType(FeaturesObjectTypesEnum type) {
		switch (type) {
			case INFORMATION_DICTIONARY:
				return isInformationDictEnabled();
			case METADATA:
				return isMetadataEnabled();
			case DOCUMENT_SECURITY:
				return isDocumentSecurityEnabled();
			case SIGNATURE:
				return isSignaturesEnabled();
			case LOW_LEVEL_INFO:
				return isLowLevelInfoEnabled();
			case EMBEDDED_FILE:
				return isEmbeddedFilesEnabled();
			case ICCPROFILE:
				return isIccProfilesEnabled();
			case OUTPUTINTENT:
				return isOutputIntentsEnabled();
			case OUTLINES:
				return isOutlinesEnabled();
			case ANNOTATION:
				return isAnnotationsEnabled();
			case PAGE:
				return isPagesEnabled();
			case EXT_G_STATE:
				return isGraphicsStatesEnabled();
			case COLORSPACE:
				return isColorSpacesEnabled();
			case PATTERN:
				return isPatternsEnabled();
			case SHADING:
				return isShadingsEnabled();
			case FORM_XOBJECT:
			case IMAGE_XOBJECT:
			case POSTSCRIPT_XOBJECT:
			case FAILED_XOBJECT:
				return isXobjectsEnabled();
			case FONT:
				return isFontsEnabled();
			case PROPERTIES:
				return isPropertiesDictsEnabled();
			default:
				return false;
		}
	}

	public boolean isFeaturesEnabled() {
		return isInformationDictEnabled() || isMetadataEnabled()
				|| isDocumentSecurityEnabled() || isSignaturesEnabled()
				|| isLowLevelInfoEnabled() || isEmbeddedFilesEnabled()
				|| isIccProfilesEnabled() || isOutputIntentsEnabled()
				|| isOutlinesEnabled() || isAnnotationsEnabled()
				|| isPagesEnabled() || isGraphicsStatesEnabled()
				|| isColorSpacesEnabled() || isPatternsEnabled()
				|| isShadingsEnabled() || isXobjectsEnabled()
				|| isFontsEnabled() || isPropertiesDictsEnabled();
	}

	public boolean isInformationDictEnabled() {
		return informationDict != null && informationDict.booleanValue();
	}

	public boolean isMetadataEnabled() {
		return metadata != null && metadata.booleanValue();
	}

	public boolean isDocumentSecurityEnabled() {
		return documentSecurity != null && documentSecurity.booleanValue();
	}

	public boolean isSignaturesEnabled() {
		return signatures != null && signatures.booleanValue();
	}

	public boolean isLowLevelInfoEnabled() {
		return lowLevelInfo != null && lowLevelInfo.booleanValue();
	}

	public boolean isEmbeddedFilesEnabled() {
		return embeddedFiles != null && embeddedFiles.booleanValue();
	}

	public boolean isIccProfilesEnabled() {
		return iccProfiles != null && iccProfiles.booleanValue();
	}

	public boolean isOutputIntentsEnabled() {
		return outputIntents != null && outputIntents.booleanValue();
	}

	public boolean isOutlinesEnabled() {
		return outlines != null && outlines.booleanValue();
	}

	public boolean isAnnotationsEnabled() {
		return annotations != null && annotations.booleanValue();
	}

	public boolean isPagesEnabled() {
		return pages != null && pages.booleanValue();
	}

	public boolean isGraphicsStatesEnabled() {
		return graphicsStates != null && graphicsStates.booleanValue();
	}

	public boolean isColorSpacesEnabled() {
		return colorSpaces != null && colorSpaces.booleanValue();
	}

	public boolean isPatternsEnabled() {
		return patterns != null && patterns.booleanValue();
	}

	public boolean isShadingsEnabled() {
		return shadings != null && shadings.booleanValue();
	}

	public boolean isXobjectsEnabled() {
		return xobjects != null && xobjects.booleanValue();
	}

	public boolean isFontsEnabled() {
		return fonts != null && fonts.booleanValue();
	}

	public boolean isPropertiesDictsEnabled() {
		return propertiesDicts != null && propertiesDicts.booleanValue();
	}

	/**
	 *	Converts PluginsCollectionConfig to XML,
	 *	@see javax.xml.bind.JAXB for more details
	 */
	public static String toXml(final FeaturesConfig toConvert, Boolean prettyXml)
			throws JAXBException, IOException {
		String retVal = "";
		try (StringWriter writer = new StringWriter()) {
			toXml(toConvert, writer, prettyXml);
			retVal = writer.toString();
			return retVal;
		}
	}

	/**
	 *	Converts XML file to PluginsCollectionConfig,
	 *	@see javax.xml.bind.JAXB for more details
	 */
	public static FeaturesConfig fromXml(final String toConvert)
			throws JAXBException {
		try (StringReader reader = new StringReader(toConvert)) {
			return fromXml(reader);
		}
	}

	/**
	 *	Converts PluginsCollectionConfig to XML,
	 *	@see javax.xml.bind.JAXB for more details
	 */
	public static void toXml(final FeaturesConfig toConvert,
							 final OutputStream stream, Boolean prettyXml) throws JAXBException {
		Marshaller varMarshaller = getMarshaller(prettyXml);
		varMarshaller.marshal(toConvert, stream);
	}

	/**
	 *	Converts XML file to PluginsCollectionConfig,
	 *	@see javax.xml.bind.JAXB for more details
	 */
	public static FeaturesConfig fromXml(final InputStream toConvert)
			throws JAXBException {
		Unmarshaller stringUnmarshaller = getUnmarshaller();
		return (FeaturesConfig) stringUnmarshaller.unmarshal(toConvert);
	}

	static void toXml(final FeaturesConfig toConvert, final Writer writer,
					  Boolean prettyXml) throws JAXBException {
		Marshaller varMarshaller = getMarshaller(prettyXml);
		varMarshaller.marshal(toConvert, writer);
	}

	static FeaturesConfig fromXml(final Reader toConvert)
			throws JAXBException {
		Unmarshaller stringUnmarshaller = getUnmarshaller();
		return (FeaturesConfig) stringUnmarshaller.unmarshal(toConvert);
	}

	private static Unmarshaller getUnmarshaller() throws JAXBException {
		JAXBContext context = JAXBContext
				.newInstance(FeaturesConfig.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return unmarshaller;
	}

	private static Marshaller getMarshaller(Boolean setPretty)
			throws JAXBException {
		JAXBContext context = JAXBContext
				.newInstance(FeaturesConfig.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, setPretty);
		return marshaller;
	}

	public static final class Builder {

		private Boolean informationDict = Boolean.TRUE;
		private Boolean metadata = Boolean.FALSE;
		private Boolean documentSecurity = Boolean.FALSE;
		private Boolean signatures = Boolean.FALSE;
		private Boolean lowLevelInfo = Boolean.FALSE;
		private Boolean embeddedFiles = Boolean.FALSE;
		private Boolean iccProfiles = Boolean.FALSE;
		private Boolean outputIntents = Boolean.FALSE;
		private Boolean outlines = Boolean.FALSE;
		private Boolean annotations = Boolean.FALSE;
		private Boolean pages = Boolean.FALSE;
		private Boolean graphicsStates = Boolean.FALSE;
		private Boolean colorSpaces = Boolean.FALSE;
		private Boolean patterns = Boolean.FALSE;
		private Boolean shadings = Boolean.FALSE;
		private Boolean xobjects = Boolean.FALSE;
		private Boolean fonts = Boolean.FALSE;
		private Boolean propertiesDicts = Boolean.FALSE;

		public Builder() {
		}

		public FeaturesConfig build() {
			return new FeaturesConfig(this.informationDict, this.metadata,
					this.documentSecurity, this.signatures,
					this.lowLevelInfo, this.embeddedFiles,
					this.iccProfiles, this.outputIntents,
					this.outlines, this.annotations,
					this.pages, this.graphicsStates,
					this.colorSpaces, this.patterns,
					this.shadings, this.xobjects,
					this.fonts, this.propertiesDicts);
		}

		public Builder informationDict(Boolean informationDict) {
			this.informationDict = informationDict;
			return this;
		}

		public Builder metadata(Boolean metadata) {
			this.metadata = metadata;
			return this;
		}

		public Builder documentSecurity(Boolean documentSecurity) {
			this.documentSecurity = documentSecurity;
			return this;
		}

		public Builder signatures(Boolean signatures) {
			this.signatures = signatures;
			return this;
		}

		public Builder lowLevelInfo(Boolean lowLevelInfo) {
			this.lowLevelInfo = lowLevelInfo;
			return this;
		}

		public Builder embeddedFiles(Boolean embeddedFiles) {
			this.embeddedFiles = embeddedFiles;
			return this;
		}

		public Builder iccProfiles(Boolean iccProfiles) {
			this.iccProfiles = iccProfiles;
			return this;
		}

		public Builder outputIntents(Boolean outputIntents) {
			this.outputIntents = outputIntents;
			return this;
		}

		public Builder outlines(Boolean outlines) {
			this.outlines = outlines;
			return this;
		}

		public Builder annotations(Boolean annotations) {
			this.annotations = annotations;
			return this;
		}

		public Builder pages(Boolean pages) {
			this.pages = pages;
			return this;
		}

		public Builder graphicsStates(Boolean graphicsStates) {
			this.graphicsStates = graphicsStates;
			return this;
		}

		public Builder colorSpaces(Boolean colorSpaces) {
			this.colorSpaces = colorSpaces;
			return this;
		}

		public Builder patterns(Boolean patterns) {
			this.patterns = patterns;
			return this;
		}

		public Builder shadings(Boolean shadings) {
			this.shadings = shadings;
			return this;
		}

		public Builder xobjects(Boolean xobjects) {
			this.xobjects = xobjects;
			return this;
		}

		public Builder fonts(Boolean fonts) {
			this.fonts = fonts;
			return this;
		}

		public Builder propertiesDicts(Boolean propertiesDicts) {
			this.propertiesDicts = propertiesDicts;
			return this;
		}
	}
}
