// =================================================================================================
// ADOBE SYSTEMS INCORPORATED
// Copyright 2006 Adobe Systems Incorporated
// All Rights Reserved
//
// NOTICE:  Adobe permits you to use, modify, and distribute this file in accordance with the terms
// of the Adobe license agreement accompanying it.
// =================================================================================================

package org.verapdf.xmp.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.verapdf.xmp.XMPConst;
import org.verapdf.xmp.XMPError;
import org.verapdf.xmp.XMPException;
import org.verapdf.xmp.XMPSchemaRegistry;
import org.verapdf.xmp.containers.StaticXmpCoreContainers;
import org.verapdf.xmp.options.AliasOptions;
import org.verapdf.xmp.properties.XMPAliasInfo;


/**
 * The schema registry handles the namespaces, aliases and global options for the XMP Toolkit. There
 * is only one single instance used by the toolkit.
 * 
 * @since 27.01.2006
 */
public final class XMPSchemaRegistryImpl implements XMPSchemaRegistry, XMPConst {
	/**
	 * a map from a namespace URI to its registered prefix
	 */
	private final Map<String, String> standardNamespaceToPrefixMap = new HashMap<>();

	/**
	 * a map from a prefix to the associated namespace URI
	 */
	private final Map<String, String> standardPrefixToNamespaceMap = new HashMap<>();

	/**
	 * a map of all registered aliases.
	 * The map is a relationship from a qname to an <code>XMPAliasInfo</code>-object.
	 */
	private Map aliasMap = new HashMap();
	/**
	 * The pattern that must not be contained in simple properties
	 */
	private Pattern p = Pattern.compile("[/*?\\[\\]]");

	
	/**
	 * Performs the initialisation of the registry with the default namespaces, aliases and global
	 * options.
	 */
	public XMPSchemaRegistryImpl() {
		try {
			StaticXmpCoreContainers.clearAllContainers();
			registerStandardNamespaces();
			registerStandardAliases();
		} catch (XMPException e) {
			throw new RuntimeException("The XMPSchemaRegistry cannot be initialized!");
		}
	}

	
	// ---------------------------------------------------------------------------------------------
	// Namespace Functions
	

	/**
	 * @see XMPSchemaRegistry#registerNamespace(String, String, boolean)
	 */
	public synchronized String registerNamespace(String namespaceURI, String suggestedPrefix, boolean isStandard)
			throws XMPException {
		ParameterAsserts.assertSchemaNS(namespaceURI);
		ParameterAsserts.assertPrefix(suggestedPrefix);
		
		if (suggestedPrefix.charAt(suggestedPrefix.length() - 1) != ':') {
			suggestedPrefix += ':';
		}
		
		if (!Utils.isXMLNameNS(suggestedPrefix.substring(0,
				suggestedPrefix.length() - 1))) {
			throw new XMPException("The prefix is a bad XML name", XMPError.BADXML);
		}
		
		String registeredPrefix = getNamespacePrefix(namespaceURI);
		String registeredNS = getNamespaceURI(suggestedPrefix);
		if (registeredPrefix != null) {
			// Return the actual prefix
			return registeredPrefix;
		} else {
			if (registeredNS != null) {
				// the namespace is new, but the prefix is already engaged,
				// we generate a new prefix out of the suggested
				String generatedPrefix = suggestedPrefix;
				for (int i = 1; containsPrefix(generatedPrefix); i++) {
					generatedPrefix = suggestedPrefix.substring(0, suggestedPrefix.length() - 1) + "_" + i + "_:";
				}
				suggestedPrefix = generatedPrefix;
			}
			if (isStandard) {
				standardPrefixToNamespaceMap.put(suggestedPrefix, namespaceURI);
				standardNamespaceToPrefixMap.put(namespaceURI, suggestedPrefix);
			} else {
				StaticXmpCoreContainers.getPrefixToNamespaceMap().put(suggestedPrefix, namespaceURI);
				StaticXmpCoreContainers.getNamespaceToPrefixMap().put(namespaceURI, suggestedPrefix);
			}
			
			// Return the suggested prefix
			return suggestedPrefix;
		}
	}

	public synchronized String registerStandardNamespace(String namespaceURI, String suggestedPrefix) throws XMPException {
		return registerNamespace(namespaceURI, suggestedPrefix, true);
	}
	
	private boolean containsPrefix(String namespacePrefix) {
		return StaticXmpCoreContainers.getPrefixToNamespaceMap().containsKey(namespacePrefix) || standardPrefixToNamespaceMap.containsKey(namespacePrefix);
	}


	/**
	 * @see XMPSchemaRegistry#deleteNamespace(String)
	 */
	public synchronized void deleteNamespace(String namespaceURI)
	{
		String prefixToDelete = getNamespacePrefix(namespaceURI);
		if (prefixToDelete != null)
		{
			StaticXmpCoreContainers.getNamespaceToPrefixMap().remove(namespaceURI);
			StaticXmpCoreContainers.getPrefixToNamespaceMap().remove(prefixToDelete);
			standardNamespaceToPrefixMap.remove(namespaceURI);
			standardPrefixToNamespaceMap.remove(prefixToDelete);
		}	
	}


	/**
	 * @see XMPSchemaRegistry#getNamespacePrefix(String)
	 */
	public synchronized String getNamespacePrefix(String namespaceURI)
	{
		String namespacePrefix = StaticXmpCoreContainers.getNamespaceToPrefixMap().get(namespaceURI);
		if (namespacePrefix == null) {
			namespacePrefix = standardNamespaceToPrefixMap.get(namespaceURI);
		}
		return namespacePrefix;
	}


	/**
	 * @see XMPSchemaRegistry#getNamespaceURI(String)
	 */
	public synchronized String getNamespaceURI(String namespacePrefix)
	{
		if (namespacePrefix != null  &&  !namespacePrefix.endsWith(":"))
		{
			namespacePrefix += ":";
		}
		String namespaceURI = StaticXmpCoreContainers.getPrefixToNamespaceMap().get(namespacePrefix);
		if (namespaceURI == null) {
			namespaceURI = standardPrefixToNamespaceMap.get(namespacePrefix);
		}
		return namespaceURI;
	}


	/**
	 * @see XMPSchemaRegistry#getNamespaces()
	 */
	public synchronized Map getNamespaces()
	{
		Map<String, String> map = new TreeMap<>(StaticXmpCoreContainers.getNamespaceToPrefixMap());
		map.putAll(standardNamespaceToPrefixMap);
		return Collections.unmodifiableMap(map);
	}
	
	
	/**
	 * @see XMPSchemaRegistry#getPrefixes()
	 */
	public synchronized Map getPrefixes()
	{
		Map<String, String> map = new TreeMap<>(StaticXmpCoreContainers.getPrefixToNamespaceMap());
		map.putAll(standardPrefixToNamespaceMap);
		return Collections.unmodifiableMap(map);
	}
	
	
	/**
	 * Register the standard namespaces of schemas and types that are included in the XMP
	 * Specification and some other Adobe private namespaces.
	 * Note: This method is not lock because only called by the constructor.
	 * 
	 * @throws XMPException Forwards processing exceptions
	 */
	private void registerStandardNamespaces() throws XMPException
	{
		// register standard namespaces
		registerStandardNamespace(NS_XML, "xml");
		registerStandardNamespace(NS_RDF, "rdf");
		registerStandardNamespace(NS_DC, "dc");
		registerStandardNamespace(NS_IPTCCORE, "Iptc4xmpCore");
		registerStandardNamespace(NS_IPTCEXT, "Iptc4xmpExt");
		registerStandardNamespace(NS_DICOM, "DICOM");
		registerStandardNamespace(NS_PLUS, "plus");

		// register Adobe standard namespaces
		registerStandardNamespace(NS_X, "x");
		registerStandardNamespace(NS_IX, "iX");

		registerStandardNamespace(NS_XMP, "xmp");
		registerStandardNamespace(NS_XMP_RIGHTS, "xmpRights");
		registerStandardNamespace(NS_XMP_MM, "xmpMM");
		registerStandardNamespace(NS_XMP_BJ, "xmpBJ");
		registerStandardNamespace(NS_XMP_NOTE, "xmpNote");
		
		registerStandardNamespace(NS_PDF, "pdf");
		registerStandardNamespace(NS_PDFX, "pdfx");
		registerStandardNamespace(NS_PDFX_ID, "pdfxid");
		registerStandardNamespace(NS_PDFA_SCHEMA, "pdfaSchema");
		registerStandardNamespace(NS_PDFA_PROPERTY, "pdfaProperty");
		registerStandardNamespace(NS_PDFA_TYPE, "pdfaType");
		registerStandardNamespace(NS_PDFA_FIELD, "pdfaField");
		registerStandardNamespace(NS_PDFA_ID, "pdfaid");
		registerStandardNamespace(NS_PDFA_EXTENSION, "pdfaExtension");
		registerStandardNamespace(NS_PHOTOSHOP, "photoshop");
		registerStandardNamespace(NS_PSALBUM, "album");
		registerStandardNamespace(NS_EXIF, "exif");
		registerStandardNamespace(NS_EXIFX, "exifEX");
		registerStandardNamespace(NS_EXIF_AUX, "aux");
		registerStandardNamespace(NS_TIFF, "tiff");
		registerStandardNamespace(NS_PNG, "png");
		registerStandardNamespace(NS_JPEG, "jpeg");
		registerStandardNamespace(NS_JP2K, "jp2k");
		registerStandardNamespace(NS_CAMERARAW, "crs");
		registerStandardNamespace(NS_ADOBESTOCKPHOTO, "bmsp");
		registerStandardNamespace(NS_CREATOR_ATOM, "creatorAtom");
		registerStandardNamespace(NS_ASF, "asf");
		registerStandardNamespace(NS_WAV, "wav");
		registerStandardNamespace(NS_BWF, "bext");
		registerStandardNamespace(NS_RIFFINFO, "riffinfo");
		registerStandardNamespace(NS_SCRIPT, "xmpScript");
		registerStandardNamespace(NS_TXMP, "txmp");
		registerStandardNamespace(NS_SWF, "swf");
		
		// register Adobe private namespaces
		registerStandardNamespace(NS_DM, "xmpDM");
		registerStandardNamespace(NS_TRANSIENT, "xmpx");
		
		// register Adobe standard type namespaces
		registerStandardNamespace(TYPE_TEXT, "xmpT");
		registerStandardNamespace(TYPE_PAGEDFILE, "xmpTPg");
		registerStandardNamespace(TYPE_GRAPHICS, "xmpG");
		registerStandardNamespace(TYPE_IMAGE, "xmpGImg");
		registerStandardNamespace(TYPE_FONT, "stFnt");
		registerStandardNamespace(TYPE_DIMENSIONS, "stDim");
		registerStandardNamespace(TYPE_RESOURCEEVENT, "stEvt");
		registerStandardNamespace(TYPE_RESOURCEREF, "stRef");
		registerStandardNamespace(TYPE_ST_VERSION, "stVer");
		registerStandardNamespace(TYPE_ST_JOB, "stJob");
		registerStandardNamespace(TYPE_MANIFESTITEM, "stMfs");
		registerStandardNamespace(TYPE_IDENTIFIERQUAL, "xmpidq");
	}
	

	
	// ---------------------------------------------------------------------------------------------
	// Alias Functions

	
	/**
	 * @see XMPSchemaRegistry#resolveAlias(String, String)
	 */
	public synchronized XMPAliasInfo resolveAlias(String aliasNS, String aliasProp)
	{
		String aliasPrefix = getNamespacePrefix(aliasNS);
		if (aliasPrefix == null)
		{	
			return null;
		}
		
		return (XMPAliasInfo) aliasMap.get(aliasPrefix + aliasProp);
	}


	/**
	 * @see XMPSchemaRegistry#findAlias(java.lang.String)
	 */
	public synchronized XMPAliasInfo findAlias(String qname)
	{
		return (XMPAliasInfo) aliasMap.get(qname);
	}

	
	/**
	 * @see XMPSchemaRegistry#findAliases(String)
	 */
	public synchronized XMPAliasInfo[] findAliases(String aliasNS)
	{
		String prefix = getNamespacePrefix(aliasNS);
		List result = new ArrayList(); 
		if (prefix != null)
		{
			for (Iterator it = aliasMap.keySet().iterator(); it.hasNext();)
			{
				String qname = (String) it.next();
				if (qname.startsWith(prefix))
				{
					result.add(findAlias(qname));
				}
			}
			
		}
		return (XMPAliasInfo[]) result.toArray(new XMPAliasInfo[result.size()]);
	}	
	
	
	/**
	 * Associates an alias name with an actual name.
	 * <p>
	 * Define a alias mapping from one namespace/property to another. Both
	 * property names must be simple names. An alias can be a direct mapping,
	 * where the alias and actual have the same data type. It is also possible
	 * to map a simple alias to an item in an array. This can either be to the
	 * first item in the array, or to the 'x-default' item in an alt-text array.
	 * Multiple alias names may map to the same actual, as long as the forms
	 * match. It is a no-op to reregister an alias in an identical fashion.
	 * Note: This method is not locking because only called by registerStandardAliases 
	 * which is only called by the constructor.
	 * Note2: The method is only package-private so that it can be tested with unittests 
	 * 
	 * @param aliasNS
	 *            The namespace URI for the alias. Must not be null or the empty
	 *            string.
	 * @param aliasProp
	 *            The name of the alias. Must be a simple name, not null or the
	 *            empty string and not a general path expression.
	 * @param actualNS
	 *            The namespace URI for the actual. Must not be null or the
	 *            empty string.
	 * @param actualProp
	 *            The name of the actual. Must be a simple name, not null or the
	 *            empty string and not a general path expression.
	 * @param aliasForm
	 *            Provides options for aliases for simple aliases to array
	 *            items. This is needed to know what kind of array to create if
	 *            set for the first time via the simple alias. Pass
	 *            <code>XMP_NoOptions</code>, the default value, for all
	 *            direct aliases regardless of whether the actual data type is
	 *            an array or not (see {@link AliasOptions}).
	 * @throws XMPException
	 *             for inconsistant aliases.
	 */
	synchronized void registerAlias(String aliasNS, String aliasProp, final String actualNS,
			final String actualProp, final AliasOptions aliasForm) throws XMPException
	{
		ParameterAsserts.assertSchemaNS(aliasNS);
		ParameterAsserts.assertPropName(aliasProp);
		ParameterAsserts.assertSchemaNS(actualNS);
		ParameterAsserts.assertPropName(actualProp);
		
		// Fix the alias options
		final AliasOptions aliasOpts = aliasForm != null ?
			new AliasOptions(XMPNodeUtils.verifySetOptions(
				aliasForm.toPropertyOptions(), null).getOptions()) :
			new AliasOptions();
	
		if (p.matcher(aliasProp).find()  ||  p.matcher(actualProp).find())
		{
			throw new XMPException("Alias and actual property names must be simple",
					XMPError.BADXPATH);
		}
		
		// check if both namespaces are registered
		final String aliasPrefix = getNamespacePrefix(aliasNS);
		final String actualPrefix = getNamespacePrefix(actualNS);
		if (aliasPrefix == null)
		{
			throw new XMPException("Alias namespace is not registered", XMPError.BADSCHEMA);
		}
		else if (actualPrefix == null)
		{
			throw new XMPException("Actual namespace is not registered", 
				XMPError.BADSCHEMA);
		}
		
		String key = aliasPrefix + aliasProp;
		
		// check if alias is already existing
		if (aliasMap.containsKey(key))
		{
			throw new XMPException("Alias is already existing", XMPError.BADPARAM);
		}
		else if (aliasMap.containsKey(actualPrefix + actualProp))
		{	
			throw new XMPException(
					"Actual property is already an alias, use the base property",
					XMPError.BADPARAM);
		}
		
		XMPAliasInfo aliasInfo = new XMPAliasInfo()
		{
			/**
			 * @see XMPAliasInfo#getNamespace()
			 */
			public String getNamespace()
			{
				return actualNS;
			}

			/**
			 * @see XMPAliasInfo#getPrefix()
			 */
			public String getPrefix()
			{
				return actualPrefix;
			}

			/**
			 * @see XMPAliasInfo#getPropName()
			 */
			public String getPropName()
			{
				return actualProp;
			}
			
			/**
			 * @see XMPAliasInfo#getAliasForm()
			 */
			public AliasOptions getAliasForm()
			{
				return aliasOpts;
			}
			
			public String toString()
			{
				return actualPrefix + actualProp + " NS(" + actualNS + "), FORM ("
						+ getAliasForm() + ")"; 
			}
		};
		
		aliasMap.put(key, aliasInfo);
	}

		
	/**
	 * @see XMPSchemaRegistry#getAliases()
	 */
	public synchronized Map getAliases()
	{
		return Collections.unmodifiableMap(new TreeMap(aliasMap));
	}
	
	
	/**
	 * Register the standard aliases.
	 * Note: This method is not lock because only called by the constructor.
	 * 
	 * @throws XMPException If the registrations of at least one alias fails.
	 */
	private void registerStandardAliases() throws XMPException
	{
		AliasOptions aliasToArrayOrdered = new AliasOptions().setArrayOrdered(true);
		AliasOptions aliasToArrayAltText = new AliasOptions().setArrayAltText(true);
		
		
		// Aliases from XMP to DC.
		registerAlias(NS_XMP, "Author", NS_DC, "creator", aliasToArrayOrdered);
		registerAlias(NS_XMP, "Authors", NS_DC, "creator", null);
		registerAlias(NS_XMP, "Description", NS_DC, "description", null);
		registerAlias(NS_XMP, "Format", NS_DC, "format", null);
		registerAlias(NS_XMP, "Keywords", NS_DC, "subject", null);
		registerAlias(NS_XMP, "Locale", NS_DC, "language", null);
		registerAlias(NS_XMP, "Title", NS_DC, "title", null);
		registerAlias(NS_XMP_RIGHTS, "Copyright", NS_DC, "rights", null);

		// Aliases from PDF to DC and XMP.
		registerAlias(NS_PDF, "Author", NS_DC, "creator", aliasToArrayOrdered);
		registerAlias(NS_PDF, "BaseURL", NS_XMP, "BaseURL", null);
		registerAlias(NS_PDF, "CreationDate", NS_XMP, "CreateDate", null);
		registerAlias(NS_PDF, "Creator", NS_XMP, "CreatorTool", null);
		registerAlias(NS_PDF, "ModDate", NS_XMP, "ModifyDate", null);
		registerAlias(NS_PDF, "Subject", NS_DC, "description", aliasToArrayAltText);
		registerAlias(NS_PDF, "Title", NS_DC, "title", aliasToArrayAltText);

		// Aliases from PHOTOSHOP to DC and XMP.
		registerAlias(NS_PHOTOSHOP, "Author", NS_DC, "creator", aliasToArrayOrdered);
		registerAlias(NS_PHOTOSHOP, "Caption", NS_DC, "description", aliasToArrayAltText);
		registerAlias(NS_PHOTOSHOP, "Copyright", NS_DC, "rights", aliasToArrayAltText);
		registerAlias(NS_PHOTOSHOP, "Keywords", NS_DC, "subject", null);
		registerAlias(NS_PHOTOSHOP, "Marked", NS_XMP_RIGHTS, "Marked", null);
		registerAlias(NS_PHOTOSHOP, "Title", NS_DC, "title", aliasToArrayAltText);
		registerAlias(NS_PHOTOSHOP, "WebStatement", NS_XMP_RIGHTS, "WebStatement", null);

		// Aliases from TIFF and EXIF to DC and XMP.
		registerAlias(NS_TIFF, "Artist", NS_DC, "creator", aliasToArrayOrdered);
		registerAlias(NS_TIFF, "Copyright", NS_DC, "rights", null);
		registerAlias(NS_TIFF, "DateTime", NS_XMP, "ModifyDate", null);
		registerAlias(NS_TIFF, "ImageDescription", NS_DC, "description", null);
		registerAlias(NS_TIFF, "Software", NS_XMP, "CreatorTool", null);

		// Aliases from PNG (Acrobat ImageCapture) to DC and XMP.
		registerAlias(NS_PNG, "Author", NS_DC, "creator", aliasToArrayOrdered);
		registerAlias(NS_PNG, "Copyright", NS_DC, "rights", aliasToArrayAltText);
		registerAlias(NS_PNG, "CreationTime", NS_XMP, "CreateDate", null);
		registerAlias(NS_PNG, "Description", NS_DC, "description", aliasToArrayAltText);
		registerAlias(NS_PNG, "ModificationTime", NS_XMP, "ModifyDate", null);
		registerAlias(NS_PNG, "Software", NS_XMP, "CreatorTool", null);
		registerAlias(NS_PNG, "Title", NS_DC, "title", aliasToArrayAltText);
	}
}
