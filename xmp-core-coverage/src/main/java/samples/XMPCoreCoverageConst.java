// =================================================================================================
// ADOBE SYSTEMS INCORPORATED
// Copyright 2006 Adobe Systems Incorporated
// All Rights Reserved
//
// NOTICE:  Adobe permits you to use, modify, and distribute this file in accordance with the terms
// of the Adobe license agreement accompanying it.
// =================================================================================================

package samples;


/**
 * Constants for the XMPCoreCoverage example.
 * 
 * @since   21.02.2007
 */
public interface XMPCoreCoverageConst
{
	/** */
	String NS1 = "ns:test1/";
	/** */
	String NS2 = "ns:test2/";
	/** RDF metadata that covers most variants of XMP data structures */
	String RDF_COVERAGE =
		"<rdf:RDF xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'>" +
		"  <rdf:Description rdf:about='Test:XMPCoreCoverage/kRDFCoverage' xmlns:ns1='ns:test1/' " +
		"		xmlns:ns2='ns:test2/'>" +
		"" +
		"    <ns1:SimpleProp1>Simple1 value</ns1:SimpleProp1>" +
		"    <ns1:SimpleProp2 xml:lang='x-default'>Simple2 value</ns1:SimpleProp2>" +
		"" +
		"    <ns1:ArrayProp1>" +
		"      <rdf:Bag>" +
		"        <rdf:li>Item1.1 value</rdf:li>" +
		"        <rdf:li>Item1.2 value</rdf:li>" +
		"      </rdf:Bag>" +
		"    </ns1:ArrayProp1>" +
		"" +
		"    <ns1:ArrayProp2>" +
		"      <rdf:Alt>" +
		"        <rdf:li xml:lang='x-one'>Item2.1 value</rdf:li>" +
		"        <rdf:li xml:lang='x-two'>Item2.2 value</rdf:li>" +
		"      </rdf:Alt>" +
		"    </ns1:ArrayProp2>" +
		"" +
		"    <ns1:ArrayProp3>" +
		"      <rdf:Alt>" +
		"        <rdf:li xml:lang='x-one'>Item3.1 value</rdf:li>" +
		"        <rdf:li>Item3.2 value</rdf:li>" +
		"      </rdf:Alt>" +
		"    </ns1:ArrayProp3>" +
		"" +
		"    <ns1:ArrayProp4>" +
		"      <rdf:Alt>" +
		"        <rdf:li>Item4.1 value</rdf:li>" +
		"        <rdf:li xml:lang='x-two'>Item4.2 value</rdf:li>" +
		"      </rdf:Alt>" +
		"    </ns1:ArrayProp4>" +
		"" +
		"    <ns1:ArrayProp5>" +
		"      <rdf:Alt>" +
		"        <rdf:li xml:lang='x-xxx'>Item5.1 value</rdf:li>" +
		"        <rdf:li xml:lang='x-xxx'>Item5.2 value</rdf:li>" +
		"      </rdf:Alt>" +
		"    </ns1:ArrayProp5>" +
		"" +
		"    <ns1:StructProp rdf:parseType='Resource'>" +
		"      <ns2:Field1>Field1 value</ns2:Field1>" +
		"      <ns2:Field2>Field2 value</ns2:Field2>" +
		"    </ns1:StructProp>" +
		"" +
		"    <ns1:QualProp1 rdf:parseType='Resource'>" +
		"      <rdf:value>Prop value</rdf:value>" +
		"      <ns2:Qual>Qual value</ns2:Qual>" +
		"    </ns1:QualProp1>" +
		"" +
		"    <ns1:QualProp2 rdf:parseType='Resource'>" +
		"      <rdf:value xml:lang='x-default'>Prop value</rdf:value>" +
		"      <ns2:Qual>Qual value</ns2:Qual>" +
		"    </ns1:QualProp2>" +
		"" +
		"    <!-- " +
		"	NOTE: QualProp3 is not quite kosher. Normally a qualifier on a struct is attached " +
		"	to the struct node in the XMP tree, and the same for an array. See QualProp4 and " +
		"	QualProp5. But for the pseudo-struct of a qualified simple property there is no " +
		" 	struct node that can own the qualifier. Instead the qualifier is attached to the " +
		"   value. The alternative of attaching the qualifier to the value and all other " +
		"	qualifiers is not compelling. This issue only arises for xml:lang, it is the only " +
		"	qualifier that RDF has as an attribute." +
		"   -->" +
		"" +
		"    <ns1:QualProp3 xml:lang='x-default' rdf:parseType='Resource'>" +
		"      <rdf:value>Prop value</rdf:value>" +
		"      <ns2:Qual>Qual value</ns2:Qual>" +
		"    </ns1:QualProp3>" +
		"" +
		"    <ns1:QualProp4 xml:lang='x-default' rdf:parseType='Resource'>" +
		"      <ns2:Field1>Field1 value</ns2:Field1>" +
		"      <ns2:Field2>Field2 value</ns2:Field2>" +
		"    </ns1:QualProp4>" +
		"" +
		"    <ns1:QualProp5 xml:lang='x-default'>" +
		"      <rdf:Bag>" +
		"        <rdf:li>Item1.1 value</rdf:li>" +
		"        <rdf:li>Item1.2 value</rdf:li>" +
		"      </rdf:Bag>" +
		"    </ns1:QualProp5>" +
		"" +
		"    <ns2:NestedStructProp rdf:parseType='Resource'>" +
		"      <ns1:Outer rdf:parseType='Resource'>" +
		"        <ns1:Middle rdf:parseType='Resource'>" +
		"          <ns1:Inner rdf:parseType='Resource'>" +
		"            <ns1:Field1>Field1 value</ns1:Field1>" +
		"            <ns2:Field2>Field2 value</ns2:Field2>" +
		"          </ns1:Inner>" +
		"        </ns1:Middle>" +
		"      </ns1:Outer>" +
		"    </ns2:NestedStructProp>" +
		"" +
		"  </rdf:Description>" +
		"</rdf:RDF>";

	/** */
	String SIMPLE_RDF =
		"<rdf:RDF xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'>" +
		"  <rdf:Description rdf:about='Test:XMPCoreCoverage/kSimpleRDF' xmlns:ns1='ns:test1/' " +
		"		xmlns:ns2='ns:test2/'>" +
		"" +
		"    <ns1:SimpleProp>Simple value</ns1:SimpleProp>" +
		"" +
		"    <ns1:ArrayProp>" +
		"      <rdf:Bag>" +
		"        <rdf:li>Item1 value</rdf:li>" +
		"        <rdf:li>Item2 value</rdf:li>" +
		"      </rdf:Bag>" +
		"    </ns1:ArrayProp>" +
		"" +
		"    <ns1:StructProp rdf:parseType='Resource'>" +
		"      <ns2:Field1>Field1 value</ns2:Field1>" +
		"      <ns2:Field2>Field2 value</ns2:Field2>" +
		"    </ns1:StructProp>" +
		"" +
		"    <ns1:QualProp rdf:parseType='Resource'>" +
		"      <rdf:value>Prop value</rdf:value>" +
		"      <ns2:Qual>Qual value</ns2:Qual>" +
		"    </ns1:QualProp>" +
		"" +
		"    <ns1:AltTextProp>" +
		"      <rdf:Alt>" +
		"        <rdf:li xml:lang='x-one'>x-one value</rdf:li>" +
		"        <rdf:li xml:lang='x-two'>x-two value</rdf:li>" +
		"      </rdf:Alt>" +
		"    </ns1:AltTextProp>" +
		"" +
		"    <ns1:ArrayOfStructProp>" +
		"      <rdf:Bag>" +
		"        <rdf:li rdf:parseType='Resource'>" +
		"          <ns2:Field1>Item-1</ns2:Field1>" +
		"          <ns2:Field2>Field 1.2 value</ns2:Field2>" +
		"        </rdf:li>" +
		"        <rdf:li rdf:parseType='Resource'>" +
		"          <ns2:Field1>Item-2</ns2:Field1>" +
		"          <ns2:Field2>Field 2.2 value</ns2:Field2>" +
		"        </rdf:li>" +
		"      </rdf:Bag>" +
		"    </ns1:ArrayOfStructProp>" +
		"" +
		"  </rdf:Description>" +
		"</rdf:RDF>";

	/** RDF metadata introducing new namespaces */
	String NAMESPACE_RDF =
		"<rdf:RDF xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'>" +
		"  <rdf:Description rdf:about='Test:XMPCoreCoverage/kNamespaceRDF' " +
		"		xmlns:ns1='ns:test1/'>" +
		"" +
		"    <ns1:NestedStructProp rdf:parseType='Resource'>" +
		"      <ns2:Outer rdf:parseType='Resource' xmlns:ns2='ns:test2/' xmlns:ns3='ns:test3/'>" +
		"        <ns3:Middle rdf:parseType='Resource' xmlns:ns4='ns:test4/'>" +
		"          <ns4:Inner rdf:parseType='Resource' xmlns:ns5='ns:test5/' " +
		"				xmlns:ns6='ns:test6/'>" +
		"            <ns5:Field1>Field1 value</ns5:Field1>" +
		"            <ns6:Field2>Field2 value</ns6:Field2>" +
		"          </ns4:Inner>" +
		"        </ns3:Middle>" +
		"      </ns2:Outer>" +
		"    </ns1:NestedStructProp>" +
		"" +
		"  </rdf:Description>" +
		"</rdf:RDF>";

	/** RDF metadata embedded in XML tag. */
	String XMPMETA_RDF =
		"<x:Outermost xmlns:x='adobe:ns:meta/'>" +
		"" +
		"<rdf:RDF xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'>" +
		"  <rdf:Description rdf:about='Test:XMPCoreCoverage/kBogusLeadingRDF' " +
		"		xmlns:ns1='ns:test1/'>" +
		"    <ns1:BogusLeadingProp>bogus packet</ns1:BogusLeadingProp>" +
		"  </rdf:Description>" +
		"</rdf:RDF>" +
		"" +
		"<x:xmpmeta>" +
		"<rdf:RDF xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'>" +
		"  <rdf:Description rdf:about='Test:XMPCoreCoverage/kXMPMetaRDF' xmlns:ns1='ns:test1/'>" +
		"    <ns1:XMPMetaProp>xmpmeta packet</ns1:XMPMetaProp>" +
		"  </rdf:Description>" +
		"</rdf:RDF>" +
		"</x:xmpmeta>" +
		"" +
		"<rdf:RDF xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'>" +
		"  <rdf:Description rdf:about='Test:XMPCoreCoverage/kBogusTrailingRDF'" +
		"		xmlns:ns1='ns:test1/'>" +
		"    <ns1:BogusTrailingProp>bogus packet</ns1:BogusTrailingProp>" +
		"  </rdf:Description>" +
		"</rdf:RDF>" +
		"" +
		"</x:Outermost>";

	/** RDF metadata with properties that contain whitespaces */
	String NEWLINE_RDF =
		"<rdf:RDF xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'>" +
		"  <rdf:Description rdf:about='Test:XMPCoreCoverage/kNewlineRDF' xmlns:ns1='ns:test1/'>" +
		"" +
		"    <ns1:HasCR>ASCII &#xD; CR</ns1:HasCR>" +
		"    <ns1:HasLF>ASCII &#xA; LF</ns1:HasLF>" +
		"    <ns1:HasCRLF>ASCII &#xD;&#xA; CRLF</ns1:HasCRLF>" +
		"" +
		"  </rdf:Description>" +
		"</rdf:RDF>";

	/** Inconsistant RDF metadata */
	String INCONSISTENT_RDF =
		"<rdf:RDF xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'>" +
		"  <rdf:Description rdf:about='Test:XMPCoreCoverage/kInconsistentRDF'" +
		"                   xmlns:pdf='http://ns.adobe.com/pdf/1.3/'" +
		"                   xmlns:xmp='http://ns.adobe.com/xap/1.0/'" +
		"                   xmlns:dc='http://purl.org/dc/elements/1.1/'>" +
		"" +
		"    <pdf:Author>PDF Author</pdf:Author>" +
		"    <xmp:Author>XMP Author</xmp:Author>" +
		"" +
		"    <xmp:Authors>" +
		"      <rdf:Seq>" +
		"        <rdf:li>XMP Authors [1]</rdf:li>" +
		"      </rdf:Seq>" +
		"    </xmp:Authors>" +
		"" +
		"    <dc:creator>" +
		"      <rdf:Seq>" +
		"        <rdf:li>DC Creator [1]</rdf:li>" +
		"      </rdf:Seq>" +
		"    </dc:creator>" +
		"" +
		"  </rdf:Description>" +
		"</rdf:RDF>";

	/** Example for the usage of date and time properties */
	String DATETIME_RDF =
		"<rdf:RDF xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'>" +
		"  <rdf:Description rdf:about='Test:XMPCoreCoverage/kDateTimeRDF' xmlns:ns1='ns:test1/'>" +
		"" +
		"    <ns1:Date1>2003</ns1:Date1>" +
		"    <ns1:Date2>2003-12</ns1:Date2>" +
		"    <ns1:Date3>2003-12-31</ns1:Date3>" +
		"" +
		"    <ns1:Date4>2003-12-31T12:34Z</ns1:Date4>" +
		"    <ns1:Date5>2003-12-31T12:34:56Z</ns1:Date5>" +
		"" +
		"    <ns1:Date6>2003-12-31T12:34:56.001Z</ns1:Date6>" +
		"    <ns1:Date7>2003-12-31T12:34:56.000000001Z</ns1:Date7>" +
		"" +
		"    <ns1:Date8>2003-12-31T10:04:56-02:30</ns1:Date8>" +
		"    <ns1:Date9>2003-12-31T15:49:56+03:15</ns1:Date9>" +
		"" +
		"  </rdf:Description>" +
		"</rdf:RDF>";	
}
