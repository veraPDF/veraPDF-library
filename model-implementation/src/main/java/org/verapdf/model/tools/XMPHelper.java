package org.verapdf.model.tools;

import java.util.HashSet;
import java.util.Set;

/**
 * Helper class for xmp package
 *
 * @author Maksim Bezrukov
 */
public final class XMPHelper {

	public static final String NSPDFAID = "http://www.aiim.org/pdfa/ns/id/";

	public static final String NSPDF = "http://ns.adobe.com/pdf/1.3/";
	public static final String NSDC = "http://purl.org/dc/elements/1.1/";
	public static final String NSEXIF = "http://ns.adobe.com/exif/1.0/";
	public static final String NSTIFF = "http://ns.adobe.com/tiff/1.0/";
	public static final String NSPHOTOSHOP = "http://ns.adobe.com/photoshop/1.0/";
	public static final String NSXMPBJ = "http://ns.adobe.com/xap/1.0/bj/";
	public static final String NSXMPBASIC = "http://ns.adobe.com/xap/1.0/";
	public static final String NSXMPMM = "http://ns.adobe.com/xap/1.0/mm/";
	public static final String NSXMPTPG = "http://ns.adobe.com/xap/1.0/t/pg/";
	public static final String NSXMPRIGHTS = "http://ns.adobe.com/xap/1.0/rights/";

	public static final String NSSTDIM = "http://ns.adobe.com/xap/1.0/sType/Dimensions#";
	public static final String NSXMPGIMG = "http://ns.adobe.com/xap/1.0/g/img/ ";
	public static final String NSSTEVT = "http://ns.adobe.com/xap/1.0/sType/ResourceEvent#";
	public static final String NSSTREF = "http://ns.adobe.com/xap/1.0/sType/ResourceRef# ";
	public static final String NSSTVER = "http://ns.adobe.com/xap/1.0/sType/Version# ";
	public static final String NSSTJOB = "http://ns.adobe.com/xap/1.0/sType/Job#";
	public static final String NSXMPIDQ = "http://ns.adobe.com/xmp/Identifier/qual/1.0/";

	public static final String NSPDFAEXTENSION = "http://www.aiim.org/pdfa/ns/extension/";
	public static final String NSPDFAFIELD = "http://www.aiim.org/pdfa/ns/field#";
	public static final String NSPDFAPROPERTY = "http://www.aiim.org/pdfa/ns/property#";
	public static final String NSPDFASCHEMA = "http://www.aiim.org/pdfa/ns/schema#";
	public static final String NSPDFATYPE = "http://www.aiim.org/pdfa/ns/type#";

	public static final String NSCRS = "http://ns.adobe.com/camera-rawsettings/1.0/";
	public static final String NSAUX = "http://ns.adobe.com/exif/1.0/aux/";
	public static final String NSXMPDM = "http://ns.adobe.com/xmp/1.0/DynamicMedia/";
	public static final String NSXAPS = "http://ns.adobe.com/xap/1.0/s/";

	public static final String NSXAPG = "http://ns.adobe.com/xap/1.0/g/";
	public static final String NSSTFNT = "http:ns.adobe.com/xap/1.0/sType/Font#";

	private static final Set<String> predifinedSchemasXMP2004;

	static {
		Set<String> predifinedSchemasTemp = new HashSet<>();
		predifinedSchemasTemp.add(NSPDFAID);

		predifinedSchemasTemp.add(NSPDF);
		predifinedSchemasTemp.add(NSDC);
		predifinedSchemasTemp.add(NSEXIF);
		predifinedSchemasTemp.add(NSTIFF);
		predifinedSchemasTemp.add(NSPHOTOSHOP);
		predifinedSchemasTemp.add(NSXMPBJ);
		predifinedSchemasTemp.add(NSXMPBASIC);
		predifinedSchemasTemp.add(NSXMPMM);
		predifinedSchemasTemp.add(NSXMPTPG);
		predifinedSchemasTemp.add(NSXMPRIGHTS);

		predifinedSchemasTemp.add(NSSTDIM);
		predifinedSchemasTemp.add(NSXMPGIMG);
		predifinedSchemasTemp.add(NSSTEVT);
		predifinedSchemasTemp.add(NSSTREF);
		predifinedSchemasTemp.add(NSSTVER);
		predifinedSchemasTemp.add(NSSTJOB);
		predifinedSchemasTemp.add(NSXMPIDQ);

		predifinedSchemasTemp.add(NSXAPG);
		predifinedSchemasTemp.add(NSSTFNT);

		predifinedSchemasTemp.add(NSPDFAEXTENSION);
		predifinedSchemasTemp.add(NSPDFAFIELD);
		predifinedSchemasTemp.add(NSPDFAPROPERTY);
		predifinedSchemasTemp.add(NSPDFASCHEMA);
		predifinedSchemasTemp.add(NSPDFATYPE);

		predifinedSchemasTemp.add(NSCRS);
		predifinedSchemasTemp.add(NSAUX);
		predifinedSchemasTemp.add(NSXMPDM);
		predifinedSchemasTemp.add(NSXAPS);

		predifinedSchemasXMP2004 = predifinedSchemasTemp;
	}

	private XMPHelper() {
		// Disable default constructor
	}

	public static boolean isPredifinedXMP2004Schema(String schemaNS) {
		return predifinedSchemasXMP2004.contains(schemaNS);
	}

}
