package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.xml.DomXmpParser;
import org.apache.xmpbox.xml.XmpParsingException;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosStream;
import org.verapdf.model.impl.pb.cos.PBCosStream;
import org.verapdf.model.impl.pb.xmp.PBXMPMainPackage;
import org.verapdf.model.impl.pb.xmp.PBXMPPackage;
import org.verapdf.model.pdlayer.PDMetadata;
import org.verapdf.model.xmplayer.XMPPackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDMetadata extends PBoxPDObject implements PDMetadata {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDMetadata.class);

    public static final String METADATA_TYPE = "PDMetadata";

    public static final String XMP_PACKAGE = "XMPPackage";
    public static final String STREAM = "stream";

    private boolean isMainMetadata;

    public PBoxPDMetadata(
            org.apache.pdfbox.pdmodel.common.PDMetadata simplePDObject,
            Boolean isMainMetadata) {
        super(simplePDObject);
        this.isMainMetadata = isMainMetadata.booleanValue();
        setType(METADATA_TYPE);
    }

    @Override
    public String getFilter() {
        List<COSName> filters = ((org.apache.pdfbox.pdmodel.common.PDMetadata) simplePDObject)
                .getFilters();
        if (filters != null && !filters.isEmpty()) {
            StringBuilder result = new StringBuilder();
            for (COSName filter : filters) {
                result.append(filter.getName()).append(' ');
            }
            return result.substring(0, result.length() - 1);
        }
        return null;
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        List<? extends Object> list;

        switch (link) {
        case XMP_PACKAGE:
            list = getXMPPackage();
            break;
        case STREAM:
            list = getStream();
            break;
        default:
            list = super.getLinkedObjects(link);
            break;
        }

        return list;
    }

    private List<XMPPackage> getXMPPackage() {
        List<XMPPackage> xmp = new ArrayList<>(1);
        try {
            COSStream stream = ((org.apache.pdfbox.pdmodel.common.PDMetadata) simplePDObject)
                    .getStream();
            if (stream != null) {
                DomXmpParser xmpParser = new DomXmpParser();
                XMPMetadata metadata = xmpParser.parse(stream
                        .getUnfilteredStream());
                xmp.add(isMainMetadata ? new PBXMPMainPackage(metadata, true)
                        : new PBXMPPackage(metadata, true));
            }
        } catch (XmpParsingException e) {
            LOGGER.error("Problems with parsing metadata. " + e.getMessage(), e);
            xmp.add(new PBXMPPackage(null, false));
        } catch (IOException e) {
            LOGGER.error("Metadata stream is closed. " + e.getMessage(), e);
            xmp.add(new PBXMPPackage(null, false));
        }
        return xmp;
    }

    private List<CosStream> getStream() {
        List<CosStream> streams = new ArrayList<>(1);
        COSStream stream = ((org.apache.pdfbox.pdmodel.common.PDMetadata) simplePDObject)
                .getStream();
        streams.add(new PBCosStream(stream));
        return streams;
    }
}