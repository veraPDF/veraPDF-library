package org.verapdf.model.tools;

import java.util.HashSet;
import java.util.Set;

/**
 * Helper class for xmp package
 * Created by bezrukov on 6/15/15.
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
    public static final String NSIPTC4XMPCORE = "http://iptc.org/std/Iptc4xmpCore/1.0/xmlns/";
    public static final String NSPDFE = "http://www.aiim.org/pdfe/ns/id/";
    public static final String NSPDFX = "http://ns.adobe.com/pdfx/1.3/";
    public static final String NSPDFXID = "http://www.npes.org/pdfx/ns/id/";
    public static final String NSXMPDM = "http://ns.adobe.com/xmp/1.0/DynamicMedia/";
    public static final String NSXAPS = "http://ns.adobe.com/xap/1.0/s/";

    public static final String NSXAPG = "http://ns.adobe.com/xap/1.0/g/";
    public static final String NSSTFNT = "http:ns.adobe.com/xap/1.0/sType/Font#";

    private static final Set<String> predifinedSchemas = new HashSet<>();

    static {
        predifinedSchemas.add(NSPDFAID);

        predifinedSchemas.add(NSPDF);
        predifinedSchemas.add(NSDC);
        predifinedSchemas.add(NSEXIF);
        predifinedSchemas.add(NSTIFF);
        predifinedSchemas.add(NSPHOTOSHOP);
        predifinedSchemas.add(NSXMPBJ);
        predifinedSchemas.add(NSXMPBASIC);
        predifinedSchemas.add(NSXMPMM);
        predifinedSchemas.add(NSXMPTPG);
        predifinedSchemas.add(NSXMPRIGHTS);

        predifinedSchemas.add(NSSTDIM);
        predifinedSchemas.add(NSXMPGIMG);
        predifinedSchemas.add(NSSTEVT);
        predifinedSchemas.add(NSSTREF);
        predifinedSchemas.add(NSSTVER);
        predifinedSchemas.add(NSSTJOB);
        predifinedSchemas.add(NSXMPIDQ);

        predifinedSchemas.add(NSXAPG);
        predifinedSchemas.add(NSSTFNT);

        predifinedSchemas.add(NSPDFAEXTENSION);
        predifinedSchemas.add(NSPDFAFIELD);
        predifinedSchemas.add(NSPDFAPROPERTY);
        predifinedSchemas.add(NSPDFASCHEMA);
        predifinedSchemas.add(NSPDFATYPE);

        predifinedSchemas.add(NSCRS);
        predifinedSchemas.add(NSAUX);
        predifinedSchemas.add(NSIPTC4XMPCORE);
        predifinedSchemas.add(NSPDFE);
        predifinedSchemas.add(NSPDFX);
        predifinedSchemas.add(NSPDFXID);
        predifinedSchemas.add(NSXMPDM);
        predifinedSchemas.add(NSXAPS);
    }

    public static boolean isPredifinedSchema(String schemaNS){
        return predifinedSchemas.contains(schemaNS);
    }

    private XMPHelper() {
    }
}
