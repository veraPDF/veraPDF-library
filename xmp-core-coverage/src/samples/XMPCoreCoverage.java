// =================================================================================================
// ADOBE SYSTEMS INCORPORATED
// Copyright 200 Adobe Systems Incorporated
// All Rights Reserved
//
// NOTICE:  Adobe permits you to use, modify, and distribute this file in accordance with the terms
// of the Adobe license agreement accompanying it.
// =================================================================================================

package samples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.XMPDateTime;
import com.adobe.xmp.XMPDateTimeFactory;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPIterator;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.XMPPathFactory;
import com.adobe.xmp.XMPSchemaRegistry;
import com.adobe.xmp.options.IteratorOptions;
import com.adobe.xmp.options.ParseOptions;
import com.adobe.xmp.options.PropertyOptions;
import com.adobe.xmp.options.SerializeOptions;
import com.adobe.xmp.properties.XMPAliasInfo;
import com.adobe.xmp.properties.XMPProperty;
import com.adobe.xmp.properties.XMPPropertyInfo;


/**
 * An example for XMPCore. 
 * It covers most of the functionality of XMPCore,
 * but does not provide any meaningful workflow.
 * 
 * @since   11.02.2007
 */
public class XMPCoreCoverage implements XMPCoreCoverageConst
{
	/** the log stream for all outputs */
	private static PrintStream log;
	/** shortcut for the schema registry */
	private static XMPSchemaRegistry registry = XMPMetaFactory.getSchemaRegistry();
	
	
	/**
	 * Runs the example
	 * @param args not used
	 */
	public static void main(String[] args)
	{
		try
		{
			URL url = XMPCoreCoverage.class.getResource(".");
			String filePath = "file:" + url.getPath().replaceFirst("/bin/", "/src/")
					+ "XMPCoreCoverage.log";
			File file = new File (new URI(filePath));
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file); 
			log = new PrintStream(out);
			
			println("XMPCoreCoverage starting   " + new Date());
			println("XMPCore Version: " + XMPMetaFactory.getVersionInfo());
			println();

			
			doCoreCoverage();

			
			println(); println();
			println("XMPCoreCoverage ending   " + new Date());
		}
		catch (XMPException e)
		{
			println("Caught XMPException " + e.getErrorCode() + " :   " +e.getMessage());
		}
		catch (Throwable e)
		{
			println("Caught Throwable '" + e.getMessage()  + "'"); 
		}
		finally
		{
			if (log != null)
			{	
				log.close();
			}
		}	
	}

			
	/**
	 * Runs example functions that explain some features of XMPCore.
	 * @throws Exception Forwards exceptions
	 */
	private static void doCoreCoverage() throws Exception
	{
		coverNamespaceRegistry();
			
		coverAliasRegistry();

		coverCreatingXMP();

		XMPMeta meta = coverSetPropertyMethods();

		coverGetPropertyMethods(meta);

		coverExistingProperties(meta);		

		coverDeleteProperties(meta);

		coverLocalisedProperties();

		coverLiteralProperties();

		coverParsing();
	
		coverLinefeedValues();
	
		coverSerialization();

		coverIterator();
		
		coverPathCreation();
		
		coverDateTime();
	}


	/**
	 * List predefined namespaces and aliases;
	 * register new namespaces and aliases.
	 * @throws XMPException Forward exceptions
	 */
	private static void coverNamespaceRegistry() throws XMPException
	{
		writeMajorLabel ("Test of namespace registry");

		// Lists of predefined namespaces
		writeMinorLabel ("List predefined namespaces");
		Map namespaces = registry.getNamespaces();
		for (Iterator it = namespaces.keySet().iterator(); it.hasNext();)
		{
			String prefix = (String) it.next();
			String namespace = (String) namespaces.get(prefix);
			println(prefix + "   --->   " + namespace);
		}
		println();
		
		
		// Registry namespace functions
		writeMinorLabel ("Test namespace registry functions");

		String prefix = registry.registerNamespace(NS1, "ns1");
		println ("registerNamespace ns1:   " + prefix + "   --->   " + 
			registry.getNamespaceURI(prefix));

		prefix = registry.registerNamespace(NS2, "ns2");
		println ("registerNamespace ns2:   " + prefix + "   --->   " + 
			registry.getNamespaceURI(prefix));

		prefix = registry.getNamespacePrefix(NS1);
		println ("getNamespacePrefix ns1:   " + prefix);

		String ns = registry.getNamespaceURI("ns1");
		println ("getNamespaceURI ns1:   " + ns);
		
		prefix = registry.getNamespacePrefix("bogus");
		println ("getNamespacePrefix bogus:   " + prefix);
		
		ns = registry.getNamespaceURI("bogus");
		println ("getNamespaceURI ns1:   " + ns);
	}


	/**
	 * List predefined aliases, register new aliases and resolve aliases.
	 * @throws XMPException Forward exceptions
	 */
	private static void coverAliasRegistry() throws XMPException
	{
		writeMajorLabel ("Test alias registry and functions");
		dumpAliases();

		// Register new aliases
		writeMinorLabel ("Add ns2: to ns1: aliases");
		
		dumpAliases();


		// Resolve aliases
		writeMinorLabel ("Resolve ns2: to ns1: aliases");

		XMPAliasInfo aliasInfo = registry.resolveAlias (NS1, "SimpleActual");
		println ("resolveAlias ns1:SimpleActual:   " + aliasInfo + "   (wrong way!)");

		aliasInfo = registry.resolveAlias (NS2, "SimpleAlias");
		println ("resolveAlias ns2:SimpleAlias:   " + aliasInfo);
		println();

		
		aliasInfo = registry.resolveAlias (NS2, "BagAlias");
		println ("resolveAlias ns2:BagAlias:   " + aliasInfo);

		aliasInfo = registry.resolveAlias (NS2, "SeqAlias");
		println ("resolveAlias ns2:SeqAlias:   " + aliasInfo);

		aliasInfo = registry.resolveAlias (NS2, "AltAlias");
		println ("resolveAlias ns2:AltAlias:   " + aliasInfo);

		aliasInfo = registry.resolveAlias (NS2, "AltTextAlias");
		println ("resolveAlias ns2:AltTextAlias:   " + aliasInfo);
		println();

		
		aliasInfo = registry.resolveAlias (NS2, "BagItemAlias");
		println ("resolveAlias ns2:BagItemAlias:   " + aliasInfo);

		aliasInfo = registry.resolveAlias (NS2, "SeqItemAlias");
		println ("resolveAlias ns2:SeqItemAlias:   " + aliasInfo);

		aliasInfo = registry.resolveAlias (NS2, "AltItemAlias");
		println ("resolveAlias ns2:AltItemAlias:   " + aliasInfo);

		aliasInfo = registry.resolveAlias (NS2, "AltTextItemAlias");
		println ("resolveAlias ns2:AltTextItemAlias:   " + aliasInfo);
		println();
		
		
		// set alias properties
		writeMinorLabel ("Test setProperty through ns2: simple aliases");
		
		XMPMeta meta = XMPMetaFactory.create();
		meta.setProperty (NS2, "SimpleAlias", "Simple value");
		meta.setProperty (NS2, "ns2:BagItemAlias", "BagItem value");
		meta.setProperty (NS2, "SeqItemAlias", "SeqItem value");
		meta.setProperty (NS2, "AltItemAlias", "AltItem value");
		meta.setProperty (NS2, "AltTextItemAlias", "AltTextItem value");
		printXMPMeta(meta, "Check for aliases and bases");

		
		// delete aliases
		writeMinorLabel ("Delete some ns2: to ns1: aliases");

		dumpAliases();
	}
	
	
	/**
	 * Test simple constructors and parsing, setting the instance ID
	 * @throws XMPException Forwards exceptions
	 */
	private static void coverCreatingXMP() throws XMPException
	{
		writeMajorLabel ("Test simple constructors and parsing, setting the instance ID");

		XMPMeta meta1 = XMPMetaFactory.create();
		printXMPMeta(meta1, "Empty XMP object");
				
		XMPMeta meta2 = XMPMetaFactory.create();
		meta2.setObjectName("New object name");
		printXMPMeta(meta2, "XMP object with name");
		
		XMPMeta meta3 = XMPMetaFactory.parseFromString(RDF_COVERAGE);
		printXMPMeta(meta3, "Construct and parse from buffer");

		meta3.setProperty (XMPConst.NS_XMP_MM, "InstanceID", "meta2:Original");
		printXMPMeta(meta3, "Add instance ID");

		XMPMeta meta4 = (XMPMeta) meta3.clone();
		meta4.setProperty (XMPConst.NS_XMP_MM, "InstanceID", "meta2:Clone");
		printXMPMeta(meta3, "Clone and add instance ID");
	}
	

	/**
	 * Cover some basid set calls (including arrays and structs).
	 * @return Returns an <code>XMPMeta</code> object that is reused in the next examples.
	 * @throws XMPException Forwards Exceptions
	 */
	private static XMPMeta coverSetPropertyMethods() throws XMPException
	{
		// Basic set/get methods
		writeMajorLabel ("Test setProperty and related methods");

		XMPMeta meta = XMPMetaFactory.create();
		meta.setProperty (NS1, "Prop", "Prop value");
		meta.setProperty (NS1, "ns1:XMLProp", "<PropValue/>");
		meta.setProperty (NS1, "ns1:URIProp", "URI:value/", new PropertyOptions().setURI(true));

		meta.appendArrayItem(NS1, "Bag", new PropertyOptions().setArray(true), "BagItem value",
				null);
		meta.appendArrayItem(NS1, "ns1:Seq", new PropertyOptions().setArrayOrdered(true),
				"SeqItem value", null);
		meta.appendArrayItem(NS1, "ns1:Alt", new PropertyOptions().setArrayAlternate(true),
				"AltItem value", null);

		meta.setArrayItem (NS1, "Bag", 1, "BagItem 3");
		meta.insertArrayItem (NS1, "ns1:Bag", 1, "BagItem 1");
		meta.insertArrayItem (NS1, "ns1:Bag", 2, "BagItem 2");
		meta.appendArrayItem (NS1, "Bag", "BagItem 4");

		meta.setStructField (NS1, "Struct", NS2, "Field1", "Field1 value");
		meta.setStructField (NS1, "ns1:Struct", NS2, "Field2", "Field2 value");
		meta.setStructField (NS1, "ns1:Struct", NS2, "Field3", "Field3 value");

		printXMPMeta(meta, "A few basic set property calls");
		
		// -----------------------------------------------------------------------------------------
		
		// Add some properties with qualifier
		writeMinorLabel ("Add some properties with qualifier");
		println ("CountArrayItems Bag = " + meta.countArrayItems(NS1, "Bag"));

		meta.setProperty (NS1, "QualProp1", "Prop value");
		meta.setQualifier (NS1, "QualProp1", NS2, "Qual1", "Qual1 value");
		meta.setProperty (NS1, "QualProp1/?ns2:Qual3", "Qual3 value");
		meta.setProperty (NS1, "QualProp1/?xml:lang", "x-qual");

		meta.setProperty (NS1, "QualProp2", "Prop value");
		meta.setQualifier (NS1, "QualProp2", XMPConst.NS_XML, "lang", "en-us");
		meta.setProperty (NS1, "QualProp2/@xml:lang", "x-attr");

		meta.setProperty (NS1, "QualProp3", "Prop value");
		meta.setQualifier (NS1, "ns1:QualProp3", XMPConst.NS_XML, "xml:lang", "en-us");
		meta.setQualifier (NS1, "ns1:QualProp3", NS2, "ns2:Qual", "Qual value");

		meta.setProperty (NS1, "QualProp4", "Prop value");
		meta.setQualifier (NS1, "QualProp4", NS2, "Qual", "Qual value");
		meta.setQualifier (NS1, "QualProp4", XMPConst.NS_XML, "lang", "en-us");
		printXMPMeta(meta, "Add some qualifiers");

		meta.setProperty (NS1, "QualProp1", "new value");
		meta.setProperty (NS1, "QualProp2", "new value");
		meta.setProperty (NS1, "QualProp3", "new value");
		meta.setProperty (NS1, "QualProp4", "new value");
		printXMPMeta (meta, "Change values and keep qualifiers");
		
		return meta;
	}
	
	
	/**
	 * Test getProperty, deleteProperty and related methods.
	 * @param meta a predefined <code>XMPMeta</code> object.
	 * @throws XMPException Forwards exceptions
	 */
	private static void coverGetPropertyMethods(XMPMeta meta) throws XMPException
	{
		writeMajorLabel ("Test getProperty, deleteProperty and related methods");
		
		meta.deleteProperty (NS1, "QualProp1");	// ! Start with fresh qualifiers.
		meta.deleteProperty (NS1, "ns1:QualProp2");
		meta.deleteProperty (NS1, "ns1:QualProp3");
		meta.deleteProperty (NS1, "QualProp4");

		
		writeMinorLabel("Set properties with qualifier");
		
		meta.setProperty (NS1, "QualProp1", "Prop value");
		meta.setQualifier (NS1, "QualProp1", NS2, "Qual1", "Qual1 value");

		meta.setProperty (NS1, "QualProp2", "Prop value");
		meta.setQualifier (NS1, "QualProp2", XMPConst.NS_XML, "lang", "en-us");

		meta.setProperty (NS1, "QualProp3", "Prop value");
		meta.setQualifier (NS1, "QualProp3", XMPConst.NS_XML, "lang", "en-us");
		meta.setQualifier (NS1, "QualProp3", NS2, "Qual", "Qual value");

		meta.setProperty (NS1, "QualProp4", "Prop value");
		meta.setQualifier (NS1, "QualProp4", NS2, "Qual", "Qual value");
		meta.setQualifier (NS1, "QualProp4", XMPConst.NS_XML, "lang", "en-us");

		printXMPMeta (meta, "XMP object");

		
		writeMinorLabel("Get simple properties");
		
		XMPProperty property = meta.getProperty(NS1, "Prop");
		println("getProperty ns1:Prop =   " + property.getValue() + " (" + property.getOptions().getOptionsString() + ")");

		property = meta.getProperty(NS1, "ns1:XMLProp");
		println("getProperty ns1:XMLProp =   " + property.getValue() + " (" + property.getOptions().getOptionsString() + ")");

		property = meta.getProperty(NS1, "ns1:URIProp");
		println("getProperty ns1:URIProp =   " + property.getValue() + " (" + property.getOptions().getOptionsString() + ")");

		property = meta.getArrayItem(NS1, "Bag", 2);
		println("getArrayItem ns1:Bag[2] =   " + property.getValue() + " (" + property.getOptions().getOptionsString() + ")");

		try
		{
			meta.getArrayItem(null, "ns1:Bag", 1);
		}
		catch (XMPException e)
		{
			println("getArrayItem with no schema URI - threw XMPException #" + e.getErrorCode() +" :   " + e.getMessage() + ")");
		}
		

		writeMinorLabel("Get array items and struct fields");
		
		property = meta.getArrayItem(NS1, "ns1:Seq", 1);
		println("getArrayItem ns1:Seq[1] =   " + property.getValue() + " (" + property.getOptions().getOptionsString() + ")");

		property = meta.getArrayItem(NS1, "ns1:Alt", 1);
		println("getArrayItem ns1:Alt[1] =   " + property.getValue() + " (" + property.getOptions().getOptionsString() + ")");
		println();

		property = meta.getStructField(NS1, "Struct", NS2, "Field1");
		println("getStructField ns1:Struct/ns2:Field1 =   " + property.getValue() + " (" + property.getOptions().getOptionsString() + ")");

		property = meta.getStructField(NS1, "ns1:Struct", NS2, "Field2");
		println("getStructField ns1:Struct/ns2:Field2 =   " + property.getValue() + " (" + property.getOptions().getOptionsString() + ")");

		property = meta.getStructField(NS1, "ns1:Struct", NS2, "ns2:Field3");
		println("getStructField ns1:Struct/ns2:Field3 =   " + property.getValue() + " (" + property.getOptions().getOptionsString() + ")");

		property = meta.getStructField(NS1, "ns1:Struct", NS2, "ns2:Field3");
		println("getStructField ns1:Struct/ns2:Field3 =   " + property.getValue() + " (" + property.getOptions().getOptionsString() + ")");

		property = meta.getStructField(NS1, "ns1:Struct", NS2, "ns2:Field3");
		println("getStructField ns1:Struct/ns2:Field3 =   " + property.getValue() + " (" + property.getOptions().getOptionsString() + ")");
		println();
		
		
		writeMinorLabel("Get qualifier");
		
		property = meta.getQualifier(NS1, "QualProp1", NS2, "Qual1");
		println("getQualifier  ns1:QualProp1/?ns2:Qual1 =   " + property.getValue() + " (" + property.getOptions().getOptionsString() + ")");

		try
		{
			meta.getQualifier(null, "ns1:QualProp1", NS2, "Qual1");
		}
		catch (XMPException e)
		{
			println("getQualifier with no schema URI - threw XMPException #" + e.getErrorCode() + " :   " + e.getMessage());
		}

		property = meta.getQualifier(NS1, "QualProp3", XMPConst.NS_XML, "xml:lang");
		println("getQualifier ns1:QualProp3/@xml-lang =   " + property.getValue() + " (" + property.getOptions().getOptionsString() + ")");

		property = meta.getQualifier(NS1, "QualProp3", NS2, "ns2:Qual");
		println("getQualifier ns1:QualProp3/?ns2:Qual =   " + property.getValue() + " (" + property.getOptions().getOptionsString() + ")");
		println();

		
		writeMinorLabel("Get non-simple properties");
		
		property = meta.getProperty(NS1, "Bag");
		println("getProperty ns1:Bag =   " + property.getValue() + " ("
				+ property.getOptions().getOptionsString() + ")");

		property = meta.getProperty(NS1, "Seq");
		println("getProperty ns1:Seq =   " + property.getValue() + " ("
				+ property.getOptions().getOptionsString() + ")");

		property = meta.getProperty(NS1, "Alt");
		println("getProperty ns1:Alt =   " + property.getValue() + " ("
				+ property.getOptions().getOptionsString() + ")");

		property = meta.getProperty(NS1, "Struct");
		println("getProperty ns1:Struct =   " + property.getValue() + " ("
				+ property.getOptions().getOptionsString() + ")");
		println();
		
		
		writeMinorLabel("Get not existing properties");
		
		try
		{
			meta.getProperty("ns:bogus/", "Bogus");
		}
		catch (XMPException e)
		{
			println("getProperty with bogus schema URI - threw XMPException #" + e.getErrorCode() + " :   " + e.getMessage());
		}
		
		property = meta.getProperty (NS1, "Bogus");
		println ("getProperty ns1:Bogus (not existing) =   " + property);

		property = meta.getArrayItem(NS1, "Bag", 99);
		println ("ArrayItem ns1:Bag[99] (not existing) =   " + property);

		property = meta.getStructField(NS1, "Struct", NS2, "Bogus");
		println ("getStructField ns1:Struct/ns2:Bogus (not existing) =   " + property);

		property = meta.getQualifier (NS1, "Prop", NS2, "Bogus");
		println ("getQualifier ns1:Prop/?ns2:Bogus (not existing) =   " + property);
	}
	
	
	/**
	 * Test doesPropertyExist, deleteProperty, and related methods.
	 * @param meta a predefined <code>XMPMeta</code> object.
	 */
	private static void coverExistingProperties(XMPMeta meta)
	{
		writeMajorLabel ("Test doesPropertyExist, deleteProperty, and related methods");

		printXMPMeta (meta, "XMP object");

		println("doesPropertyExist ns1:Prop =    " + meta.doesPropertyExist(NS1, "Prop"));
		println("doesPropertyExist ns1:Struct =    " + meta.doesPropertyExist(NS1, "ns1:Struct"));
		println("doesArrayItemExist ns1:Bag[2] =    " + meta.doesArrayItemExist(NS1, "Bag", 2));
		println("doesArrayItemExist ns1:Seq[last()] =    "
				+ meta.doesArrayItemExist(NS1, "ns1:Seq", XMPConst.ARRAY_LAST_ITEM));
		println("doesStructFieldExist ns1:Struct/ns2:Field1 =    "
				+ meta.doesStructFieldExist(NS1, "Struct", NS2, "Field1"));
		println("doesQualifierExist ns1:QualProp1/?ns2:Qual1 =    "
				+ meta.doesQualifierExist(NS1, "QualProp1", NS2, "Qual1"));
		println("doesQualifierExist ns1:QualProp2/?xml:lang =    "
				+ meta.doesQualifierExist(NS1, "QualProp2", XMPConst.NS_XML, "lang"));
		println();
		println("doesPropertyExist (namespace is null) =    "
				+ meta.doesPropertyExist(null, "ns1:Bag"));
		println("doesArrayItemExist (namespace is null) =    "
				+ meta.doesArrayItemExist(null, "ns1:Bag", XMPConst.ARRAY_LAST_ITEM));
		println("doesQualifierExist ns:Bogus (namespace not existing) =    "
				+ meta.doesPropertyExist("ns:bogus/", "Bogus"));
		println("doesPropertyExist ns1:Bogus =    " + meta.doesPropertyExist(NS1, "Bogus"));
		println("doesArrayItemExist ns1:Bag[99] =    " + meta.doesArrayItemExist(NS1, "Bag", 99));
		println("doesStructFieldExist ns1:Struct/ns2:Bogus =    "
				+ meta.doesStructFieldExist(NS1, "Struct", NS2, "Bogus"));
		println("doesQualifierExist ns1:Prop/?ns2:Bogus =    "
				+ meta.doesQualifierExist(NS1, "Prop", NS2, "Bogus"));
	}


	/**
	 * Tests deletion of properties, array items, struct fields and qualifer.
	 * @param meta a predefined <code>XMPMeta</code> object.
	 */
	private static void coverDeleteProperties(XMPMeta meta)
	{
		writeMajorLabel("Test deleteProperty");
		
		meta.deleteProperty (NS1, "Prop");
		meta.deleteArrayItem (NS1, "Bag", 2);
		meta.deleteStructField (NS1, "Struct", NS2, "Field1");

		printXMPMeta (meta, "Delete Prop, Bag[2], and Struct1/Field1");

		meta.deleteQualifier (NS1, "QualProp1", NS2, "Qual1");
		meta.deleteQualifier (NS1, "QualProp2", XMPConst.NS_XML, "lang");
		meta.deleteQualifier (NS1, "QualProp3", NS2, "Qual");
		meta.deleteQualifier (NS1, "QualProp4", XMPConst.NS_XML, "lang");

		printXMPMeta(meta,
			"Delete QualProp1/?ns2:Qual1, QualProp2/?xml:lang, " +
			"QualProp3:/ns2:Qual, and QualProp4/?xml:lang");

		meta.deleteProperty (NS1, "Bag");
		meta.deleteProperty (NS1, "Struct");

		printXMPMeta (meta, "Delete all of Bag and Struct");
	}

	
	/**
	 * Localized text set/get methods.
	 * @throws XMPException Forwards exceptions
	 */
	private static void coverLocalisedProperties() throws XMPException
	{
		writeMajorLabel ("Test setLocalizedText and getLocalizedText");

		XMPMeta meta = XMPMetaFactory.create();		
		meta.setLocalizedText (NS1, "AltText", "", "x-default", "default value");
		printXMPMeta (meta, "Set x-default value");

		meta.setLocalizedText (NS1, "AltText", "en", "en-us", "en-us value");
		printXMPMeta (meta, "Set en/en-us value");

		meta.setLocalizedText (NS1, "AltText", "en", "en-uk", "en-uk value");
		printXMPMeta (meta, "Set en/en-uk value");
		println();

		XMPProperty property = meta.getLocalizedText(NS1, "AltText", "en", "en-ca");
		println("getLocalizedText en/en-ca =   " + property.getValue() + " (lang: " + property.getLanguage() + ", opt: " + property.getOptions().getOptionsString() + ")");

		property = meta.getProperty(NS1, "AltText");
		println("getProperty ns1:AltText =   "  + property.getValue() + " (lang: " + property.getLanguage() + ", opt: " + property.getOptions().getOptionsString() + ")");
	}


	/**
	 * Literal value set/get methods
	 * @throws XMPException
	 */
	private static void coverLiteralProperties() throws XMPException
	{
		writeMajorLabel("Test setProperty... and getProperty... methods " +
			"(set/get with literal values)");
		
		XMPMeta meta = XMPMetaFactory.parseFromString(DATETIME_RDF);
		XMPDateTime dateValue = XMPDateTimeFactory.create(2000, 1, 2, 3, 4, 5, 0);

		meta.setPropertyBoolean (NS1, "Bool0", false);
		meta.setPropertyBoolean (NS1, "Bool1", true);
		meta.setPropertyInteger (NS1, "Int", 42);
		meta.setPropertyDouble (NS1, "Double", 4.2);

		meta.setPropertyDate (NS1, "Date10", dateValue);
		int offset = (/* hour */ 06 * 3600 * 1000 + /* minute */ 07 * 60 * 1000) * /* sign */ 1; 
		dateValue.setTimeZone(new SimpleTimeZone(offset, "test"));
		meta.setPropertyDate (NS1, "Date11", dateValue);
		offset *= -1; 
		dateValue.setTimeZone(new SimpleTimeZone(offset, "test"));
		meta.setPropertyDate (NS1, "Date12", dateValue);
		dateValue.setNanoSecond(9);
		meta.setPropertyDate (NS1, "Date13", dateValue);

		printXMPMeta (meta, "A few basic binary Set... calls");
		println();
		
		Boolean bool = meta.getPropertyBoolean(NS1, "Bool0");
		println ("getPropertyBoolean ns1:Bool0 =   " + bool);

		bool = meta.getPropertyBoolean(NS1, "Bool1");
		println ("getPropertyBoolean ns1:Bool1 =   " + bool);

		Integer integer = meta.getPropertyInteger(NS1, "Int");
		println ("getPropertyBoolean ns1:Int =   " + integer);

		Double d = meta.getPropertyDouble(NS1, "Double");
		println ("getPropertyBoolean ns1:Int =   " + d);
		println();
		
		for (int i = 1; i <= 13; i++)
		{
			String dateName = "Date" + i;
			XMPDateTime dt = meta.getPropertyDate (NS1, dateName);
			println ("getPropertyDate (" + i + ") =   " + dt);
			meta.setPropertyDate (NS2, dateName, dateValue);
		}

		printXMPMeta (meta, "Get and re-set the dates in NS2");
	}	

	
	/**
	 * Parse and serialize methods. 
	 * @throws XMPException Forwards exceptions
	 */
	private static void coverParsing() throws XMPException
	{
		writeMajorLabel ("Test parsing with multiple buffers and various options");
	
		XMPMeta meta = XMPMetaFactory.parseFromString(SIMPLE_RDF);
		printXMPMeta (meta, "Parse from String");
	
		meta = XMPMetaFactory.parseFromString(SIMPLE_RDF, new ParseOptions()
				.setRequireXMPMeta(true));
		printXMPMeta(meta, "Parse and require xmpmeta element, which is missing");

		meta = XMPMetaFactory.parseFromString(NAMESPACE_RDF);
		printXMPMeta(meta, "Parse RDF with multiple nested namespaces");

		meta = XMPMetaFactory.parseFromString(XMPMETA_RDF, new ParseOptions()
				.setRequireXMPMeta(true));
		printXMPMeta(meta, "Parse and require xmpmeta element, which is present");

		meta = XMPMetaFactory.parseFromString(INCONSISTENT_RDF);
		printXMPMeta(meta, "Parse and reconcile inconsistent aliases");
	
		try
		{
			XMPMetaFactory.parseFromString(INCONSISTENT_RDF, new ParseOptions()
					.setStrictAliasing(true));
		}
		catch (XMPException e)
		{
			println("Parse and do not reconcile inconsistent aliases - threw XMPException #" + e.getErrorCode() + " :   " + e.getMessage());
		}
	}

	
	/**
	 * Test CR and LF in values.
	 * @throws XMPException Forwards exceptions
	 */
	private static void coverLinefeedValues() throws XMPException
	{
		writeMajorLabel ("Test CR and LF in values");
		
		String valueWithCR		= "ASCII \r CR";
		String valueWithLF		= "ASCII \n LF";
		String valueWithCRLF	= "ASCII \r\n CRLF";
	
		XMPMeta meta = XMPMetaFactory.parseFromString(NEWLINE_RDF);
	
		meta.setProperty (NS2, "HasCR", valueWithCR);
		meta.setProperty (NS2, "HasLF", valueWithLF);
		meta.setProperty (NS2, "HasCRLF", valueWithCRLF);
	
		String result = XMPMetaFactory.serializeToString(meta, new SerializeOptions()
				.setOmitPacketWrapper(true));
		println(result);
		
		String hasCR = meta.getPropertyString (NS1, "HasCR");
		String hasCR2 = meta.getPropertyString (NS2, "HasCR");
		String hasLF = meta.getPropertyString (NS1, "HasLF");    
		String hasLF2 = meta.getPropertyString (NS2, "HasLF");
		String hasCRLF = meta.getPropertyString (NS1, "HasCRLF");
		String hasCRLF2 = meta.getPropertyString (NS2, "HasCRLF");
		if (hasCR.equals(valueWithCR)  &&  hasCR2.equals(valueWithCR)  &&
			hasLF.equals(valueWithLF)  &&  hasLF2.equals(valueWithLF)  &&
			hasCRLF.equals(valueWithCRLF)  &&  hasCRLF2.equals(valueWithCRLF))
		{
			println();
			println("\n## HasCR and HasLF and HasCRLF correctly retrieved\n");
		}
	}	
	
	
	/**
	 * Covers the serialization of an <code>XMPMeta</code> object with different options.
	 * @throws Exception Forwards exceptions 
	 */
	private static void coverSerialization() throws Exception
	{
		writeMajorLabel ("Test serialization with various options");

		XMPMeta meta = XMPMetaFactory.parseFromString(SIMPLE_RDF);
		meta.setProperty (NS2, "Another", "Something in another schema");
		meta.setProperty (NS2, "Yet/pdf:More", "Yet more in another schema");

		printXMPMeta (meta, "Parse simple RDF, serialize with various options");

		writeMinorLabel ("Default serialize");
		println(XMPMetaFactory.serializeToString(meta, null));

		writeMinorLabel ("Compact RDF, no packet serialize");
		println(XMPMetaFactory.serializeToString(meta, new SerializeOptions().setUseCompactFormat(
				true).setOmitPacketWrapper(true)));

		writeMinorLabel ("Read-only serialize");
		println(XMPMetaFactory.serializeToString(meta, new SerializeOptions()
				.setReadOnlyPacket(true)));

		writeMinorLabel ("Alternate newline serialize");
		println(XMPMetaFactory.serializeToString(meta, new SerializeOptions().setNewline(
				"<--newline-->\n").setOmitPacketWrapper(true)));

		writeMinorLabel ("Alternate indent serialize");
		println(XMPMetaFactory.serializeToString(meta, new SerializeOptions().setIndent("-->")
				.setBaseIndent(5).setOmitPacketWrapper(true)));

		writeMinorLabel ("Small padding serialize");
		println(XMPMetaFactory.serializeToString(meta, new SerializeOptions().setPadding(10)));

		writeMinorLabel ("Serialize with exact packet size");
		int s = XMPMetaFactory.serializeToBuffer(meta, new SerializeOptions()
				.setReadOnlyPacket(true)).length;
		println ("Minimum packet size is " + s + " bytes\n");

		// with the flag "exact packet size" the padding becomes the overall length of the packet
		byte[] buffer = XMPMetaFactory.serializeToBuffer(meta, new SerializeOptions()
				.setExactPacketLength(true).setPadding(s));
		println(new String(buffer, "UTF-8"));

		try
		{
			XMPMetaFactory.parseFromString(XMPMetaFactory.serializeToString(meta,
					new SerializeOptions().setExactPacketLength(true).setPadding(s - 1)));
		}
		catch (XMPException e)
		{
			println("\nExact packet size smaller than minimal packet length - " +
				"threw XMPException #" + e.getErrorCode() + " :   " + e.getMessage());
		}
	}	
	

	/**
	 * Cover different use cases of the <code>XMPIterator</code>. 
	 * @throws XMPException Forwards exceptions
	 */
	private static void coverIterator() throws XMPException
	{
		writeMajorLabel ("Test iteration methods");
	
		XMPMeta meta = XMPMetaFactory.parseFromString(RDF_COVERAGE);
		meta.setProperty (NS2, "Prop", "Prop value");
		meta.appendArrayItem (NS2, "Bag", new PropertyOptions().setArray(true),
			"BagItem 2", null);
		meta.appendArrayItem(NS2, "Bag", "BagItem 1");
		meta.appendArrayItem(NS2, "Bag", "BagItem 3");
	
		printXMPMeta (meta, "Parse \"coverage\" RDF, add Bag items out of order");
	
		writeMinorLabel ("Default iteration");
		for (XMPIterator it = meta.iterator(); it.hasNext();)
		{
			XMPPropertyInfo prop = (XMPPropertyInfo) it.next();
			printPropertyInfo(prop);
		}
		
		writeMinorLabel ("Iterate omitting qualifiers");
		for (XMPIterator it = meta.iterator(new IteratorOptions().setOmitQualifiers(true)); it
				.hasNext();)
		{
			XMPPropertyInfo prop = (XMPPropertyInfo) it.next();
			printPropertyInfo(prop);
		}
	
		writeMinorLabel("Iterate with just leaf names");
		for (XMPIterator it = meta.iterator(new IteratorOptions().setJustLeafname(true)); it
				.hasNext();)
		{
			XMPPropertyInfo prop = (XMPPropertyInfo) it.next();
			printPropertyInfo(prop);
		}

		writeMinorLabel("Iterate with just leaf nodes");
		for (XMPIterator it = meta.iterator(new IteratorOptions().setJustLeafnodes(true)); it
				.hasNext();)
		{
			XMPPropertyInfo prop = (XMPPropertyInfo) it.next();
			printPropertyInfo(prop);
		}

		writeMinorLabel("Iterate just the schema nodes");
		for (XMPIterator it = meta.iterator(new IteratorOptions().setJustChildren(true)); it
				.hasNext();)
		{
			XMPPropertyInfo prop = (XMPPropertyInfo) it.next();
			printPropertyInfo(prop);
		}
	
		writeMinorLabel("Iterate the ns2: namespace");
		for (XMPIterator it = meta.iterator(NS2, null, null); it.hasNext();)
		{
			XMPPropertyInfo prop = (XMPPropertyInfo) it.next();
			printPropertyInfo(prop);
		}

		writeMinorLabel("Start at ns2:Bag");
		for (XMPIterator it = meta.iterator(NS2, "Bag", null); it.hasNext();)
		{
			XMPPropertyInfo prop = (XMPPropertyInfo) it.next();
			printPropertyInfo(prop);
		}

		writeMinorLabel("Start at ns2:NestedStructProp/ns1:Outer");
		for (XMPIterator it = meta.iterator(NS2, "NestedStructProp/ns1:Outer", null); it.hasNext();)
		{
			XMPPropertyInfo prop = (XMPPropertyInfo) it.next();
			printPropertyInfo(prop);
		}

		writeMinorLabel("Iterate an empty namespace");
		for (XMPIterator it = meta.iterator("ns:Empty", null, null); it.hasNext();)
		{
			XMPPropertyInfo prop = (XMPPropertyInfo) it.next();
			printPropertyInfo(prop);
		}
		
		writeMinorLabel("Iterate the top of the ns2: namespace with just leaf names");
		for (XMPIterator it = meta.iterator(NS2, null, new IteratorOptions().setJustChildren(true)
				.setJustLeafname(true)); it.hasNext();)
		{
			XMPPropertyInfo prop = (XMPPropertyInfo) it.next();
			printPropertyInfo(prop);
		}

		writeMinorLabel("Iterate the top of the ns2: namespace with just leaf nodes");
		for (XMPIterator it = meta.iterator(NS2, null, new IteratorOptions().setJustChildren(true)
				.setJustLeafnodes(true)); it.hasNext();)
		{
			XMPPropertyInfo prop = (XMPPropertyInfo) it.next();
			printPropertyInfo(prop);
		}
	}


	/**
	 * XPath composition utilities using the <code>XMPPathFactory</code>.
	 * @throws XMPException Forwards exceptions
	 */
	private static void coverPathCreation() throws XMPException
	{
		writeMajorLabel ("XPath composition utilities");

		XMPMeta meta = XMPMetaFactory.create(); 
		
		meta.appendArrayItem(NS1, "ArrayProp", new PropertyOptions().setArray(true), 
			"Item 1", null);
		
		String path = XMPPathFactory.composeArrayItemPath("ArrayProp", 2);
		println ("composeArrayItemPath ArrayProp[2] =   " + path);
		meta.setProperty (NS1, path, "new ns1:ArrayProp[2] value");

		path = "StructProperty";
		path += XMPPathFactory.composeStructFieldPath(NS2, "Field3");
		println ("composeStructFieldPath StructProperty/ns2:Field3 =   " + path);
		meta.setProperty (NS1, path, "new ns1:StructProp/ns2:Field3 value");

		path = "QualProp";
		path += XMPPathFactory.composeQualifierPath(NS2, "Qual");
		println ("composeStructFieldPath QualProp/?ns2:Qual =   " + path);
		meta.setProperty (NS1, path, "new ns1:QualProp/?ns2:Qual value");

		meta.setLocalizedText(NS1, "AltTextProp", null, "en-US", "initival value");
		path = "AltTextProp";
		path += XMPPathFactory.composeQualifierPath(XMPConst.NS_XML, "lang");
		println ("composeQualifierPath ns1:AltTextProp/?xml:lang =   " + path);
		meta.setProperty (NS1, path, "new ns1:AltTextProp/?xml:lang value");

		printXMPMeta (meta, "Modified simple RDF");
	}	


	/**
	 * Date/Time utilities
	 */
	private static void coverDateTime()
	{
		writeMajorLabel ("Test date/time utilities and special values");
	
		XMPDateTime	date1	= XMPDateTimeFactory.create(2000, 1, 31, 12, 34, 56, -1);
		date1.setTimeZone(TimeZone.getTimeZone("PST"));
		XMPDateTime	date2	= XMPDateTimeFactory.create(0, 0, 0, 0, 0, 0, 0);
		GregorianCalendar cal = new GregorianCalendar(2007, 1, 28);
		XMPDateTime	date3	= XMPDateTimeFactory.createFromCalendar(cal);
		XMPDateTime currentDateTime = XMPDateTimeFactory.getCurrentDateTime();

		println("Print date 2000 Jan 31 12:34:56 PST =   " + date1.toString());
		println("Print zero date =   " + date2.toString());
		println("Print date created by a calendar =   " + date3.toString());
		println("Print current date =   " + currentDateTime);
		println();
	}

	
	
	
	// ---------------------------------------------------------------------------------------------
	// Utilities for this example
	// ---------------------------------------------------------------------------------------------
	
	/**
	 * Print the content of an XMPMeta object a headline and its name.
	 * @param meta an <code>XMPMeta</code> object
	 * @param title the headline
	 */
	private static void printXMPMeta(XMPMeta meta, String title)
	{
		String name = meta.getObjectName();
		if (name != null  &&  name.length() > 0)
		{
			println(title + " (Name: '" + name + "'):");
		}
		else
		{
			println(title + ":");
		}
		println(meta.dumpObject());
		println();
	}


	/**
	 * @param prop an <code>XMPPropertyInfo</code> from the <code>XMPIterator</code>.
	 */
	private static void printPropertyInfo(XMPPropertyInfo prop)
	{
		println("NS (" + prop.getNamespace() + ")   PATH (" + prop.getPath() + ")   VALUE ("
				+ prop.getValue() + ")  OPTIONS (" + prop.getOptions().getOptionsString() + ")");
	}

	
	/**
	 * Dump the alias list to the output.
	 */
	private static void dumpAliases()
	{
		Map aliases;
		aliases = registry.getAliases();
		for (Iterator it = aliases.keySet().iterator(); it.hasNext();)
		{
			String qname = (String) it.next();
			XMPAliasInfo aliasInfo = (XMPAliasInfo) aliases.get(qname);
			println("" + qname + "   --->   " + aliasInfo);
		}
		println();
	}
		
	
	/**
	 * Writes a major headline to the output.
	 * @param title the headline
	 */
	private static void writeMajorLabel (String title)
	{
		println();
		println("// =============================================================================");
		println("// " + title);
		println("// =============================================================================");
		println();
	}

	
	/**
	 * Writes a minor headline to the output.
	 * @param title the headline
	 */
	private static void writeMinorLabel (String title)
	{
		println();
		println ("// -----------------------------------------------------------------------------"
			.substring(0, title.length() + 3));
		println ("// " + title);
		println();
	}
	

	/**
	 * Prints a string message to both log file and system out.
	 * @param message the message
	 */
	private static void println(String message)
	{
		System.out.println(message);
		log.println(message);
	}

	
	/**
	 * Prints a newline to both log file and system out.
	 */
	private static void println()
	{
		println("");
	}
}
