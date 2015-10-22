package org.verapdf.metadata.fixer.utils;

/**
 * @author Evgeniy Muravitskiy
 */
public class MetadataFixerConstants {

	public static final String METADATA_TITLE = "title";
	public static final String METADATA_SUBJECT = "description";
	public static final String METADATA_AUTHOR = "creator";
	public static final String METADATA_CREATION_DATE = "CreateDate";
	public static final String METADATA_CREATOR = "CreatorTool";
	public static final String METADATA_MODIFICATION_DATE = "ModifyDate";

	public static final String KEYWORDS = "Keywords";
	public static final String PRODUCER = "Producer";

	public static final String INFO_TITLE = "Title";
	public static final String INFO_SUBJECT = "Subject";
	public static final String INFO_AUTHOR = "Author";
	public static final String INFO_CREATOR = "Creator";
	public static final String INFO_CREATION_DATE = "CreationDate";
	public static final String INFO_MODIFICATION_DATE = "ModDate";

	public static final String PROCESSED_OBJECTS_PROPERTIES_PATH = "/processed-objects.properties";
	public static final String RULE_DESCRIPTION_TAG = "ruleDescription";
	public static final String OBJECT_TYPE_TAG = "objectType";
	public static final String TEST_TAG = "test";

	public static final String PDF_DATE_FORMAT_REGEX = "(D:)?(\\d\\d){2,7}((([+-](\\d\\d[']))(\\d\\d['])?)?|[Z])";
	public static final String UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
	public static final String DEFAULT_PREFIX = "veraPDF_";
}
