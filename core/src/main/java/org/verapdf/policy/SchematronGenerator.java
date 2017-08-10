package org.verapdf.policy;

import javanet.staxutils.IndentingXMLStreamWriter;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.objects.Feature;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.util.EnumSet;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class SchematronGenerator {

	public static final String SCH_NAMESPACE = "http://purl.oclc.org/dsdl/schematron";
	public static final String SCH_PREFIX = "sch";

	public static final String ROOT_NAME = "schema";
	public static final String ENABLED_FEATURES_ATTRIBUTE_NAME = "veraPDFNecessaryFeatures";

	private static final String DOC_RESOURCES = "/documentResources";

	private SchematronGenerator() {
	}

	public static void writeSchematron(List<Assertion> assertions, OutputStream os) throws XMLStreamException {
		if (assertions == null || assertions.isEmpty()) {
			throw new IllegalArgumentException("Assertions has to be non empty list");
		}
		XMLOutputFactory xof = XMLOutputFactory.newInstance();
		XMLStreamWriter xtw = new IndentingXMLStreamWriter(xof.createXMLStreamWriter(os));
		xtw.writeStartDocument("utf-8","1.0");
		xtw.setPrefix(SCH_PREFIX, SCH_NAMESPACE);
		xtw.writeStartElement(SCH_NAMESPACE,ROOT_NAME);
		xtw.writeNamespace(SCH_PREFIX, SCH_NAMESPACE);
		xtw.writeAttribute("queryBinding", "xslt");
		String enabledFeatures = getEnabledFeatures(assertions);
		if (enabledFeatures != null) {
			xtw.writeAttribute(ENABLED_FEATURES_ATTRIBUTE_NAME, enabledFeatures);
		}
		for (Assertion assertion : assertions) {
			xtw.writeStartElement(SCH_NAMESPACE, "pattern");
			xtw.writeStartElement(SCH_NAMESPACE, "rule");
			String ruleContext = getRuleContext(assertion.getFeatureType());
			xtw.writeAttribute("context", ruleContext);
			xtw.writeStartElement(SCH_NAMESPACE, "assert");
			SchematronOperation.AssertionInformation assertionInfo = assertion.getAssertionInfo();
			xtw.writeAttribute("test", assertionInfo.getTestAssertion());
			xtw.writeCharacters(assertionInfo.getAssertionDescription());
			xtw.writeEndElement();
			xtw.writeEndElement();
			xtw.writeEndElement();
		}
		xtw.writeEndElement();
		xtw.writeEndDocument();
		xtw.close();
	}

	private static String getEnabledFeatures(List<Assertion> assertions) {
		EnumSet<FeatureObjectType> res = EnumSet.noneOf(FeatureObjectType.class);
		for (Assertion assertion : assertions) {
			res.add(assertion.getFeatureType());
		}
		if (res.isEmpty()) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		for (FeatureObjectType feature : res) {
			builder.append(feature.getFullName()).append(",");
		}
		return builder.substring(0, builder.length() - 1);
	}

	private static String getRuleContext(FeatureObjectType featureType) {
		String base = "/report/jobs/job/featuresReport";
		switch (featureType) {
			case SIGNATURE:
				return base + "/signatures";
			case ACTION:
				return base + "/actions";
			case INTERACTIVE_FORM_FIELDS:
				return base + "/interactiveFormFields";
			case EMBEDDED_FILE:
				return base + "/embeddedFiles";
			case ICCPROFILE:
				return base + "/iccProfiles";
			case OUTPUTINTENT:
				return base + "/outputIntents";
			case ANNOTATION:
				return base + "/annotations";
			case PAGE:
				return base + "/pages";
			case EXT_G_STATE:
				return base + DOC_RESOURCES + "/graphicsStates";
			case COLORSPACE:
				return base + DOC_RESOURCES + "/colorSpaces";
			case PATTERN:
				return base + DOC_RESOURCES + "/patterns";
			case SHADING:
				return base + DOC_RESOURCES + "/shadings";
			case FORM_XOBJECT:
			case IMAGE_XOBJECT:
			case POSTSCRIPT_XOBJECT:
				return base + DOC_RESOURCES + "/xobjects";
			case FONT:
				return base + DOC_RESOURCES + "/fonts";
			case PROPERTIES:
				return base + DOC_RESOURCES + "/propertiesDicts";
			default:
				return base;
		}
	}

	public static class Assertion {
		private FeatureObjectType featureType;
		private Feature feature;
		private SchematronOperation operation;
		private String argument;

		public Assertion(FeatureObjectType featureType, Feature feature, SchematronOperation operation, String argument) {
			if (featureType == null || feature == null || operation == null) {
				throw new IllegalArgumentException("Arguments feature type, feature and operation can not be null");
			}
			this.featureType = featureType;
			this.feature = feature;
			this.operation = operation;
			this.argument = argument;
		}

		public FeatureObjectType getFeatureType() {
			return featureType;
		}

		public Feature getFeature() {
			return feature;
		}

		public SchematronOperation getOperation() {
			return operation;
		}

		public String getArgument() {
			return argument;
		}

		public SchematronOperation.AssertionInformation getAssertionInfo() {
			return this.operation.getAssertionInfo(feature, argument);
		}
	}
}
